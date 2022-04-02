package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

public class RayTracerBasic extends  RayTracerBase{
    /**
     * A builder
     * @param scene that the ray cross
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }
    @Override
    public Color traceRay(Ray ray) {
        List<Point> points = scene.geometries.findIntersections(ray);

        if (points != null) {
            Point closePoint = ray.findClosestPoint(points);
            return calcColor(closePoint);
        }
        //no points
        return scene.background;
    }
    private Color calcColor(Point p)
    {
        return  scene.ambientLight.getIntensity();
    }
}
