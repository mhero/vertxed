package com.mac.rx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class Keys {
	// Read more at https://www.baeldung.com/java-keystore
	public Keys() {
		try {
			KeyStore ks = KeyStore.getInstance("jceks");
			char[] pwdArray = "secret".toCharArray();
			ks.load(null, pwdArray);

			try (FileOutputStream fos = new FileOutputStream("keystore.jceks")) {
				ks.store(fos, pwdArray);
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}

	}
}
