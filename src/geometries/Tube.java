package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Tube extends  Geometry{
    Ray axisRay;
    Double radius;

    public Tube(Ray axisRay, Double radius) {
        this.axisRay = axisRay;
        this.radius = radius;
    }
    public Vector getNormal(Point p)
    {
        Point p0 = axisRay.getP0();
        Vector v = axisRay.getDir();
        //t = v (P – P0)
        double t = p.subtract(p0).dotProduct(v);
        // O = P0 + tv
        Point o=null;
        if (!isZero(t))// if it's close to 0, we'll get ZERO vector exception
            o = p0.add(v.scale(t));
        Vector n = p.subtract(o);
        return n.normalize();
    }

    public Ray getAxisRay() {
        return axisRay;
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Tube{" +
                "axisRay=" + axisRay +
                ", radius=" + radius +
                '}';
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Vector vAxis = axisRay.getDir(); //direction of the axis ray
        Vector v = ray.getDir(); //direction of ray
        Point p0 = ray.getP0(); //point of ray

        // At^2+Bt+C=0
        double a = 0;
        double b = 0;
        double c = 0;

        double vVa = alignZero(v.dotProduct(vAxis)); //vVa=v*vAxis
        Vector vVaVa;
        Vector vMinusVVaVa;
        if (vVa == 0) { // the ray is orthogonal to the axis
            vMinusVVaVa = v;
        } else {
            vVaVa = vAxis.scale(vVa); //vVAVA=vVa*vAxis
            try {
                vMinusVVaVa = v.subtract(vVaVa); //v-vVaVa
            } catch (IllegalArgumentException e1) { // the rays is parallel to axis
                return null;
            }
        }
        // A = (v-(v*va)*va)^2
        a = vMinusVVaVa.lengthSquared();

        Vector deltaP = null;
        try {
            deltaP = p0.subtract(axisRay.getP0()); //deltaP=p0-pAxis
        } catch (IllegalArgumentException e1) { // the ray begins at axis P0
            // the ray is orthogonal to Axis, only one intersection on edge
            if (vVa == 0) {
                return List.of(new GeoPoint(this, ray.getPoint(radius))); //return paxis+radius*vaxis
            }

            double t = alignZero(Math.sqrt(radius * radius / vMinusVVaVa.lengthSquared()));
            return t == 0 ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
        }

        double dPVAxis = alignZero(deltaP.dotProduct(vAxis)); //dpvaxis=deltap*vaxis
        Vector dPVaVa;
        Vector dPMinusdPVaVa;
        if (dPVAxis == 0) {
            dPMinusdPVaVa = deltaP;
        } else {
            dPVaVa = vAxis.scale(dPVAxis);
            try {
                dPMinusdPVaVa = deltaP.subtract(dPVaVa);
            } catch (IllegalArgumentException e1) {
                double t = alignZero(Math.sqrt(radius * radius / a));
                return t == 0 ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
            }
        }

        // B = 2(v - (v*va)*va) * (dp - (dp*va)*va))
        b = 2 * alignZero(vMinusVVaVa.dotProduct(dPMinusdPVaVa));
        c = dPMinusdPVaVa.lengthSquared() - radius * radius;

        // A*t^2 + B*t + C = 0 - lets resolve it
        double discr = alignZero(b * b - 4 * a * c);
        if (discr <= 0) return null; // the ray is outside or tangent to the tube

        double doubleA = 2 * a;
        double tm = alignZero(-b / doubleA);
        double th = Math.sqrt(discr) / doubleA;
        if (isZero(th)) return null; // the ray is tangent to the tube

        double t1 = alignZero(tm + th);
        if (t1 <= 0) // t1 is behind the head
            return null; // since th must be positive (sqrt), t2 must be non-positive as t1

        double t2 = alignZero(tm - th);

        // if both t1 and t2 are positive
        if (t2 > 0)
            return List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)));
        else // t2 is behind the head
            return List.of(new GeoPoint(this, ray.getPoint(t1)));
    }
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Vector vAxis = axisRay.getDir(); //direction of the axis ray
        Vector v = ray.getDir(); //direction of ray
        Point p0 = ray.getP0(); //point of ray

        // At^2+Bt+C=0
        double a = 0;
        double b = 0;
        double c = 0;

        double vVa = alignZero(v.dotProduct(vAxis)); //vVa=v*vAxis
        Vector vVaVa;
        Vector vMinusVVaVa;
        if (vVa == 0) { // the ray is orthogonal to the axis
            vMinusVVaVa = v;
        } else {
            vVaVa = vAxis.scale(vVa); //vVAVA=vVa*vAxis
            try {
                vMinusVVaVa = v.subtract(vVaVa); //v-vVaVa
            } catch (IllegalArgumentException e1) { // the rays is parallel to axis
                return null;
            }
        }
        // A = (v-(v*va)*va)^2
        a = vMinusVVaVa.lengthSquared();

        Vector deltaP = null;
        try {
            deltaP = p0.subtract(axisRay.getP0()); //deltaP=p0-pAxis
        } catch (IllegalArgumentException e1) { // the ray begins at axis P0
            // the ray is orthogonal to Axis, only one intersection on edge
            if (vVa == 0) {
                Point point = ray.getPoint(radius);
                double dis = point.distance(ray.getP0());
                if (alignZero(dis - maxDistance) <= 0) //if the distance is less than maximum
                    return List.of(new GeoPoint(this, point)); //return paxis+radius*vaxis
            }

            double t = alignZero(Math.sqrt(radius * radius / vMinusVVaVa.lengthSquared()));
            if (t == 0)
                return null;

            Point point = ray.getPoint(t);
            double dis = point.distance(ray.getP0());
            if (alignZero(dis - maxDistance) <= 0) //if the distance is less than maximum
                return List.of(new GeoPoint(this, point));

            else
                return null;
        }

        double dPVAxis = alignZero(deltaP.dotProduct(vAxis)); //dpvaxis=deltap*vaxis
        Vector dPVaVa;
        Vector dPMinusdPVaVa;
        if (dPVAxis == 0) {
            dPMinusdPVaVa = deltaP;
        } else {
            dPVaVa = vAxis.scale(dPVAxis);
            try {
                dPMinusdPVaVa = deltaP.subtract(dPVaVa);
            } catch (IllegalArgumentException e1) {
                double t = alignZero(Math.sqrt(radius * radius / a));
                if (t == 0)
                    return null;

                Point point = ray.getPoint(t);
                double dis = point.distance(ray.getP0());
                if (alignZero(dis - maxDistance) <= 0) //if the distance is less than maximum
                    return List.of(new GeoPoint(this, point));

                else
                    return null;
            }
        }

        // B = 2(v - (v*va)*va) * (dp - (dp*va)*va))
        b = 2 * alignZero(vMinusVVaVa.dotProduct(dPMinusdPVaVa));
        c = dPMinusdPVaVa.lengthSquared() - radius * radius;

        // A*t^2 + B*t + C = 0 - lets resolve it
        double discr = alignZero(b * b - 4 * a * c);
        if (discr <= 0) return null; // the ray is outside or tangent to the tube

        double doubleA = 2 * a;
        double tm = alignZero(-b / doubleA);
        double th = Math.sqrt(discr) / doubleA;
        if (isZero(th)) return null; // the ray is tangent to the tube

        double t1 = alignZero(tm + th);
        if (t1 <= 0) // t1 is behind the head
            return null; // since th must be positive (sqrt), t2 must be non-positive as t1

        double t2 = alignZero(tm - th);

        // if both t1 and t2 are positive
        if (t2 > 0) {
            List<GeoPoint> list = null;

            Point point = ray.getPoint(t1);
            double dis = point.distance(ray.getP0());
            if (alignZero(dis - maxDistance) <= 0) { //if the distance is less than maximum
                list = new LinkedList<>();
                list.add(new GeoPoint(this, point));
            }

            point = ray.getPoint(t2);
            dis = point.distance(ray.getP0());
            if (alignZero(dis - maxDistance) <= 0) { //if the distance is less than maximum
                if (list == null)
                    list = new LinkedList<>();
                list.add(new GeoPoint(this, point));
            }
            return list;
        } else { // t2 is behind the head
            Point point = ray.getPoint(t1);
            double dis = point.distance(ray.getP0());
            if (alignZero(dis - maxDistance) <= 0) { //if the distance is less than maximum
                return List.of(new GeoPoint(this, ray.getPoint(t1)));
            }
            return null;
        }
    }
}
