package algGeom.lib3D;

import Util.BasicGeom;

enum classifyLines {NON_INTERSECT, PARALLEL, INTERSECT, THESAME}

public class Line3d extends Edge3d {

    public Line3d(Vect3d o, Vect3d d) {
        super(o, d);
    }

    @Override
    public void out() {
        System.out.print("Line->Origin: ");
        orig.out();
        System.out.print("Line->Destination: ");
        dest.out();
    }

    public double distance(Vect3d p) {  
        Vect3d u = new Vect3d(orig.sub(dest));
        Vect3d A = new Vect3d(orig);
        Vect3d AP = new Vect3d(p.sub(A));
        
        return u.xProduct(AP).module()/u.module();
    }
    
    public double distance(Line3d p) {
        return GetPointA(p).distance(GetPointB(p));
    }

    public classifyLines classify (Line3d l, Vect3d pA, Vect3d pB){
        if(orig.equals(l.orig) && dest.equal(l.dest))
            return classifyLines.THESAME;
        if(BasicGeom.equal(getD(l), BasicGeom.ZERO))
            return classifyLines.PARALLEL;
        if(GetPointA(l).equals(GetPointB(l)))
                return classifyLines.INTERSECT;
        return classifyLines.NON_INTERSECT;     
    }
   
    public Vect3d GetPointA(Line3d p){
        Vect3d v = dest.sub(orig);
        return new Vect3d(orig.add(v.scalarMul(getS(p))));
    }
    
    public Vect3d GetPointB(Line3d p){
        Vect3d w = p.dest.sub(p.orig);
        return new Vect3d(p.orig.add(w.scalarMul(getT(p))));
    }
    
    private double getS(Line3d p){
        Vect3d v = dest.sub(orig);
        Vect3d w = p.dest.sub(p.orig);
        Vect3d PQ = new Vect3d(p.orig.sub(orig));
        return  ((-PQ.dot(w)*w.dot(v)) + (PQ.dot(v)*w.dot(w))) / getD(p);
    }
    
    private double getT(Line3d p){
        Vect3d v = dest.sub(orig);
        Vect3d w = p.dest.sub(p.orig);
        Vect3d PQ = new Vect3d(p.orig.sub(orig));
        return ((PQ.dot(v)*v.dot(w)) - (PQ.dot(w)*v.dot(v))) / getD(p);
    }
    
    private double getD(Line3d p){
        Vect3d v = dest.sub(orig);
        Vect3d w = p.dest.sub(p.orig);
        return (v.dot(v) * w.dot(w)) - (v.dot(w) * v.dot(w));
    } 
}
