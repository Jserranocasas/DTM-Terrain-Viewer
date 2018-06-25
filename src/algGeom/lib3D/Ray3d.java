package algGeom.lib3D;

public class Ray3d extends Edge3d {

    public Ray3d(Vect3d o, Vect3d d) {
        super(o, d);
    }

    /**
     * Muestra en pantalla los valores los valores de Edge3d
     */
    @Override
    public void out() {
        System.out.print("Ray->Origin: ");
        orig.out();
        System.out.print("Ray->Destination: ");
        dest.out();
    }

}
