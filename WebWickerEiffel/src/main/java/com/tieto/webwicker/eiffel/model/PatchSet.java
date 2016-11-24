package com.tieto.webwicker.eiffel.model;

import com.google.gson.JsonObject;
import com.tieto.webwicker.api.model.Model;

public class PatchSet implements Model {
	private static final long serialVersionUID = -3358763923428510530L;

	private String patchSetId = "";
	private String created = "";
	private String updated = "";
	private String verified = "0";
	private String codeReviewed = "0";
	private String merged = "No";
	
	public PatchSet() { }
	
	public PatchSet(final String patchSetId, final String created) {
		this.patchSetId = patchSetId;
		this.created = created;
		this.updated = created;
	}
	
	public String getVerified() {
		return verified;
	}

	public PatchSet setVerified(final String verified) {
		this.verified = verified;
		return this;
	}

	public String getCodeReviewed() {
		return codeReviewed;
	}

	public PatchSet setCodeReview(final String codeReviewed) {
		this.codeReviewed = codeReviewed;
		return this;
	}

	public String getPatchSetId() {
		return patchSetId;
	}
	
	public String getCreated() {
		return created;
	}
	
	public String getUpdated() {
		return updated;
	}
	
	public void setUpdated(final String updated) {
		this.updated = updated;
	}
	
	public String getMerged() {
		return merged;
	}
	
	public PatchSet setMerged(final String merged) {
		this.merged = merged;
		return this;
	}

	@Override
	public void fromJson(final JsonObject json) {
		this.patchSetId = json.has("patchSetId") ? json.get("patchSetId").getAsString() : "";
		this.verified = json.has("verified") ? json.get("verified").getAsString() : "";
		this.codeReviewed = json.has("codeReviewed") ? json.get("codeReviewed").getAsString() : "";
		this.merged = json.has("merged") ? json.get("merged").getAsString() : "";
		this.created = json.has("created") ? json.get("created").getAsString() : "";
		this.updated = json.has("updated") ? json.get("updated").getAsString() : "";
	}

	@Override
	public JsonObject toJson() {
		final JsonObject result = new JsonObject();
		result.addProperty("patchSetId", patchSetId);
		result.addProperty("verified", verified);
		result.addProperty("codeReviewed", codeReviewed);
		result.addProperty("merged", merged);
		result.addProperty("created", created);
		result.addProperty("updated", updated);
		return result;
	}

	@Override
	public String getId() {
		return patchSetId;
	}

}
