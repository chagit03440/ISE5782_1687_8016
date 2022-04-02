package geometries;

import primitives.*;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

public class Plane implements  Geometry{
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
    public List<Point> findIntersections(Ray ray) {

        //N*v
        double t_denominator = normal.dotProduct(ray.getDir());
        //if the ray is parallel to the plane - there is no intersections points
        if(t_denominator == 0)
            return null;
        //if the ray start in the normal point - there is no intersections points (q0 -p0 is vector 0 , ERROR)
        if(normal.get_head().equals(ray.getP0()))
            return null;
        // (N * (q0 - p0)) / (N*v)
        double t = normal.dotProduct(q0.subtract(ray.getP0())) / t_denominator;
        Point p;
        //only if t>0
        if(!isZero(t) && t>0)
            //p = p0 + t*v
            p = ray.getPoint(t);
        else
            //if t<=0 there is no intersections points
            return null;
        ArrayList<Point> intsersection = new ArrayList<Point>();
        intsersection.add(p);
        return intsersection;
    }
}
