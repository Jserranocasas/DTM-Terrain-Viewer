package algGeom.lib3D;

import Util.*;
import javax.media.opengl.*;

public class DrawVect3d extends Draw {

    Vect3d v3;

    public DrawVect3d(Vect3d p) {
        v3 = p;
    }

    public Vect3d getPoint() {
        return v3;
    }

    @Override
    public void drawObject(GL g) {
        drawObjectC(g, 1, 0, 0);
    }

    @Override
    public void drawObjectC(GL g, float R, float G, float B) {

        g.glColor3f(R, G, B);
        g.glPointSize(4.0f);
        g.glBegin(GL.GL_POINTS);
        g.glVertex3d(v3.x, v3.y, v3.z);
        g.glEnd();

    }
}
