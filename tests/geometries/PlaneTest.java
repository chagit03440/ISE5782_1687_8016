package geometries;


import java.util.Arrays;
import java.util.List;




import geometries.Plane;
import geometries.Sphere;
import primitives.*;

import org.junit.jupiter.api.Test;
import primitives.Double3;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {

    @Test
    void testGetNormal() {
        Plane plane = new Plane(new Point((new Double3(4, 5, 6))), new Vector(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============

        assertEquals(plane.getNormal(new Point(1, 2, 3)), new Vector(1, 0, 0),"ERROR: Norma does not work correctly");
        assertEquals(plane.getNormal(new Point(-1, 2, 3)), new Vector(1, 0, 0),"ERROR: Norma does not work correctly" );
        assertEquals( plane.getNormal(new Point(1, -2, -3)), new Vector(1, 0, 0),"ERROR: Norma does not work correctly");
        assertEquals(plane.getNormal(new Point(-1, -2, -3)), new Vector(1, 0, 0),"ERROR: Norma does not work correctly");
    }


}