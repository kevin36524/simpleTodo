package com.example.patelkev.simpletodo;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by patelkev on 9/24/16.
 */

public class PersistanceManager {
    private static PersistanceManager instance = null;
    private static String fileName = "tododb.txt";
    public Context applicationContext = null;
    protected PersistanceManager() {
        // Exists only to defeat instantiation.
    }
    public static PersistanceManager sharedInstance() {
        if (instance == null) {
            instance = new PersistanceManager();
        }
        return instance;
    }

    public ArrayList<String> readItems() {
        ArrayList<String> items = new ArrayList<String>();
        try {
            FileInputStream fileInputStream = applicationContext.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            items = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            return items;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return items;
        }

        return items;
    }

    public void writeItems(ArrayList<String> items) {
        FileOutputStream fileOutputStream = null;
        applicationContext.deleteFile(fileName);
        try {
            fileOutputStream = applicationContext.openFileOutput(fileName,applicationContext.MODE_APPEND);
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
            oos.writeObject(items);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}