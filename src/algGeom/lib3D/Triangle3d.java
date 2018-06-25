package algGeom.lib3D;

import Util.BasicGeom;

enum PointPosition {
    POSITIVE, NEGATIVE, COPLANAR
};

enum PointTrianglePosition {
    PARALELL, COLLINEAR, INTERSECTS, NO_INTERSECTS
};

public class Triangle3d {

    /**
     * un triangulo viene definido por tres puntos en el espacio
     */
    protected Vect3d a, b, c;
    
    /**
     * Constructor por defecto a valor (0,0)
     */
    public Triangle3d() {
        a = new Vect3d();
        b = new Vect3d();
        c = new Vect3d();
    }

    /**
     * Constructor a partir de coordenadas de los tres puntos
     * @param ax
     * @param ay
     * @param az
     * @param bx
     * @param by
     * @param bz
     * @param cx
     * @param cy
     * @param cz
     */
    public Triangle3d(double ax, double ay, double az,
                        double bx, double by, double bz,
                           double cx, double cy, double cz) {
        a = new Vect3d(ax, ay, az);
        b = new Vect3d(bx, by, bz);
        c = new Vect3d(cx, cy, cz);
    }

    /**
     * Constructor copia
     * @param t
     */
    public Triangle3d(Triangle3d t) {
        a = new Vect3d(t.a);
        b = new Vect3d(t.b);
        c = new Vect3d(t.c);
    }

    /**
     * Constructor a partir de tres Vect3d de javax
     * @param va
     * @param vb
     * @param vc
     */
    public Triangle3d(Vect3d va, Vect3d vb, Vect3d vc) {
        a = new Vect3d(va);
        b = new Vect3d(vb);
        c = new Vect3d(vc);
    }

    /**
     * modifica los valores de los vertices de los triangulos
     * @param va
     * @param vb
     * @param vc
     */
    public void set(Vect3d va, Vect3d vb, Vect3d vc) {
        a = va;
        b = vb;
        c = vc;
    }

    /**
     * Obtiene el Vect3d de a
     * @return 
     */
    public Vect3d getA() {
        return a;
    }

    /**
     * Obtiene el Punto b
     * @return 
     */
    public Vect3d getB() {
        return b;
    }

    /**
     * Obtiene el Punto c
     * @return 
     */
    public Vect3d getC() {
        return c;
    }

    /**
     * Obtiene el punto i
     * @param i
     * @return 
     */
    public Vect3d getPoint(int i) {
        return (i == 0 ? a : (i == 1 ? b : c));
    }

    public Vect3d[] getPoints() {
        Vect3d[] vt = {a, b, c};
        return vt;
    }

    /**
     * Devuelve una copia del objeto Punto
     * @return 
     */
    public Triangle3d copy() {
        return new Triangle3d(a, b, c);
    }

    /**
     * Modify value's a
     * @param pa
     */
    public void setA(Vect3d pa) {
        a = pa;
    }

    /**
     * Modify value's b
     * @param pb
     */
    public void setB(Vect3d pb) {
        b = pb;
    }

    /**
     * Modify value's c
     * @param pc
     */
    public void setC(Vect3d pc) {
        c = pc;
    }

    /**
     * devuelve la normal al triangulo
     * @return 
     */
    public Vect3d normal() {
        Vect3d v1 = new Vect3d(b.sub(a));
        Vect3d v2 = new Vect3d(c.sub(a));
        Vect3d n = new Vect3d(v1.xProduct(v2));
        double longi = n.module();

        return (n.scalarMul(1.0 / longi));
    }

    /**
     * Muestra un punto 3d en pantalla
     */
    public void out() {
        System.out.println("Triangle3d: (" + a + "-" + b + "-" + c + ")");
    }

    public double area() {
       Vect3d AB = new Vect3d(b.sub(a));
       Vect3d AC = new Vect3d(c.sub(a));
       
       return AB.xProduct(AC).module()/2.0;
    }
    
    public boolean equal(Triangle3d t){
        return (this.a.equal(t.a)) && (this.b.equal(t.b)) && (this.c.equal(t.c));
    }

    public AABB getAABB(){
        Vect3d min = new Vect3d(BasicGeom.min3(a.x, b.x, c.x),
                                BasicGeom.min3(a.y, b.y, c.y),
                                BasicGeom.min3(a.z, b.z, c.z));
        Vect3d max = new Vect3d(BasicGeom.max3(a.x, b.x, c.x),
                                BasicGeom.max3(a.y, b.y, c.y),
                                BasicGeom.max3(a.z, b.z, c.z));
        return (new AABB(min,max));
    }

    /**
     * Check intersections of a ray with a triangle.
     * @param r Ray
     * @param p Vector
     * @return if there are intersection
     * @post If there are intersection then modify value p vector
     */
    public boolean ray_tri (Ray3d r, Vect3d p){
        Vect3d AB = b.sub(a);
        Vect3d AC = c.sub(a);
        Vect3d rayVector = r.dest.sub(r.orig);
        Vect3d h = rayVector.xProduct(AC);
        
        double fa,f,u,v,t;
                
        fa = AB.dot(h);
        if (BasicGeom.equal(fa, BasicGeom.ZERO)){
            return false;
        }
        
        f = 1.0/fa;
        Vect3d s = r.orig.sub(a);
        u = f * s.dot(h);
        
        if (u < 0.0 || u > 1.0)
            return false;
        Vect3d q = s.xProduct(AB);

        v = f * rayVector.dot(q);
        
        if (v < 0.0 || u + v > 1.0)
            return false;
        // At this stage we can compute t to find out where the intersection point is on the line.
        t = f * AC.dot(q);
        
        if (t > BasicGeom.ZERO){ // ray intersection
            Vect3d aux = r.orig.add(rayVector.scalarMul(t)); 
            p.setVert(aux.x,aux.y,aux.z);
            
            return true;
        } else{ // This means that there is a line intersection but not a ray intersection.
            return false;
        }
    }
    
    
    /**
     * Classify a triangle with respect to a vector
     * @param p Vector to check
     * @return PointPosition(Coplanar, positive, or negative)
     */
    public PointPosition classify(Vect3d p) {
        Vect3d v = new Vect3d(p.sub(a));
        double len = v.module();
        
        if(BasicGeom.equal(len, BasicGeom.ZERO)){
            return PointPosition.COPLANAR;
        }
        v = v.scalarMul(1.0/len);
        double d = v.dot(this.normal());
        
        if(d > BasicGeom.ZERO){
            return PointPosition.POSITIVE;
        } else if (d < -BasicGeom.ZERO){
            return PointPosition.NEGATIVE;
        } else {
            return PointPosition.COPLANAR;
        }
    }    
}