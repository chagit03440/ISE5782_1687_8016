package geometries;

import org.junit.jupiter.api.Test;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    public void findIntsersections() {
        Triangle triangle = new Triangle(new Point(-1, 0, 0), new Point(1, 0, 0), new Point(0, 1, 0));

        // ============ Equivalence Partitions Tests ==============
        // TC01: Point inside the triangle (1 points)
        Point p1 = new Point(0, 0.41, 0);
        List<Point> result1 = triangle.findIntersections(new Ray(new Point(0, 0.41, 2), new Vector(0, 0, -1)));
        assertEquals( 1, result1.size(),"Wrong number of points");
        assertEquals( p1, result1.get(0),"Point inside the triangle");

        // TC02: Point outside against edge (0 points)
        List<Point> result2 = triangle.findIntersections(new Ray(new Point(-1, 1, 2), new Vector(0, 0, -1)));
        assertEquals( null, result2,"Point outside against edge");

        // TC03: Point outside against vertex (0 points)
        List<Point> result3 = triangle.findIntersections(new Ray(new Point(-1.56017,-0.40906,2), new Vector(0, 0, -1)));
        assertEquals( null, result3,"Point outside against vertex");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray begins "before" the plane

        // TC04: Point on edge (0 points)
        List<Point> result4 = triangle.findIntersections(new Ray(new Point(-0.38, 0.62, 2), new Vector(0, 0, -1)));
        assertEquals( null, result4,"Point on edge");

        // TC05: Point in vertex (0 points)
        List<Point> result5 = triangle.findIntersections(new Ray(new Point(-1, 0, 2), new Vector(0, 0, -1)));
        assertEquals( null, result5,"Point in vertex");

        // TC06: Point on edge's continuation (0 points)
        List<Point> result6 = triangle.findIntersections(new Ray(new Point(2, 0, 2), new Vector(0, 0, -1)));
        assertEquals( null, result6,"Point on edge's continuation");

    }
}