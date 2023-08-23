package ru.green.nca.security;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomPasswordGenerator {

    private static final int PASSWORD_LENGTH = 10; // Длина генерируемого пароля

    public static String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[PASSWORD_LENGTH];
        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}