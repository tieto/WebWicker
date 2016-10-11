package com.tieto.ciweb.api.persistence;

import java.util.List;

import com.google.gson.JsonObject;

public interface PersistenceLayer {
	void store(String collection, JsonObject object, String id);
	
	JsonObject fetchOneWithId(String collection, String id);
	JsonObject fetchOne(String collection, String query);
	List<JsonObject> fetchMany(String collection, String sortBy, boolean sortAscending, String query);
	
	long count(String collection, String query);
}
