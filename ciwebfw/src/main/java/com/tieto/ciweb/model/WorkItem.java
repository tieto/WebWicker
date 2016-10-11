package com.tieto.ciweb.model;

import com.google.gson.JsonObject;
import com.tieto.ciweb.api.model.Model;

public class WorkItem implements Model {
	private static final long serialVersionUID = 6070383652943497942L;
	private String itemId = "";
	private String itemLink = "";
	
	public WorkItem() { }
	
	public WorkItem(final String itemId, final String itemLink) {
		this.itemId = itemId;
		this.itemLink = itemLink;
	}
	
	public String getItemId() {
		return itemId;
	}

	public String getItemLink() {
		return itemLink;
	}

	@Override
	public void fromJson(final JsonObject json) {
		this.itemId = json.has("itemId") ? json.get("itemId").getAsString() : "";
		this.itemLink = json.has("itemLink") ? json.get("itemLink").getAsString() : "";
	}

	@Override
	public JsonObject toJson() {
		final JsonObject result = new JsonObject();
		result.addProperty("itemId", itemId);
		result.addProperty("itemLink", itemLink);
		return result;
	}

	@Override
	public String getId() {
		return itemId;
	}

}
