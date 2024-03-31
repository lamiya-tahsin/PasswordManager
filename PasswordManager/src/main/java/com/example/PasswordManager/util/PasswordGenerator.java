package com.example.PasswordManager.util;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC_CHARACTERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=";

    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder();

        // SecureRandom for generating random numbers
        SecureRandom random = new SecureRandom();

        // At least one character from each character set
        password.append(UPPERCASE_CHARACTERS.charAt(random.nextInt(UPPERCASE_CHARACTERS.length())));
        password.append(LOWERCASE_CHARACTERS.charAt(random.nextInt(LOWERCASE_CHARACTERS.length())));
        password.append(NUMERIC_CHARACTERS.charAt(random.nextInt(NUMERIC_CHARACTERS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Remaining characters are random
        for (int i = 4; i < length; i++) {
            String characterSet = UPPERCASE_CHARACTERS + LOWERCASE_CHARACTERS + NUMERIC_CHARACTERS + SPECIAL_CHARACTERS;
            password.append(characterSet.charAt(random.nextInt(characterSet.length())));
        }

        // Shuffle the characters to make the password more random
        String shuffledPassword = shuffle(password.toString());
        return shuffledPassword;
    }

    private static String shuffle(String str) {
        char[] array = str.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int index = new SecureRandom().nextInt(i + 1);
            char temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return new String(array);
    }
}
