package com.example.vladimir.sityinfov113;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Created by Vladimir on 22.08.2017.
 */

public class Matrix4f {
    public float values[] = new float[16];

    public Matrix4f(){}
    public Matrix4f(Matrix4f other){ this.values = other.values; }

    public Vector4f mult(Vector4f vector){
        Vector4f res = new Vector4f();
        Matrix.multiplyMV(res.values,0,this.values,0,vector.values,0);
        return  res;
    }

    public Matrix4f mult(Matrix4f mat){
        Matrix4f res = new Matrix4f();
        Matrix.multiplyMM(res.values,0,this.values,0,mat.values,0);
        return  res;
    }

    public void setPerspective(float fovy, float aspect, float near, float far){
        Matrix.perspectiveM(values,0,fovy,aspect,near,far);
    }

    public void setToIndentity(){
        Matrix.setIdentityM(values,0);
    }

    public void setViewport(int w, int h){
        float w2 = w / 2;
        float h2 = h / 2;
        this.setColumn(0,w2,0.0f,0.0f,0.0f);
        this.setColumn(1,0.0f,h2,0.0f,0.0f);
        this.setColumn(2,0.0f,0.0f,1.0f,0.0f);
        this.setColumn(3,w2,h2,0.0f,1.0f);
    }

    public void setLookAt(Vector3f eye, Vector3f center, Vector3f up) {
        Matrix.setLookAtM(values,0,eye.x(), eye.y(), eye.z(), center.x(), center.y(), center.z(), up.x(), up.y(), up.z());
    }

    public void setColumn(int column, float x, float y, float z, float w){
        int offset = column * 4;
        this.values[offset] = x;
        this.values[offset + 1] = y;
        this.values[offset + 2] = z;
        this.values[offset + 3] = w;
    }

    public void scale(float x, float y, float z){
        Matrix.scaleM(this.values,0,x,y,z);
    }

    public void translate(float x, float y, float z){
        Matrix.translateM(this.values,0,x,y,z);
    }

    public void rotate(float angle, float x, float y, float z){
        Matrix.rotateM(this.values, 0, angle, x, y, z);
    }


    public Matrix4f inverted(){
        Matrix4f res = new Matrix4f(this);
        Matrix.invertM(res.values,0,this.values,0);
        return  res;
    }

    //geters
    Vector4f getColumn(int column){
        int offset = column * 4;
        return new Vector4f(values[offset],values[offset + 1], values[offset + 2], values[offset + 3]);
    }
}
