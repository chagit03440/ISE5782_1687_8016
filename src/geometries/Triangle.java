package geometries;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import geometries.Plane;
import geometries.Polygon;
import geometries.Triangle;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

public class Triangle extends  Polygon{
    public Triangle(Point... vertices) {
        super(vertices);
    }
    /**
     * @param ray to find intersections points with the triangle
     * @return list of the intersections points with the triangle
     */
    @Override
    public ArrayList<Point> findIntersections(Ray ray) {
        //v1 = p1 - p0
        Vector v1 = vertices.get(0).subtract(ray.getP0());
        //v2 = p2 - p0
        Vector v2 = vertices.get(1).subtract(ray.getP0());
        //v3 = p3 - p0
        Vector v3 = vertices.get(2).subtract(ray.getP0());
        //N1 = normalize(v1 x v2)
        Vector N1 = v1.crossProduct(v2).normalize();
        //N1 = normalize(v2 x v3)
        Vector N2 = v2.crossProduct(v3).normalize();
        //N1 = normalize(v3 x v1)
        Vector N3 = v3.crossProduct(v1).normalize();
        //v*N1
        double sign1 = ray.getDir().dotProduct(N1);
        //v*N2
        double sign2 = ray.getDir().dotProduct(N2);
        //v*N3
        double sign3 = ray.getDir().dotProduct(N3);
        // if one or more are 0.0 – no intersection
        alignZero(sign1);
        alignZero(sign2);
        alignZero(sign3);
        //The point is inside if all v*Ni have the same sign
        ArrayList<Point> intsersection = new ArrayList<Point>();
        if((sign1 > 0 && sign2 > 0 && sign3 > 0) || (sign1 < 0 && sign2 < 0 && sign3 < 0))
        {
            intsersection = (ArrayList<Point>) this.plane.findIntersections(ray);
            if (intsersection != null) //to change the geometry type to this
                for (Point i: intsersection)
                    i.geometry = this;
            return intsersection;
        }
        return null;
    }

}
