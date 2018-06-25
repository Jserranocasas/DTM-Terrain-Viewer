package Parser;

import algGeom.lib3D.Test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @brief   Parser to DTM files
 * @file    parserDTM.java
 * @date    30/05/2018
 * @author  Javier Serrano Casas
 */
public class ParserDTM {
    private static ParserDTM INSTANCE = null; 
    private ArrayList<Double> heightMap = null; 
    private double heightMin;
    private double heightMax;
    private double seaLevel;
    private int cellSize;
    private int nRows;
    private int nCols;

    /**
     * @brief Constructor parametrizado de la clase ParserDTM.
     */
    private ParserDTM(String path) throws IOException, Exception{
        if(getExtension(path).equals("sec")){
            loadTerrainSEC(path);
        } 
        else if(getExtension(path).equals("asc")){
            loadTerrainASC(path);
        }
        else {
            throw new Exception("Error loading terrain file. Format isn't .sec or .asc");
        }
    }
    
    private synchronized static void createInstance(String path) throws IOException, Exception {
        if (INSTANCE == null) { 
            INSTANCE = new ParserDTM(path);
        }
    }
        
    public static ParserDTM getInstance() throws Exception {
        if (INSTANCE == null) try {
            createInstance(Test.TERRAINPATH);
        } catch (IOException ex) {
            Logger.getLogger(ParserDTM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return INSTANCE;
    }
    
    /* -------------------------- Getters Methods --------------------------- */
    public ArrayList<Double> getHeightMap() {
        return heightMap;
    }

    public double getHeightMin() {
        return heightMin;
    }

    public double getHeightMax() {
        return heightMax;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getNRows() {
        return nRows;
    }

    public int getNCols() {
        return nCols;
    }
    /* ---------------------------------------------------------------------- */
    
    
    /**
     * @brief Load a terrain in ASC format and initiate its variables
     * @param path that contains the file path
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void loadTerrainASC(String path) throws FileNotFoundException, IOException {
        heightMap = new ArrayList<Double>();
        double lessHeight = Double.MAX_VALUE;
        double higherHeight = Double.MIN_VALUE;
        
        String bond;
        String[] rowNum;
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);
        
        bond = b.readLine();
        rowNum = bond.split(" ");
        nCols = Integer.parseInt(rowNum[1]);  // Get rows numbers
        bond = b.readLine();
        rowNum = bond.split(" ");
        nRows = Integer.parseInt(rowNum[1]);  // Get columns numbers
        b.readLine(); b.readLine();
        bond = b.readLine();
        rowNum = bond.split(" ");
        cellSize = Integer.parseInt(rowNum[1]);  // Get cell size
        bond = b.readLine();
        rowNum = bond.split(" ");
        seaLevel = Integer.parseInt(rowNum[1]);  // Get sea level
        
        double height;
        while((bond = b.readLine()) != null) {
            rowNum = bond.split(" ");
            if(!rowNum[0].equals("\u001a")){
                for (String num : rowNum) { // Get elevation points
                    height = Double.parseDouble(num);
                    heightMap.add(height);

                    if(height < lessHeight  && height != seaLevel) lessHeight = height;
                    if(height > higherHeight) higherHeight = height;
                }
            }
        } 
        
        // Get min and max value of height
        heightMin = lessHeight;
        heightMax = higherHeight;
    }
    
    /**
     * @brief Load a terrain in SEC format and initiate its variables
     * @param path that contains the file path
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void loadTerrainSEC(String path) throws FileNotFoundException, IOException {
        heightMap = new ArrayList<Double>();
        Map<Double,Integer> countNumX = new HashMap<Double, Integer>();
        Map<Double,Integer> countNumY = new HashMap<Double, Integer>();
        double lessHeight = Double.MAX_VALUE;
        double higherHeight = Double.MIN_VALUE;
        
        String bond;
        String[] rowNum;
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);
                
        double height=0.0;
        while((bond = b.readLine()) != null) {
            rowNum = bond.split(",");
            int cont = 1;
            for (String num : rowNum) {    
                // If cont is 1 then this is x coordenate. So I insert number of times the number appears
                if (cont == 1) countNum(countNumX,Double.parseDouble(num));
                // If cont is 2 then this is y coordenate. So I insert number of times the number appears
                if (cont == 2) countNum(countNumY,Double.parseDouble(num));
                // If cont is 3 then this is height coordenate. So I insert data
                if (cont == 3) height = Double.parseDouble(num);

                cont++;
            }
            if(height < lessHeight) lessHeight = height;
            if(height > higherHeight) higherHeight = height;
            
            heightMap.add(height);
        } 
        
        // Get row size and column size by the number of times a number is repeated
        Map.Entry<Double,Integer> entry = countNumX.entrySet().iterator().next();
        nRows = entry.getValue();
        entry = countNumY.entrySet().iterator().next();
        nCols = entry.getValue();
        
        // Get min and max value of height
        heightMin = lessHeight;
        heightMax = higherHeight;
        
        // Get cell size by the difference between any two coordinates
        cellSize = calculateCellSize(path);
        
        seaLevel = 200.0;
    }

    /**
     * @brief Count the number time what it repeats a number
     * @param coordenate Buffer with coordenates to check
     * @param num Number that we want to know how many times it repeats
     */
    private void countNum(Map<Double, Integer> coordenate, double num){
        Integer numTime;
        if ((numTime = coordenate.get(num)) == null) {
            coordenate.put(num,1);
        } else {
            coordenate.put(num,numTime+1);
        }
    }
    
    /**
     * @brief Calculate cell size by the difference between two x coordenates
     * @param path String that contains the file path
     * @return cell size
     * @throws IOException 
     */
    private int calculateCellSize(String path) throws IOException{
        String bond;
        String[] rowNum;
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);

        bond = b.readLine();
        rowNum = bond.split(",");
        double num1 = Double.parseDouble(rowNum[0]);
        bond = b.readLine();
        rowNum = bond.split(",");
        double num2 = Double.parseDouble(rowNum[0]);
            
        return (int) Math.abs(num1-num2);
    }
    
    /**
     * @brief Get file extension to know what method to use
     * @param name String that contains the file path
     * @return String with the extension type
     */
    private String getExtension(String name) {
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

}