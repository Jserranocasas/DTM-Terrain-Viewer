package Util;

import java.util.ArrayList;


public class SeedGenerator {
    
    public final static long TAM_IMAGE_X = 100;
    public final static long TAM_IMAGE_Y = 100;
    public final static int TAM_SEED = 9;
    
    private static SeedGenerator inst = null;
    private ArrayList< ArrayList< randColor > >Image;
    
    private  SeedGenerator()
    {
        Image = new ArrayList< ArrayList <randColor > >();
        for(int i=0; i<TAM_IMAGE_X;i++)
        {
            Image.add(new ArrayList <randColor>());
            for(int j=0;j<TAM_IMAGE_Y;j++)
            {
                Image.get(i).add(new randColor());
            }
        }
    }
    
    public static SeedGenerator getInst()
    {
        if(inst == null)
            inst = new SeedGenerator(); 
        return inst;
    }
    
    public long getSeed()
    {
        long pr = (long)(Math.random() * Math.pow(10, TAM_SEED));
        long pg =  (long)(Math.random() * Math.pow(10, TAM_SEED));
        long pb =  (long)(Math.random() * Math.pow(10, TAM_SEED));
        
        long sum = 0;
        
        for(int i=0; i<TAM_IMAGE_X;i++)
        {
            for(int j=0; j<TAM_IMAGE_Y;j++)
            {
                sum += Image.get(i).get(j).getLong(pr, pg, pb);
            }
        }
        return sum;
    }
    
    //TODO Seed reader form image public long getSeed(String filename){}
    
    private class randColor
    {
        public double r;
        public double g;
        public double b;
        
        public randColor()
        {
            r = Math.random();
            g = Math.random();
            b = Math.random();
        }
        
        public long getLong(long pr, long pg, long pb)
        {
            return (long)(pr * r + pg * g + pb * b);
        }
    
    }
}
