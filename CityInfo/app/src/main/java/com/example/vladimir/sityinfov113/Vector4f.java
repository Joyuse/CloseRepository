package com.example.vladimir.sityinfov113;

/**
 * Created by Vladimir on 22.08.2017.
 */



public class Vector4f
{
    public boolean globalContext = false;
    public float values[] = new float[4];

    public Vector4f(){}
    public Vector4f(Vector3f position, float w){ set(position.x(), position.y(), position.z(), w); }
    public Vector4f(float x, float y, float z, float w) {  set(x,y,z,w);  }

    public float x(){return values[0];}
    public float y(){return values[1];}
    public float z(){return values[2];}
    public float w(){return values[3];}

    public void set(float x, float y, float z, float w){ setX(x); setY(y); setZ(z); setW(w);}
    public void setX(float x){ values[0] = x; }
    public void setY(float y){ values[1] = y; }
    public void setZ(float z){ values[2] = z; }
    public void setW(float w){ values[3] = w; }

    public Vector3f toVector3f(){ return  new Vector3f(x(), y(), z()); }
    public Vector3f toAffine(){ return (w() != 0) ?  new Vector3f(x() / w(), y() / w(), z() /w()) : new Vector3f(x(),y(),z()); }

    @Override
    public String toString(){
        String res = new String("Vector4f(");
        for(int i = 0 ; i < 3; i++)
            res += values[i] + ", ";
        res += values[3] + ")";
        return res;
    }
}
