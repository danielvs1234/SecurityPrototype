package com.example.securityprototype.Model;

import android.content.Context;
import android.util.Log;

import com.example.securityprototype.Interfaces.IStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StorageHandler implements IStorage {

    private Context context;

    public StorageHandler(Context context){
        this.context = context;
    }



    @Override
    public void write(HashMap<String, byte[]> map, String filename) {

        FileOutputStream fileOutputStream;

        try {

            fileOutputStream = this.context.openFileOutput("map.dat", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
            os.writeObject(map);
            os.close();
            fileOutputStream.close();
            Log.d("saveStorage", "write, method successful");

        }catch(IOException e){
            Log.d("Save to Storage", "saveMapToStorage: Method failed ");
            e.printStackTrace();
        }

    }

    @Override
    public Map read(String filename) {

        Map<String, byte[]> map;
        try {

            FileInputStream fis = context.openFileInput("map.dat");
            ObjectInputStream input = new ObjectInputStream(fis);
            map = (Map<String, byte[]> ) input.readObject();
            input.close();
            fis.close();

        } catch (IOException e) {
            Log.d("Read from Storage", "getMapFromStorage: Method failed");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("storage.Read", "could not find target class");
            return null;
        }

        return map;
    }

    @Override
    public boolean checkIfFileExists(){
        File file = context.getFileStreamPath("map.dat");
        return file.exists();
    }


    private void debugHashMap(Map map, String string){
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Log.d("debugHashMap", "Map: " + string + " Key: "  + pair.getKey() + "  Value: " + pair.getValue() + "\n");
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
