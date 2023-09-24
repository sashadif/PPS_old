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
public class WaitSim extends PetriSim {

    private Control control;
    private int id; // 0 - wait, 1 - wait1, 2 - wait2, 3 - wait3
    private ThreadModel next;

    public WaitSim(PetriNet net) {
        super(net);
    }

    public WaitSim(int id) throws ExceptionInvalidNetStructure {
        super(NetLibrary.CreateNetWait());
        this.id = id;

    }
    
     public WaitSim(int cores, int id) throws ExceptionInvalidNetStructure {
        super(NetLibrary.CreateNetWait(cores));
        this.id = id;

    }

    public void printState() {
        System.out.println(this.getName() + ":  "
                + this.getNet().getListP()[1].getName() + " = " + this.getNet().getListP()[1].getMark() + " , "
                + this.getNet().getListP()[3].getName() + " =" + this.getNet().getListP()[3].getMark() + " , "
                + this.getNet().getListP()[6].getName() + " =" + this.getNet().getListP()[6].getMark() + " , "
                + this.getNet().getListP()[7].getName() + " = " + this.getNet().getListP()[7].getMark());
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

    @Override
    public void doT() {
        // the condition control
       
        if (id == 0) {
            if (this.getEventMin().getName().equalsIgnoreCase("lock")
                    || this.getEventMin().getName().equalsIgnoreCase("getSignal")) {

                getCond().setMark(control.bufferNotEmpty());
            }
            if (this.getEventMin().getName().equalsIgnoreCase("check")) {
//                System.out.println("wwwwwwwwwwwwwwwwwww_notempty_wwwwwwwwwwwwwwwwwwwwwwwwwww");
                control.setTlim(control.getTfirst());

            }
        }

        if (id == 3) {
            if (this.getEventMin().getName().equalsIgnoreCase("lock")
                    || this.getEventMin().getName().equalsIgnoreCase("getSignal")) {
                if (next != null) // начебто непотрібно перевіряти та інколи виникає помилка
                {
                    getCond().setMark(next.getControl().bufferNotMax());  // 13.02.2021 для next перевіряти, було для this
                }//                System.out.println(this.getName()+"  set cond buffer not max = "+ next.getControl().bufferNotMax()
//                                        +", buffer = "+ next.getControl().getBuffersize());

            }
//             if (this.getEventMin().getName().equalsIgnoreCase("check")) {
//                System.out.println("wwwwwwwwwwwwwwwwwww_notmax_wwwwwwwwwwwwwwwwwwwwwwwwwww");
//                
//
//            }

        }

    }

    public PetriP getCond() {
        return this.getNet().getListP()[3];
    }

    public PetriP getSignal() {
        return this.getNet().getListP()[7];
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
