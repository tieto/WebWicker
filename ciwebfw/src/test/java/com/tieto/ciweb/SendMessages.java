package com.tieto.ciweb;

import java.util.UUID;

import org.apache.wicket.util.file.File;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SendMessages {
	public static final String FILE = System.getProperty("user.home")+File.separator+"data.json";
	
	public static long time = 1473152884000L;
	
	public static void main(String[] args) {
		JsonArray messages = new JsonArray();
		
		// 0-9
		messages.add(generateChangeCreatedMessage("John Doe", "221547", "master", "common"));
		messages.add(generateChangeCreatedMessage("Bob Smith", "837230", "master", "storage"));
		messages.add(generateConfidenceLevelMessage(messages.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "VERIFIED", "+1"));
		messages.add(generateChangeCreatedMessage("Alice Cooper", "328338", "master", "common"));
		messages.add(generateConfidenceLevelMessage(messages.get(0).getAsJsonObject().getAsJsonObject("meta").get("id").getAsString(), "CODE_REVIEW", "+2"));
		messages.add(generateChangeCreatedMessage("Danny Brown", "123456", "master", "common"));
		messages.add(generateChangeCreatedMessage("Susan Kelly", "924302", "master", "service"));
		messages.add(generateChangeCreatedMessage("Bob Smith", "434578", "master", "storage"));
		messages.add(generateChangeCreatedMessage("John Doe", "982143", "master", "common"));
		messages.add(generateChangeCreatedMessage("Eric Garrison", "358372", "master", "common"));
		
		// 10-19
		messages.add(generateChangeCreatedMessage("Gary Johnson", "985733", "master", "storage"));
	}
	
	private static JsonObject generateChangeCreatedMessage(String userName, String changeId, String branch, String project) {
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
        
        data.add("change", change);
        data.add("author", author);
        data.add("gitIdentifier", gitIdentifier);
        
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
		time += 4500L;
		
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
