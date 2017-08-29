package com.example.vladimir.sityinfov113;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private Camera camera = new Camera();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setContentView(R.layout.main_activity);

        glSurfaceView = findViewById(R.id.OpenGLSurfaceViewID);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new OpenGLProjectRenderer());

        final Button reset_camera_button = findViewById(R.id.reset);
        final Button zoom_in_camera_button = findViewById(R.id.ZoomIn);

        reset_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("W","= " + camera.eye);
                Log.w("W","= " + camera.forward);
                Log.w("W","= " + camera.up);
                Log.w("W","reset_camera_button click");
                camera.resetCamera();
            }
        });

        zoom_in_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("W","zoom_in_camera_button click");
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
