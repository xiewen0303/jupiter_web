package com.jupiter.web.manager.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Random;

public class AESUtil {

    private static final String CYPHER_MODE = "AES/CBC/NoPadding";

    public static String getRandomKey() {
        Random random = new Random();
        String key = random.nextInt(1000) + "key" + random.nextInt(1000);
        key = Utils.encodeMD5(key);
        return key.substring(0, 16);
    }

    public static byte[] encrypt(byte[] key, byte[] initVector, byte[] value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(CYPHER_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            int blockSize = cipher.getBlockSize();
            byte[] plaintext = padding(value, blockSize);
            return cipher.doFinal(plaintext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] key, byte[] initVector, byte[] encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(CYPHER_MODE);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return unpadding(cipher.doFinal(encrypted));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static byte[] padding(byte[] value, int blockSize) {
        int plaintextLength = value.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(value, 0, plaintext, 0, value.length);
        return plaintext;
    }

    private static byte[] unpadding(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

    public static void main(String[] args) {
//        try {
//            byte[] key = "uxc4REON2aCMgd66".getBytes();
//            byte[] iv = "uxc4REON2aCMgd66".getBytes();
//            byte[] content = "{\"addChannel\":\"app\",\"addProduct\":\"India\",\"devicePlatform\":\"android\",\"marketChannel\":\"google_play\",\"mobile\":\"1861708643\",\"password\":\"33333\",\"productVersion\":\"1.0.0\"}".getBytes("utf-8");
//            byte[] cyphertext = encrypt(key, iv, content);
//            String b64 = new String(Base64.encode(cyphertext));
//            System.out.println(b64);
//            byte[] de_b64 = decrypt(key, iv, Base64.decodeToBytes(b64));
//            String plaintext = new String(de_b64);
//            System.out.println(plaintext);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
    }
}
