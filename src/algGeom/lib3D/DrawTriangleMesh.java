package algGeom.lib3D;


import Util.*;
import javax.media.opengl.GL;


public class DrawTriangleMesh extends Draw {

    TriangleMesh m;

    public DrawTriangleMesh(TriangleMesh m) {
        this.m = m;
    }

    @Override
    public void drawObject(GL g) {
        for (int i = 0; i < m.getFacesSize(); i++) {
            Triangle3d t = new Triangle3d(m.getTriangle(i));
            DrawTriangle3d dt = new DrawTriangle3d(t);
            dt.drawObject(g);
        }
    }

    @Override
    public void drawObjectC(GL g, float R, float G, float B) {
        g.glColor3f(R, G, B);
        drawObject(g);
    }
    
    public void drawObjectC(GL g, float R, float G, float B, float A) {
        g.glColor4f(R, G, B, A);
        drawObject(g);
    }
}
