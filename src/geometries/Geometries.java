package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Geometries implements Intersectable{
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
    public List<Point> findIntersections(Ray ray)
    {
        ArrayList<Point> intersections = new ArrayList<Point>();
        //temp - for save the points in the i geometry
        ArrayList<Point> temp = new ArrayList<Point>();
        for(Intersectable i: _geometries)
        {
            temp = (ArrayList<Point>) i.findIntersections(ray);
            if(temp != null)
                //if there is intersections points with i geometry - copy all this points to intersections list
                for(Point j: temp)
                    intersections.add(j);
        }
        if(intersections.isEmpty())
            return null;
        return intersections;
    }
}
