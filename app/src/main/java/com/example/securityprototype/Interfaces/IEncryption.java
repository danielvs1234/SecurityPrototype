package com.example.securityprototype.Interfaces;

import java.util.HashMap;

public interface IEncryption {

    HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes);
    byte[] decryptData(HashMap<String, byte[]> map);
}
