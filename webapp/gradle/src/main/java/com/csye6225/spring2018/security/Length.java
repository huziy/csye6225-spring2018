package com.csye6225.spring2018.security;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Length {

    public static String getHash(String password) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] message = messageDigest.digest(password.getBytes());
            BigInteger number = new BigInteger(1, message);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
