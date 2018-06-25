package algGeom.lib3D;

import Util.*;
import javax.media.opengl.*;

public class DrawAABB extends Draw {

    AABB box;

    public DrawAABB(AABB box) {
        this.box = box;
    }

    public AABB getBox() {
        return box;
    }

    @Override
    public void drawObject(GL g) {
        g.glLineWidth(1.5f);
        
        // Primero la base
        g.glBegin(GL.GL_LINE_STRIP);
            g.glVertex3d(box.min.x, box.min.y, box.min.z);
            g.glVertex3d(box.max.x, box.min.y, box.min.z);
            g.glVertex3d(box.max.x, box.min.y, box.max.z);
            g.glVertex3d(box.min.x, box.min.y, box.max.z);
            g.glVertex3d(box.min.x, box.min.y, box.min.z);
        g.glEnd();
        // Ahora la tapa
        g.glBegin(GL.GL_LINE_STRIP);
            g.glVertex3d(box.min.x, box.max.y, box.min.z);
            g.glVertex3d(box.max.x, box.max.y, box.min.z);
            g.glVertex3d(box.max.x, box.max.y, box.max.z);
            g.glVertex3d(box.min.x, box.max.y, box.max.z);
            g.glVertex3d(box.min.x, box.max.y, box.min.z);
        g.glEnd();
        // Ahora las aristas que conectan la base con la ta
        g.glBegin(GL.GL_LINES);
            g.glVertex3d(box.min.x, box.min.y, box.min.z);
            g.glVertex3d(box.min.x, box.max.y, box.min.z);
            
            g.glVertex3d(box.max.x, box.min.y, box.min.z);
            g.glVertex3d(box.max.x, box.max.y, box.min.z);
            
            g.glVertex3d(box.max.x, box.min.y, box.max.z);
            g.glVertex3d(box.max.x, box.max.y, box.max.z);
            
            g.glVertex3d(box.min.x, box.min.y, box.max.z);
            g.glVertex3d(box.min.x, box.max.y, box.max.z);
        g.glEnd();
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