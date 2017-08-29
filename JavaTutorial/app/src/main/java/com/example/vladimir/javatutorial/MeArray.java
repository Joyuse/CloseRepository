package com.example.vladimir.javatutorial;

import android.util.Log;

/**
 * Created by Vladimir on 29.08.2017.
 */

public class MeArray {

    int array[] = new int[50];

    public void setValue (int index, char value) {

        for(index = 0 ; index < array.length; index ++) {
            value = (char) index;
            Log.w("W" , "lenght" + array.length);
        array[index] = value;
        }
        System.out.print(array);
    }

    public void getValue (int index) {
        char some_value;
        some_value = (char) array[index];
        System.out.print(some_value);
    }

    public void getValues (int start_index, int end_index ){
        int size_new_mass =0;

        for (int i = start_index; i<end_index; i++){
            size_new_mass++;
        }
        System.out.print(size_new_mass);

        char some_value []  = new char[size_new_mass];
        for (int i = start_index; i < end_index; i++)
        {
            some_value[i] = (char) array[i];
        }
        System.out.print(some_value);
    }

    public void add (int index) {
        int size_old_mass = array.length;
        System.out.print(size_old_mass);
        char new_array[] = new char [size_old_mass*2];

    }

    public void deleteValue () {

    }

    public void deleteValues () {
        
    }

    private void copy (int index) {
        char some_value []  = new char[array.length];
        for(index = 0; index <array.length; index++){
            some_value[index] = (char) array[index];
        }
        System.out.print(some_value);
    }

}
