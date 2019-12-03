package com.example.securityprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private final String pass = "c89SF3ff34J999";

    private Button button;
    private Button decryptButton;
    private TextView textView;
    private EditText inputField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.saveButton);
        decryptButton =  findViewById(R.id.decryptButton);
        textView = findViewById(R.id.textView1);
        inputField = findViewById(R.id.textField1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encryptTextFromFieldAndSaveIt();
                textView.append("String saved to Storage: " + inputField.getText().toString());

            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.append(decryptTextFromHashMapAndShowIt().toString());
            }
        });

    }

    private void encryptTextFromFieldAndSaveIt(){
        String text = inputField.getText().toString();
        HashMap<String, byte[]> map = encryptBytes(text.getBytes());
        saveMapToStorage(map);
    }

    private byte[] decryptTextFromHashMapAndShowIt(){
        Map<String, byte[]> map = getMapFromStorage();
        byte[] decryptedData = decryptData((HashMap) map);
        return decryptedData;

    }

    private void saveMapToStorage(Map map){

        try {
            FileOutputStream fos = openFileOutput("map.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(map);
            oos.close();
        }catch(Exception e){
            Log.d("Save to Storage", "saveMapToStorage: Method failed ");
            e.printStackTrace();
        }
    }

    private Map getMapFromStorage(){

        Map<String, byte[]> map;
        try {
            FileInputStream fis = new FileInputStream("map.dat");
            ObjectInputStream input = new ObjectInputStream(fis);
            map = (Map<String, byte[]> ) input.readObject();
            fis.read();

        } catch (Exception e) {
            Log.d("Get from Storage", "getMapFromStorage: Method failed");
            e.printStackTrace();
            return null;
        }

        return map;
    }



    private HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes) {
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



    private byte[] decryptData(HashMap<String, byte[]> map)
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
            Log.d("Decrypt", "decryptData: Method Failes");
            e.printStackTrace();
        }

        return decryptedData;
    }
}