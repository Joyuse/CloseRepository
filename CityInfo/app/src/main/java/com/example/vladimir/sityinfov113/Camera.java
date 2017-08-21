package com.example.vladimir.sityinfov113;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Matrix4f;

import java.util.Vector;

/**
 * Created by Vladimir on 18.08.2017.
 */

public class Camera {

    private float[] myViewMatrix = new float[16];

    private float[] myProjectionMatrix = new float[16];

    private float[] myViewPortMatrix = new float[16];




    /**позиции камеры**/

    /**центер*/
    private final float[] center = {0, 0f, 0f};
    /**глаз*/
    private final float[] eye = {0, 0f, 5f};
    /**up-vector*/
    private final float[] up = {0, 1f, 0f};

    public Camera(int width,int height){
        /*
        float aspect = (float) width / height;
        GLES20.glViewport(0, 0, width, height);
        Matrix.setLookAtM(myViewMatrix, 0 , eye[0], eye[1], eye[2], center[0], center[1], center[2], up[0],up[1],up[2]);
        Matrix.frustumM(myProjectionMatrix, 0, -aspect, aspect, -1, 1, 4, 7);
        */
    }

    public void setViewport (int x1, int y1, int w, int h){
        GLES20.glViewport(x1,y1,w,h);
        float w2 = (float)w / 2;
        float h2 = (float)h / 2;

        Matrix.setIdentityM(myViewPortMatrix,0);
/*
        myViewPortMatrix = {w2,0,0,0,
                            0,h2,0,0,
                            0,0,1,0,
                            w2,h2,0,0};
                            */
    }

    public void updateProjectionMatrix() {
        //
        Matrix.setIdentityM(myProjectionMatrix,0);
        //инвентируем по Х
        Matrix.scaleM(myProjectionMatrix,0,1,0,0);
        //float aspectratio = w/h;
        //Matrix.perspectiveM(myProjectionMatrix,45,aspectratio,nearp,farp);
    }
}

/*
QVector3D CameraBase::unproject(qint32 x, qint32 y, float z)
{
    QVector4D v4(x,viewport.height() - y,2.0f*z-1.0f,1.0f);
    QVector4D res = getViewProjectionViewportMatrixInv() * v4;
    res/=res.w();
    return res.toVector3DAffine();
}

QPoint CameraBase::project(const QVector3D &pos)
{
    QVector4D res = getViewProjectionViewportMatrix() * pos;
    return QPoint(res.x(), viewport.height() - res.y());
}

void CameraBase::setViewport(qint32 l, qint32 t, qint32 w, qint32 h)
{
    viewport = QRectF(l,t,w,h);
    float w2 = float(w) / 2;
    float h2 = float(h) / 2;
    viewportMatrix.setToIdentity();
    viewportMatrix.setColumn(0,QVector4D(w2,0.0f,0.0f,0.0f));
    viewportMatrix.setColumn(1,QVector4D(0.0f,h2,0.0f,0.0f));
    viewportMatrix.setColumn(2,QVector4D(0.0f,0.0f,1.0f,0.0f));
    viewportMatrix.setColumn(3,QVector4D(w2,h2,0.0f,1.0f));
}

void CameraBase::updateProjectionMatrix()
{
    projectionMatrix.setToIdentity();
    projectionMatrix.scale(-1,1,1);

    float aspectRatio = viewport.width() / viewport.height();
    projectionMatrix.perspective(45, aspectRatio, nearp, farp);
}

* */