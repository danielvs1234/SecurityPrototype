package com.example.securityprototype.Model;

import android.util.Log;

import com.example.securityprototype.Interfaces.IEncryption;

import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionHandler implements IEncryption {

    private final String pass = "c89SF3ff34J999";

    public EncryptionHandler(){

    }


    @Override
    public HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes) {
        HashMap<String, byte[]> map = new HashMap<String, byte[]>();

        try {

            //Create salt
            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[256];
            random.nextBytes(salt);

            //Get key from password
            char[] passwordChar = pass.toCharArray();
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 1234, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Create IV
            SecureRandom ivRandom = new SecureRandom();
            byte[] iv = new byte[16];
            ivRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            //Encrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainTextBytes);

            map.put("salt", salt);
            map.put("iv", iv);
            map.put("encrypted", encrypted);

        } catch(Exception e){
            Log.d("Encrypt", "encryptBytes: Method failed");
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public byte[] decryptData(HashMap<String, byte[]> map)
    {
        byte[] decryptedData = null;

        try
        {
            byte salt[] = map.get("salt");
            byte iv[] = map.get("iv");
            byte encrypted[] = map.get("encrypted");

            //Create key from pass
            char[] passwordChar = pass.toCharArray();
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 1234, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Decrypt bytes
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            decryptedData = cipher.doFinal(encrypted);
        }
        catch (Exception e){
            Log.d("Decrypt", "decryptData: Method Failed");
            e.printStackTrace();
        }

        return decryptedData;
    }

}
