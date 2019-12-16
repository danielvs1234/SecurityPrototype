package com.example.securityprototype.Interfaces;

import java.util.HashMap;
import java.util.Map;

public interface IStorage {

    void write(HashMap<String, byte[]> map, String filename);
    Map read(String filename);
    boolean checkIfFileExists();

}
