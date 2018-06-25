package algGeom.lib3D;

import Util.BasicGeom;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Random;
import java.util.StringTokenizer;

public final class TriangleMesh {
    ArrayList<Face> faces;
    ArrayList<Vect3d> vertices;
    boolean setNormal;
    
    public TriangleMesh(String filename) throws IOException {
        faces = new ArrayList<Face>();
        vertices = new ArrayList<Vect3d>();
        setNormal = false;
        String obj = "obj";

        File file;
        FileReader fr;
        BufferedReader reader;

        file = new File(filename);
        fr = new FileReader(file);
        reader = new BufferedReader(fr);

        String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
        if (!extension.equals(obj)) {
            System.out.println("Only obj model sare supported ");
        } else {
            loadobject(reader);
            setNormals();
            setNormal = true;
        }
    }

    public ArrayList<Triangle3d> getTriangules() {
        ArrayList<Triangle3d> triangles = new ArrayList<Triangle3d>();
        for (int i = 0; i < faces.size(); i++) {
            Face f = faces.get(i);
            Vect3d a = vertices.get(f.v1 - 1);
            Vect3d b = vertices.get(f.v2 - 1);
            Vect3d c = vertices.get(f.v3 - 1);
            triangles.add(new Triangle3d(a, b, c));
        }
        return triangles;
    }

    public double[] getVerticesTriangles() {
        double[] tri = new double[faces.size() * 9];
        for (int i = 0; i < faces.size(); i++) {
            Face f = faces.get(i);
            Vect3d a = vertices.get(f.v1 - 1);
            tri[i * 9] = a.x;
            tri[i * 9 + 1] = a.y;
            tri[i * 9 + 2] = a.z;
            Vect3d b = vertices.get(f.v2 - 1);
            tri[i * 9 + 3] = b.x;
            tri[i * 9 + 4] = b.y;
            tri[i * 9 + 5] = b.z;
            Vect3d c = vertices.get(f.v3 - 1);
            tri[i * 9 + 6] = c.x;
            tri[i * 9 + 7] = c.y;
            tri[i * 9 + 8] = c.z;
        }
        return tri;
    }

    public double[] getVertices() {
        double[] vertex = new double[3 * vertices.size()];
        int j = 0;
        for (int i = 0; i < vertices.size(); i++) {
            vertex[j++] = vertices.get(i).x;
            vertex[j++] = vertices.get(i).y;
            vertex[j++] = vertices.get(i).z;

        }
        return vertex;
    }


    public int[] getIndiceFaces() {
        int[] faces = new int[3 * this.faces.size()];
        int j = 0;
        for (int i = 0; i < this.faces.size(); i++) {
            faces[j++] = this.faces.get(i).v1;
            faces[j++] = this.faces.get(i).v2;
            faces[j++] = this.faces.get(i).v3;
        }
        return faces;
    }

    public Triangle3d getTriangle(int i) {
        Face f = faces.get(i);
        Vect3d a = vertices.get(f.v1 - 1);
        Vect3d b = vertices.get(f.v2 - 1);
        Vect3d c = vertices.get(f.v3 - 1);
        return new Triangle3d(a, b, c);
    }


    public Vect3d getVertice(int i) {
        return new Vect3d(vertices.get(i).getX(), vertices.get(i).getY(), vertices.get(i).getZ());
    }

    public void setNormals() {
        for (int i = 0; i < faces.size(); i++) {
            Face f = faces.get(i);
            Vect3d a = vertices.get(f.v1 - 1);
            Vect3d b = vertices.get(f.v2 - 1);
            Vect3d c = vertices.get(f.v3 - 1);
            Triangle3d t = new Triangle3d(a, b, c);
            f.setNormal(t.normal());
        }
        setNormal = true;
    }

    public double[] getNormals() {
        double[] nor = new double[3 * faces.size()];
        if (!setNormal) {
            setNormals();
        }
        for (int i = 0; i < faces.size(); i++) {
            Face f = faces.get(i);
            Vect3d b = f.getNormal();
            nor[i * 3] = b.x;
            nor[i * 3 + 1] = b.y;
            nor[i * 3 + 2] = b.z;
        }
        return nor;
    }

    
    /**
     * carga el modelo de la variable fichero
     *
     * @param br variable fichero
     */
    public void loadobject(BufferedReader br) {
        String line = "";

        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                line = line.replaceAll("  ", " ");
                if (line.length() > 0) {
                    if (line.startsWith("v ")) {
                        float[] vert = read3Floats(line);
                        Vect3d point = new Vect3d(vert[0], vert[1], vert[2]);
                        vertices.add(point);
                    } else if (line.startsWith("vt")) {

                        continue;

                    } else if (line.startsWith("vn")) {

                        continue;
                    } else if (line.startsWith("f ")) {
                        int[] faces = read3Integer(line);
                        this.faces.add(new Face(faces));
                    } else if (line.startsWith("g ")) {
                        continue;
                    } else if (line.startsWith("usemtl")) {
                        continue;
                    } else if (line.startsWith("mtllib")) {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("GL_OBJ_Reader.loadObject() failed at line: " + line);
        }

        System.out.println("Obj loader: vertices " + vertices.size()
                + " faces " + faces.size());

    }

    public int getFacesSize() {
        return faces.size();
    }

    public int getVerticesSize() {
        return vertices.size();
    }

    private int[] read3Integer(String line) {
        try {
            StringTokenizer st = new StringTokenizer(line, " ");
            st.nextToken();
            if (st.countTokens() == 2) {
                return new int[]{Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), 0};
            } else {
                return new int[]{Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken())};
            }
        } catch (NumberFormatException e) {
            System.out.println("GL_OBJ_Reader.read3Floats(): error on line '" + line + "', " + e);
            return null;
        }
    }

    private float[] read3Floats(String line) {
        try {
            StringTokenizer st = new StringTokenizer(line, " ");
            st.nextToken();
            if (st.countTokens() == 2) {
                return new float[]{Float.parseFloat(st.nextToken()),
                    Float.parseFloat(st.nextToken()),
                    0};
            } else {
                return new float[]{Float.parseFloat(st.nextToken()),
                    Float.parseFloat(st.nextToken()),
                    Float.parseFloat(st.nextToken())};
            }
        } catch (NumberFormatException e) {
            System.out.println("GL_OBJ_Reader.read3Floats(): error on line '" + line + "', " + e);
            return null;
        }
    }

    /**
     * Get triangle mesh aabb
     * @return aabb
     * @throws Exception 
     */
    public AABB getAABB() throws Exception {
        PointCloud3d pointCloud3d = new PointCloud3d(vertices);
        
        return pointCloud3d.getAABB();
    }


    /**
     * Check intersections between a ray and a triangle mesh
     * @param r Ray
     * @param p Point where intersect
     * @param t Triangle to modify if there are intersection
     * @return the first triangle that intersects and a boolean with the result
     */
    public boolean rayTraversalExh (Ray3d r, Vect3d p, Triangle3d t){
        Triangle3d taux;
        for(int i = 0; i<faces.size(); i++){
            taux = getTriangle(i);
            if(taux.ray_tri(r, p)){
                t.set(taux.getA(), taux.getB(), taux.getC());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check intersections between a ray and a triangle mesh
     * @param r Ray
     * @param p Point where intersect the first time
     * @param at Triangles Array to add triangles that intersect
     * @return triangles array that intersect and a boolean with the result
     */
    public boolean rayTraversalExh (Ray3d r, Vect3d p, ArrayList<Triangle3d> at){
        boolean intersection = false;
        boolean isFirst = true;
        Vect3d vaux = new Vect3d();      
        Triangle3d taux;
        
        for(int i = 0; i<faces.size(); i++){
            taux = getTriangle(i);
            if(taux.ray_tri(r, p)){
                at.add(taux);
                intersection = true;
                if(isFirst){
                    vaux.setVert(p.x, p.y, p.z);
                    isFirst = false;
                }
            }
        }
        p.setVert(vaux.x, vaux.y ,vaux.z);
        
        return intersection;
    }
    
   
        
    
    /**
     * Determinate if a point is inside of triangle mesh
     * @param p Vector
     * @return
     * @throws java.lang.Exception
     */
    public boolean puntoEnMesh (Vect3d p) throws Exception{
        ArrayList<Triangle3d> triangles = new ArrayList<Triangle3d>();
        boolean first =false, second = false;
        Vect3d pointIntersect = new Vect3d();
        
        //We launch the first ray
        Ray3d firstRay = new Ray3d(p, RandomVector());
        if(rayTraversalExh(firstRay, pointIntersect, triangles)){
            if(triangles.size() % 2 != 0){
                first = true;
            }
        }
        
        //We launch the second ray
        triangles.clear();
        Ray3d secondRay = new Ray3d(p, RandomVector());
        if(rayTraversalExh(secondRay, pointIntersect, triangles)){
            if(triangles.size() % 2 != 0){
                second = true;
            }
        }
        
        if(first == second)
            return first;
        else{
            //We launch the third ray
            triangles.clear();
            Ray3d thirdRay = new Ray3d(p, RandomVector());
            if(rayTraversalExh(thirdRay, pointIntersect, triangles)){
                if(triangles.size() % 2 != 0){
                    return true;
                }
            }
            return false;
        }
    }
    
    
    /**
     * 
     * @return 
     */
    private Vect3d RandomVector(){
        Random r = new Random (40);
        double x, y, z;
        int pos;
        
        pos = r.nextBoolean() ? 1 : -1;
        x = r.nextDouble() * pos * BasicGeom.RANGE;
        pos = r.nextBoolean() ? 1 : -1;
        y = r.nextDouble() * pos * BasicGeom.RANGE;
        pos = r.nextBoolean() ? 1 : -1;
        z = r.nextDouble() * pos * BasicGeom.RANGE;

        return new Vect3d (x,y,z);
    }
    
    /**
     * @brief 
     * @param x
     * @param y
     * @param z
     * @return 
     */
    private int smaller(double x, double y, double z){
        int aux;
        if(x<y){
            aux = (x<z) ? 0 : 2;
        } else {
            aux = (y<z) ? 1 : 2;
        }
        return aux;
    }
    
    private void removeDuplicate(ArrayList<Triangle3d> at){
        ArrayList<Triangle3d> aux = new ArrayList<Triangle3d>();
        for(int i=0; i<at.size(); i++){
            boolean insert = true;
            for(int j=0; j<aux.size(); j++){
                if(aux.get(j).equal(at.get(i)))
                    insert = false;
            }
            if(insert)
                aux.add(at.get(i));
        }
        at.clear();
        at.addAll(aux);
    }
}
