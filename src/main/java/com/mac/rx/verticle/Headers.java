package com.mac.rx.verticle;

import java.util.HashSet;
import java.util.Set;

public class Headers {
    static final Set<String> allowedHeaders = new HashSet<>();

    static {
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-allowed-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");
    }

}
