package geometries;

import primitives.*;

public class Plane implements  Geometry{
    Point q0;
    Vector normal;

    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal;
    }

    public Plane(Point v1, Point v2, Point v3) {


        Vector myVec1 = v1.subtract(v3);
        Vector myVec2 = v3.subtract(v1);
        this.q0 = new Point(v1.xyz);
        Vector myV1=new Vector(v1.xyz);
        Vector myV2=new Vector(v2.xyz);
        this.normal =myV1.crossProduct(myV2).normalize();
    }

    public  Vector getNormal(Point p)
    {
        return this.normal;
    }

    public Point getQ0() {
        return q0;
    }

    public Vector getNormal() {
        return normal;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "q0=" + q0 +
                ", normal=" + normal +
                '}';
    }
}
