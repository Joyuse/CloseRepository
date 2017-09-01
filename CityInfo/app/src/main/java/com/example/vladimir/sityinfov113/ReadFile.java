package com.example.vladimir.sityinfov113;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Vladimir on 01.09.2017.
 */

public class ReadFile {

    public void readBufferFile(String file_name) throws IOException {
        //имя файла
        File file = new File(file_name);
        //файл ридер считывает файл
        FileReader fr = new FileReader(file);
        //прочитанное в буфер
        BufferedReader br = new BufferedReader(fr);
        //выводим в консоль
        String line;
        while((line = br.readLine()) != null){
            //обрабатываем считанную строку - пишем ее в консоль
            System.out.println(line);
        }
        br.close();
        fr.close();
    }
}
