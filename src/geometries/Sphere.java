package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere implements  Geometry{
    Point center;
    Double radius;

    public Sphere(Point center, Double radius) {
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
}
