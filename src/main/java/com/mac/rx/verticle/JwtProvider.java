package com.mac.rx.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class JwtProvider {

	private Vertx vertx;
	private ConfigRetriever configRetriever;

	public JwtProvider(Vertx vertx, ConfigRetriever configRetriever) {
		this.vertx = vertx;
		this.configRetriever = configRetriever;

	}

	// Read more at https://vertx.io/docs/vertx-web/java/#_jwt_authorisation
	public JWTAuth createJwtProvider() {
		JWTAuthOptions authConfig = new JWTAuthOptions()
				.setKeyStore(new KeyStoreOptions().setType("jceks").setPath("keystore.jceks").setPassword("secret"));
		return JWTAuth.create(vertx, authConfig);
	}
}
