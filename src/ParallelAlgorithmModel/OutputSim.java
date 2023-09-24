/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParallelAlgorithmModel;

import LibNet.NetLibrary;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriNet;
import PetriObj.PetriSim;

/**
 *
 * @author innastetsenko
 */
public class OutputSim extends PetriSim {
    private Control control;
    private ThreadModel next;
     
     
    
    public OutputSim(PetriNet net) {
        super(net);
    }
    public OutputSim(int numEvents) throws ExceptionInvalidNetStructure{ 
        super(NetLibrary.CreateNetOutput(numEvents)); 
        next = null;
    }
    
    public OutputSim(int cores, int numEvents) throws ExceptionInvalidNetStructure{ 
        super(NetLibrary.CreateNetOutput(cores, numEvents)); 
        next = null;
    }

    /**
     * @return the control
     */
    public Control getControl() {
        return control;
    }

    /**
     * @param control the control to set
     */
    public void setControl(Control control) {
        this.control = control;
    }
    

    public void printState(){
        System.out.println(this.getName()+":  "+this.getNet().getListT()[4].condition(this.getNet().getListP())+" , "+
                    this.getNet().getListP()[6].getName()+" = "+this.getNet().getListP()[6].getMark()+" , "+
                    this.getNet().getListP()[13].getName()+" ="+this.getNet().getListP()[13].getMark()+" , "+
                    this.getNet().getListP()[12].getName()+" = "+this.getNet().getListP()[12].getMark());
    }
    
    
    
    @Override
    public void doT() {
//        System.out.println(this.getName()+" ------------------------ "+(next.getControl().getName())+","
//                +this.getControl().
//                        getTloc()
//        ); 
       
        if (this.getEventMin().getName().equalsIgnoreCase("trActout")) {
            
            if (next != null) {
                next.getControl().addExtinput(this.getControl().getTloc());   // 14.02.2021 передається tloc об'єкта, який додає зовнішню подію
            }
//            System.out.println(this.getNet().getListT()[4].condition(this.getNet().getListP())
//                    + ", mark 6 = " + this.getNet().getListP()[6].getMark()
//                    + ", mark 13 =" + this.getNet().getListP()[13].getMark()
//                    + ", mark 12 = " + this.getNet().getListP()[12].getMark());
        }

    }

  

    /**
     * @return the next
     */
    public ThreadModel getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNoNext(ThreadModel next) {
        this.next = next;
        if(next==null){
           
             this.getNet().getListP()[13].setMark(1);  // no next will check 
        } else{
           
            this.getNet().getListP()[13].setMark(0); // no next will check 
        }

    }
    
}
