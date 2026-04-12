package com.mac.rx;

import org.mindrot.jbcrypt.BCrypt;
import java.nio.file.Files;
import java.nio.file.Paths;
import io.vertx.core.json.JsonObject;

public class Password {

    private static final String CONFIG_PATH = "my-config.json";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: Password <plaintext-password>");
            System.exit(1);
        }

        String hashed = BCrypt.hashpw(args[0], BCrypt.gensalt());

        try {
            String raw = Files.readString(Paths.get(CONFIG_PATH));
            JsonObject config = new JsonObject(raw);
            config.put("user_password", hashed);
            Files.writeString(Paths.get(CONFIG_PATH), config.encodePrettily());
            System.out.println("my-config.json updated with hashed password.");
        } catch (Exception e) {
            System.err.println("Failed to update config: " + e.getMessage());
            System.err.println("Hash manually: " + hashed);
            System.exit(1);
        }
    }

    public static boolean verify(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }
}