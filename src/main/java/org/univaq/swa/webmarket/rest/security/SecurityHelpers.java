/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.security;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SecurityHelpers {


    //--------- PASSWORD SECURITY ------------
    //support functions for the password hashing functions
    private static String bytesToHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            char[] hexDigits = new char[2];
            hexDigits[0] = Character.forDigit((byteArray[i] >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((byteArray[i] & 0xF), 16);
            hexStringBuffer.append(new String(hexDigits));
        }
        return hexStringBuffer.toString();
    }

    private static byte[] hexStringToBytes(String hexString) {
        byte[] byteArray = new byte[hexString.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            int val = Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
            byteArray[i] = (byte) val;
        }
        return byteArray;
    }


    //password hashing with PBKDF2 + salt
    private static String getPasswordHashPBKDF2(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (salt.length != 16) {
            throw new IllegalArgumentException("Salt must be 16 bytes");
        }
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        byte[] digest = factory.generateSecret(spec).getEncoded();
        return bytesToHexString(salt) + bytesToHexString(digest);
    }

    public static String getPasswordHashPBKDF2(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return getPasswordHashPBKDF2(password, salt);
    }

    //check password hashed by getPasswordHashPBKDF2
    public static boolean checkPasswordHashPBKDF2(String password, String passwordhash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        System.arraycopy(hexStringToBytes(passwordhash), 0, salt, 0, 16);
        return (getPasswordHashPBKDF2(password, salt)).equals(passwordhash);
    }

}
