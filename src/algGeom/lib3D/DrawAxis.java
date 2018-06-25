package algGeom.lib3D;

import Util.*;
import javax.media.opengl.*;

public class DrawAxis extends Draw {

    public DrawAxis() {

    }

    @Override
    public void drawObject(GL g) {
        // X axis segments 
        for (int i = -BasicGeom.RANGE; i <= BasicGeom.RANGE; i += 10) {
            Segment3d s = new Segment3d(new Vect3d((double)i, -1.5 , 0.0), 
                                            new Vect3d((double)i, 1.5, 0.0));
            DrawSegment3d ds = new DrawSegment3d(s);
            ds.drawObjectC(g, 1f, 0f, 0f);
        }

        // Y axis segments  
        for (int i = -BasicGeom.RANGE; i <= BasicGeom.RANGE; i += 10) {
            Segment3d s = new Segment3d(new Vect3d(-1.5,(double) i, 0.0), 
                                new Vect3d(1.5,(double)i,  0.0));
            DrawSegment3d ds = new DrawSegment3d(s);
            ds.drawObjectC(g, 0f, 1f, 0f);
        }

        // Z axis segments  
        for (int i = -BasicGeom.RANGE; i <= BasicGeom.RANGE; i += 10) {
            Segment3d s = new Segment3d(new Vect3d(-1.5, 0.0, (double) i), 
                                new Vect3d(1.5, 0.0, (double)i));
            DrawSegment3d ds = new DrawSegment3d(s);
            ds.drawObjectC(g, 0f, 0f, 1f);
        }
        
        Line3d x = new Line3d(new Vect3d(0,0,0), new Vect3d(1,0,0)); 
        DrawLine3d dlx = new DrawLine3d(x);
        dlx.drawObjectC(g, 1.0f, 0.0f, 0.0f);
        
        Line3d y = new Line3d(new Vect3d(0,0,0), new Vect3d(0,1,0)); 
        DrawLine3d dly = new DrawLine3d(y);
        dly.drawObjectC(g, 0.0f, 1.0f, 0.0f);
        
        Line3d z = new Line3d(new Vect3d(0,0,0), new Vect3d(0,0,1)); 
        DrawLine3d dlz = new DrawLine3d(z);
        dlz.drawObjectC(g, 0.0f, 0.0f, 1.0f);
        
        // Origin
        Vect3d p = new Vect3d(0, 0, 0);
        DrawVect3d dp = new DrawVect3d(p);
        dp.drawObjectC(g, 1f, 1f, 1f);
    }

    @Override
    public void drawObjectC(GL g, float R, float G, float B) {
        throw new UnsupportedOperationException();
    }

}
