package com.sea.pxxd.util;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESCrypto {

    private String rule;

    public AESCrypto(String rule) {
        this.rule = rule;
    }

    public String encode(String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(rule.getBytes());
            keygen.init(128, secureRandom);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteEncode = content.getBytes("utf-8");
            byte[] byteAES = cipher.doFinal(byteEncode);
            return new String(Base64.getEncoder().encode(byteAES));
        } catch (Exception e) {
            Log.a("AES error.");
            return null;
        }
    }

    public String decode(String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(rule.getBytes());
            keygen.init(128, secureRandom);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] byteContent = Base64.getDecoder().decode(content);
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            Log.a("AES error.");
            return null;
        }
    }
}
