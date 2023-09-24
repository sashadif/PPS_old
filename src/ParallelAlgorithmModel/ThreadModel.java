/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParallelAlgorithmModel;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriP;

/**
 *
 * @author dyfuchyna
 */
public class ThreadModel {

    private RunSim run;
    private WaitSim wait;
    private GoUntilSim goUntil;
    private OutputSim output;
    private WaitSim wait1;

    private WaitSim wait3;
    private Control control;
    private ThreadModel prev;
    private ThreadModel next;

    // tsim is the simultion time for parallel algorithm   
    public ThreadModel(double tsim, int numEvents, int maxBuffer) throws ExceptionInvalidNetStructure {
        prev = null;
        next = null;
        run = new RunSim();

        wait = new WaitSim(0);
        run.getNet().getListP()[5] = wait.getNet().getListP()[0];
        run.getNet().getListP()[6] = wait.getNet().getListP()[6];
      
        goUntil = new GoUntilSim(numEvents);
        goUntil.setNoNext(this.next);
        run.getNet().getListP()[10] = goUntil.getNet().getListP()[0];
        run.getNet().getListP()[11] = goUntil.getNet().getListP()[3];
     

        output = new OutputSim(numEvents);  // = complexity/numThreads
        output.setNoNext(this.next);
        goUntil.getNet().getListP()[6] = output.getNet().getListP()[0]; //start
        goUntil.getNet().getListP()[7] = output.getNet().getListP()[12]; //finish

        wait3 = new WaitSim(3);
        output.getNet().getListP()[6] = wait3.getNet().getListP()[0]; //start
        output.getNet().getListP()[10] = wait3.getNet().getListP()[6];//finish
        
       
        
        control = new Control(tsim, maxBuffer); // tSim = 1000; maxExternInput = 3
        run.setControl(control);
        wait.setControl(control);
        goUntil.setControl(control);
        output.setControl(control);
        wait3.setControl(control);

    }
    
     public ThreadModel(int cores, double tsim, int numEvents, int maxBuffer) throws ExceptionInvalidNetStructure {
        prev = null;
        next = null;
        run = new RunSim(cores);

        wait = new WaitSim(cores, 0);
        run.getNet().getListP()[5] = wait.getNet().getListP()[0];
        run.getNet().getListP()[6] = wait.getNet().getListP()[6];
      

        goUntil = new GoUntilSim(cores, numEvents);
        goUntil.setNoNext(this.next);
        run.getNet().getListP()[10] = goUntil.getNet().getListP()[0];
        run.getNet().getListP()[11] = goUntil.getNet().getListP()[3];

        output = new OutputSim(cores, numEvents);  // = complexity/numThreads
        output.setNoNext(this.next);
        goUntil.getNet().getListP()[6] = output.getNet().getListP()[0]; //start
        goUntil.getNet().getListP()[7] = output.getNet().getListP()[12]; //finish

        wait3 = new WaitSim(cores, 3);
        output.getNet().getListP()[6] = wait3.getNet().getListP()[0]; //start
        output.getNet().getListP()[10] = wait3.getNet().getListP()[6];//finish
        
        
        wait.getNet().getListP()[ wait.getNet().getListP().length-1] = run.getNet().getListP()[ run.getNet().getListP().length-1];
        goUntil.getNet().getListP()[ goUntil.getNet().getListP().length-1] = run.getNet().getListP()[ run.getNet().getListP().length-1];
        output.getNet().getListP()[ output.getNet().getListP().length-1] = run.getNet().getListP()[ run.getNet().getListP().length-1];  
        wait3.getNet().getListP()[ wait3.getNet().getListP().length-1] = run.getNet().getListP()[ run.getNet().getListP().length-1];


        control = new Control(tsim, maxBuffer); // tSim = 1000; maxExternInput = 3
        run.setControl(control);
        wait.setControl(control);
        goUntil.setControl(control);
        output.setControl(control);
        wait3.setControl(control);

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

    public void addPrev(ThreadModel other) {
        run.setPrev(true);
        goUntil.setPrev(true);
        control.setPrev(true);

        prev = other;
        goUntil.setPrev(other);

        this.getGoUntil().getNet().getListP()[25] = prev.getWait3().getNet().getListP()[7]; // link signals condLimit between this and other
        this.getGoUntil().getNet().getListP()[24] = prev.getWait3().getNet().getListP()[1]; //unlock
        
        this.getRun().getNet().getListP()[17] = prev.getWait3().getNet().getListP()[7]; // link signals condLimit between this and other
        this.getRun().getNet().getListP()[15] = prev.getWait3().getNet().getListP()[1]; //unlock

        prev.getGoUntil().getNet().getListP()[16] = this.getWait().getNet().getListP()[7];   // link signals between this and other
        prev.getGoUntil().getNet().getListP()[15] = this.getWait().getNet().getListP()[1]; //Unlock 

// added 14.02.2021, edit 25.02.2021
        prev.getOutput().getNet().getListP()[8] = this.getWait().getNet().getListP()[7]; //link signal  
        prev.getOutput().getNet().getListP()[7] = this.getWait().getNet().getListP()[1]; //UnLock
     
    }

    public void addNext(ThreadModel other) {
        control.setNext(true);
        next = other;
        goUntil.setNoNext(other);
        output.setNoNext(other);
        wait3.setNext(other);

        //signal from prev obj = signal to Next 
        this.getWait3().getNet().getListP()[7] = next.getGoUntil().getNet().getListP()[25]; // link signals condLimit between this and other
        this.getWait3().getNet().getListP()[1] = next.getGoUntil().getNet().getListP()[24]; //unlock

        this.getWait3().getNet().getListP()[7] = next.getRun().getNet().getListP()[17]; // link signals condLimit between this and other
        this.getWait3().getNet().getListP()[1] = next.getRun().getNet().getListP()[15]; //unlock
        
        // signal to Next  = signal from prev obj
        this.getGoUntil().getNet().getListP()[16] = next.getWait().getNet().getListP()[7];   // link signals between this and other
        this.getGoUntil().getNet().getListP()[15] = next.getWait().getNet().getListP()[1]; //Unlock 
       
        this.getOutput().getNet().getListP()[8] = next.getWait().getNet().getListP()[7]; //link signal
        this.getOutput().getNet().getListP()[7] = next.getWait().getNet().getListP()[1]; //UnLock
    }

    public PetriP getSignalFromPrev() {
        return wait.getSignal();
    }

    public PetriP getSignalNext() {
        return wait3.getSignal();
    }

    /**
     * @return the run
     */
    public RunSim getRun() {
        return run;
    }

    /**
     * @param run the run to set
     */
    public void setRun(RunSim run) {
        this.run = run;
    }

    /**
     * @return the wait
     */
    public WaitSim getWait() {
        return wait;
    }

    /**
     * @param wait the wait to set
     */
    public void setWait(WaitSim wait) {
        this.wait = wait;
    }

    /**
     * @return the goUnti
     */
    public GoUntilSim getGoUntil() {
        return goUntil;
    }

    /**
     * @param goUntil the goUnti to set
     */
    public void setGoUntil(GoUntilSim goUntil) {
        this.goUntil = goUntil;
    }

    /**
     * @return the output
     */
    public OutputSim getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(OutputSim output) {
        this.output = output;
    }

    /**
     * @return the wait1
     */
    public WaitSim getWait1() {
        return wait1;
    }

    /**
     * @param wait1 the wait1 to set
     */
    public void setWait1(WaitSim wait1) {
        this.wait1 = wait1;
    }

  

    /**
     * @return the wait3
     */
    public WaitSim getWait3() {
        return wait3;
    }

    /**
     * @param wait3 the wait3 to set
     */
    public void setWait3(WaitSim wait3) {
        this.wait3 = wait3;
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
    public void setNext(ThreadModel next) {
        this.next = next;
    }

}
