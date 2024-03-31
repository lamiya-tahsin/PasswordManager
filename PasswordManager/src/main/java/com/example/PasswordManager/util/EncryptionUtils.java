package com.example.PasswordManager.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String MODE = "CBC";
    private static final String PADDING = "PKCS5Padding";
    private static final int KEY_LENGTH = 256; // 256 bits for AES-256
    private static final int IV_LENGTH = 16; // 16 bytes for AES
    private static final int ITERATION_COUNT = 65536; // Iteration count for key derivation
    private static final String SALT = "YourSalt"; // Change this to a random salt value

    private static final SecretKey secretKey = generateSecretKey();

    public static String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM + "/" + MODE + "/" + PADDING);
            byte[] iv = generateIV();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encryptedByteValue);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public static String decrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM + "/" + MODE + "/" + PADDING);
            String[] parts = value.split(":");
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] encryptedData = Base64.getDecoder().decode(parts[1]);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] decryptedByteValue = cipher.doFinal(encryptedData);
            return new String(decryptedByteValue);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

//    private static SecretKey generateSecretKey() {
//        try {
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            KeySpec spec = new PBEKeySpec("YourSecretEncryptionKey".toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGTH);
//            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ALGORITHM);
//        } catch (Exception e) {
//            throw new RuntimeException("Key generation failed", e);
//        }
//    }
private static SecretKey generateSecretKey() {
    try {
        // Use a fixed key instead of generating a random one
        byte[] keyBytes = "888YourSecretKey1234567890123456".getBytes(StandardCharsets.UTF_8); // Change the key to be 16, 24, or 32 bytes long
        return new SecretKeySpec(keyBytes, ALGORITHM);
    } catch (Exception e) {
        throw new RuntimeException("Key generation failed", e);
    }
}



    private static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
