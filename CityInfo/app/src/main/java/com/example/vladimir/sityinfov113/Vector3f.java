package com.example.vladimir.sityinfov113;

/**
 * Created by Vladimir on 22.08.2017.
 */

public class Vector3f {
    public float[] values= new float[3];

    public Vector3f(){}
    public Vector3f(float x, float y, float z) { set(x,y,z);  }

    public float x(){ return values[0]; }
    public float y(){ return values[1]; }
    public float z(){ return values[2]; }

    public void set(float x, float y, float z){ setX(x); setY(y); setZ(z); }
    public void setX(float x){ values[0] = x; }
    public void setY(float y){ values[1] = y; }
    public void setZ(float z){ values[2] = z; }
}
