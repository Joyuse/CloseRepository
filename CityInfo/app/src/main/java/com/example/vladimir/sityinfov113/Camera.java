package com.example.vladimir.sityinfov113;

import android.graphics.Point;

/**
 * Created by Vladimir on 18.08.2017.
 */


public class Camera {

    int width,height;
    boolean is_need_update_mvp=true;

    Vector3f rotation_point;
    Vector3f eye = new Vector3f(0.0f,0.0f,100.0f);
    Vector3f center = new Vector3f(0.0f,0.0f,0.0f);
    Vector3f up = new Vector3f(0.0f,1.0f,0.0f);

    Matrix4f projection_matrix = new Matrix4f();
    Matrix4f view_matrix = new Matrix4f();

    Matrix4f view_projection_matrix;
    Matrix4f view_projection_matrix_inv;

    //setters
    public void setViewport(int w, int h)
    {
        width = w;
        height = h;
        Matrix4f scalem = new Matrix4f();
        scalem.setToIndentity();
        scalem.scale(-1,1,1);

        projection_matrix.setPerspective(45,(float)w / h,15.0f, 150000.0f);
        projection_matrix = scalem.mult(projection_matrix);

        needUpdateMatrix();
    }

    public void translate(float x, float y){
        eye.setAdd(x,y);
        center.setAdd(x,y);
        needUpdateMatrix();
    }

    public void zoom(float distance){
        eye.setZ(eye.z() + distance);
        needUpdateMatrix();
    }

    public void setRotationPoint(Vector3f pos){
        rotation_point = pos;
    }

    public void rotate(float fovy){
        Matrix4f rotm = new Matrix4f();
        rotm.setToIndentity();
        rotm.translate(rotation_point.x(), rotation_point.y(), 0);
        rotm.rotate(fovy,0,0,1.0f);
        rotm.translate(-rotation_point.x(), -rotation_point.y(), 0);

        eye = rotm.mult(eye).toVector3f();
        center = rotm.mult(center).toVector3f();
        up = rotm.mult(new Vector4f(up.x(), up.y(), up.z(), 0.0f)).toVector3f();

        needUpdateMatrix();
    }

    public Vector3f unprojectPlane(float x, float y)
    {
        Vector3f near = unproject(x,y,0f);
        Vector3f far = unproject(x,y,1f);
//        Log.e("NEAR",near.debug());
//        Log.e("FAR",far.debug());
//        Log.e("END","END");
        Vector3f ray = far.sub(near);
        float dif = -near.z() / ray.z();
        return  near.add(ray.mult(dif));
    }

    //calc functions
    public Vector3f unproject(float x,float y,float z)
    {
        Vector4f res = new Vector4f(x / width * 2f - 1f, (height - y) / height * 2f - 1f, 2f * z - 1f, 1f);
        res = getViewProjectionMatrixInv().mult(res);
        return  res.toAffine();
    }

    public Point project(Vector3f position) //incorrect
    {
        Vector4f pr = getViewProjectionMatrix().mult(position);
        return new Point((int)pr.x(), height - (int)pr.y());
    }

    //getters
    public Matrix4f getViewProjectionMatrix () {
        if(is_need_update_mvp) updateViewProjectionViewportMatrix();
        return view_projection_matrix;
    }

    public Matrix4f getViewProjectionMatrixInv(){
        if(is_need_update_mvp) updateViewProjectionViewportMatrix();
        return view_projection_matrix_inv;
    }

    //private
    void needUpdateMatrix(){
        is_need_update_mvp = true;
    }

    void updateViewProjectionViewportMatrix(){
        view_matrix.setLookAt(eye,center,up);
        view_projection_matrix = projection_matrix.mult(view_matrix);
        view_projection_matrix_inv = view_projection_matrix.inverted();

        is_need_update_mvp = false;
    }

}