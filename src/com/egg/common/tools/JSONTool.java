package com.egg.common.tools;

import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONTool {

	public static String get(JSONObject json, String name, String def) {
		if (json.containsKey(name)) {
			return json.getString(name);
		}
		return def;
	}

	public static int get(JSONObject json, String name, int def) {
		if (json.containsKey(name)) {
			return NumberUtils.toInt(json.getString(name), def);
		}
		return def;
	}

	public static long get(JSONObject json, String name, long def) {
		if (json.containsKey(name)) {
			return NumberUtils.toLong(json.getString(name), def);
		}
		return def;
	}

	public static double get(JSONObject json, String name, double def) {
		if (json.containsKey(name)) {
			return NumberUtils.toDouble(json.getString(name), def);
		}
		return def;
	}

	public static JSONArray get(JSONObject json, String name, JSONArray def) {
		if (json.containsKey(name)) {
			return json.getJSONArray(name);
		}
		return def;
	}

	public static void put(JSONObject json, String name, String val) {
		json.put(name, (val == null ? "" : val));
	}

	public static void put(JSONObject json, String name, Integer val) {
		json.put(name, (val == null ? 0 : val));
	}

	public static void put(JSONObject json, String name, Long val) {
		json.put(name, (val == null ? 0L : val));
	}

	public static void put(JSONObject json, String name, Boolean val) {
		json.put(name, ((val == null || !val) ? 0 : 1));
	}

	public static void put(JSONObject json, String name, JSONObject val) {
		json.put(name, (val == null ? new JSONObject() : val));
	}

	public static void put(JSONObject json, String name, JSONArray val) {
		json.put(name, (val == null ? new JSONArray() : val));
	}

	public static void JSONObject(JSONObject json, String name, JSONArray val) {
		json.put(name, (val == null ? new JSONArray() : val));
	}

}
