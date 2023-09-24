/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import ParallelAlgorithmModel.Model;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriObjModel;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author innastetsenko
 */
public class Individual {
    
    private final int numParams;
    private int[] params;
    private double fit;  
    
    public Individual(int[] values){
         fit =Double.MAX_VALUE;              
        this.numParams=values.length;
        this.params = values;
    }
    // for the case of two parameters (using for the parallel simulation alg optimization)
    public Individual(int k, int limit){
                        
        this.numParams=2;
        params = new int[numParams];
        this.params[0] = k;
         this.params[1] = limit;
          fit =Double.MAX_VALUE; 
    }
    
    public Individual(int[] min, int[] max) {
        Random r = new Random();
        this.numParams = min.length;
        params = new int[numParams];
        for (int i = 0; i < numParams; i++) {
            this.params[i] = min[i] + r.nextInt(max[i] - min[i]);
        }
    }
    
    public void calcFit(int progon)  {
        double f = 0;
        int cores = 2;
        double timeMod = 100; // *1000000 при запуску моделі, тобто задаємо ча в секундах, а в моделі у мікросекундах час
        int totalComplexity = 400;   
        
        PetriObjModel model;
        try {
            for (int i = 0; i < progon; i++) {
                // Модель створюється наново, щоб з початкового стану імітація відбувалась
                f += Model.getTimePerformance(cores,timeMod,totalComplexity/params[0],params[0],params[1]);//[0]-thread complexity, [1]-limit 
            }
        } catch (ExceptionInvalidNetStructure ex) {
            Logger.getLogger(Individual.class.getName()).log(Level.SEVERE, null, ex);
        }

        fit = f / progon;
        
        System.out.println("calcFit "+ fit +", for params  "+params[0]+ " "+ params[1]);

    }

    public double getFit() {
        if(fit==Double.MAX_VALUE ){
            calcFit(4); // 4 progons by default
        }
        return fit;
    }
    
    public int[] getParams(){
        return params;
    }
    
    public void print(){
        System.out.println(this.getParams()[0]+"\t"+this.getParams()[1]+"\t "+this.getFit());  
    }
    
    
    
    
    
    
    
    
}
