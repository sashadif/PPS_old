/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParallelAlgorithmModel;

import LibNet.NetLibrary;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriSim;

/**
 *
 * @author innastetsenko
 */
public class GoUntilSim extends PetriSim {

    private Control control;
    private ThreadModel next, prev;
    private int threadComplexity;

    public GoUntilSim(PetriNet net) {
        super(net);
        next=null;
        prev = null;
        threadComplexity = 10; // 
    }

    public GoUntilSim(int threadComplexity) throws ExceptionInvalidNetStructure {
        super(NetLibrary.CreateNetGoUntil(threadComplexity));
        this.threadComplexity = threadComplexity;
    }
     public GoUntilSim(int cores, int threadComplexity) throws ExceptionInvalidNetStructure {
        super(NetLibrary.CreateNetGoUntil(cores, threadComplexity));
        this.threadComplexity = threadComplexity;
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

    public void setPrev(boolean isPrev) {
        if (isPrev) {
            this.getNet().getListP()[19].setMark(1);
        } else {
            this.getNet().getListP()[19].setMark(0);
        }
    }

   

    @Override
    public void doT() {
       
        if (this.getEventMin().getName().equalsIgnoreCase("start")) {
            getCondWhile().setMark(control.tlocLessLim());       
            this.getCondIf1().setMark(control.tMinLessLim());
             this.getCondFinish().setMark(control.tlimBiggerTmod()); // 25.02.2021  the error should be corrected in PPS24febr
            this.getCondNoNext().setMark(control.noNext()); //25.02.2021  the error!!! should be corrected in PPS24febr
        }
        if (this.getEventMin().getName().equalsIgnoreCase("doWhile")) { //  to while
            getCondWhile().setMark(control.tlocLessLim());   
            this.getCondIf1().setMark(control.tMinLessLim());
            this.getCondFinish().setMark(control.tlimBiggerTmod()); //25.02.2021 the error!!! should be corrected in PPS24febr
            this.getCondNoNext().setMark(control.noNext()); //25.02.2021 the error!!! should be corrected in PPS24febr
        }

        if (this.getEventMin().getName().equalsIgnoreCase("input")) {  // add external event
            control.moveTmin(threadComplexity);  // 16.03.2021
//            System.out.println(this.getName()+", tmin = "+control.getTmin()+", tloc"+control.getTloc());
            getCondIf1().setMark(control.tMinLessLim());
            getCondIf2().setMark(control.tlimBiggerTmod());
        }
        if (this.getEventMin().getName().equalsIgnoreCase("settLoc=tMin")) {
            control.setTloc(control.getTmin());
            getCondWhile().setMark(control.tlocLessLim());

        }

        if (this.getEventMin().getName().equalsIgnoreCase("settloc=tmod")) {
            control.setTloc(control.getTmod());
            //addExtInput
            if (next != null) {
                this.getNext().getControl().addExtinput(Double.MAX_VALUE); //?? а як обробити?
               
            }
        }

        if (this.getEventMin().getName().equalsIgnoreCase("settloc=tlim")) {
            control.setTloc(control.getTlim());
            getCondWhile().setMark(control.tlocLessLim());
        }

        if (this.getEventMin().getName().equalsIgnoreCase("remove")) {         
            this.getControl().removeExtinput();
        }
//         if (this.getEventMin().getName().equalsIgnoreCase("noNext")) {         
//            System.out.println("!!!!!!!!!!! the last object has finished to work !!!!!!!!!!!");
//        }

    }
    

    public void printState(){
        System.out.println(this.getName()+":  "+
                    this.getNet().getListP()[2].getName()+" = "+this.getNet().getListP()[2].getMark()+" , "+
                    this.getNet().getListP()[8].getName()+" ="+this.getNet().getListP()[8].getMark()+" , "+
                    this.getNet().getListP()[11].getName()+" ="+this.getNet().getListP()[11].getMark()+" , "+
                    this.getNet().getListP()[13].getName()+" = "+this.getNet().getListP()[13].getMark()+" , "+
                           this.getNet().getListP()[15].getName()+" = "+this.getNet().getListP()[15].getMark()+" , "+
                            this.getNet().getListP()[16].getName()+" = "+this.getNet().getListP()[16].getMark()+" , "+
                            this.getNet().getListP()[19].getName()+" = "+this.getNet().getListP()[19].getMark()+" , "+
                            this.getNet().getListP()[25].getName()+" = "+this.getNet().getListP()[25].getMark());
        
    }

//    public PetriP getCondBufferBigggerMax() {
//        return this.getNet().getListP()[30]; 
//    }

    public PetriP getCondIf1() {
        return this.getNet().getListP()[8];
    }
    public PetriP getCondIf2() {
        return this.getNet().getListP()[11];
    }
     
  

    public PetriP getCondWhile() {
        return this.getNet().getListP()[2];
    }
     public PetriP getCondNoNext() {
        return this.getNet().getListP()[13];
    }
     
      public PetriP getCondFinish() {
        return this.getNet().getListP()[11];
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
            this.getNet().getListP()[13].setMark(1);
        } else{
           this.getNet().getListP()[13].setMark(0); 
        }
    }

    /**
     * @return the prev
     */
    public ThreadModel getPrev() {
        return prev;
    }

    /**
     * @param prev the prev to set
     */
    public void setPrev(ThreadModel prev) {
        this.prev = prev;
        if(prev !=null){
            this.getNet().getListP()[19].setMark(1);
        } else{
            this.getNet().getListP()[19].setMark(1); 
        }
    }

}
