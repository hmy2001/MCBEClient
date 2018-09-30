package jp.dip.hmy2001.mcbeClient.utils;

import java.io.*;
import java.util.*;

public class Config {
    private MyProperties properties;

    public Config(){
        properties = new MyProperties();

        if(new File(getFilePath()).exists()){
            readFile();
        }else{
            createFile();
        }
    }

    public String get(String contentName){
        return properties.getProperty(contentName, "");
    }

    private String getFilePath(){
        String path = new File(".").getAbsoluteFile().getParent();

        return path + "/" + "config.properties";
    }

    private void createFile(){
        try {
            Random random = new Random(System.currentTimeMillis());
            byte[] bytes = new byte[10];
            random.nextBytes(bytes);
            UUID uuid = UUID.nameUUIDFromBytes(bytes);
            System.out.println(uuid);

            properties.setProperty("serverAddress", "localhost");
            properties.setProperty("serverPort", "19132");
            properties.setProperty("username", "Hmy2001Sub");
            properties.setProperty("clientUUID", uuid.toString());

            properties.store(new FileOutputStream(getFilePath()));

            properties.load(new FileInputStream(getFilePath()));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(){
        try {
            properties.load(new FileInputStream(getFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
