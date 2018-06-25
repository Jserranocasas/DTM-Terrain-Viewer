package algGeom.lib3D;

import Util.*;
import javax.media.opengl.GL;

public class DrawLine3d extends Draw{
    Line3d line;

    public DrawLine3d(Line3d e) {
        line = e;
    }

    public Line3d getLine() {
        return line;
    }

    @Override
    public void drawObject(GL g) {
        // screen coordiantes
        double ax = line.getPoint(-BasicGeom.RANGE).x, 
                ay = line.getPoint(-BasicGeom.RANGE).y, 
                 az = line.getPoint(-BasicGeom.RANGE).z;
        double bx = line.getPoint(BasicGeom.RANGE).x, 
                by = line.getPoint(BasicGeom.RANGE).y, 
                 bz = line.getPoint(BasicGeom.RANGE).z;
        
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
