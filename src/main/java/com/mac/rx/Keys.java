package com.mac.rx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class Keys {

    public static void main(String[] args) {
        String password = args.length > 0 ? args[0] : "secret";

        try {
            KeyStore ks = KeyStore.getInstance("jceks");
            char[] pwdArray = password.toCharArray();
            ks.load(null, pwdArray);

            try (FileOutputStream fos = new FileOutputStream("keystore.jceks")) {
                ks.store(fos, pwdArray);
            }

            System.out.println("Keystore generated at keystore.jceks");
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            System.err.println("Failed to generate keystore: " + e.getMessage());
            System.exit(1);
        }
    }
}