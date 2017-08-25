package com.example.vladimir.sityinfov113;

/**
 * Created by Vladimir on 22.08.2017.
 */

public class Vector3f {
    public float[] values= new float[3];

    private Vector3f(Vector3f other){ set(other.x(), other.y(), other.z()); }

    public Vector3f(){}
    public Vector3f(float x, float y, float z) { set(x,y,z);  }
    public Vector3f(double x, double y, double z) { set(x,y,z);  }

    public float x(){ return values[0]; }
    public float y(){ return values[1]; }
    public float z(){ return values[2]; }

    public double xd() { return (double)x(); }
    public double yd() { return (double)y(); }
    public double zd() { return (double)z(); }

    public void set(float x, float y, float z){ setX(x); setY(y); setZ(z); }
    public void setX(float x){ values[0] = x; }
    public void setY(float y){ values[1] = y; }
    public void setZ(float z){ values[2] = z; }
    public void set(double x, double y, double z){ setX(x); setY(y); setZ(z); }
    public void setX(double x){ values[0] = (float)x; }
    public void setY(double y){ values[1] = (float)y; }
    public void setZ(double z){ values[2] = (float)z; }

    public void add(float x, float y, float z){
        add(x,y);
        values[2] += z;
    }

    public static float dotProduct(Vector3f v1, Vector3f v2)
    {
        return (float)(v1.xd() * v2.xd() + v1.yd() * v2.yd() + v1.zd() * v2.zd());
    }

    public static Vector3f crossProduct(Vector3f v1, Vector3f v2)
    {
        return new Vector3f(v1.yd() * v2.zd() - v1.zd() * v2.yd(),
                v1.zd() * v2.xd() - v1.xd() * v2.zd(),
                v1.xd() * v2.yd() - v1.yd() * v2.xd());
    }

    void negative(){
        set(-x(), -y(), -z());
    }

    Vector3f negatived(){
        return  new Vector3f(-x(), -y(), -z());
    }

    Vector3f normalized(){
        Vector3f res = new Vector3f(this);
        res.normalize();
        return  res;
    }

    void normalize(){
        double xd = xd(), yd = yd(), zd = zd();
        double len = xd * xd +
                yd * yd +
                zd * zd;
        if(len == 1.f){

        }
        if (len != 0.f){
            double sqrtLen = Math.sqrt(len);
            set(xd / sqrtLen,yd / sqrtLen,zd / sqrtLen);
        }
        else
            set(0f,0f,0f);
    }

    public void div(float v){
        set(this.x() / v, this.y() / v, this.z() / v);
    }

    public void sub(Vector3f other)
    {
        set(this.xd() - other.xd(), this.yd() - other.yd(), this.zd() - other.zd());
    }

    public void add(Vector3f other)
    {
        set(this.x() + other.x(), this.y() + other.y(), this.z() + other.z());
    }

    public void add(float x, float y) {
        values[0] += x;
        values[1] += y;
    }

    public void mult(float value)
    {
        set(this.x() * value, this.y() * value, this.z() * value);
    }

    public Vector3f dived(float v){
        return  new Vector3f(this.x() / v, this.y() / v, this.z() / v);
    }

    public Vector3f subed(Vector3f other)
    {
        return new Vector3f(this.xd() - other.xd(), this.yd() - other.yd(), this.zd() - other.zd());
    }

    public Vector3f added(Vector3f other)
    {
        return new Vector3f(this.x() + other.x(), this.y() + other.y(), this.z() + other.z());
    }

    public Vector3f multed(float value)
    {
        return new Vector3f(this.x() * value, this.y() * value, this.z() * value);
    }


    String debug(){
        String res = new String();
        for(int i = 0 ; i < 3; i++)
            res += values[i] + " ";
        return  res;
    }
}
