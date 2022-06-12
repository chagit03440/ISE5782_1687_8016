package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

import java.util.List;

public class DirectionalLight extends Light implements LightSource{
    /**
     * Direction of the light
     */
    private  Vector direction;

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    /**
     * constructor Light
     *
     * @param intensity
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction=direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return super.getIntensity();
    }

    @Override
    public Vector getL(Point p) {
        return direction.normalize();
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
    @Override
    public List<Vector> getBeamL(Point dummyPoint3D, double dummyRadius, int dummyInt) {
        return List.of(new Vector(new Double3(direction.getX(),direction.getY(),direction.getZ())));
    }
}
