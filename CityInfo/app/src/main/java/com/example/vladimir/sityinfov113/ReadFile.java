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

    public float[] read_file() {
        float [] mass_vertices = new float[0];
        String fileName = "city.model";
        StringBuilder stringBuilder = null;
        Log.w("W", "СЧИТЫВАЕМ ФАЙЛ");

        File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName);
        try {
            FileInputStream inputStream = new FileInputStream(myFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            stringBuilder = new StringBuilder();
            String line;
            //считываем построчно
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    Log.w("W","ADD STRING IN FILE");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        catch (FileNotFoundException e) {
            //e.printStackTrace();
            Log.w("W", "ФАЙЛ НЕ НАЙДЕН");
        }

        //стринг билдер = файлу
        Log.w("W", "stringbuilder = " + stringBuilder);

        //stringBuilder.toString();
        Log.w("W", "stringbuilder = " + stringBuilder);
        //заносим каждый элемент в массив после запятой EZ
        String[] test_line_string = stringBuilder.toString().split(",");
        mass_vertices = new float[test_line_string.length];
        for(int i = 0; i < test_line_string.length; i++)
            mass_vertices[i] = Float.parseFloat(test_line_string[i]);
        //возвращаем готовый массив
        return mass_vertices;
    }
}