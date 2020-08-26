package com.sea.lyrad.util;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密解密工具
 */
public class AESCrypto {

    private final String rule;

    /**
     * @param rule 密钥
     */
    public AESCrypto(String rule) {
        this.rule = rule;
    }

    /**
     * 加密
     *
     * @param content 待加密字符串
     * @return 加密后的字符串
     */
    public String encode(String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(rule.getBytes(StandardCharsets.UTF_8));
            keygen.init(128, secureRandom);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteEncode = content.getBytes(StandardCharsets.UTF_8);
            byte[] byteAES = cipher.doFinal(byteEncode);
            return new String(Base64.getEncoder().encode(byteAES));
        } catch (Exception e) {
            Log.a("AES error.");
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content 待解密字符串
     * @return 解密后的字符串
     */
    public String decode(String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(rule.getBytes(StandardCharsets.UTF_8));
            keygen.init(128, secureRandom);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] byteContent = Base64.getDecoder().decode(content);
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            Log.a("AES error.");
            return null;
        }
    }
}
