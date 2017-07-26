package com.mac.rx;

import org.mindrot.jbcrypt.BCrypt;

public class Password {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: Password <plaintext-password>");
            System.exit(1);
        }

        String hashed = BCrypt.hashpw(args[0], BCrypt.gensalt());
        System.out.println("Add this to my-config.json as user_password:");
        System.out.println(hashed);
    }

    public static boolean verify(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }
}