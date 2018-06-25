package algGeom.lib3D;

import Util.BasicGeom;
import java.util.ArrayList;
import javafx.util.Pair;

public class AABB {
    Vect3d min; //menor x,y,z
    Vect3d max; //max x, y, z
    double vmin;
    double vmax;
    
    public AABB(Vect3d min, Vect3d max) {
        this.min = min;
        this.max = max;
    }
    
    public void setMin(Vect3d min) {
        this.min = min;
    }

    public void setMax(Vect3d max) {
        this.max = max;
    }

    /**
    * @brief get center vector3d of two points
    * @return center
    */
    public Vect3d getCenter (){
        return new Vect3d((max.x-min.x)/2+min.x, (max.y-min.y)/2+min.y, (max.z-min.z)/2+min.z);
    }

    public Vect3d getExtent(){
        return new Vect3d((max.x-min.x)/2.0, (max.y-min.y)/2.0, (max.z-min.z)/2.0);
    }
    
    public Vect3d getMin() {
        return min;
    }
    
    public Vect3d getMax() {
        return max;
    }
    
    private void findMinMax(double x0, double x1, double x2) {
        vmin = x0;
        vmax = x0;
        if (x1 < vmin) {
            vmin = x1;
        }
        if (x1 > vmax) {
            vmax = x1;
        }
        if (x2 < vmin) {
            vmin = x2;
        }
        if (x2 > vmax) {
            vmax = x2;
        }
    }
        
    public boolean intersectRayAABB(Ray3d ray, Vect3d v) throws Exception{
        ArrayList<Pair<Integer,Double>> cutPoints = new ArrayList<Pair<Integer,Double>>();
        Vect3d d = ray.dest.sub(ray.orig), p;
        double t[] = new double[6];
        
        //To avoid dividing by zero
        if(BasicGeom.equal(d.x,0)) { //If is equals to 0, to give negative value.
            t[0] = -1;
            t[1] = -1;
        } else{
            t[0] = (min.x - ray.orig.x) / d.x;
            t[1] = (max.x - ray.orig.x) / d.x;
        }
        if(BasicGeom.equal(d.y,0)) {
            t[2] = -1;
            t[3] = -1;
        } else{
            t[2] = (min.y - ray.orig.y) / d.y;
            t[3] = (max.y - ray.orig.y) / d.y;
        }
        if(BasicGeom.equal(d.z,0)) {
            t[4] = -1;
            t[5] = -1;
        } else{
            t[4] = (min.z - ray.orig.z) / d.z;
            t[5] = (max.z - ray.orig.z) / d.z;
        }     
        
        for(int i=0; i<6;i++){
            if(t[i] > 0.0){
                Vect3d pt = ray.getPoint(t[i]);
                if(min.x <= pt.x && pt.x <= max.x && min.y <= pt.y && pt.y <= max.y  
                                                  && min.z <= pt.z && pt.z <= max.z)
                    cutPoints.add(new Pair(i, t[i]));
            } 
        }
        
        if(cutPoints.isEmpty()) return false;
        if(cutPoints.size() > 2){
            throw new Exception("AABB::intersectRayAABB : The cut points number isn't correct");
        }
        
        if(cutPoints.size() == 2){
            if(cutPoints.get(0).getValue() < cutPoints.get(1).getValue()){
                p = ray.getPoint(t[cutPoints.get(0).getKey()]);
            } else {
                p = ray.getPoint(t[cutPoints.get(1).getKey()]);
            }
        } else {
            p = ray.getPoint(t[cutPoints.get(0).getKey()]);
        }
        
        v.setVert(p.x, p.y, p.z);
        
        return true;
    }
    
    public boolean aabbTri(Triangle3d triangle){
        Vect3d boxcenter = new Vect3d((max.x+min.x)/2.0, 
                                        (max.y+min.y)/2.0, 
                                            (max.z+min.z)/2.0);
        Vect3d boxhalfsize = getExtent();

        if( triangle.a.x < boxcenter.x + boxhalfsize.x &&
                triangle.a.y < boxcenter.y + boxhalfsize.y &&
                triangle.a.z < boxcenter.z + boxhalfsize.z &&
                triangle.a.x > boxcenter.x - boxhalfsize.x &&
                triangle.a.y > boxcenter.y - boxhalfsize.y &&
                triangle.a.z > boxcenter.z - boxhalfsize.z){
            return true;
        }
        
        if( triangle.b.x < boxcenter.x + boxhalfsize.x &&
                triangle.b.y < boxcenter.y + boxhalfsize.y &&
                triangle.b.z < boxcenter.z + boxhalfsize.z &&
                triangle.b.x > boxcenter.x - boxhalfsize.x &&
                triangle.b.y > boxcenter.y - boxhalfsize.y &&
                triangle.b.z > boxcenter.z - boxhalfsize.z){
            return true;
        }
        
        if( triangle.c.x < boxcenter.x + boxhalfsize.x &&
                triangle.c.y < boxcenter.y + boxhalfsize.y &&
                triangle.c.z < boxcenter.z + boxhalfsize.z &&
                triangle.c.x > boxcenter.x - boxhalfsize.x &&
                triangle.c.y > boxcenter.y - boxhalfsize.y &&
                triangle.c.z > boxcenter.z - boxhalfsize.z){
            return true;
        }
        
        /*    use separating axis theorem to test overlap between triangle and box */
        /*    need to test for overlap in these directions: */
        /*    1) the {x,y,z}-directions (actually, since we use the AABB of the triangle */
        /*       we do not even need to test these) */
        /*    2) normal of the triangle */
        /*    3) crossproduct(edge from tri, {x,y,z}-directin) */
        /*       this gives 3x3=9 more tests */
        double fex, fey, fez;

        /* This is the fastest branch on Sun */
        /* move everything so that the boxcenter is in (0,0,0) */
        Vect3d v0 = new Vect3d(triangle.a.sub(boxcenter));
        Vect3d v1 = new Vect3d(triangle.b.sub(boxcenter));
        Vect3d v2 = new Vect3d(triangle.c.sub(boxcenter));

        /* Bullet 2: */
        /*  test if the box intersects the plane of the triangle */
        /*  compute plane equation of triangle: normal*x+d=0 */
        if (!planeBoxOverlap(boxhalfsize, v0, triangle.normal())) {
                return false;
        }

        /* Bullet 3:  */
        /*  test the 9 tests first (this was faster) */
        Vect3d e0 = new Vect3d(triangle.b.sub(triangle.a));   /* tri edge 0 */
        Vect3d e1 = new Vect3d(triangle.c.sub(triangle.b));   /* tri edge 1 */
        Vect3d e2 = new Vect3d(triangle.a.sub(triangle.c));   /* tri edge 2 */
        
        fex = Math.abs(e0.x);
        fey = Math.abs(e0.y);
        fez = Math.abs(e0.z);
        
        if(!axisTestX01(e0.z, e0.y, fez, fey, boxhalfsize, v0, v2))
                return false;

        if(!axisTestY02(e0.z, e0.x, fez, fex, boxhalfsize, v0, v2))
                return false;
        if(!axisTestZ12(e0.y, e0.x, fey, fex, boxhalfsize, v1, v2))
                return false;

        fex = Math.abs(e1.x);
        fey = Math.abs(e1.y);
        fez = Math.abs(e1.z);
        
        if(!axisTestX01(e1.z, e1.y, fez, fey, boxhalfsize, v0, v2))
                return false;
        if(!axisTestY02(e1.z, e1.x, fez, fex, boxhalfsize, v0, v2))
                return false;
        if(!axisTestZ0(e1.y, e1.x, fey, fex, boxhalfsize, v0, v1))
                return false;

        fex = Math.abs(e2.x);
        fey = Math.abs(e2.y);
        fez = Math.abs(e2.z);
        
        if(!axisTestX2(e2.z, e2.y, fez, fey, boxhalfsize, v0, v1))
                return false;
        if(!axisTestY1(e2.z, e2.x, fez, fex, boxhalfsize, v0, v1))
                return false;
        if(!axisTestZ12(e2.y, e2.x, fey, fex, boxhalfsize, v1, v2))
                return false;

        return true;
    }

    private boolean planeBoxOverlap(Vect3d maxbox, Vect3d v0, Vect3d normal){
        Vect3d minV = new Vect3d(min); 
        Vect3d maxV = new Vect3d(max); 
        
        if(normal.x > 0.0f){
            minV.setX(-maxbox.x-v0.x);
            maxV.setX(maxbox.x-v0.x);
        } else {
            minV.setX(maxbox.x-v0.x);
            maxV.setX(-maxbox.x-v0.x);
        }
        
        if(normal.y > 0.0f){
            minV.setY(-maxbox.y-v0.y);
            maxV.setY(maxbox.y-v0.y);
        } else {
            minV.setY(maxbox.y-v0.y);
            maxV.setY(-maxbox.y-v0.y);
        }
                
        if(normal.z > 0.0f){
            minV.setZ(-maxbox.z-v0.z);
            maxV.setZ(maxbox.z-v0.z);
        } else {
            minV.setZ(maxbox.z-v0.z);
            maxV.setZ(-maxbox.z-v0.z);
        }
        
        if(normal.dot(minV) > 0.0f) 
            return false;
        
        return normal.dot(maxV) >= 0.0f;
    }
    

    /*======================== X-tests ========================*/
    private boolean axisTestX01(double a, double b, double fa, double fb, 
                                Vect3d boxhalfsize, Vect3d v0, Vect3d v2) {
        double p0 = a * v0.y - b * v0.z;
        double p2 = a * v2.y - b * v2.z;

        if (p0 < p2) {
                vmin = p0;
                vmax = p2;
        } else {
                vmin = p2;
                vmax = p0;
        }

        double rad = fa * boxhalfsize.y + fb * boxhalfsize.z;

        return vmin <= rad && vmax >= -rad;
    }

    private boolean axisTestX2(double a, double b, double fa, double fb,
                               Vect3d boxhalfsize, Vect3d v0, Vect3d v1) {
        double p0 = a * v0.y - b * v0.z;
        double p1 = a * v1.y - b * v1.z;

        if (p0 < p1) {
                vmin = p0;
                vmax = p1;
        } else {
                vmin = p1;
                vmax = p0;
        }

        double rad = fa * boxhalfsize.y + fb * boxhalfsize.z;

        return vmin <= rad && vmax >= -rad;
    }
        
    /*======================== Y-tests ========================*/
    private boolean axisTestY02(double a, double b, double fa, double fb,
                                Vect3d boxhalfsize, Vect3d v0, Vect3d v2) {
        double p0 = -a * v0.x + b * v0.z;
        double p2 = -a * v2.x + b * v2.z;

        if (p0 < p2) {
                vmin = p0;
                vmax = p2;
        } else {
                vmin = p2;
                vmax = p0;
        }

        double rad = fa * boxhalfsize.x + fb * boxhalfsize.z;

        return vmin <= rad && vmax >= -rad;
    }

    private boolean axisTestY1(double a, double b, double fa, double fb,
                               Vect3d boxhalfsize, Vect3d v0, Vect3d v1) {
        double p0 = -a * v0.x + b * v0.z;
        double p1 = -a * v1.x + b * v1.z;

        if (p0 < p1) {
                vmin = p0;
                vmax = p1;
        } else {
                vmin = p1;
                vmax = p0;
        }

        double rad = fa * boxhalfsize.x + fb * boxhalfsize.z;

        return vmin <= rad && vmax >= -rad;
    }

    /*======================== Z-tests ========================*/
    private boolean axisTestZ12(double a, double b, double fa, double fb,
                                Vect3d boxhalfsize, Vect3d v1, Vect3d v2) {
        double p1 = a * v1.x - b * v1.y;
        double p2 = a * v2.x - b * v2.y;
        
        if (p2 < p1) {
                vmin = p2;
                vmax = p1;
        } else {
                vmin = p1;
                vmax = p2;
        }
        
        double rad = fa * boxhalfsize.x + fb * boxhalfsize.y;
        
        return vmin <= rad && vmax >= -rad;
    }

    private boolean axisTestZ0(double a, double b, double fa, double fb,
                                Vect3d boxhalfsize, Vect3d v0, Vect3d v1) {
        double p0 = a * v0.x - b * v0.y;
        double p1 = a * v1.x - b * v1.y;
        
        if (p0 < p1) {
                vmin = p0;
                vmax = p1;
        } else {
                vmin = p1;
                vmax = p0;
        }
       
        double rad = fa * boxhalfsize.x + fb * boxhalfsize.y;
        
        return vmin <= rad && vmax >= -rad;
    }

}
