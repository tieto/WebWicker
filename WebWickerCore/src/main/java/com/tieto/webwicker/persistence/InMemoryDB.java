package com.tieto.webwicker.persistence;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class InMemoryDB {
	private static InMemoryDB INSTANCE = null;
	
	private final Map<String,Map<String,JsonObject>> collections;

	private InMemoryDB() {
		collections = new HashMap<>();
	}
	
	public static InMemoryDB getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new InMemoryDB();
		}
		return INSTANCE;
	}
	
	public Map<String,Map<String,JsonObject>> collections() {
		return collections;
	}
}
