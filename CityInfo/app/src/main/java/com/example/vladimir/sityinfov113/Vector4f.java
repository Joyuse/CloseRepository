package com.example.vladimir.sityinfov113;

import android.util.Log;

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

    public Vector3f toAffine(){ return  new Vector3f(x() / w(), y() / w(), z() /w()); }

    String debug(){
        String res = new String();
        for(int i = 0 ; i < 4; i++)
            res += values[i] + " ";
        return  res;
    }
}
