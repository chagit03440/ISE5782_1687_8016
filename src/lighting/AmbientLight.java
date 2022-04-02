package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {
    Color intensity;

    public AmbientLight(Color _iA, Double3 _kA) {
        //Ip = Ka * Ia
        intensity=_iA.scale(_kA);
    }
    public AmbientLight()
    {
        intensity=Color.BLACK;
    }
   public Color getIntensity()
   {
       return intensity;
   }
}
