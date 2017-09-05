package com.example.vladimir.sityinfov113;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Vladimir on 01.09.2017.
 */

public class ReadFile {
    String fileName = "123";

    public void read_file() {
        Log.w("W","1");
        Log.w("W","СЧИТЫВАЕМ ФАЙЛ");
        File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName);
        try {
            FileInputStream inputStream = new FileInputStream(myFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                //вернуть стринг билдер
                //String[] parts = text.split("\n");
                //Log.w("W","PARTS = " + parts[1]);
                //textView.setText(stringBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
