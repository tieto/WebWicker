package com.tieto.webwicker.persistence;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.tieto.webwicker.api.conf.Configuration;
import com.tieto.webwicker.api.persistence.PersistenceLayer;
import com.tieto.webwicker.api.persistence.PersistenceLayerFactory;
import com.tieto.webwicker.lib.json.ExtJsonElement;

public class InMemoryStorage implements PersistenceLayer {
	private final Map<String,Map<String,JsonObject>> collections;
	
	public InMemoryStorage() {
		collections = new HashMap<>();
	}

	@Override
	public void store(String collection, JsonObject object, String id) {
		if(!collections.containsKey(collection)) {
			collections.put(collection, new LinkedHashMap<>());
		}
		collections.get(collection).put(id, object);
	}
	
	@Override
	public JsonObject fetchOneWithId(String collection, String id) {
		return collections.containsKey(collection) ? collections.get(collection).get(id) : null;
	}

	@Override
	public JsonObject fetchOne(String collection, String query) {
		if(collections.containsKey(collection) && query.contains("=")) {
			String path = query.substring(0, query.indexOf("="));
			String value = query.substring(query.indexOf("=")+1);
			
			for(JsonObject obj : collections.get(collection).values()) {
				if(ExtJsonElement.match(new ExtJsonElement(obj), path, value)) {
					return obj;
				}
			}
		}
		return null;
	}
	
	@Override
	public List<JsonObject> fetchMany(final String collection, final String sortBy, final boolean sortAscending, final String query) {
		List<JsonObject> result = collections.containsKey(collection) ? new LinkedList<>(collections.get(collection).values()) : Collections.emptyList();
		Collections.sort(result, new Comparator<JsonObject>() {
			@Override
			public int compare(JsonObject o1, JsonObject o2) {
				String s1 = o1.has(sortBy) ? o1.get(sortBy).getAsString() : "";
				String s2 = o2.has(sortBy) ? o2.get(sortBy).getAsString() : "";
				
				return sortAscending ? s2.compareTo(s1) : s1.compareTo(s2);
			}
		});
		return result;
	}

	@Override
	public long count(String collection, String query) {
		return collections.containsKey(collection) ? collections.get(collection).size() : 0;
	}
	
	public static class InMemoryStorageFactory extends PersistenceLayerFactory {
		@Override
		public PersistenceLayer create(Configuration configuration) {
			return new InMemoryStorage();
		}
	}
}
