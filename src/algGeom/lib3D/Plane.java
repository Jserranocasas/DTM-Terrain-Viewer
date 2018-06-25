package algGeom.lib3D;

import Util.*;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

enum intersectionType {
    POINT, SEGMENT, COPLANAR
}

public class Plane {

    static public class IntersectionTriangle {
        public intersectionType type;
        public Vect3d p;
        public Segment3d s;
    }

    static public class IntersectionLine {
        public intersectionType type;
        public Vect3d p;
    }

    Vect3d a, b, c; //tres puntos cualquiera del plano  

    /**
     *
     * @param p en pi = p+u * lambda + v * mu -> r en los puntos (R,S,T)
     * @param u en pi = p+u * lambda + v * mu -> d en los puntos (R,S,T)
     * @param v en pi = p+u * lambda + v * mu -> t en los puntos (R,S,T)
     * @param arePoints = false entonces los parámetros son p+u * lambda + v * mu sino son los puntos (R,S,T)
     */
    public Plane(Vect3d p, Vect3d u, Vect3d v, boolean arePoints) {
        if (!arePoints) { // son vectores de la forma: pi =  p+u * lambda + v * mu 
            a = p;
            b = u.add(a);
            c = v.add(a);
        } else { // son 3 puntos del plano cualesquiera 
            a = p;
            b = u;
            c = v;
        }
    }

    /**
     *
     * @return el valor de A en AX+BY+CZ+D = 0;
     *
     */
    public double getA() {

        return (BasicGeom.determinant2x2(c.getZ() - a.getZ(), c.getY() - a.getY(), b.getY() - a.getY(), b.getZ() - a.getZ()));
    }

    /**
     *
     * @return el valor de B en AX+BY+CZ+D = 0;
     *
     */
    public double getB() {

        return (BasicGeom.determinant2x2(c.getX() - a.getX(), c.getZ() - a.getZ(), b.getZ() - a.getZ(), b.getX() - a.getX()));
    }

    /**
     *
     * @return el valor de C en AX+BY+CZ+D = 0;
     *
     */
    public double getC() {
        return (BasicGeom.determinant2x2(c.getY() - a.getY(), c.getX() - a.getX(), b.getX() - a.getX(), b.getY() - a.getY()));
    }

    /**
     *
     * @return el valor de D en AX+BY+CZ+D = 0;
     *
     */
    public double getD() {
        return (-1) * (getA() * a.getX() + getB() * a.getY() + getC() * a.getZ());
    }

    /**
     *
     * @return el vector normal formado por (A,B,C) de la ecuación Ax+By+Cz+D = 0
     */
    public Vect3d getNormal() {
        double A = getA();
        double B = getB();
        double C = getC();
        Vect3d normal = new Vect3d(A, B, C);
        return normal.scalarMul(1 / normal.module());
    }

    /**
     * Devuelve un punto usando la ecuacion parametrica (plano=p+u*lambda+v*mu) del plano a partir de unos parametros
     * @param lambda
     * @param mu
     * @return 
     */
    public Vect3d getPointParametric(double lambda, double mu) {
        Vect3d u = b.sub(a),
                v = c.sub(a);

        return a.add(u.scalarMul(lambda)).add(v.scalarMul(mu));
    }

    /**
     * Calcula la distancia entre el plano y el punto dado
     * @param p
     * @return 
     */
    public double distance(Vect3d p) {
        double denominator = abs(getA()*p.x + getB()*p.y + getC()*p.z + getD());
        double numerator = sqrt(getA()*getA() + getB()*getB() + getC()*getC());
        return denominator / numerator;
    }

    /**
     * Dice si un punto pertenece a un plano o no
     * @param p
     * @return 
     */
    public boolean coplanar(Vect3d p) {
        return BasicGeom.equal(distance(p), BasicGeom.ZERO);
    }

    

    /**
     * Muestra en pantalla los valores los valores de Plano
     */
    public void out() {
        System.out.print("Plane->a: ");
        System.out.println(a);
        System.out.print("Plane->b: ");
        System.out.println(b);
        System.out.print("Plane->c: ");
        System.out.println(c);
    }

}
