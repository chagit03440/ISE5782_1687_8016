package geometries;

import primitives.Material;
import primitives.Point;
import primitives.Vector;
import primitives.Color;
import renderer.Camera;

import java.util.Objects;

public abstract class Geometry extends Intersectable{
    /**
     * basic emission color of the 3d object
     */
   protected Color emission=Color.BLACK;
    /**
     * material of a geometry object
     */
    private Material material = new Material();

    /**
     *
     * @param p of geometry
     * @return the normal to this geometry in a given point p
     */

    public abstract   Vector getNormal(Point p);

    public Color getEmission() {
        return emission;
    }

    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }
    /**
     * builder pattern setter for material field
     *
     * @param material material value
     * @return Geometry object
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * getter for the material
     *
     * @return material
     */
    public Material getMaterial() {
        return material;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geometry geometry = (Geometry) o;
        return Objects.equals(emission, geometry.emission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emission);
    }
}
