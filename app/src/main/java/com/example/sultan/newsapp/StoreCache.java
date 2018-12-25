package com.example.sultan.newsapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class StoreCache extends AppCompatActivity implements Serializable {

    private String fileName = "newsCache.tmp"; //the file containing cache data
    private Context context; //app context
    private File cacheDir; //app's cache directory
    private File cacheFile; //the cache file's reference

    public StoreCache(Context context) {
        this.context = context;
        cacheDir = context.getCacheDir();
        cacheFile = new File(cacheDir, fileName);
    }

    /*returns stored cache data*/
    public JSONObject getCache() {
        JSONObject object = null;

        try {
            //create a stream to read the cache file
            InputStream is = new FileInputStream(cacheFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //store the file data in a variable
            String data = br.readLine();
            System.out.println("Reading cache file...");

            //convert the data to a json object
            object = new JSONObject(data);

            br.close();
            is.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void storeData(JSONObject data) {
        try {
            //delete the existing file to free up space
            cacheFile.delete();
            //create a new file
            cacheFile.createNewFile();
            //create a writer to store cache data into the file
            BufferedWriter bw = new BufferedWriter(new FileWriter(cacheFile.getAbsolutePath()));
            bw.write(data.toString());

            bw.flush();
            bw.close();
            System.out.println("Cache file created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
