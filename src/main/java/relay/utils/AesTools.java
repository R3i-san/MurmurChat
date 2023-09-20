package relay.utils;

import relay.control.Config;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class AesTools {

    public static String encrypt(String text, byte[] IV, String key)
    {
        byte[] msg = text.getBytes();

        // Get Cipher Instance
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");

            // Create SecretKeySpec
            SecretKeySpec keySpec = new SecretKeySpec(computeKey(key).getEncoded(), "AES");

            // Create GCMParameterSpec
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(Config.GCM_TAG_LENGTH * 8, IV);

            // Initialize Cipher for ENCRYPT_MODE
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

            // Perform Encryption

            return inBase64(IV, cipher.doFinal(msg)) + "\r\n";

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String text, String key) {

        byte[] bytes = fromBase64(text);
        byte[] IV = Arrays.copyOfRange(bytes, 0, Config.GCM_IV_LENGTH);
        byte[] msg = Arrays.copyOfRange(bytes, Config.GCM_IV_LENGTH, bytes.length);

        try {
            // Get Cipher Instance
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            // Create SecretKeySpec
            SecretKeySpec keySpec = new SecretKeySpec(computeKey(key).getEncoded(), "AES");

            // Create GCMParameterSpec
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(Config.GCM_TAG_LENGTH * 8, IV);

            // Initialize Cipher for DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

            // Perform Decryption
            byte[] decryptedText = cipher.doFinal(msg);

            return new String(decryptedText);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return null;

    }

    private static String inBase64(byte[] IV, byte[] msg){
        byte[] total = new byte[IV.length + msg.length];

        System.arraycopy(IV, 0, total, 0, IV.length);
        System.arraycopy(msg, 0, total, IV.length, msg.length);

        return Base64.getEncoder().encodeToString(total);
    }

    private static byte[] fromBase64(String msg){
        return Base64.getDecoder().decode(msg);
    }

    public static SecretKey computeKey(String plainkey){
        String key = plainkey;
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static byte[] generateIV(){
        byte[] IV = new byte[Config.GCM_IV_LENGTH];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(IV);
        return IV;
    }

}
