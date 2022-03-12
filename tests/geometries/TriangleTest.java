package geometries;

import org.junit.jupiter.api.Test;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: we check if the vector is in the triangle, we mean that If
        Triangle t =new Triangle(new Point(0,0,0),new Point(1,0,0),new Point(0,1,0));
        assertEquals(new Vector(0,0,1),t.getNormal(null),"GetNormal ERROR");
    }

}