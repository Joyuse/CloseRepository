package com.example.vladimir.sityinfov113;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;
import javax.vecmath.Matrix4f;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.Vector;

/**
 * Created by Vladimir on 18.08.2017.
 */



public class Camera {
    private float[] myViewMatrix = new float[16];
    private float[] myProjectionMatrix = new float[16];
    private float[] myViewPortMatrix = new float[16];
    private float[] myViewProjectionMatrix = new float [16];
    /**позиции камеры**/

    /**центер*/
    private final float[] center = {0, 0f, 0f};
    /**глаз*/
    private final float[] eye = {0, 0f, 5f};
    /**up-vector*/
    private final float[] up = {0, 1f, 0f};

    int w,h;
    float nearp = 1.0f;
    float farp = 150000.0f;

    public void setViewport(int x, int y, int w, int h)
    {

    }


    public void  unproject(int x,int y,int z){
        Vector4d v4 = new Vector4d(x,h-y,2.0f*z-1.0f,1.0f);
//        Vector4d res = myViewMatrix() * v4;
//        res /= w;
    }

    private void project(Vector3d pos)
    {
        Vector4d res;
        Matrix.multiplyMM(myViewProjectionMatrix,0,myViewMatrix,0,myProjectionMatrix,0);
// Matrix.multiplyMV();
        //Vector4d res = getViewProjectionMatrix() * pos;
 //return Point(res.x,h - res.y);
    }

    public void getViewProjectionMatrix () {

        return;
    }
/**
    public void setViewport (int x1, int y1, int w, int h){
        GLES20.glViewport(x1,y1,w,h);
        RectF viewport = new RectF(x1, y1, w, h);
        //viewport = new int [] {x1,y1,w,h};
        float w2 = (float)w / 2;
        float h2 = (float)h / 2;
        Matrix.setIdentityM(myViewPortMatrix,0);
        myViewPortMatrix = new float[]{w2, 0, 0, 0,
                                        0, h2, 0, 0,
                                        0, 0, 1, 0,
                                        w2, h2, 0, 0};
    }
**/
    public void updateProjectionMatrix() {
        Matrix.setIdentityM(myProjectionMatrix,0);
        //инвентируем по Х
        Matrix.scaleM(myProjectionMatrix,0,-1,1,1);
        float aspectratio = w/h;
        Matrix.perspectiveM(myProjectionMatrix,0,45,aspectratio,nearp,farp);
    }

    public Camera(int width,int height){
        float aspect = (float) width / height;
        GLES20.glViewport(0, 0, width, height);
        Matrix.setLookAtM(myViewMatrix, 0 , eye[0], eye[1], eye[2], center[0], center[1], center[2], up[0],up[1],up[2]);
        Matrix.frustumM(myProjectionMatrix, 0, -aspect, aspect, -1, 1, 4, 7);
    }
}