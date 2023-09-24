/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParallelAlgorithmModel;

//import LibNet.NetLibrary;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriObjModel;
import PetriObj.PetriSim;
import java.util.ArrayList;

/**
 *
 * @author dyfuchyna
 */
public class Model {

    public static void main(String[] args) throws ExceptionInvalidNetStructure {
        // 
        double tmodel=100;
//        ParallelSimulationModel model = getModel(tmodel, 2, 100, 3);

ParallelSimulationModel model = getModelCore(2,tmodel, 20, 10, 3);    // n and k/n 

//         for(PetriSim sim: model.getListObj()){
////            sim.printMark();
////            sim.printBuffer();
//        }
        model.setIsProtokol(false);
//       modelA.printStatistics();

        double simTime = tmodel*100000;  //дати можливість імітаційному алгоритму відтворити роботу паралельного алгоритму
        model.go(simTime);
        
        System.out.println("Current time at the final moment of simulation = "+model.getCurrentTime()+", no events in model = " + model.isStop());
      
//        for (PetriSim sim : modelA.getListObj()) {         
//            if(sim.getEventMin()==null){
//                System.out.println("stop in "+sim.getName());                
//            }
//        }
        for (PetriSim sim : model.getListObj()) {
            sim.printMark();
//            sim.printBuffer();
        }
//        modelA.printStatistics();
    }

    public static PetriObjModel getSimpleModel(int numThreads, int complexity) throws ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        PetriSim main = new PetriSim(LibNet.NetLibrary.CreateNetMain(numThreads, complexity));
        list.add(main);
        ThreadModel th = new ThreadModel(1000, 5, 3);
        list.add(th.getRun());
        list.add(th.getWait());
        list.add(th.getGoUntil());
        list.add(th.getWait1());
        list.add(th.getOutput());
        list.add(th.getWait3());
        main.getNet().getListP()[6] = th.getRun().getRunStart();
        main.getNet().getListP()[7] = th.getRun().getRunFinish();

        return new PetriObjModel(list);
    }

    public static ParallelSimulationModel getModel(double tsim, int numThreads, int threadComplexity, int maxBuffer) throws ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        PetriSim main = new PetriSim(LibNet.NetLibrary.CreateNetMain(numThreads, numThreads*threadComplexity));
        list.add(main);
        list.get(0).setName("Main");
        ThreadModel[] threads = new ThreadModel[numThreads];
        ThreadModel th;
        for (int i = 0; i < numThreads; i++) {
            th = new ThreadModel(tsim, threadComplexity, maxBuffer); // thread conplexity = numEvents in thread

            list.add(th.getRun());
            list.add(th.getWait());
            list.add(th.getGoUntil());
            list.add(th.getOutput());
            list.add(th.getWait3());
            th.getRun().getNet().getListP()[0] = main.getNet().getListP()[3]; //start
            th.getRun().getNet().getListP()[7] = main.getNet().getListP()[4]; //finish

            th.getRun().setName("Run_" + i);
            th.getWait().setName("Wait_" + i);
            th.getWait3().setName("Wait3_" + i);
            th.getGoUntil().setName("GoUntil_" + i);
            th.getOutput().setName("Output_" + i);
            th.getControl().setName("threadControl_"+ i);

            threads[i] = th;
        }
 
        for (int i = 1; i < numThreads; i++) {
            threads[i].addPrev(threads[i - 1]);  // links are there
        }

        for (int i = 0; i < numThreads - 1; i++) {
            threads[i].addNext(threads[i + 1]);  // links are there          
        }
        return new ParallelSimulationModel(list);
        
    }
    
    public static ParallelSimulationModel getModelCore(int cores, double tsim, int numThreads, int threadComplexity, int maxBuffer) throws ExceptionInvalidNetStructure {
        ArrayList<PetriSim> list = new ArrayList<>();
        PetriSim main = new PetriSim(LibNet.NetLibrary.CreateNetMain(cores,numThreads, numThreads*threadComplexity));
        list.add(main);
        list.get(0).setName("Main");
        ThreadModel[] threads = new ThreadModel[numThreads];
        ThreadModel th; 
        
        for (int i = 0; i < numThreads; i++) {
            th = new ThreadModel(cores, tsim, threadComplexity, maxBuffer); // thread conplexity = numEvents in thread

            list.add(th.getRun());
            list.add(th.getWait());
            list.add(th.getGoUntil());
            list.add(th.getOutput());
            list.add(th.getWait3());
            th.getRun().getNet().getListP()[0] = main.getNet().getListP()[3]; //start
            th.getRun().getNet().getListP()[7] = main.getNet().getListP()[4]; //finish
    
            // cores
//            System.out.println(th.getRun().getNet().getListP()[th.getRun().getNet().getListP().length - 1].getName());
            th.getRun().getNet().getListP()[th.getRun().getNet().getListP().length - 1] = main.getNet().getListP()[main.getNet().getListP().length - 1];
            th.getWait().getNet().getListP()[th.getWait().getNet().getListP().length - 1] = main.getNet().getListP()[main.getNet().getListP().length - 1];
            th.getGoUntil().getNet().getListP()[th.getGoUntil().getNet().getListP().length - 1] = main.getNet().getListP()[main.getNet().getListP().length - 1];
            th.getOutput().getNet().getListP()[th.getOutput().getNet().getListP().length - 1] = main.getNet().getListP()[main.getNet().getListP().length - 1];
            th.getWait3().getNet().getListP()[th.getWait3().getNet().getListP().length - 1] = main.getNet().getListP()[main.getNet().getListP().length - 1];

            th.getRun().setName("Run_" + i);
            th.getWait().setName("Wait_" + i);
            th.getWait3().setName("Wait3_" + i);
            th.getGoUntil().setName("GoUntil_" + i);
            th.getOutput().setName("Output_" + i);
            th.getControl().setName("threadControl_"+ i);
            
           th.getRun().getNet().getListP()[ th.getRun().getNet().getListP().length-1]= // cores
                   main.getNet().getListP()[ main.getNet().getListP().length-1];

            threads[i] = th;
        }

        for (int i = 1; i < numThreads; i++) {
            threads[i].addPrev(threads[i - 1]);  // links are there
        }

        for (int i = 0; i < numThreads - 1; i++) {
            threads[i].addNext(threads[i + 1]);  // links are there          
        }
        
  
        return new ParallelSimulationModel(list);
    }
    
    public static double getTimePerformance(int cores, double tmodel, int numThreads, int threadComplexity, int limit) throws ExceptionInvalidNetStructure{
             
//        ParallelSimulationModel model = getModel(tmodel, 2, 100, 3);
          ParallelSimulationModel model = getModelCore(cores,tmodel, numThreads, threadComplexity, limit);        

        model.setIsProtokol(false);
//       modelA.printStatistics();

        double simTime = tmodel*100000;  //дати можливість імітаційному алгоритму відтворити роботу паралельного алгоритму
       
        model.go(simTime);
        
//        System.out.println("Current time at the final moment of simulation = "+model.getCurrentTime()+", no events in model = " + model.isStop());
        return model.getCurrentTime();
    }
    
    
    
}
