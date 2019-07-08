package com.watson.annababy;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * id管理
 *    * 生成id
 *    * 持久化id
 */
public class IdManager {

    final String TAG = "ID_MANAGER";

    final String UUID_FILE_PATH = "/sdcard/annababy.uuid";

    String genId() {
        return UUID.randomUUID().toString();
    }

    void saveToFile(String id) {
        try{
            File f = new File(UUID_FILE_PATH);
            BufferedWriter w = new BufferedWriter(new FileWriter(f));
            w.write(id);
            w.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    String readFromFile() {
        try {
            File f = new File(UUID_FILE_PATH);
            BufferedReader r = new BufferedReader(new FileReader(f));
            String id = r.readLine();
            r.close();
            return id;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public String getId() {
        String id = readFromFile();
        if(null == id) {
            Log.i(TAG, "Id not found, create a new one.");
            id = genId();
            saveToFile(id);
        }
        Log.d(TAG, "return id: "+ id);
        return id;
    }

}
