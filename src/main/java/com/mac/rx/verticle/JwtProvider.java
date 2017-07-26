package com.mac.rx.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.Future;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class JwtProvider {

    private static final String KEYSTORE_TYPE = "keystore_type";
    private static final String KEYSTORE_PATH = "keystore_path";
    private static final String KEYSTORE_PASSWORD = "keystore_password";

    private final Vertx vertx;
    private final ConfigRetriever configRetriever;
    private KeyStoreOptions keyStoreOptions;

    public JwtProvider(Vertx vertx, ConfigRetriever configRetriever) {
        this.vertx = vertx;
        this.configRetriever = configRetriever;
    }

    public Future<JWTAuth> createJwtProvider() {
        return configRetriever.getConfig()
            .map(config -> {
                this.keyStoreOptions = new KeyStoreOptions()
                    .setType(config.getString(KEYSTORE_TYPE))
                    .setPath(config.getString(KEYSTORE_PATH))
                    .setPassword(config.getString(KEYSTORE_PASSWORD));

                JWTAuthOptions authConfig = new JWTAuthOptions().setKeyStore(this.keyStoreOptions);
                return JWTAuth.create(vertx, authConfig);
            });
    }
}
