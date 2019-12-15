package com.example.securityprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
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


    private Button button;
    private Button decryptButton;
    private Button deleteButton;
    private Button goToMapButton;
    private TextView textView;
//    private EditText inputField;
//    private IStorage storageHandler;
//    private IEncryption encryptionHandler;
    private TrackController trackController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.saveButton);
        decryptButton =  findViewById(R.id.decryptButton);
        deleteButton = findViewById(R.id.deleteButton);
        goToMapButton = findViewById(R.id.goToMapsButton);

        textView = findViewById(R.id.textView1);
        textView.setMovementMethod(new ScrollingMovementMethod());
//        inputField = findViewById(R.id.textField1);
//
//        storageHandler = new StorageHandler(getApplicationContext());
//        encryptionHandler = new EncryptionHandler();

        trackController = new TrackController(getApplicationContext());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // encryptTextFromFieldAndSaveIt();

                trackController.newTrack();
                trackController.newTrack();
                trackController.newTrack();
                trackController.saveTrackArrayToStorage();


            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  decryptTextFromHashMapAndShowIt();
                ArrayList<Track> tracks = trackController.loadTrackArrayListFromStorage();
                textView.append( "Track 1: " + "Latlng: " +tracks.get(0).getDateAndTime());
                textView.append( "Track 2: " + "Latlng: " +tracks.get(1).getDateAndTime());
                textView.append( "Track 3: " + "Latlng: " +tracks.get(2).getDateAndTime());

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getApplicationContext().deleteFile("map.dat");
            }
        });

        goToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

    }

//    private void encryptTextFromFieldAndSaveIt(){
//        String text = inputField.getText().toString();
//        textView.append("Text without encryption: " + text + "\n");
//
//        if(storageHandler.checkIfFileExists()){
//            StringBuilder stringBuilder = new StringBuilder();
//            String string = new String(encryptionHandler.decryptData((HashMap<String, byte[]>) storageHandler.read("")));
//            stringBuilder.append(string);
//            stringBuilder.append(text);
//
//            HashMap<String, byte[]> map = encryptionHandler.encryptBytes(stringBuilder.toString().getBytes());
//            storageHandler.write(map, "");
//        } else{
//            HashMap<String, byte[]> map = encryptionHandler.encryptBytes(text.getBytes());
//            storageHandler.write(map, "");
//        }
//
//
//    }

//    private byte[] decryptTextFromHashMapAndShowIt(){
//
//        if(storageHandler.checkIfFileExists()) {
//
//
//            Map<String, byte[]> map = storageHandler.read("");
//           // textView.append("Text before decryption: " + map.toString() + "\n");
//
//            byte[] decryptedData = encryptionHandler.decryptData((HashMap<String, byte[]>) map);
//            String string = new String(decryptedData);
//            textView.append("Text after decryption: " + string + "\n");
//
//            return decryptedData;
//        } else{
//            textView.append("File not Found");
//            return null;
//        }
//    }









}