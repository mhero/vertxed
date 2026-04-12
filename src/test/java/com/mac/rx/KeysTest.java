package com.mac.rx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class KeysTest {

    @TempDir
    Path tempDir;

    @Test
    void testMainGeneratesKeystore() {
        String keystorePath = tempDir.resolve("keystore.jceks").toString();
        System.setProperty("user.dir", tempDir.toString());

        Keys.main(new String[]{"testpassword"});

        File keystore = new File("keystore.jceks");
        assertTrue(keystore.exists());
        assertTrue(keystore.length() > 0);
        keystore.delete();
    }

    @Test
    void testMainWithNoArgsFallsBackToDefault() {
        Keys.main(new String[]{});

        File keystore = new File("keystore.jceks");
        assertTrue(keystore.exists());
        assertTrue(keystore.length() > 0);
        keystore.delete();
    }
}