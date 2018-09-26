package com.example.sultan.newsapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class StoreCache extends AppCompatActivity implements Serializable {

   String file = "newsCache.txt";

    public void storeData(Context context, CardAdapter adapter) {
        try {
            FileOutputStream os = context.openFileOutput(file, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(adapter);
            os.close();
            System.out.print(adapter.getItem(0));
        }
        catch(IOException e) {e.printStackTrace();}
    }

    public Object retrieveCache(Context context) {
        try {
            FileInputStream fis = context.openFileInput(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object cards = ois.readObject();

            return cards;
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        catch(IOException e) {e.printStackTrace();}

        return "";
    }

    public void manageCache() {

    }
}
