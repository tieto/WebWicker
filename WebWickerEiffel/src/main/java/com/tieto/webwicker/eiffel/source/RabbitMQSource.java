package com.tieto.webwicker.eiffel.source;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeoutException;

import ro.fortsoft.pf4j.Extension;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.conf.Settings;
import com.tieto.webwicker.api.persistence.PersistenceLayer;
import com.tieto.webwicker.api.source.Source;
import com.tieto.webwicker.api.source.SourceFactory;
import com.tieto.webwicker.lib.json.ExtJsonElement;
import com.tieto.webwicker.eiffel.model.Commit;
import com.tieto.webwicker.eiffel.model.PatchSet;
import com.tieto.webwicker.eiffel.model.WorkItem;

public class RabbitMQSource implements Source {
	private final String exchangeName;
	private final String hostName;
	private final int port;
	private PersistenceLayer persistenceLayer = null;

	public RabbitMQSource(final Configuration configuration) {
		persistenceLayer = configuration.getPersistenceLayer();
		
		final Settings settings = configuration.getSettings();  
		
		hostName = settings.getSetting("Eiffel", "mb.host").orElse("localhost");
		port = Integer.parseInt(settings.getSetting("Eiffel", "mb.port").orElse("5672"));
		exchangeName = settings.getSetting("Eiffel", "mb.exchange").orElse("");
	}

	@Override
	public void run() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(hostName);
			factory.setPort(port);
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
	
			try {
				channel.exchangeDeclarePassive(exchangeName);
			} catch(IOException e) {
				channel.exchangeDeclare(exchangeName, "topic");
			}
			
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, exchangeName, "#");
	
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope,
						AMQP.BasicProperties properties, byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					System.out.println(" [x] Received '" + message + "'");
					handleMessage(message);
				}
			};
			channel.basicConsume(queueName, true, consumer);
		} catch(TimeoutException | IOException e) {
			System.err.println("Connection to Message Bus failed. RabbitMQSource is disabled...");
		}
	}

	private void handleMessage(final String message) {
		try {
			final ExtJsonElement jsonMsg = new ExtJsonElement(message);
			final String msgType = jsonMsg.getString("","meta","type");
			
			if("EiffelSourceChangeCreatedEvent".equals(msgType)) {
				handleNewPatchset(jsonMsg);
			} else if("EiffelSourceChangeSubmittedEvent".equals(msgType)) {
				handleMerge(jsonMsg);
			} else if("EiffelConfidenceLevelModifiedEvent".equals(msgType)) {
				handleConfidenceLevel(jsonMsg);
			}
		} catch(JsonParseException | IllegalStateException e) {
			// Ignore the message if not JSON compliant
		}
	}

	private void handleNewPatchset(ExtJsonElement jsonMsg) {
		String changeId = jsonMsg.getString("","data","change","id");
		Long epochTime = jsonMsg.getLong(0, "meta", "time");
		LocalDateTime timeStamp = LocalDateTime.ofEpochSecond(epochTime / 1000, (int)(epochTime % 1000) * 1000000, ZoneOffset.UTC);
		PatchSet ps = new PatchSet(jsonMsg.getString("", "meta", "id"), timeStamp.toString());
		JsonObject jsonCommit = persistenceLayer.fetchOneWithId("commits", changeId);
		Commit commit;
		
		if(jsonCommit != null) {
			commit = new Commit();
			commit.fromJson(jsonCommit);
		} else {
			String author = jsonMsg.getString("", "data", "author", "name");
			String authorId = jsonMsg.getString("", "data", "author", "id");
			String team = jsonMsg.getString("", "data", "author", "group");
			String project = jsonMsg.getString("", "data", "gitIdentifier", "repoName");
			String branch = jsonMsg.getString("", "data", "gitIdentifier", "branch");
			commit = new Commit(changeId, author, authorId, team, project, branch, timeStamp.toString());
		}

		commit.clearWorkItems();
		for(JsonElement issue : jsonMsg.getJsonArray("data", "issues")) {
			commit.addWorkItem(new WorkItem(issue.getAsJsonObject().get("id").getAsString(), ""));
		}

		commit.addPatchSet(ps);
		
		persistenceLayer.store("commits", commit.toJson(), changeId);
	}

	private void handleMerge(ExtJsonElement jsonMsg) {
		JsonArray links = jsonMsg.getJsonArray("links");
		String patchSetId = "";
		Long epochTime = jsonMsg.getLong(0, "meta", "time");
		LocalDateTime timeStamp = LocalDateTime.ofEpochSecond(epochTime / 1000, (int)(epochTime % 1000) * 1000000, ZoneOffset.UTC);

		for(JsonElement l : links) {
			ExtJsonElement link = new ExtJsonElement(l);
			
			if("CHANGE".equals(link.getString("", "type"))) {
				patchSetId = link.getString("", "target");
				break;
			}
		}
		
		if(!patchSetId.isEmpty()) {
			JsonObject json = persistenceLayer.fetchOne("commits", "patchSets[*].patchSetId="+patchSetId);
			
			if(json != null) {
				Commit commit = new Commit(); 
				commit.fromJson(json);
				commit.getPatchSet(patchSetId).setMerged("Yes");
				commit.getPatchSet(patchSetId).setUpdated(timeStamp.toString());
				persistenceLayer.store("commits", commit.toJson(), commit.getId());
			}
		}
	}

	private void handleConfidenceLevel(ExtJsonElement jsonMsg) {
		JsonArray links = jsonMsg.getJsonArray("links");
		String patchSetId = "";
		Long epochTime = jsonMsg.getLong(0, "meta", "time");
		LocalDateTime timeStamp = LocalDateTime.ofEpochSecond(epochTime / 1000, (int)(epochTime % 1000) * 1000000, ZoneOffset.UTC);
		
		for(JsonElement l : links) {
			ExtJsonElement link = new ExtJsonElement(l);
			if("SUBJECT".equals(link.getString("", "type"))) {
				patchSetId = link.getString("", "target");
				break;
			}
		}
		
		String clName = jsonMsg.getString("", "data", "name");
		String clValue = jsonMsg.getString("", "data", "value");
		
		if(!patchSetId.isEmpty() && ("VERIFIED".equals(clName) || "CODE_REVIEW".equals(clName))) {
			JsonObject json = persistenceLayer.fetchOne("commits", "patchSets[*].patchSetId="+patchSetId);
		
			if(json != null) {
				Commit commit = new Commit(); 
				commit.fromJson(json);
			
				if("VERIFIED".equals(clName)) {
					commit.getPatchSet(patchSetId).setVerified(clValue);
				} else if("CODE_REVIEW".equals(clName)) {
					commit.getPatchSet(patchSetId).setCodeReview(clValue);
				}
				commit.getPatchSet(patchSetId).setUpdated(timeStamp.toString());
				persistenceLayer.store("commits", commit.toJson(), commit.getId());
			}
		}
	}
	
	@Extension
	public static class RabbitMQSourceFactory extends SourceFactory {
		@Override
		public Source create(Configuration configuration) {
			return new RabbitMQSource(configuration);
		}
	}
}
