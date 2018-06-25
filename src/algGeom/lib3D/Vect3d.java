package algGeom.lib3D;

import Util.BasicGeom;
import static java.lang.Math.sqrt;

public class Vect3d {

    double x, y, z;

    public Vect3d(double aa, double bb, double cc) {
        x = aa;
        y = bb;
        z = cc;
    }

    public Vect3d(Vect3d v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vect3d() {
        x = 0;
        y = 0;
        z = 0;
    }
    
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double[] getVert() {
        double[] vt = {x, y, z};
        return vt;
    }

    public void setVert(double aa, double bb, double cc) {
        x = aa;
        y = bb;
        z = cc;
    }

    /**
     * result = this - b
     * @param b
     * @return 
     */
    public Vect3d sub(Vect3d b) {
        return new Vect3d(x - b.getX(), y - b.getY(), z - b.getZ());

    }

    /**
     * result = this + b
     * @param b
     * @return 
     */
    public Vect3d add(Vect3d b) {
        return new Vect3d(x + b.getX(), y + b.getY(), z + b.getZ());

    }

    /**
     * producto por un escalar result = this * valorEscalar
     * @param val
     * @return 
     */
    public Vect3d scalarMul(double val) {
        return new Vect3d(x * val, y * val, z * val);
    }

    /**
     * producto escalar result = this.dot(b)
     * @param v
     * @return 
     */
    public double dot(Vect3d v) {
        return (x * v.getX() + y * v.getY() + z * v.getZ());
    }

    /**
     * devuelve this X b (producto cruzado)
     * @param b
     * @return 
     */
    public Vect3d xProduct(Vect3d b) {
        return new Vect3d(y * b.getZ() - z * b.getY(),
                z * b.getX() - x * b.getZ(),
                x * b.getY() - y * b.getX());
    }

    /**
     * devuelve la longitud del vector
     * @return 
     */
    public double module() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    public double distance(Vect3d p) {
        return sqrt((p.x-x*p.x-x)+(p.y-y*p.y-y)+(p.z-z*p.z-z));    
    }

    public boolean collinear(Vect3d a, Vect3d b) {
        Triangle3d tr = new Triangle3d(a, b, this);
        
        return BasicGeom.equal(tr.area(), BasicGeom.ZERO);
    }
    
    boolean equal(Vect3d p) {
        return (BasicGeom.equal(x, p.x) && BasicGeom.equal(y, p.y));
    }
    
    /**
     * Muestra en pantalla los valores de las coordenadas del Point.
     */
    public void out() {
        System.out.print("Coordenada x: ");
        System.out.println(x);
        System.out.print("Coordenada y: ");
        System.out.println(y);
        System.out.print("Coordenada z: ");
        System.out.println(z);
    }

}
