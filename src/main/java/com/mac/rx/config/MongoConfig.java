package com.mac.rx.config;

import io.vertx.core.json.JsonObject;

public class MongoConfig {
	private final String connectionString;
	private final String mongoDb;

	public MongoConfig(String connectionString, String mongoDb) {
		this.connectionString = connectionString;
		this.mongoDb = mongoDb;

	}

	public JsonObject config() {
		return new JsonObject().put("connection_string", connectionString).put("db_name", mongoDb);
	}
}
