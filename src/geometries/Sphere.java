package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Sphere extends  Geometry{
    Point center;
    Double radius;

    public Sphere(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }
    public Vector getNormal(Point p)
    {
        Vector n = p.subtract(center);
        return n.normalize();
    }

    public Point getCenter() {
        return center;
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();

        //if P0 is the center of the sphere, return the radius to there
        if (P0.equals(center)) {
            //center+(radius*v)
            return List.of(new GeoPoint(this, center.add(v.scale(radius))));
        }

        Vector U = center.subtract(P0); //u=center-p0

        double tm = alignZero(v.dotProduct(U)); //tm=v*u
        //d is the distance from the center to the ray
        double d = alignZero(Math.sqrt(U.lengthSquared() - tm * tm)); //d=squrt(|u|^2-tm^2)

        //if d is larger are equals radius, then there are no intersections
        if (d >= radius) {
            return null;
        }

        //th is the last part of the triangle between radius and d, calculate according to pythagoras
        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm - th); //t1=tm-th
        double t2 = alignZero(tm + th); //t2=tm+th

        if (t1 > 0 && t2 > 0) { //if they are both positive then there are 2 intersections
            Point P1 = ray.getPoint(t1); //p1=p0+t1*v
            Point P2 = ray.getPoint(t2); //p2=p0+t2*v
            return List.of(new GeoPoint(this, P1), new GeoPoint(this, P2));
        }
        //otherwise if only one is positive so there is one intersection
        if (t1 > 0) {
            Point P1 = ray.getPoint(t1); //p1=p0+t1*v
            return List.of(new GeoPoint(this, P1));
        }
        if (t2 > 0) {
            Point P2 = ray.getPoint(t2); //p2=p0+t2*v
            return List.of(new GeoPoint(this, P2));
        }

        //if they are both negative then there are no intersections
        return null;
    }

}
