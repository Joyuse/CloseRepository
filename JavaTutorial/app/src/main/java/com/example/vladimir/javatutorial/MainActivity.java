package com.example.vladimir.javatutorial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public RandomWords random_words = new RandomWords();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_layout);
        final TextView text_view = (TextView) findViewById(R.id.textView2);
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_view.setText(random_words.words);
                random_words.add_random_word();
                random_words.random_words();
                text_view.setText(random_words.words_plus_words_array);
            }
        });
    }
}
