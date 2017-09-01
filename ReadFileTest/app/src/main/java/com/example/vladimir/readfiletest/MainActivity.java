package com.example.vladimir.readfiletest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "my_tag";
    private String mFileName = "myfile";

    Button saveButton, readbutton;
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        saveButton = (Button) findViewById(R.id.button);
        readbutton = (Button) findViewById(R.id.button2);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(mFileName, editText.getText().toString());
                Log.d("LOG","САХРАНЕНО");
            }
        });

        readbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(readData(mFileName));
                Log.d("LOG","ПРОЧИТАНО");
            }
        });
    }

    public String readData(String fileName){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
            String line = "";
            while ((line = reader.readLine())!=null){
              return  line;
            }
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveData (String file_name, String data){
        try{
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(file_name,MODE_PRIVATE)));
            writer.write(data);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

