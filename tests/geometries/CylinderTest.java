package geometries;

import static primitives.Util.isZero;
import static primitives.Util.alignZero;

import geometries.Cylinder;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    @Test
    void testGetNormal() {
        Cylinder c = new Cylinder( new Ray(new Point(new Double3(1, 1, 1)),new Vector(1, 0, 0)), 3.0,1.0);
        Double3 p = new Double3(2, 0, 1);
        Point p1=new Point(p);
        assertEquals(c.getNormal(p1), new Vector(0, -1, 0).normalize(),"ERROR: not the correct normal");
    }


}