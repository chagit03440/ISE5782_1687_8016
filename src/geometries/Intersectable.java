package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
/**
 * interface Intersectable
 */
public abstract class Intersectable {
    /**
     * @param ray to find intersections points with the geomtry
     * @return list of the intersections points with the geomtry
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray){

        return findGeoIntersectionsHelper(ray);
    }
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);


    /**
     * GeoPoint helper class
     *
     * @author Chagit and Avital
     */
    public static class GeoPoint {
        public Geometry geometry;
        public Point point;

        /**
         * GeoPoint constructor
         *
         * @param geometry
         * @param point
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        /**
         * @param obj - a Intersectable object
         * @return if two Intersectable object are equals
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            GeoPoint other = (GeoPoint) obj;
            if (geometry == null) {
                if (other.geometry != null)
                    return false;
            } else if (!(geometry.getClass().equals(other.geometry.getClass())))
                return false;
            if (point == null) {
                if (other.point != null)
                    return false;
            } else if (!point.equals(other.point))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }



    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(gp -> gp.point).toList();
    }

}



