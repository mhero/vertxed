package com.mac.rx.verticle;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class JwtProvider {
	// Read more at https://vertx.io/docs/vertx-web/java/#_jwt_authorisation
	public JWTAuth createJwtProvider(Vertx vertx) {
		JWTAuthOptions authConfig = new JWTAuthOptions()
				.setKeyStore(new KeyStoreOptions().setType("jceks").setPath("keystore.jceks").setPassword("secret"));
		return JWTAuth.create(vertx, authConfig);
	}
}
