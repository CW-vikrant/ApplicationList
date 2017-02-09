package com.applicationlist.utility;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Vikrant Chauhan on 10/10/2016.
 */

public class FileHelper {

    public static final String FILE_NAME = "contact_file.txt";
    public static final String FILE_NAME_EXTERNAL = "my_file.txt";
    public static final String FILE_NAME_DIRECTORY = "test.txt";
    //android.os.Environment.getExternalStorageDirectory(): /storage/emulated/0
    public static final String PATH = android.os.Environment.getExternalStorageDirectory()+"/download/";

    /**
     * write to default internal path
     */
    public static void writeToFile(String data, Context context,String fileName){
        //checkExternalStorage();

        try {
            OutputStreamWriter f = new OutputStreamWriter(context.openFileOutput(fileName,
                    Context.MODE_PRIVATE));
            f.write(data);
            f.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * write to any directory
     * @param dir
     * @param data
     */

    public static void writeToDirectory(File dir,String data, String fileName){

        //file location: /data/user/0/com.applicationlist/cache/file_name
        try {
            dir.mkdirs();
            FileOutputStream f = new FileOutputStream(new File(dir,fileName));
            PrintWriter pw = new PrintWriter(f);
            pw.write(data);
            pw.close();
            f.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * write to PATH
     */
    public static void writeToExternalStorage(String data,String fileName){

        //file location: /storage/emulated/0/download/file_name
        try {
            File dir = new File (PATH);
            dir.mkdirs();
            FileOutputStream f = new FileOutputStream(new File(dir,fileName));
            PrintWriter pw = new PrintWriter(f);
            pw.write(data);
            pw.close();
            f.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * read from specified directory
     * @param dir
     * @return file data if file found
     */

    public static String readFromDirectory(File dir,String fileName){

        StringBuilder sb = new StringBuilder();

        try {
            FileInputStream inputStream = new FileInputStream(new File(dir,fileName));
            if(inputStream!=null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while ((s=br.readLine()) != null) {
                    sb.append(s);
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * read from PATH
     * @return string read from file if file found
     * else empty string
     */

    public static String readFromExternalStorage(String fileName){

        StringBuilder sb = new StringBuilder();

        try {
            FileInputStream inputStream = new FileInputStream(new File(PATH + fileName));
            if(inputStream!=null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while ((s=br.readLine()) != null) {
                    sb.append(s);
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * read from default internal storage
     * @param context
     * @return file data if file found
     * else empty string
     */
    public static String readFromFile(Context context,String fileName){

        InputStream inputStream = null;
        StringBuilder sb = new StringBuilder();

        try {
            inputStream = context.openFileInput(fileName);
            if(inputStream!=null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while ((s=br.readLine()) != null) {
                    sb.append(s);
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
