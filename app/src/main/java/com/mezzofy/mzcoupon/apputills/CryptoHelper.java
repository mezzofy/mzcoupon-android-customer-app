package com.mezzofy.mzcoupon.apputills;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class CryptoHelper {

    public static String encryptAES(String data, byte[] ivx, byte[] key) throws Exception {

        byte[] srcBuff = data.getBytes("UTF8");

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivx);
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

        byte[] dstBuff = ecipher.doFinal(srcBuff);

        String base64 = Base64.encodeToString(dstBuff, Base64.DEFAULT);

        return base64;
    }


    public static String decryptAES(String encrypted, byte[] ivx, byte[] key) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivx);
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

        byte[] raw = Base64.decode(encrypted, Base64.DEFAULT);

        byte[] originalBytes = ecipher.doFinal(raw);

        String original = new String(originalBytes, "UTF8");

        return original;
    }
}