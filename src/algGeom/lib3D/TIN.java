package algGeom.lib3D;

import Parser.ParserDTM;
import java.util.ArrayList;

/**
 * @brief   Class that represent a Triangulated Irregular Network
 * @file    TIN.java
 * @date    30-05-2018
 * @author  Javier Serrano Casas
 */
public class TIN {
    private final double heightMin = ParserDTM.getInstance().getHeightMin(); 
    private final double heightMax = ParserDTM.getInstance().getHeightMax(); 
    private final ArrayList<Triangle3d> triangles;
    private final ArrayList<Vect3d> vertices;
    private final ArrayList<Edge3d> edges;
    private final double diffElevation;

    public TIN(double diffHeight) throws Exception{
        diffElevation = diffHeight;
        triangles = new ArrayList<Triangle3d>();
        vertices = new ArrayList<Vect3d>();
        edges = new ArrayList<Edge3d>();
        
        int nRows = ParserDTM.getInstance().getNRows();
        int nCols = ParserDTM.getInstance().getNCols();
        int cellSize = ParserDTM.getInstance().getCellSize();
        ArrayList<Double> heightMap = ParserDTM.getInstance().getHeightMap();
        double seaLevel = ParserDTM.getInstance().getSeaLevel();

        int n = 0;
        double y ;
        
        // Initiate vertices values
        for(int i=0; i<nRows; i++){
            for(int j=0; j<nCols; j++){
                y = heightMap.get(n++);
                y = ( y == seaLevel) ?  -1.0 : (1 - ((heightMax-y) / 
                                 (heightMax - heightMin))) * diffElevation;
                vertices.add(new Vect3d(j*cellSize, y, i*cellSize));
            }
        }
        
        // Initiate triangles values and edges values
        for(int i=0; i<nRows-1; i++){
            for(int j=0; j<nCols-1; j++){
                triangles.add(createTriangle(j+(i*nCols), j+((i+1)*nCols), j+1+((i+1)*nCols))); 
                triangles.add(createTriangle(j+(i*nCols), j+1+((i+1)*nCols), j+1+(i*nCols))); 
                
                edges.add(createEdge(j+(i*nCols), j+1+(i*nCols)));
                edges.add(createEdge(j+(i*nCols), j+((i+1)*nCols)));
                edges.add(createEdge(j+(i*nCols), j+1+((i+1)*nCols)));
                
                // If this is the last column
                if(j == nCols-2){ 
                    edges.add(createEdge(j+1+(i*nCols), j+1+((i+1)*nCols)));
                }
                // If this is the last row
                if(i == nRows-2){ 
                    edges.add(createEdge(j+((i+1)*nCols), j+1+((i+1)*nCols)));
                }
            }
        }
    }
    
    /* -------------------------- Getters Methods --------------------------- */
    public ArrayList<Triangle3d> getTriangles() {
        return triangles;
    }

    public ArrayList<Vect3d> getVertices() {
        return vertices;
    }

    public ArrayList<Edge3d> getEdges() {
        return edges;
    }
    /* ---------------------------------------------------------------------- */
    
    /**
     * @brief Create a new edge with vertices indexes passed by parameter
     * @param index1 Vertex index 1 
     * @param index2 Vertex index 2 
     * @return Edge built with vertices indexes
     */
    private Edge3d createEdge(int index1, int index2){
        return new Edge3d(vertices.get(index1), vertices.get(index2));
    }
    
    /**
     * @brief Create a new Triangle with vertices indexes passed by parameter
     * @param index1 Vertex index 1 
     * @param index2 Vertex index 2 
     * @param index3 Vertex index 3 
     * @return Triangle built with vertices indexes
     */
    private Triangle3d createTriangle(int index1, int index2, int index3){
        return new Triangle3d(vertices.get(index1), vertices.get(index2), vertices.get(index3));
    }
    
    /**
     * @brief Method to get vertex
     * @param index Identification number of an vertex
     * @return Vect3d correspondent
     */
    public Vect3d getVertex(int index){
        return getVertices().get(index);
    }
    
    /**
     * @brief Method to get triangle
     * @param index Identification number of an triangle
     * @return Triangle3d correspondent
     */
    public Triangle3d getTriangle(int index){
        return getTriangles().get(index);
    }
    
    /**
     * @brief Method to get edge
     * @param index Identification number of an edge
     * @return Edge3d correspondent
     */
    public Edge3d getEdge(int index){
        return getEdges().get(index);
    }
    
    /**
     * @brief Calculate triangle color with respect to height
     * @param index Identification number of an triangulo
     * @return Color obtained
     */
    public Vect3d getColorTriangle(int index){
        Triangle3d t = triangles.get(index);
        double halfElevation = (t.a.getY() + t.b.getY() + t.c.getY())/3.0;
        double elevationPercentage = halfElevation / diffElevation;
        
        double red, green, blue;
        //   Dark Brown = Color(0.3, 0.18, 0.15)
        if(elevationPercentage >= 0.75){ 
            red = ((1 - ((elevationPercentage-0.75) / 0.25)) * 0.26) + 0.3;
            green = ((1 - ((elevationPercentage-0.75) / 0.25)) * 0.07) + 0.18;
            blue = 0.15;
        } // Light Brown = Color(0.56, 0.25, 0.15)
        else if(elevationPercentage >= 0.5 && elevationPercentage < 0.75){
            red = (((elevationPercentage-0.5) / 0.25) * 0.36) + 0.2;
            green = ((1 - ((elevationPercentage-0.5) / 0.25)) * 0.25) + 0.25;
            blue = (((elevationPercentage-0.5) / 0.25) * 0.05) + 0.1;
        } // Light Green = Color(0.2, 0.5, 0.1)
        else if(elevationPercentage >= 0.25 && elevationPercentage < 0.5){
            red = (((elevationPercentage-0.25) / 0.25) * 0.1) + 0.1;
            green = (((elevationPercentage-0.25) / 0.25) * 0.17) + 0.33;
            blue = ((1 - ((elevationPercentage-0.25) / 0.25)) * 0.08) + 0.1;
        } // Dark Green = Color(0.1, 0.33, 0.18)
        else {
            red = ((elevationPercentage / 0.25) * 0.03) + 0.07;
            green = ((elevationPercentage / 0.25) * 0.02) + 0.31;
            blue = ((elevationPercentage / 0.25) * 0.14) + 0.04;
        } // Dark Green = Color(0.07, 0.31, 0.04)
        
        return new Vect3d(red, green, blue);
    }
    
    /**
     * @brief Check if the triangle is at sea level
     * @param index Identification number of an triangle
     * @return true if if all its vertices have a height of seaLevel
     * @throws java.lang.Exception
     */
    public boolean TriangleIsSea(int index) throws Exception{
        boolean isSea = true;
        if(triangles.get(index).getA().getY() > 0.1)
            isSea = false;
        if(triangles.get(index).getB().getY() > 0.1)
            isSea = false;
        if(triangles.get(index).getC().getY() > 0.1)
            isSea = false;
        
        return isSea;
    }
    
}
