package com.example.vladimir.sityinfov113;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.Vector;

/**
 * Created by Vladimir on 18.08.2017.
 */

public class Camera {

    //матрица вида
    private float[] mViewMatrix = new float[16];
    //матрица проекции
    private float[] mProjectionMatrix = new float[16];

    /**позиции камеры**/

    /**центер*/
    private final float[] center = {0, 0f, 0f};
    /**глаз*/
    private final float[] eye = {0, 0f, 5f};
    /**up-vector*/
    private final float[] up = {0, 1f, 0f};

    public Camera(int width,int height){
        float aspect = (float) width / height;
        GLES20.glViewport(0, 0, width, height);
        Matrix.setLookAtM(mViewMatrix, 0 , eye[0], eye[1], eye[2], center[0], center[1], center[2], up[0],up[1],up[2]);
        Matrix.frustumM(mProjectionMatrix, 0, -aspect, aspect, -1, 1, 4, 7);
    }
}
