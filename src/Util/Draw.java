package Util;

import Util.BasicGeom;
import javax.media.opengl.*;

/**
 * Abstract class for drawing the primitives
 */
public abstract class Draw {

    public static int WIDTH, HEIGH, DEPTH = 100, POINT_SIZE = 6;

    public float convCoordX(double x) {
        return ((float) (x * WIDTH) / (2 * BasicGeom.RANGE) + (WIDTH / 2));
    }

    public float convCoordY(double y) {
        return ((float) ((y * HEIGH) / (2 * BasicGeom.RANGE) + (HEIGH / 2)));
    }

    public float convCoordZ(double y) {
        return ((float) ((y * DEPTH) / (2 * BasicGeom.RANGE) + (DEPTH / 2)));
    }

    /**
     * primitiva abstracta
     * @param gl
     */
    public abstract void drawObject(GL gl);

    public abstract void drawObjectC(GL gl, float R, float G, float B);
}
