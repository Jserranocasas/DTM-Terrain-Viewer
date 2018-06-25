package algGeom.lib3D;

import Util.*;
import javax.media.opengl.GL;

public class DrawPointCloud3d extends Draw {

    PointCloud3d cloud;

    public DrawPointCloud3d(PointCloud3d cloud) {
        this.cloud = cloud;
    }

    public PointCloud3d getCloud() {
        return cloud;
    }

    @Override
    public void drawObject(GL g) {
        for (int i=0; i<cloud.size(); ++i) {
            DrawVect3d dv3 = new DrawVect3d(cloud.getPoint(i));
            dv3.drawObject(g);
        }
    }

    @Override
    public void drawObjectC(GL g, float R, float G, float B) {
        for (int i=0; i<cloud.size(); ++i) {
            DrawVect3d dv3 = new DrawVect3d(cloud.getPoint(i));
            dv3.drawObjectC(g, R, G, B);
        }
    }
    
    
    public void drawObjectC(GL g, float R, float G, float B, float A) {
        g.glColor4f(R, G, B, A);
        drawObject(g);
    }

}