package com.mac.rx.movie;

import io.vertx.core.json.JsonObject;

public class Movie {

	private String id;

	private String name;

	private String rate;

	public Movie(String name, String rate) {
		this.name = name;
		this.rate = rate;
		this.id = "";
	}

	public Movie(JsonObject json) {
		this.name = json.getString("name");
		this.rate = json.getString("rate");
		this.id = json.getString("_id");
	}

	public Movie() {
		this.id = "";
	}

	public Movie(String id, String name, String rate) {
		this.id = id;
		this.name = name;
		this.rate = rate;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject().put("name", name).put("rate", rate);
		if (id != null && !id.isEmpty()) {
			json.put("_id", id);
		}
		return json;
	}

	public String getName() {
		return name;
	}

	public String getRate() {
		return rate;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrigin(String rate) {
		this.rate = rate;
	}

	public void setId(String id) {
		this.id = id;
	}
}
