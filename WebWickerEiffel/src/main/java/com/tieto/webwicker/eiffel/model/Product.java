package com.tieto.webwicker.eiffel.model;

import com.google.gson.JsonObject;
import com.tieto.webwicker.api.model.Model;

public class Product implements Model {
	private static final long serialVersionUID = 3637127585942018000L;
	
	private String _id;

	private String artifactId = "";
	private String latestRelease = "";
	private String latestSnapshot = "";
	private String commitsInReview = "";

	public Product() {}
	
	public Product(final String artifactId, final String latestRelease, final String latestSnapshot, final String commitsInReview) {
		this.artifactId = artifactId;
		this.latestRelease = latestRelease;
		this.latestSnapshot = latestSnapshot;
		this.commitsInReview = commitsInReview;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getLatestRelease() {
		return latestRelease;
	}

	public String getLatestSnapshot() {
		return latestSnapshot;
	}

	public String getCommitsInReview() {
		return commitsInReview;
	}

	@Override
	public void fromJson(final JsonObject json) {
		artifactId = json.has("artifactId") ? json.get("artifactId").getAsString() : "";
		latestRelease = json.has("latestRelease") ? json.get("latestRelease").getAsString() : "";
		latestSnapshot = json.has("latestSnapshot") ? json.get("latestSnapshot").getAsString() : "";
		commitsInReview = json.has("commitsInReview") ? json.get("commitsInReview").getAsString() : "";
	}

	@Override
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		result.addProperty("_id", _id);
		result.addProperty("artifactId", artifactId);
		result.addProperty("latestRelease", latestRelease);
		result.addProperty("latestSnapshot", latestSnapshot);
		result.addProperty("commitsInReview", commitsInReview);
		return result;
	}

	@Override
	public String getId() {
		return _id;
	}
}
