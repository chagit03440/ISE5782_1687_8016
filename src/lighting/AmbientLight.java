package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light {
    private  Color IA;
    private  Double3 KA;
//    private  Double3 KA;
    //private  Color intensity;

    /**
     * AmbientLight constructor
     */
    public AmbientLight(Color IA, Double3 KA) {
        super(IA.scale(KA));
//        this.IA = IA;
//      this.KA = KA;
        // intensity =  IA.scale(KA);
    }

    /**
     * AmbientLight constructor
     */
    public AmbientLight() {
        super(Color.BLACK);
    }
//    /**
//     * get Intensity
//     */
//    public Color getIntensity() {
//        return intensity;
//    }
}
