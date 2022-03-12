package geometries;
import geometries.Cylinder;
import geometries.Tube;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TubeTest {
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Tube t = new Tube( new Ray(new Point(1, 1, 1), new Vector(1, 0, 0)),1.0);
        Point p = new Point(2, 0, 1);
        assertEquals( t.getNormal(p), new Vector(0, -1, 0).normalize(),"ERROR: not the correct normal");
    }


}