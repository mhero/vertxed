package com.mac.rx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordTest {

    @Test
    void testVerifyCorrectPassword() {
        String plaintext = "secret";
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(plaintext, org.mindrot.jbcrypt.BCrypt.gensalt());

        assertTrue(Password.verify(plaintext, hashed));
    }

    @Test
    void testVerifyWrongPassword() {
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw("secret", org.mindrot.jbcrypt.BCrypt.gensalt());

        assertFalse(Password.verify("wrongpassword", hashed));
    }

    @Test
    void testVerifyEmptyPassword() {
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw("secret", org.mindrot.jbcrypt.BCrypt.gensalt());

        assertFalse(Password.verify("", hashed));
    }
}