package com.example.vladimir.sityinfov113;

import android.util.Log;

/**
 * Created by Vladimir on 18.09.2017.
 */

public class FPSCounter {
    private long lastFrame = System.nanoTime();
    public float FPS = 0;

    public void logFrame() {
        long time = (System.nanoTime() - lastFrame);
        FPS = 1/(time/1000000000.0f);
        lastFrame = System.nanoTime();
        Log.w("W","FPS = " + FPS);
    }
}