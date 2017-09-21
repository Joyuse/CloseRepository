package com.example.vladimir.sityinfov113;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private OpenGLSurfaceView glSurfaceView;
    NavigationView navigation_view;
    final float ratio = 0.25f;
    //Главная функция
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //проверка на первый запуск
        SharedPreferences first_start_app = getPreferences(MODE_PRIVATE);
        if(first_start_app.getBoolean("isFirstRun", true)){
            Intent load_file_activity = new Intent(getApplicationContext(), Load_file_activity.class);
            startActivity(load_file_activity);
            Log.e("E","FIRST RUN");
        }

        else {
            Log.e("E","NOT FIRST RUN");
        }
        first_start_app.edit().putBoolean("isFirstRun",false).apply();

        //Проверяем поддерживается ли OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            Toast.makeText(this, "Так то все збс", Toast.LENGTH_LONG).show();
        }

        else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show(); return;
        }

        //после всех проверок выдаем на экран, такую штуку
        setContentView(R.layout.main_activity);
        glSurfaceView = findViewById(R.id.OpenGLSurfaceViewID);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new OpenGLProjectRenderer());

        //design and other
        final Button reset_camera_button = findViewById(R.id.reset);
        final Button zoom_in_camera_button = findViewById(R.id.ZoomIn);
        final Button zoom_out_camera_button = findViewById(R.id.ZoomOut);

        //кнопочки выбора
        navigation_view = (NavigationView) findViewById(R.id.NavigationView);

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.message:
                       Log.w("W","Clicked");
                       break;
                   case R.id.home_id:
                       Intent load_file_activity = new Intent(getApplicationContext(), Load_file_activity.class);
                       startActivity(load_file_activity);
                       break;
                   case R.id.favorites:
                       Log.w("W","Clicked");
                       break;
                   case R.id.city:
                       Log.w("W","Clicked city");
                       Intent start_choice_city_activity = new Intent(getApplicationContext(), ChoiceCityActivity.class);
                       startActivity(start_choice_city_activity);
                       Log.w("W","Clicked");
                       break;
                   case R.id.swap:
                       Log.w("W","Clicked");
                       break;
                   case R.id.nature:
                       Log.w("W","Clicked");
                       break;
               }
               return false;
           }
        });
        //Обработчики кнопок
        reset_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("W","reset_camera_button click");
                glSurfaceView.renderer.camera.resetCamera();
            }
        });

        zoom_in_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("W","zoom_in_camera_button click = " + ratio);
                glSurfaceView.renderer.camera.zoomInCamera(ratio);
            }
        });

        zoom_out_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("W","zoom_out_camera_button click = " + ratio);
                glSurfaceView.renderer.camera.zoomOutCamera(ratio);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        glSurfaceView.onPause();
    }
}
