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

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */

    @Test
    public void findIntersections() {
        Plane pl = new Plane(new Point(0, 0, 1), new Vector(1, 1, 1));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray into plane
        assertEquals(List.of(new Point(1, 0, 0)),
                pl.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0))),
                "Bad plane intersection");

        // TC02: Ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(2, 0, 0), new Vector(1, 0, 0))),
                "Must not be plane intersection");

        // =============== Boundary Values Tests ==================
        // TC11: Ray parallel to plane
        assertNull(pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(0, 1, -1))),
                "Must not be plane intersection");

        // TC12: Ray in plane
        assertNull(pl.findIntersections(new Ray(new Point(0, 0.5, .5), new Vector(0, 1, -1))),
                "Must not be plane intersection");


        // TC13: Orthogonal ray into plane
        assertEquals(List.of(new Point(1d / 3, 1d / 3, 1d / 3)),
                pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-1, -1, -1))),
                "Bad plane intersection");

        // TC14: Orthogonal ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, 1))),
                "Must not be plane intersection");

        // TC15: Orthogonal ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, 1))),
                "Must not be plane intersection");

        // TC16: Orthogonal ray from plane
        assertNull(pl.findIntersections(new Ray(new Point(0, 0.5, 0.5), new Vector(1, 1, 1))),
                "Must not be plane intersection");

        // TC17: Ray from plane
        assertNull(pl.findIntersections(new Ray(new Point(0, 0.5, 0.5), new Vector(1, 1, 0))),
                "Must not be plane intersection");

//        // TC18: Ray from plane's Q point
//        assertNull(pl.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 0))),
//                "Must not be plane intersection");

    }
}