
import primitives.*;
import geometries.Polygon;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import geometries.Triangle;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import renderer.*;
import scene.Scene;
import geometries.*;



public class Project {

    @Test
    public void project() {
        Camera camera = new Camera(
                new Point(0, 0, 1000),
                new Vector(0, 0, -1),
                new Vector(0, 1, 0))
                .setVPSize(200, 125)
                .setVPDistance(800);


        Scene scene = new Scene("Test Scene");

    }
}
