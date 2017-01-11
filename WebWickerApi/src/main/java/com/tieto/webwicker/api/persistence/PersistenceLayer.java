package com.tieto.webwicker.api.persistence;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonObject;

public interface PersistenceLayer extends Serializable {
	void store(String collection, JsonObject object, String id);
	
	JsonObject fetchOneWithId(String collection, String id);
	JsonObject fetchOne(String collection, String query);
	List<JsonObject> fetchMany(String collection, String sortBy, boolean sortAscending, String query);
	
	long count(String collection, String query);
}
