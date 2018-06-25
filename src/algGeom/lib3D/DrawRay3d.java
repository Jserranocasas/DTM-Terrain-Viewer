package algGeom.lib3D;

import Util.*;
import javax.media.opengl.*;

public class DrawRay3d extends Draw {

    Ray3d vr;

    public DrawRay3d(Ray3d r) {
        vr = r;
    }

    public Ray3d getRay3d() {
        return vr;
    }

    @Override
    public void drawObject(GL g) {
        // screen coordiantes
        double ax = vr.orig.x, ay = vr.orig.y, az = vr.orig.z;
        double bx = vr.getPoint(BasicGeom.RANGE).x, by = vr.getPoint(BasicGeom.RANGE).y, bz = vr.getPoint(BasicGeom.RANGE).z;

        g.glBegin(GL.GL_LINES);
        g.glVertex3d(ax, ay, az);
        g.glVertex3d(bx, by, bz); //the fourth (w) component is zero!
        g.glEnd();
    }

    @Override
    public void drawObjectC(GL g, float R, float G, float B) {
        g.glColor3f(R, G, B);
        drawObject(g);
    }
}
