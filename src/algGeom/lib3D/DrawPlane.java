package algGeom.lib3D;

import Util.*;
import javax.media.opengl.*;

public class DrawPlane extends Draw {

    Plane pl;

    public DrawPlane(Plane p) {
        pl = p;
    }

    public Plane getPlane() {
        return pl;
    }

    @Override
    public void drawObject(GL g) {
        Vect3d A = pl.a;
        Vect3d B = pl.b;
        Vect3d C = pl.c;
             
        Vect3d AB = B.sub(A);
        Vect3d AC = C.sub(A);
        Vect3d D = AB.add(AC);

        //Expanding your points
        Vect3d AD = D.sub(A);
        Vect3d aux = A.sub(AD.scalarMul(4));
        D = A.add(AD.scalarMul(4));
        A = aux;
        
        Vect3d BC = C.sub(B);   
        aux = B.sub(BC.scalarMul(4));
        C = B.add(BC.scalarMul(4));
        B = aux;
      
        g.glBegin(GL.GL_QUADS);

        g.glVertex3d(A.x, A.y, A.z);
        g.glVertex3d(B.x, B.y, B.z);
        g.glVertex3d(D.x, D.y, D.z);
        g.glVertex3d(C.x, C.y, C.z);

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
