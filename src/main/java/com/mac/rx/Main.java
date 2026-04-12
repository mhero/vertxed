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

        configRetriever.getConfig()
                .onFailure(err -> {
                    System.err.println("Failed to load config: " + err.getMessage());
                    System.exit(1);
                })
                .onSuccess(config -> {
                    System.out.println("Config loaded successfully.");
                    JsonObject mongoConfig = new MongoConfig(
                            config.getString("connectionString"),
                            config.getString("mongoDb")
                    ).config();

                    vertx.deployVerticle(new RestVerticle(config.getInteger("vertxPort"), mongoConfig, configRetriever))
                            .onFailure(err -> {
                                System.err.println("Failed to deploy verticle: " + err.getMessage());
                                System.exit(1);
                            });
                });
    }
}