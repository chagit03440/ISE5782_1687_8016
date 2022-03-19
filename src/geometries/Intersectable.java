package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
/**
 * interface Intersectable
 */
public interface Intersectable {
    /**
     * @param ray to find intersections points with the geomtry
     * @return list of the intersections points with the geomtry
     */
    public List<Point> findIntersections(Ray ray);
}
