package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Plane extends  Geometry{
    Point q0;
    Vector normal;

    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal;
    }

    public Plane(Point p1, Point p2, Point p3) {
        Vector myVec1 = p3.subtract(p1);
        Vector myVec2 = p2.subtract(p1);
        this.q0 = p1;

        this.normal =myVec2.crossProduct(myVec1).normalize();
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

    /**
     * @param ray to find intersections points with the plane
     * @return list of the intersections points with the plane
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();
        Vector n = normal;
        double nv = n.dotProduct((v)); //nv=n*v

        // ray parallel to plane
        if (isZero(nv)) {
            return null;
        }

        //  ray cannot start from the plane
        if (q0.equals(P0)) {
            return null;
        }

        Vector Q0_P0 = q0.subtract(P0);

        double numerator = n.dotProduct(Q0_P0); //numerator=n*Q0_P0

        //in this case P0 is on the plane, so return null
        if (isZero(numerator)) {
            return null;
        }
        double t = alignZero(numerator / nv); //t=numerator/nv

        //if t>0 the ray does point toward the plane
        if (t > 0) {
            GeoPoint P = new GeoPoint(this, P0.add(v.scale(t))); //new GeoPoint{geometry=this, point=p0+tv}
            return List.of(P);
        }
        //otherwise it doesn't point toward the plane, so return null
        return null;
    }

}
