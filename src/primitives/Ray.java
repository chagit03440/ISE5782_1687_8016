package primitives;

import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;
import java.util.List;
import java.util.Objects;
import static primitives.Util.isZero;


/**
 * class for Ray
 */
public class Ray {
    final Point p0; //center point of the ray
    final Vector dir; //direction of the ray

    //for constructing reflected and refracted rays
    private static final double DELTA = 0.01;

    /**
     * Constructor of the ray
     *
     * @param point  center point of the ray
     * @param vector direction of the ray
     */
    public Ray(Point point, Vector vector) {
        p0 = point;
        dir = vector.normalize();
    }

    /**
     * Constructor that gets 3 parameters
     * @param point
     * @param direction
     * @param normal
     * @return point + normal.scale(DELTA)
     */
    public Ray(Point point, Vector direction, Vector normal)
    {
        this.dir = direction.normalize();
        double nV = normal.dotProduct(direction);
        Vector delta = normal.scale(nV >= 0 ? DELTA : -DELTA);
        this.p0 = point.add(delta);
    }

    /**
     * returns the point of the ray
     *
     * @return the point
     */
    public Point getP0() {
        return p0;
    }

    /**
     * returns the vector(direction) of the ray
     *
     * @return vector
     */
    public Vector getDir() {
        return dir;
    }

    /**
     * compare between two rays
     *
     * @param o is the object
     * @return true if the rays are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return p0.equals(ray.p0) && dir.equals(ray.dir);
    }

    /**
     * @return an integer value - same hashCode if the rays are equals
     */
    @Override
    public int hashCode() {
        return Objects.hash(p0, dir);
    }

    /**
     * override function for to string
     *
     * @return the ray's values
     */
    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }

    /**
     * @param t is a scalar
     * @return the point applied to the function multiplied by the scalar
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return p0;
        }
        return p0.add(dir.normalize().scale(t));
    }

    /**
     * In the intersections - find the point with minimal distance from the ray
     * head and return it
     * @param geoPointList intersection points
     * @return closest point
     */
    public GeoPoint findClosestGeoPoint (List<GeoPoint> geoPointList) {
        GeoPoint result = null;
        double distance = Double.MAX_VALUE;
        double d;
        if(geoPointList==null){
            return null;
        }
        for (var geo : geoPointList) {
            d = geo.point.distance(p0);
            if (d < distance) {
                distance = d;
                result = geo;
            }
        }
        return result;
    }

    /**
     * find the closet Point that intersects with the ray
     * @param points intersection points list
     * @return the closest point
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }
}