package geometries;

import primitives.Point;
import primitives.Vector;

public interface Geometry {
    /**
     *
     * @param p of geometry
     * @return the normal to this geometry in a given point p
     */

    public  Vector getNormal(Point p);
}
