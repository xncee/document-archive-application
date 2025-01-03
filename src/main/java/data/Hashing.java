package data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Hashing {

    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16]; // 16 bytes salt
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static String hash(String value) throws NoSuchAlgorithmException {

        byte[] salt = generateSalt();

        // Create the SHA-256 MessageDigest instance
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Combine the value bytes and the salt
        digest.update(salt);
        byte[] hashedPassword = digest.digest(value.getBytes());

        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String encodedHash = Base64.getEncoder().encodeToString(hashedPassword);

        return encodedSalt + ":" + encodedHash;
    }

    // Method to match the entered value with the stored hash
    public static boolean match(String storedHash, String enteredValue) throws NoSuchAlgorithmException {
        // Split the stored hash into salt and hashed value
        String[] parts = storedHash.split(":");
        if (parts.length != 2) {
            return false;
        }

        String storedSaltBase64 = parts[0];
        String storedHashBase64 = parts[1];

        byte[] salt = Base64.getDecoder().decode(storedSaltBase64);
        byte[] decodedStoredHash  = Base64.getDecoder().decode(storedHashBase64);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hashedPassword = digest.digest(enteredValue.getBytes());

        return MessageDigest.isEqual(decodedStoredHash , hashedPassword);
    }
}
