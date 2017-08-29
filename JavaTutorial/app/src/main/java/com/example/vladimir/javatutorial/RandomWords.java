package com.example.vladimir.javatutorial;

import android.util.Log;

import java.util.Random;

/**
 * Created by Vladimir on 29.08.2017.
 */

public class RandomWords {

    public String[] words_array = {"Koshka", "Vodka", "Balalayka", "Moped", "Medved", "Obed" , "Kompaniya"};

    String words = "Medved and ";
    String words_plus_words_array;
    public void random_words () {
        words = "Medved and ";
    }

    public void add_random_word (){
        Random random =  new Random();
        int index_array = random.nextInt(10);
        Log.w("W","index array = " + index_array);
        switch (index_array){
            case 0 : Log.w("W", " CASE 0 = " + words_array[0]);
                words_plus_words_array = words + words_array[0];
                break;
            case 1 : Log.w("W", " CASE 1 = " + words_array[1]);
                words_plus_words_array = words + words_array[1];
                break;
            case 2 : Log.w("W", " CASE 2 = " + words_array[2]);
                words_plus_words_array = words + words_array[2];
                break;
            case 3 : Log.w("W", " CASE 3 = " + words_array[3]);
                words_plus_words_array = words + words_array[3];
                break;
            case 4 : Log.w("W", " CASE 4 = " + words_array[4]);
                words_plus_words_array = words + words_array[4];
                break;
            case 5 : Log.w("W", " CASE 5 = " + words_array[5]);
                words_plus_words_array = words + words_array[5];
                break;
            case 6 : Log.w("W", " CASE 6 = " + words_array[6]);
                words_plus_words_array = words + words_array[6];
                break;
            default: out_toast(); break;
        }
    }

    public void out_toast ()
    {
        words_plus_words_array = "Медведь сел в машину и сгорел D:<";
    }
}
