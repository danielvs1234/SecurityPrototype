package com.example.securityprototype.Interfaces;

import java.util.HashMap;
import java.util.Map;

public interface IEncryption {

    Map<String, byte[]> encryptBytes(byte[] plainTextBytes);
    byte[] decryptData(HashMap<String, byte[]> map);
}
