/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParallelAlgorithmModel;

import PetriObj.ArcIn;
import PetriObj.ArcOut;
import PetriObj.PetriNet;
import PetriObj.PetriObjModel;
import PetriObj.PetriP;
import PetriObj.PetriSim;
import PetriObj.PetriT;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author innastetsenko
 */
public class ParallelSimulationModel extends PetriObjModel{
    
    public ParallelSimulationModel(ArrayList<PetriSim> List) {
        super(List);
    }
    
    
    @Override
     public boolean isFinishSimulation(){  // additional condition to ctop simulation
       
        return this.getListObj().get(0).getNet().getListP()[5].getMark()==1;
    }
     
  
    
  
    
}
