package com.tieto.webwicker.eiffel;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SendMessages {
	public static final String REMREM_PUBLISH_CLI_PATH = System.getProperty("user.home")+File.separator+"eiffel-remrem-publish.war";
    
	public static long time = new Date().getTime() - 100000L;

	public static void main(String[] args) throws IOException {
		JsonArray messages = new JsonArray();

		JsonArray batch1 = new JsonArray();
		batch1.add(generateChangeCreatedMessage("John Doe", "221547", "master", "common", "STORY-0103", "STORY-0106"));
		batch1.add(generateConfidenceLevelMessage(batch1.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));
		batch1.add(generateConfidenceLevelMessage(batch1.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "SUCCESS"));
		batch1.add(generateChangeSubmittedMessage(batch1.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString()));

		JsonArray batch2 = new JsonArray();
		batch2.add(generateChangeCreatedMessage("Bob Smith", "837230", "master", "storage", "BUG-0034"));
		batch2.add(generateConfidenceLevelMessage(batch2.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));
		batch2.add(generateChangeCreatedMessage("Bob Smith", "837230", "master", "storage", "BUG-0034"));
		batch2.add(generateConfidenceLevelMessage(batch2.get(2).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "FAILURE"));
		batch2.add(generateChangeCreatedMessage("Bob Smith", "837230", "master", "storage", "BUG-0034"));
		batch2.add(generateConfidenceLevelMessage(batch2.get(4).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "FAILURE"));
		batch2.add(generateChangeCreatedMessage("Bob Smith", "837230", "master", "storage", "BUG-0034"));
		batch2.add(generateConfidenceLevelMessage(batch2.get(6).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));
		batch2.add(generateConfidenceLevelMessage(batch2.get(6).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "INCONCLUSIVE"));
		batch2.add(generateChangeCreatedMessage("Bob Smith", "837230", "master", "storage", "BUG-0034"));
		batch2.add(generateConfidenceLevelMessage(batch2.get(9).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));
		batch2.add(generateConfidenceLevelMessage(batch2.get(9).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "SUCCESS"));
		batch2.add(generateChangeSubmittedMessage(batch2.get(9).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString()));

		JsonArray batch3 = new JsonArray();
		batch3.add(generateChangeCreatedMessage("Alice Cooper", "328338", "master", "common", "BUG-0034"));
		batch3.add(generateConfidenceLevelMessage(batch3.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "FAILURE"));
		batch3.add(generateChangeCreatedMessage("Alice Cooper", "328338", "master", "common", "BUG-0034"));
		batch3.add(generateConfidenceLevelMessage(batch3.get(2).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));
		batch3.add(generateConfidenceLevelMessage(batch3.get(2).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "FAILURE"));
		batch3.add(generateChangeCreatedMessage("Alice Cooper", "328338", "master", "common", "BUG-0034", "BUG-0036"));
		batch3.add(generateConfidenceLevelMessage(batch3.get(5).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));
		batch3.add(generateConfidenceLevelMessage(batch3.get(5).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "FAILURE"));
		batch3.add(generateChangeCreatedMessage("Alice Cooper", "328338", "master", "common", "BUG-0034", "BUG-0036"));
		batch3.add(generateConfidenceLevelMessage(batch3.get(8).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));
		batch3.add(generateConfidenceLevelMessage(batch3.get(8).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "SUCCESS"));
		batch3.add(generateChangeSubmittedMessage(batch3.get(8).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString()));

		JsonArray batch4 = new JsonArray();
		batch4.add(generateChangeCreatedMessage("Danny Brown", "123456", "dev-test", "common", "STORY-1001"));
		batch4.add(generateConfidenceLevelMessage(batch4.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));

		JsonArray batch5 = new JsonArray();
		batch5.add(generateChangeCreatedMessage("Susan Kelly", "924302", "master", "service", "STORY-2005"));
		batch5.add(generateConfidenceLevelMessage(batch5.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "FAILURE"));
		batch5.add(generateChangeCreatedMessage("Susan Kelly", "924302", "master", "service"));
		batch5.add(generateConfidenceLevelMessage(batch5.get(2).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));

		JsonArray batch6 = new JsonArray();
		batch6.add(generateChangeCreatedMessage("Bob Smith", "434578", "master", "storage", "STORY-0023", "STORY-0024", "STORY-0025"));
		batch6.add(generateConfidenceLevelMessage(batch6.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "FAILURE"));
		batch6.add(generateConfidenceLevelMessage(batch6.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "FAILURE"));

		JsonArray batch7 = new JsonArray();
		batch7.add(generateChangeCreatedMessage("John Doe", "982143", "legacy-branch", "common", "BUG-0001"));
		batch7.add(generateConfidenceLevelMessage(batch7.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "FAILURE"));
		batch7.add(generateConfidenceLevelMessage(batch7.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "SUCCESS"));

		JsonArray batch8 = new JsonArray();
		batch8.add(generateChangeCreatedMessage("Eric Garrison", "358372", "master", "common", "STORY-0837", "BUG-0101"));
		batch8.add(generateConfidenceLevelMessage(batch8.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));
		batch8.add(generateConfidenceLevelMessage(batch8.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "SUCCESS"));

		JsonArray batch9 = new JsonArray();
		batch9.add(generateChangeCreatedMessage("Gary Johnson", "985733", "master", "storage", "STORY-0635"));
		batch9.add(generateConfidenceLevelMessage(batch9.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "SUCCESS"));

		JsonArray batch10 = new JsonArray();
		batch10.add(generateChangeCreatedMessage("Susan Kelly", "245646", "test-branch", "service", "BUG-0100"));
		batch10.add(generateChangeCreatedMessage("Susan Kelly", "245646", "test-branch", "service", "BUG-0100", "BUG-0101"));
		batch10.add(generateChangeCreatedMessage("Susan Kelly", "245646", "test-branch", "service", "BUG-0100", "BUG-0101", "BUG-0102"));
		batch10.add(generateChangeCreatedMessage("Susan Kelly", "245646", "test-branch", "service", "BUG-0100", "BUG-0101", "BUG-0102", "BUG-0103"));
		batch10.add(generateConfidenceLevelMessage(batch10.get(3).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "SUCCESS"));

		messages.addAll(batch1);
		messages.addAll(batch2);
		messages.addAll(batch3);
		messages.addAll(batch4);
		messages.addAll(batch5);
		messages.addAll(batch6);
		messages.addAll(batch7);
		messages.addAll(batch8);
		messages.addAll(batch9);
		messages.addAll(batch10);

		sendMessages(messages);
	}

	private static void sendMessages(JsonArray messages) throws IOException {
		for(JsonElement message : messages) {
			try {
				
				String json = message.toString().replace("\"", "\\\"");

				ProcessBuilder pb = new ProcessBuilder(
					"java",
					"-jar",
					REMREM_PUBLISH_CLI_PATH,
					"-en",
					"eiffel.poc",
					"-mb",
					"localhost",
					"-rk",
					"test",
					"-json",
					"\""+json+"\""
				);
				Process p = pb.start();
				p.waitFor();
			} catch(IOException | InterruptedException e) {
			}
		}
	}

	private static JsonObject generateChangeCreatedMessage(String userName, String changeId, String branch, String project, String ... workItems) {
		JsonObject message = new JsonObject();
		message.add("meta", generateMetaTag("EiffelSourceChangeCreatedEvent"));

		JsonObject data = new JsonObject();

		JsonObject change = new JsonObject();
		change.addProperty("id", changeId);

		JsonObject author = new JsonObject();
		author.addProperty("name", userName);
		author.addProperty("email", "email@company.com");
		author.addProperty("id", "userId");
		author.addProperty("group", "CI Development Team");

		JsonObject gitIdentifier = new JsonObject();
		gitIdentifier.addProperty("commitId", "fd090b60a4aedc5161da9c035a49b14a319829c5");
		gitIdentifier.addProperty("branch", branch);
		gitIdentifier.addProperty("repoUri", "https://repo.com");
		gitIdentifier.addProperty("repoName", project);
		
		JsonArray issues = new JsonArray();
		for(String workItem : workItems) {
			JsonObject issue = new JsonObject();
			issue.addProperty("id", workItem);
			issues.add(issue);
		}

		data.add("change", change);
		data.add("author", author);
		data.add("gitIdentifier", gitIdentifier);
		data.add("issues", issues);

		message.add("data", data);

		return message;
	}

	private static JsonObject generateChangeSubmittedMessage(String targetId) {
		JsonObject message = new JsonObject();
		message.add("meta", generateMetaTag("EiffelSourceChangeSubmittedEvent"));

		JsonArray links = new JsonArray();

		JsonObject link = new JsonObject();
		link.addProperty("target", targetId);
		link.addProperty("type", "CHANGE");

		links.add(link);

		message.add("links", links);

		JsonObject data = new JsonObject();

		JsonObject submitter = new JsonObject();
		submitter.addProperty("name", "Submitter");
		submitter.addProperty("email", "email@company.com");
		submitter.addProperty("id", "userId");
		submitter.addProperty("group", "CI Development Team");

		JsonObject gitIdentifier = new JsonObject();
		gitIdentifier.addProperty("commitId", "fd090b60a4aedc5161da9c035a49b14a319829c5");
		gitIdentifier.addProperty("branch", "branch");
		gitIdentifier.addProperty("repoUri", "https://repo.com");
		gitIdentifier.addProperty("repoName", "project");

		data.add("submitter", submitter);
		data.add("gitIdentifier", gitIdentifier);

		message.add("data", data);

		return message;
	}

	private static JsonObject generateConfidenceLevelMessage(String targetId, String confidenceLevel, String value) {
		JsonObject message = new JsonObject();
		message.add("meta", generateMetaTag("EiffelConfidenceLevelModifiedEvent"));

		JsonArray links = new JsonArray();

		JsonObject link = new JsonObject();
		link.addProperty("target", targetId);
		link.addProperty("type", "SUBJECT");

		links.add(link);

		message.add("links", links);

		JsonObject data = new JsonObject();
		data.addProperty("name", confidenceLevel);
		data.addProperty("value", value);

		JsonObject issuer = new JsonObject();
		issuer.addProperty("name", "Reviewer");
		issuer.addProperty("email", "email@company.com");
		issuer.addProperty("id", "userId");
		issuer.addProperty("group", "CI Development Team");

		data.add("issuer", issuer);

		message.add("data", data);

		return message;
	}

	private static JsonObject generateMetaTag(String messageType) {
		time += 4534L;

		JsonObject meta = new JsonObject();
		meta.addProperty("time", time);
		meta.addProperty("type", messageType);
		meta.addProperty("version", "1.0.0");
		meta.addProperty("id", UUID.randomUUID().toString());

		JsonObject source = new JsonObject();
		source.addProperty("domainId", "cidev.domain");

		meta.add("source", source);
		return meta;
	}
}
