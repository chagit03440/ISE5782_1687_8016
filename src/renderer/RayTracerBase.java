package renderer;
import geometries.Intersectable.GeoPoint;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import scene.Scene;

import java.util.LinkedList;

/**
 * RayTracerBase abstract class that use as an interface for RayTracerBasic
 *
 * @author Chagit orenstein & Avital Elchadad
 */
public abstract class RayTracerBase {

    /**
     * The scene of the image
     */
    protected Scene scene;

    /**
     * A builder function that get a scene
     * @param scene
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * An abstract function that get a ray and return the color of the point that cross the ray
     * @param ray ray that intersect the scene
     * @return Color
     */
    public abstract Color traceRay(Ray ray);

    /**
     * find the closest Geo point intersection to the ray
     * @param ray is the ray from the viewer
     * @return the closest intersection to the ray
     */
    public abstract GeoPoint findClosestIntersection(Ray ray);
    /**
     * make object color as point color
     * @param geoPoint is the point we need to find the color of
     * @param ray is a received ray to calculate intersections with it and the scene
     * @return the color of the point received
     */
    public abstract Color calcColor(GeoPoint geoPoint,Ray ray);
}