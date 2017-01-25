package com.tieto.webwicker.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InMemoryDB {
    private static final Logger log = LoggerFactory.getLogger(InMemoryDB.class);
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
	
	public void storeToDisk(String filePath) {
		JsonObject outputData = new JsonObject();
		
		for(String collectionName : collections.keySet()) {
			JsonObject collection = new JsonObject();
			for(Entry<String,JsonObject> entry : collections.get(collectionName).entrySet()) {
				collection.add(entry.getKey(), entry.getValue());
			}
			outputData.add(collectionName, collection);
		}
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
			oos.writeObject(outputData.toString());
			oos.close();
		} catch (IOException e) {
			log.error("Exception storing data: "+e.getMessage());
		}
	}
	
	public void readFromDisk(String filePath) {
		File file = new File(filePath);
		if(file.canRead()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))){
				JsonObject json = new JsonParser().parse((String) ois.readObject()).getAsJsonObject();
				for(Entry<String,JsonElement> collectionEntry : json.entrySet()) {
					String collectionName = collectionEntry.getKey();
					Map<String,JsonObject> collection = new LinkedHashMap<>();
					for(Entry<String,JsonElement> entry : collectionEntry.getValue().getAsJsonObject().entrySet()) {
						collection.put(entry.getKey(), entry.getValue().getAsJsonObject());
					}
					collections.put(collectionName, collection);
				}
			} catch (IOException | ClassNotFoundException e) {
				log.error("Exception loading data: "+e.getMessage());
			}
		}
	}
}
