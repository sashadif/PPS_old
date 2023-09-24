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
 * @author dyfuchyna
 */
public class RunSim extends PetriSim {

    private Control control;

    public RunSim() throws ExceptionInvalidNetStructure {
        super(NetLibrary.CreateNetRun()); // prev == null
    }
    public RunSim(PetriNet net) {
        super(net);
    }

    public RunSim(int cores) throws ExceptionInvalidNetStructure {
        super(NetLibrary.CreateNetRun(cores)); // prev == null
    }

    @Override
    public void doT() {
        if (this.getEventMin().getName().equalsIgnoreCase("start")) {
            control.setTlim(control.getTmod());
            getCondWhile().setMark(control.tlocLessTmod());
        }

        if (this.getEventMin().getName().equalsIgnoreCase("goUntilFin")) {
            getCondWhile().setMark(control.tlocLessTmod());
//            getCondGountil().setMark(control.tlocLessLim());
//            this.getCondRemove().setMark(control.tlocEQLimAndPrev());
        }
        if (this.getEventMin().getName().equalsIgnoreCase("notWait")) {
            control.setTlim(control.getTmod());
//            getCondGountil().setMark(control.tlocLessLim());
//             this.getCondRemove().setMark(control.tlocEQLimAndPrev());
            
        }
//        if (this.getEventMin().getName().equalsIgnoreCase("goUntilFin")) {
//            this.getCondGountil().setMark(control.tlocLessLim());
//        }
        if (this.getEventMin().getName().equalsIgnoreCase("updateCond")) {
            this.getCondGountil().setMark(control.tlocLessLim());
            this.getCondRemove().setMark(control.tlocEQLimAndPrev());
            
        }
        if (this.getEventMin().getName().equalsIgnoreCase("return")) {
            System.out.println(this.getName() + " return to finish.... "
                    + this.getControl().getTloc() + ">" + this.getControl().getTlim()
                    );
        }
        
         if (this.getEventMin().getName().equalsIgnoreCase("remove")) {         
            this.getControl().removeExtinput();
//            System.out.println("--------- remove in run  was performed ----------");
        }

    }

    public void printState() {
        System.out.println(this.getName() + ":  "
                + this.getNet().getListP()[3].getName() + " = " + this.getNet().getListP()[3].getMark() + " , "
                + this.getNet().getListP()[9].getName() + " =" + this.getNet().getListP()[9].getMark() + " , "
                + this.getNet().getListP()[13].getName() + " = " + this.getNet().getListP()[13].getMark());
        System.out.println(this.getName() + "_control:  tloc = "
                + this.getControl().getTloc() + " , tlim =" + this.getControl().getTlim() + " , tmin = "
                + this.getControl().getTmin() + " , "
        );
    }

    public void setPrev(boolean isPrev) {
        if (isPrev) {
            this.getNet().getListP()[8].setMark(1);

        } else {
            this.getNet().getListP()[8].setMark(0);
        }
    }

    public PetriP getCondGountil() {
        return this.getNet().getListP()[9];
    }

    public PetriP getCondWhile() {
        return this.getNet().getListP()[3];
    }

    public PetriP getRunStart() {
        return this.getNet().getListP()[0];
    }

    public PetriP getRunFinish() {
        return this.getNet().getListP()[7];
    }

    public PetriP getWaitStart() {
        return this.getNet().getListP()[5];
    }

    public PetriP getWaitFinish() {
        return this.getNet().getListP()[6];
    }

    public PetriP getGoUntilStart() {
        return this.getNet().getListP()[10];
    }

    public PetriP getGoUntilFinish() {
        return this.getNet().getListP()[11];
    }
    
     public PetriP getCondRemove() {
        return this.getNet().getListP()[13];
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

}
