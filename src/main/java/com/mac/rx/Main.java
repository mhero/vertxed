/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.rx;

import com.mac.rx.config.AppConfig;
import com.mac.rx.config.MongoConfig;
import com.mac.rx.verticle.RestVerticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author marco
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();

		Integer vertxPort = 8080;
		String connectionString = "mongodb://localhost:27017";
		String mongoDb = "vertxd";

		if (args == null || args.length == 0) {
			System.out.println("using default args");
			System.out.println("try: localhost:" + vertxPort);
		} else {
			try {
				vertxPort = Integer.parseInt(args[0]);
				connectionString = String.valueOf(args[1]);
				mongoDb = String.valueOf(args[2]);
			} catch (Exception nfe) {
				System.out.println("bad type/number of args: " + args.length);

			}
		}

		ConfigRetriever configRetriever = new AppConfig().getConfigFile(vertx);
		JsonObject mongoConfig = new MongoConfig(connectionString, mongoDb).config();
		vertx.deployVerticle(new RestVerticle(vertxPort, mongoConfig, configRetriever));
	}

}
