package com.mac.rx.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class JwtProvider {

	private final Vertx vertx;
	private final ConfigRetriever configRetriever;
	private KeyStoreOptions keyStoreOptions;

	private static final String KEYSTORE_TYPE = "keystore_type";
	private static final String KEYSTORE_PATH = "keystore_path";
	private static final String KEYSTORE_PASSWORD = "keystore_password";

	public JwtProvider(Vertx vertx, ConfigRetriever configRetriever) {
		this.vertx = vertx;
		this.configRetriever = configRetriever;
		createKeyStoreOptions();
	}

	// Read more at https://vertx.io/docs/vertx-web/java/#_jwt_authorisation
	public JWTAuth createJwtProvider() {
		JWTAuthOptions authConfig = new JWTAuthOptions().setKeyStore(this.keyStoreOptions);
		return JWTAuth.create(vertx, authConfig);
	}

	public void createKeyStoreOptions() {
		this.configRetriever.getConfig(json -> {
			JsonObject configFile = json.result();
			this.keyStoreOptions = new KeyStoreOptions().setType(configFile.getString(KEYSTORE_TYPE))
					.setPath(configFile.getString(KEYSTORE_PATH)).setPassword(configFile.getString(KEYSTORE_PASSWORD));
		});
	}

}
