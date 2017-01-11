package com.tieto.webwicker.eiffel.model;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tieto.webwicker.api.model.Model;

public class Commit implements Model {
	private static final long serialVersionUID = 8378160156490764969L;

	private String _id;
	
	private String changeId;
	private String author;
	private String authorId;
	private String team;
	private String project;
	private String branch;
	private String created;
	
	private PatchSet latestPatchSet = null;
	
	private final List<WorkItem> workItems = new LinkedList<WorkItem>();
	private final List<PatchSet> patchSets = new LinkedList<PatchSet>();
	
	public Commit() {}
	
	/**
	 * Create a new commit with all parameters
	 * @param changeId
	 * @param author
	 * @param authorId
	 * @param team
	 * @param project
	 * @param branch
	 * @param created
	 */
	public Commit(final String changeId, final String author, final String authorId, final String team, final String project,
			final String branch, final String created) {
		this._id = changeId;
		
		this.changeId = changeId;
		this.author = author;
		this.authorId = authorId;
		this.team = team;
		this.project = project;
		this.branch = branch;
		this.created = created;
	}

	public String getChangeId() {
		return changeId;
	}

	public String getAuthor() {
		return author;
	}

	public String getTeam() {
		return team;
	}

	public String getProject() {
		return project;
	}

	public String getBranch() {
		return branch;
	}

	public String getCreated() {
		return created;
	}
	
	public int getNumberOfPatchSets() {
		return patchSets.size();
	}

	public PatchSet getLatestPatchSet() {
		return latestPatchSet;
	}

	public void setAuthor(String author) {
		this.author = author;
	} 

	@Override
	public void fromJson(final JsonObject json) {
		_id = json.has("_id") ? json.get("_id").getAsString() : "";
		changeId = json.has("changeId") ? json.get("changeId").getAsString() : "";
		author = json.has("author") ? json.get("author").getAsString() : "";
		authorId = json.has("authorId") ? json.get("authorId").getAsString() : "";
		team = json.has("team") ? json.get("team").getAsString() : "";
		project = json.has("project") ? json.get("project").getAsString() : "";
		branch = json.has("branch") ? json.get("branch").getAsString() : "";
		created = json.has("created") ? json.get("created").getAsString() : "";
		
		if(json.has("patchSets")) {
			for(JsonElement jps : json.getAsJsonArray("patchSets")) {
				PatchSet ps = new PatchSet();
				ps.fromJson(jps.getAsJsonObject());
				addPatchSet(ps);
			}
		}
		
		if(json.has("workItems")) {
			for(JsonElement jwi : json.getAsJsonArray("workItems")) {
				WorkItem wi = new WorkItem();
				wi.fromJson(jwi.getAsJsonObject());
				addWorkItem(wi);
			}
		}
	}

	@Override
	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		result.addProperty("_id", _id);
		result.addProperty("changeId", changeId);
		result.addProperty("author", author);
		result.addProperty("authorId", authorId);
		result.addProperty("team", team);
		result.addProperty("project", project);
		result.addProperty("branch", branch);
		result.addProperty("created", created);
		
		JsonArray patchSets = new JsonArray();
		for(PatchSet patchSet : this.patchSets) {
			patchSets.add(patchSet.toJson());
		}
		result.add("patchSets", patchSets);

		JsonArray workItems = new JsonArray();
		for(WorkItem workItem : this.workItems) {
			workItems.add(workItem.toJson());
		}
		result.add("workItems", workItems);

		return result;
	}

	public List<PatchSet> getPatchSets() {
		return patchSets;
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}
	
	public void clearWorkItems() {
		workItems.clear();
	}
	
	public void addWorkItem(final WorkItem workItem) {
		workItems.add(workItem);
	}

	public PatchSet getPatchSet(String id) {
		for(PatchSet ps : patchSets) {
			if(id.equals(ps.getId())) {
				return ps;
			}
		}
		return null;
	}

	public void addPatchSet(PatchSet patchSet) {
		patchSets.add(patchSet);
		latestPatchSet = patchSets.get(patchSets.size()-1);
	}
	
	@Override
	public String getId() {
		return _id;
	}
}
