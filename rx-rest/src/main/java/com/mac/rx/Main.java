/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mac.rx;

import io.vertx.core.Vertx;

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
        vertx.deployVerticle(new MyFirstVerticle());
        vertx.deployVerticle(new MyFirstVerticle());
    }

}
