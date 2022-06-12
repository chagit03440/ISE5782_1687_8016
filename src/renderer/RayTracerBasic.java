package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class RayTracerBasic extends  RayTracerBase{
    /**
     * constructor that called the constructor of RayTracerBase
     *
     * @param scene ,the scene
     */
    /**
     * a constant size for shadow rays
     */
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double INITIAL_K = 1.0 ;

    private int glossinessRaysNum = 36;
    private double distanceGrid = 25;
    private double sizeGrid=4;
    protected  double beamRadius=20d;//the level of the light
    protected boolean softShadows=false;


    public void setDistanceGrid(double distanceGrid) {
        this.distanceGrid = distanceGrid;
    }
    /**
     * search for shadow shape
     * @param gp is the point to check if it's unshaded or not
     * @param l is direction from light source to point
     * @param n the normal from the point
     * @param ls the light source
     * @return if the point is unshaded. it means- if there is no shade on it- it should have light.
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource ls, double nv) {
        Vector lightDirection = l.scale(-1); //vector from the point to the light source
        Vector deltaVector=n.scale(nv<0?DELTA:-DELTA);
        Point p=gp.point.add(deltaVector);
        Ray lightRay = new Ray(gp.point, lightDirection,n);
        double lightDistance = ls.getDistance(gp.point);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay); //new Ray(lightDistance)

        //if there are no intersections return true (there is no shadow)
        if (intersections == null) {
            return true;
        }

        for (GeoPoint intersection : intersections) {
            //for each intersection if there are points in the intersections list that are closer
            //to the point then light source, return false
            if (intersection.geometry.getMaterial().kT.lowerThan(MIN_CALC_COLOR_K)) {
                return false;
            }
        }
        return true;
    }

    public RayTracerBasic(Scene scene) {
        super(scene);
        if (scene == null)
            return;
    }


    /**
     * find intersections between the ray and the 3D scene
     * @param ray is a received ray to calculate intersections with it and the scene
     * @return the color according to the intersections results
     */
    @Override
    public Color traceRay(Ray ray){
        var intersections = scene.geometries.findGeoIntersections(ray);
        // if ray didn't intersect any geometrical object - the background color of the scene will be returned:
        if (intersections == null)
            return scene.background;
        //The closest point to the beginning of the ray will be found using the new method we created: "findClosestGeoPoint".
        GeoPoint closestPoint=findClosestIntersection(ray);
        return calcColor(closestPoint,ray);
    }


    /**
     * calculates the color of the point that the ray intersect it
     * (we already get here the closest intersection point)
     * @param intersection is a geo point
     * @param ray is the ray from the viewer
     * @param level of recursion- goes down each time till it gets to 1
     * @param k- factor of reflection and refraction so far
     * @return the color
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffect(intersection, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray, level, k));
    }

    /**
     * calc the global effects- reflection and refraction.
     * this func call "calcColor" in recursion to add more and more global effects.
     * @param gp is the point to calculate the global effects of
     * @param level of recursion- goes down each time till it gets to 1
     * @param k- factor of reflection and refraction so far
     * @return the color of the effect
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k){
        Color color = Color.BLACK;
        Material material = gp.geometry.getMaterial();
        Double3 kr = material.getkR();
        Double3 kkr = k.product(kr); //in each recursive iteration the impact of the reflection decreases
        Vector n = gp.geometry.getNormal(gp.point);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) {
            List<Ray> reflectedRays = constructReflectedRays(gp,ray, material.getkG());
            primitives.Color tempColor1 = primitives.Color.BLACK;
            // each ray
            for(Ray reflectedRay: reflectedRays)
            {
                GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
                tempColor1 = tempColor1.add(reflectedPoint == null ? primitives.Color.BLACK : calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
            }

            color = color.add(tempColor1.reduce(reflectedRays.size()));
        }
        Double3 kt = material.getkT();
        Double3 kkt = k.product(kt); //in each recursive iteration the impact of the refraction decreases
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {
            List<Ray> refractedRays = constructRefractedRays(gp,ray,n);
            primitives.Color tempColor2 = primitives.Color.BLACK;
            //calculate for each ray
            for(Ray refractedRay: refractedRays)
            {
                GeoPoint refractedPoint = findClosestIntersection(refractedRay);
                tempColor2 = tempColor2.add(refractedPoint == null? primitives.Color.BLACK : calcColor(refractedPoint, refractedRay, level -1, kkt).scale(kt));
            }

            color = color.add(tempColor2.reduce(refractedRays.size()));
        }
        return color;
    }

    /**
     * calc the global effects- reflection and refraction.
     * this func call "calcColor" in recursion to add more and more global effects.
     * @param ray is the ray from the viewer
     * @param level of recursion- goes down each time till it gets to 1
     * @param kx is kR or kT factor
     * @param kkx is kR or kT factor multiplied by k - factor of reflection and refraction
     * @return the color of the effect
     * new change
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection (ray);
        return (gp == null ? scene.background : calcColor(gp, ray, level-1, kkx)
        ).scale(kx);
    }

    /**
     * this func returns a new ray- the reflected ray comes from the point because of the light- inRay
     * @param pointGeo is the point to calculate reflection with
     * @param inRay is the normalized ray from the viewer
     * @param n is the normal from the point
     * @return ReflectedRay
     */
    private Ray constructReflectedRay(GeoPoint pointGeo, Vector inRay, Vector n)
    {
        double vn = inRay.dotProduct(n);
        if (Util.isZero(vn)) {
            return null;
        }
        Vector r = inRay.subtract(n.scale(2 * vn));
        return new Ray(pointGeo.point, r,n);
    }

    /**
     * this func returns a new ray- the refracted ray comes from the point because of the light- inRay
     * @param pointGeo is the point to calculate refraction with
     * @param inRay is the normalized ray from the viewer
     * @param n is the vector - normal from the point
     * @return RefractedRay
     */
    private Ray constructRefractedRay(GeoPoint pointGeo, Vector inRay, Vector n) {
        return new Ray(pointGeo.point, inRay,n);
    }

    /**
     * make object color as point color
     * @param geoPoint is the point we need to find the color of
     * @param ray is a received ray to calculate intersections with it and the scene
     * @return the color of the point received
     */
    public Color calcColor(GeoPoint geoPoint,Ray ray) {
        Color ambientLight = scene.ambientLight.getIntensity();
        return calcColor(geoPoint,ray,MAX_CALC_COLOR_LEVEL,new Double3(INITIAL_K)).add(ambientLight);
    }


    /**
     * Calculate the local effect of light sources on a point
     * @param intersection is the point
     * @param ray is the ray from the viewer
     * @return the color of the local effect
     */
    private Color calcLocalEffect(GeoPoint intersection, Ray ray,Double3 k) {
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(v)); //nv=n*v
        if (nv == 0) {
            return Color.BLACK;
        }
        Material material = intersection.geometry.getMaterial();
        Color color = intersection.geometry.
                getEmission(); //base color
        //for each light source in the scene
        for (LightSource lightSource : scene.lights)
        {
            Vector l = lightSource.getL(intersection.point); //the direction from the light source to the point
            double nl = alignZero(n.dotProduct(l)); //nl=n*l
            //if sign(nl) == sing(nv) (if the light hits the point add it, otherwise don't add this light)
            if (nl * nv > 0) {
                Double3 ktr=transparency(lightSource,l,n,intersection,nv);
                if(!ktr.product(k).lowerThan(MIN_CALC_COLOR_K))
                {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffusive(material, l, n, lightIntensity),
                            calcSpecular(material, l, n, v, lightIntensity));
                }
            }
        }
        return color;
    }

    /**
     * Calculate the diffuse light effect on the point
     * @param material diffuse attenuation factor
     * @param l the direction of the light
     * @param n normal from the point
     * @param lightIntensity the intensity of the light source at this point
     * @return the color of the diffusive
     */
    private Color calcDiffusive(Material material, Vector l, Vector n, Color lightIntensity) {
        double ln = alignZero(abs(l.dotProduct(n))); //ln=|l*n|
        return lightIntensity.scale(material.kD.scale(Math.abs(ln))); //Kd * |l * n| * Il
    }

    /**
     * Calculate the specular factor and change the color by it, Light created by a special break of light.
     * @param material specular attenuation factor
     * @param l the direction of the light
     * @param n normal from the point
     * @param v direction of the viewer
     * @param lightIntensity the intensity of the light source at the point
     * @return the color of the point
     */
    private Color calcSpecular(Material material, Vector l, Vector n, Vector v, Color lightIntensity) {
        double ln = alignZero(l.dotProduct(n)); //ln=l*n
        Vector r = l.subtract(n.scale(2 * ln)); //r=l-2*(l*n)*n
        double vr = alignZero(v.dotProduct(r)); //vr=v*r
        double vrnsh = pow(max(0, -vr), material.nShininess); //vrnsh=max(0,-vr)^nshininess
        return lightIntensity.scale(material.kS.scale(vrnsh)); //Ks * (max(0, - v * r) ^ Nsh) * Il
    }
    private Double3 transparencyBeam(LightSource lightSource, Vector n, GeoPoint geoPoint,int beam) {
        Double3 tempKtr = new Double3(0d);
        List<Vector> beamL = lightSource.getBeamL(geoPoint.point, beamRadius,beam);

        for (Vector vl : beamL) {
            tempKtr = tempKtr.add(transparency( lightSource,n,vl,geoPoint,beam));
        }
        tempKtr = tempKtr.reduce(beamL.size());

        return tempKtr;
    }
    /**
     * this function is like unshaded but returns how much shading
     * @param geoPoint the point
     * @param l from light source to point
     * @param n the normal
     * @param light the light source
     * @return the transparency
     */
    private Double3 transparency(LightSource light, Vector l, Vector n, GeoPoint geoPoint, double nv)
    {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay;

        if(nv<0){
            lightRay=new Ray(geoPoint.point,lightDirection, n);
        }
        else
            lightRay=new Ray(geoPoint.point,lightDirection,n.scale(-1));

        double lightDistance = light.getDistance(geoPoint.point);
        double maxDistance = light.getDistance(geoPoint.point);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null) return Double3.ONE;

        Double3 ktr = Double3.ONE;//transparency initial

        for (GeoPoint gp : intersections) //move on all the geometries in the way
        {
            //if there are geometries between the point to the light- calculate the transparency
            //in order to know how much shadow there is on the point
            if (Util.alignZero(gp.point.distance(geoPoint.point)-lightDistance) <= 0)
            {
                ktr = ktr.product(gp.geometry.getMaterial().kT);//add this transparency to ktr
                if (ktr.lowerThan( MIN_CALC_COLOR_K)) //stop the loop- shadow "atum", black. very small transparency
                    return Double3.ZERO;
            }
        }
        return ktr;
    }

    /**
     * find the closest Geo point intersection to the ray
     * @param ray is the ray from the viewer
     * @return the closest intersection to the ray
     */
    public GeoPoint findClosestIntersection(Ray ray){
        if (ray == null) {
            return null;
        }

        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) {
            return null;
        }
        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
        return closestPoint;
    }
    private List<Ray> constructReflectedRays(GeoPoint geoPoint, Ray ray, double Glossy) {
        Vector v = ray.getDir();
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = alignZero(v.dotProduct(n));
        // r = v - 2*(v * n) * n
        Vector r = v.subtract(n.scale(2d * nv)).normalize();

        return raysGrid( new Ray(geoPoint.point,r,n),1,geoPoint.geometry.getMaterial().getkG(), n);
    }

    private List<Ray> constructRefractedRays(GeoPoint geoPoint, Ray inRay, Vector n) {
        return raysGrid(new Ray(geoPoint.point, inRay.getDir(), n),-1,geoPoint.geometry.getMaterial().getkG(), n);
    }
    List<Ray> raysGrid(Ray ray, int direction, double glossy, Vector n){
        int numOfRowCol = isZero(glossy)? 1: (int)Math.ceil(Math.sqrt(glossinessRaysNum));
        if (numOfRowCol == 1) return List.of(ray);
        Vector Vup ;
        double Ax= Math.abs(ray.getDir().getX()), Ay= Math.abs(ray.getDir().getY()), Az= Math.abs(ray.getDir().getZ());
        if (Ax < Ay)
            Vup= Ax < Az ?  new Vector(0, -ray.getDir().getZ(), ray.getDir().getY()) :
                    new Vector(-ray.getDir().getY(), ray.getDir().getX(), 0);
        else
            Vup= Ay < Az ?  new Vector(ray.getDir().getZ(), 0, -ray.getDir().getX()) :
                    new Vector(-ray.getDir().getY(), ray.getDir().getX(), 0);
        Vector Vright = Vup.crossProduct(ray.getDir()).normalize();
        Point pc=ray.getPoint(distanceGrid);
        double step = glossy/sizeGrid;
        Point pij=pc.add(Vright.scale(numOfRowCol/2*-step)).add(Vup.scale(numOfRowCol/2*-step));
        Vector tempRayVector;
        Point Pij1;

        List<Ray> rays = new ArrayList<>();
        rays.add(ray);
        for (int i = 1; i < numOfRowCol; i++) {
            for (int j = 1; j < numOfRowCol; j++) {
                Pij1=pij.add(Vright.scale(i*step)).add(Vup.scale(j*step));
                tempRayVector =  Pij1.subtract(ray.getP0());
                if(n.dotProduct(tempRayVector) < 0 && direction == 1) //refraction
                    rays.add(new Ray(ray.getP0(), tempRayVector));
                if(n.dotProduct(tempRayVector) > 0 && direction == -1) //reflection
                    rays.add(new Ray(ray.getP0(), tempRayVector));
            }
        }

        return rays;
    }

    /**
     * An abstract function that get a list of ray and return the color of the avarege points that cross the rays
     * @param rays  that intersect the scene
     * @return Color
     */
    public Color traceRay(List<Ray> rays)
    {
        Color finalColor=null;
        Color firstColor=null;
        Color colorTmp=new Color(0,0,0);
        for(var ray:rays)
        {
            List<GeoPoint> intersection = scene.geometries.findGeoIntersections(ray);
            if (intersection == null)
            {
                return scene.getBackground();
            }
            GeoPoint closestPoint = ray.findClosestGeoPoint(intersection);

            colorTmp=calcColor(closestPoint, ray) == null ? scene.getBackground() : calcColor(closestPoint, ray);
            if(finalColor==null)
            {
                firstColor=colorTmp;
                finalColor=new Color(0,0,0);
                for (int i = 0; i < 10; i++)
                    finalColor=finalColor.add(colorTmp);
            }

            if(!colorTmp.equals(firstColor))
                finalColor=finalColor.add(colorTmp);

        }
        if(finalColor.equals(firstColor))
            return firstColor;
        int size=rays.size()+10;
        return finalColor.reduce(size);
    }

}