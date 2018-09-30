package jp.dip.hmy2001.mcbeClient.utils;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author standstonecraft
 */
public class MyProperties {

    private ArrayList<String> keyList;
    private ArrayList<String> valList;

    public MyProperties() {
        keyList = new ArrayList<>();
        valList = new ArrayList<>();
    }

    public void load(FileInputStream fis) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        loadP(br);
    }

    public void load(FileInputStream fis, String encoding) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, encoding));
        loadP(br);
    }

    private void loadP(BufferedReader br) throws IOException {
        String s;
        while ((s = br.readLine()) != null) {
            if (s.charAt(0) != '/') {
                String ss[] = s.split("=", 2);
                keyList.add(ss[0]);
                valList.add(ss[1]);
            }
        }
        br.close();
    }

    public String getProperty(String key, String value) {
        int i = keyList.indexOf(key);
        if (i > -1) {
            return valList.get(i);
        }
        return value;
    }

    public void setProperty(String key, String value) {
        int i = keyList.indexOf(key);
        if (i > -1) {
            keyList.remove(i);
            valList.remove(i);
        }
        keyList.add(key);
        valList.add(value);
    }

    public void setComment(String comment) {
        keyList.add(comment);
        valList.add(null);
    }

    public void store(FileOutputStream fos) throws IOException {
        String s;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        storeP(bw);
    }

    public void store(FileOutputStream fos, String encoding) throws IOException {
        String s;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, encoding));
        storeP(bw);
    }

    public void store(FileOutputStream fos, String comment, String encoding) throws IOException {
        keyList.add(0, comment);
        valList.add(0, null);
        String s;
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, encoding));
        storeP(bw);
    }

    private void storeP(BufferedWriter bw) throws IOException {
        for (int i = 0; i < keyList.size(); i++) {
            String v=valList.get(i);
            if (v==null) {
                bw.write("//");
                bw.write(keyList.get(i));
            }else{
                bw.write(keyList.get(i)+"="+v);
            }
            bw.newLine();
        }
        bw.close();
    }
}