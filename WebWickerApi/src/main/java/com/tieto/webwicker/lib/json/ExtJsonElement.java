package com.tieto.webwicker.lib.json;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ExtJsonElement {
	private final JsonElement json;

	public ExtJsonElement(final JsonElement json) {
		this.json = json;
	}

	public ExtJsonElement(final String json) {
		this.json = new JsonParser().parse(json);
	}

	public int getInt(final int defValue, final Object... params) {
		final JsonElement result = getJsonElement(params);
		if (result == null || !result.isJsonPrimitive()) {
			return defValue;
		}
		try {
			return result.getAsInt();
		} catch(final NumberFormatException e) {
			return defValue;
		}
	}

	public long getLong(final long defValue, final Object... params) {
		final JsonElement result = getJsonElement(params);
		if (result == null || !result.isJsonPrimitive()) {
			return defValue;
		}
		try {
			return result.getAsLong();
		} catch(final NumberFormatException e) {
			return defValue;
		}
	}

	public String getString(final String defValue, final Object... params) {
		final JsonElement result = getJsonElement(params);
		if (result == null || !result.isJsonPrimitive()) {
			return defValue;
		}
		return result.getAsString();
	}

	public boolean getBoolean(final boolean defValue, final Object... params) {
		final JsonElement result = getJsonElement(params);
		if (result == null || !result.isJsonPrimitive()) {
			return defValue;
		}
		return result.getAsBoolean();
	}

	public JsonObject getJsonObject(final Object... params) {
		final JsonElement result = getJsonElement(params);
		if (result == null || !result.isJsonObject()) {
			return null;
		}
		return result.getAsJsonObject();
	}

	public JsonArray getJsonArray(final Object... params) {
		final JsonElement result = getJsonElement(params);
		if (result == null || !result.isJsonArray()) {
			return new JsonArray();
		}
		return result.getAsJsonArray();
	}

	public JsonElement getJsonElement(final Object... params) {
		JsonElement json = this.json;

		for (final Object param : params) {
			if (param instanceof Integer) {
				final JsonArray ja = json.getAsJsonArray();
				final Integer index = (Integer) param;
				if (index >= 0 && index < ja.size()) {
					json = ja.get(index);
				}
				else {
					json = null;
					break;
				}
			}
			else if (param instanceof String) {
				final JsonObject jo = json.getAsJsonObject();
				final String key = (String) param;
				if (jo.has(key)) {
					json = jo.get(key);
				}
				else {
					json = null;
					break;
				}
			}
		}

		return json;
	}

	public ExtJsonElement get(final Object... params) {
		final JsonElement result = getJsonElement(params);
		if (result == null) {
			return null;
		}
		return new ExtJsonElement(result);
	}

	public boolean contains(final Object... params) {
		return getJsonElement(params) != null;
	}

	public String findFirstString(final String defValue, final String path) {
		List<JsonElement> candidates = findElementsForPath(this.getJsonElement(), path);
		if(!candidates.isEmpty()) {
			for(JsonElement candidate : candidates) {
				try{
					if(candidate.isJsonPrimitive()) {
						return candidate.getAsString();
					}
				} catch(ClassCastException | IllegalStateException e) {
				}
			}
		}
		return defValue;
	}

	public static boolean match(ExtJsonElement json, String path, String value) {
		List<JsonElement> candidates = findElementsForPath(json.getJsonElement(), path); 
		for(JsonElement candidate : candidates) {
			try {
				if(candidate.isJsonPrimitive() && value.equals(candidate.getAsString())) {
					return true;
				}
			} catch(ClassCastException | IllegalStateException e) {
			}
		}
		return false;
	}

	private static List<JsonElement> findElementsForPath(JsonElement json, String path) {
		List<JsonElement> results = new LinkedList<JsonElement>();

		if(path.isEmpty()) {
			results.add(json);
		} else {
			if(path.startsWith(".")) {
				path = path.substring(1);
			}

			int dotPos = path.indexOf(".");
			int indexPos = path.indexOf("[");

			if(dotPos == -1 && indexPos == -1) {
				if(json.isJsonObject() && json.getAsJsonObject().has(path)) {
					results.add(json.getAsJsonObject().get(path));
				}
			} else if(path.startsWith("[")) {
				if(json.isJsonArray()) {
					String indexStr = path.substring(1, path.indexOf("]"));
					if(indexStr.equals("*")) {
						for(JsonElement j : json.getAsJsonArray()) {
							results.addAll(findElementsForPath(j, path.substring(path.indexOf("]")+1)));
						}
					} else {
						try {
							int index = Integer.parseInt(indexStr);
							if(index < json.getAsJsonArray().size()) {
								results.addAll(findElementsForPath(json.getAsJsonArray().get(index), path.substring(path.indexOf("]")+1)));
							}
						} catch(NumberFormatException e) {
						}
					}
				}
			} else {
				int index = lowestPositive(dotPos, indexPos);
				String key = path.substring(0, index);
				if(json.isJsonObject() && json.getAsJsonObject().has(key)) {
					results.addAll(findElementsForPath(json.getAsJsonObject().get(key), path.substring(index)));
				}
			}
		}

		return results;
	}

	private static int lowestPositive(int a, int b) {
		if(a == -1) {
			return b;
		} else if(b == -1) {
			return a;
		}
		return Math.min(a, b);
	}
}
