package com.example.vladimir.sityinfov113;

import android.graphics.PointF;

/**
 * Created by Vladimir on 18.08.2017.
 */


public class Camera {

    int width,height;
    boolean is_need_update_mvp=true;

    Vector3f rotation_point;
    Vector3f eye = new Vector3f(0.0f,0.0f,100.0f);
    Vector3f forward = new Vector3f(0.0f,0.0f,-1.0f);
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
        projection_matrix = scalem.multed(projection_matrix);

        needUpdateMatrix();
    }

    public void translate(float x, float y){
        eye.add(x,y);
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

        eye = rotm.multed(eye).toVector3f();
        forward = rotm.multed(new Vector4f(forward.x(), forward.y(), forward.z(), 0.0f)).toVector3f();
        up = rotm.multed(new Vector4f(up.x(), up.y(), up.z(), 0.0f)).toVector3f();

        needUpdateMatrix();
    }

    public Vector3f unprojectPlane(float x, float y)
    {
        Vector3f near = unproject(x,y,0f);
        Vector3f far = unproject(x,y,1f);
        Vector3f ray = far.subed(near);
        float dif = -near.z() / ray.z();
        return  near.added(ray.multed(dif));
    }

    //calc functions
    public Vector3f unproject(float x,float y,float z)
    {
        Vector4f res = new Vector4f(x / width * 2f - 1f, (height - y) / height * 2f - 1f, 2f * z - 1f, 1f);
        res = getViewProjectionMatrixInv().multed(res);
        return  res.toAffine();
    }

    public PointF project(Vector3f position)
    {
        Vector3f pr = getViewProjectionMatrix().multed(position).toAffine();
        return new PointF((pr.x() * 0.5f + 0.5f) * width, height - (pr.y() * 0.5f + 0.5f) * height);
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

        view_matrix.setLookAt(eye,forward,up);
        view_projection_matrix = projection_matrix.multed(view_matrix);
        view_projection_matrix_inv = view_projection_matrix.inverted();
        is_need_update_mvp = false;
    }

}