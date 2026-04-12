package com.mac.rx;

import com.mac.rx.config.AppConfig;
import com.mac.rx.config.MongoConfig;
import com.mac.rx.verticle.RestVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        ConfigRetriever configRetriever = new AppConfig().getConfigFile(vertx);

        configRetriever.getConfig().onSuccess(config -> {
            int port = config.getInteger("vertxPort");
            String connectionString = config.getString("connectionString");
            String mongoDb = config.getString("mongoDb");

            JsonObject mongoConfig = new MongoConfig(connectionString, mongoDb).config();
            vertx.deployVerticle(new RestVerticle(port, mongoConfig, configRetriever))
                    .onFailure(err -> {
                        System.err.println("Failed to deploy verticle: " + err.getMessage());
                        System.exit(1);
                    });
        }).onFailure(err -> {
            System.err.println("Failed to load config: " + err.getMessage());
            System.exit(1);
        });
    }
}