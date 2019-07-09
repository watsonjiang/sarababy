package com.watson.saralink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(IdManager.class);

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
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.info("Id not found, create a new one.");
            id = genId();
            saveToFile(id);
        }
        LOGGER.debug("return id: "+ id);
        return id;
    }

}
