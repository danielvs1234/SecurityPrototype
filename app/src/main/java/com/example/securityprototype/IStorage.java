package com.example.securityprototype;

import java.util.HashMap;
import java.util.Map;

public interface IStorage {

    void write(HashMap<String, byte[]> map, String filename);
    Map read(String filename);
    boolean checkIfFileExists();

}
