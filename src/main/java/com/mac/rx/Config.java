package com.mac.rx;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Config {
	// Read more https://vertx.io/docs/vertx-config/java/
	
	public ConfigRetriever getConfigFile(Vertx vertx) {
		ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file")
				.setConfig(new JsonObject().put("path", "my-config.json"));

		ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);
		return ConfigRetriever.create(vertx, options);

	}
}
