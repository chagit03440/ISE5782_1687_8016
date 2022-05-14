package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable{
    private List<Intersectable> _geometries;
    /**
     * default constructor
     */
    public Geometries() {
        super();
        this._geometries = new ArrayList<Intersectable>();
    }

    /**
     * constructor
     * @param geometries list
     */
    public Geometries(Intersectable... geometries) {
        super();
        this._geometries = Arrays.asList(geometries);
    }
    /**
     * @param ray to find intersections points with all the geometries in the list
     * @return list of the intersections points with all the geometries in the list
     */
    /**
     * @param geometries add to list
     * add a geometry/ies
     */
    public void add(Intersectable... geometries)
    {
        _geometries.addAll(Arrays.asList(geometries));
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> result = null;

        //iterate over the list of the geometries and find the intersections for each one
        //add the results to list "result"
        for (Intersectable item : _geometries) {
            List<GeoPoint> itemGeoPoints = item.findGeoIntersections(ray);
            if (itemGeoPoints != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(itemGeoPoints);
            }
        }
        return result;
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> result = null;

        //iterate over the list of the geometries and find the intersections for each one
        //add the results to list "result"
        for (Intersectable item : _geometries) {
            List<GeoPoint> itemGeoPoints = item.findGeoIntersections(ray, maxDistance);
            if (itemGeoPoints != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(itemGeoPoints);
            }
        }
        return result;
    }
}
