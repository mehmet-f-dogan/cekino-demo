package dev.mehmetfd;

import java.util.Random;

public class Util {
    public static double generateExponentialBias() {
        double u = (new Random()).nextDouble();        
        double expValue = 1 - Math.exp(-5 * u);
        return expValue;
    }
}
