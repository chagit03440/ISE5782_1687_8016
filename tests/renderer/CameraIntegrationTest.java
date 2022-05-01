package renderer;
import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for {@link Camera} class.
 */
public class CameraIntegrationTest {


    /**
     * Test helper function to count the intersections and compare with expected value
     *
     * @author Chagit Orenstein, Avital Elchadad
     * @param expected amount of intersections
     * @param cam      camera for the test
     * @param geo      3D body to test the integration of the camer with
     * @param nX
     * @param nY
     */
    private void assertCountIntersections(int expected, Camera cam, Intersectable geo, int nX, int nY) {
        int count = 0;

        List<Point> allpoints = null;

        //view plane 3X3 (WxH 3X3 & nx,ny =3 => Rx,Ry =1)
        for (int i = 0; i < nY; ++i) {
            for (int j = 0; j < nX; ++j) {
                var intersections = geo.findIntersections(cam.constructRay(nX, nY, j, i));
                if (intersections != null) {
                    if (allpoints == null) {
                        allpoints = new LinkedList<>();
                    }
                    allpoints.addAll(intersections);
                }
                count += intersections == null ? 0 : intersections.size();
            }
        }

        assertEquals(expected, count, "Wrong amount of intersections");
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Sphere intersections
     */
    @Test
    public void cameraRaySphereIntegration() {
        int nX = 3, nY = 3;
        Camera cam1 = new Camera(new Point(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(1)
                .setVPSize(nX, nY);
        // TC01: Small Sphere 2 points
        assertCountIntersections(2, cam1, new Sphere(new Point(0, 0, -2.5), 1.0), 3, 3);

        // TC02: Big Sphere 18 points
        assertCountIntersections(18, cam1, new Sphere( new Point(0, 0, -2.5),2.5), 3, 3);

        // TC03: Medium Sphere 10 points
        assertCountIntersections(10, cam1, new Sphere(new Point(0, 0, -2), 2.0), 3, 3);

        // TC04: Inside Sphere 9 points
        assertCountIntersections(9, cam1, new Sphere(new Point(0, 0, -1), 4.0), 3, 3);

        // TC05: Beyond Sphere 0 points
        assertCountIntersections(0, cam1, new Sphere(new Point(0, 0, 1), 0.5), 3, 3);
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Plane intersections
     */
    @Test
    public void cameraRayPlaneIntegration() {
        int nX = 3, nY = 3;
        Camera cam = new Camera(new Point(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(1)
                .setVPSize(nX, nY);

        // TC01: Plane against camera 9 points
        assertCountIntersections(9, cam, new Plane(new Point(0, 0, -2), new Vector(0, 0, 1)), 3, 3);

        // TC02: Plane with small angle 9 points
        assertCountIntersections(9, cam, new Plane(new Point(0, 0, -1.5), new Vector(0, -0.5, 1)), 3, 3);

        // TC03: Plane parallel to lower rays 6 points
        assertCountIntersections(6, cam, new Plane(new Point(0, 0, -3), new Vector(0, -1, 1)), 3, 3);

        // TC04: Beyond Plane 0 points
        assertCountIntersections(0, cam, new Plane(new Point(0, -5, 0), new Vector(-1, -9, 13)), 3, 3);
    }

    /**
     * Integration tests of Camera Ray construction with Ray-Triangle intersections
     */
    @Test
    public void cameraRayTriangleIntegration() {
        int nX = 3, nY = 3;
        Camera cam = new Camera(new Point(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(1)
                .setVPSize(nX, nY);

        // TC01: Small triangle 1 point
        assertCountIntersections(1, cam,  new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 3, 3);

        // TC02: Medium triangle 2 points
        assertCountIntersections(2, cam, new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 3, 3);
    }
}

