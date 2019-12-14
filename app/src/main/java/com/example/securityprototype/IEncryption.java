package com.example.securityprototype;

import java.util.HashMap;

public interface IEncryption {

    HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes);
    byte[] decryptData(HashMap<String, byte[]> map);
}
