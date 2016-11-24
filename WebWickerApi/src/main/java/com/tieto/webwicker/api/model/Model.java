package com.tieto.webwicker.api.model;

import java.io.Serializable;

import com.google.gson.JsonObject;

public interface Model extends Serializable {
	void fromJson(JsonObject json);
	JsonObject toJson();
	String getId();
}
