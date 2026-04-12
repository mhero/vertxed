package com.mac.rx.config;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MongoConfigTest {

    @Test
    void testConfig() {
        MongoConfig mongoConfig = new MongoConfig("mongodb://localhost:27017", "testdb");
        JsonObject config = mongoConfig.config();

        assertEquals("mongodb://localhost:27017", config.getString("connection_string"));
        assertEquals("testdb", config.getString("db_name"));
    }

    @Test
    void testConfigWithDifferentValues() {
        MongoConfig mongoConfig = new MongoConfig("mongodb://pe-mongodb:27017", "vertxd");
        JsonObject config = mongoConfig.config();

        assertEquals("mongodb://pe-mongodb:27017", config.getString("connection_string"));
        assertEquals("vertxd", config.getString("db_name"));
    }
}