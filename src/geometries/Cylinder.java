package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

public class Cylinder extends Tube{
    Double height;

    public Cylinder(Ray r,Double rad,Double height) {
        super(r,rad);
        this.height = height;
    }
    public  Vector getNormal(Point p)
    {
        Point p0 = axisRay.getP0();
        Vector v = axisRay.getDir();
        //t = v (P – P0)
        double t = p.subtract(p0).dotProduct(v);
        // O = P0 + tv
        Point o = null;
        if (!isZero(t))// if it's close to 0, we'll get ZERO vector exception
            o = p0.add(v.scale(t));
        Vector n = p.subtract(o);
        return n.normalize();
    }

    public Double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                super.toString()+
                "height=" + height +
                '}';
    }
}
