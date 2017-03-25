package com.nullio.opinieallegro;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class CryptoUtils {

    public static String SHA256 (String text) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");


            md.update(text.getBytes());
            byte[] digest = md.digest();

            return Base64.encodeToString(digest, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
