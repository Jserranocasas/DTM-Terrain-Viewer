package algGeom.lib3D;

public class Segment3d extends Edge3d {

    public Segment3d(Vect3d o, Vect3d d) {
        super(o, d);
    }

    /**
     * Muestra en pantalla los valores los valores de Edge3d
     */
    public void out() {
        System.out.print("Segment->Origin: ");
        orig.out();
        System.out.print("Segment->Destination: ");
        dest.out();
    }

}
