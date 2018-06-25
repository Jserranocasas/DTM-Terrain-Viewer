package algGeom.lib3D;

import Util.Draw;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;

/**
 * @brief   Class to draw a TIN
 * @file    DrawTIN.java
 * @date    01-06-2018
 * @author  Javier Serrano Casas
 */
public class DrawTIN extends Draw{
    TIN tin;
    
    public DrawTIN(TIN t) {
        this.tin = t;
    }

    @Override
    public void drawObject(GL g) {
        for (int i = 0; i < tin.getTriangles().size(); i++) {
            try {
                if(tin.TriangleIsSea(i)){
                    g.glColor3f(0.05f, 0.22f, 0.55f); // Blue Color
                } else {
                    Vect3d color = tin.getColorTriangle(i);
                    g.glColor3f((float) color.x,(float) color.y,(float) color.z);       
                }
                
            } catch (Exception ex) {
                Logger.getLogger(DrawTIN.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Triangle3d t = new Triangle3d(tin.getTriangle(i));
            DrawTriangle3d dt = new DrawTriangle3d(t);
            dt.drawObject(g);
        }
        
        if(Test.visualizeWireframe){
            for (int i = 0; i < tin.getEdges().size(); i++){
                g.glColor3f(0.0f, 0.0f, 0.0f);

                Vect3d v1 = new Vect3d(tin.getEdge(i).orig);
                Vect3d v2 = new Vect3d(tin.getEdge(i).dest);

                v1.setY(v1.getY()+0.1);
                v2.setY(v2.getY()+0.1);

                Segment3d s = new Segment3d(v1, v2);
                DrawSegment3d dt = new DrawSegment3d(s);
                dt.drawObject(g);
            }
        }
    }

    @Override
    public void drawObjectC(GL g, float R, float G, float B) {
        g.glColor3f(R, G, B);
        drawObject(g);
    }
}
