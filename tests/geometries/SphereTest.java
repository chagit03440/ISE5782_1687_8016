package geometries;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import geometries.Sphere;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Sphere s1 = new Sphere(new Point(0,0,0), 4.0);
        Sphere s2 = new Sphere(new Point(1,1,1), 1.0);

        // ============ Equivalence Partitions Tests ==============

        assertTrue(s1.getNormal(new Point(0,0,4)).equals(new Vector(new Double3(0,0,1))));
        assertTrue(s1.getNormal(new Point(0,0,-4)).equals(new Vector(new Double3(0,0,-1))));
        assertTrue(s1.getNormal(new Point(0,4,0)).equals(new Vector(new Double3(0,1,0))));
        assertTrue(s1.getNormal(new Point(0,-4,0)).equals(new Vector(new Double3(0,-1,0))));
        assertTrue(s1.getNormal(new Point(4,0,0)).equals(new Vector(new Double3(1,0,0))));
        assertTrue(s1.getNormal(new Point(-4,0,0)).equals(new Vector(new Double3(-1,0,0))));

        assertTrue(s2.getNormal(new Point(1,1,0)).equals(new Vector(new Double3(0,0,-1))));
        assertTrue(s2.getNormal(new Point(0,1,1)).equals(new Vector(new Double3(-1,0,0))));
        assertTrue(s2.getNormal(new Point(1,0,1)).equals(new Vector(new Double3(0,-1,0))));
    }


}