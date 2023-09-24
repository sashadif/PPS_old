/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParallelAlgorithmModel;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author dyfuchyna
 */
public class Control {

    /**
     * @return the prev
     */
    public boolean isPrev() {
        return prev;
    }

    /**
     * @param prev the prev to set
     */
    public void setPrev(boolean prev) {
        this.prev = prev;
    }

    /**
     * @return the next
     */
    public boolean isNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(boolean next) {
        this.next = next;
    }

    private double tloc;
    private double tmod;
    private double tlim;
    private double tmin;
    private int maxsize;
    private boolean prev;
    private boolean next;
    private String name;
    private ArrayList<Double> bufferList;
    private Random random;
    private boolean printable;

    public Control(double timeMod, int maxsize) {
        tmod = timeMod;
        tloc = 0.0;
        tlim = tmod;
        tmin = Double.MAX_VALUE;
        this.maxsize = maxsize;
        prev = false;
        next = false;
        bufferList = new ArrayList<>();
        random = new Random();
        printable = false;

    }

    public void addExtinput(double t) { // 14.02.2021          
        getBufferList().add(t);
        if (isPrintable()) {
            System.out.println("buffer add -->> ");
            printState();
        }
    }

    public void removeExtinput() {
        if (isPrintable()) {
            System.out.println("buffer remove -->> ");
        }
        double t = this.getTfirst();
        if (!bufferList.isEmpty()) {
            // getBufferList().remove(0);
            getBufferList().removeIf(x -> (x == t));  //вилучаємо усі що дорівнюють першому
        } else {
            System.out.println("error in remove call ");
        }
        if (isPrintable()) {
            printState();
        }
    }

    public void moveTmin() {
        tmin = tloc + 0.5 + 1.0 * Math.random(); // в середньому 1 (0.5 ; 1.5) ???виходить, що потік буде виконувати роботу навіть якщо зовні нічого не приходить
        // таке може бути та не у випадку смо

    }
    
     public void moveTmin(int numEvents) {
        tmin = tloc + 1.0/(numEvents) + 0.5/numEvents*(-1.0+2.0*random.nextDouble()); // в середньому 1.0/(numEvents) (0.5 ; 1.5) ??виходить, що потік буде виконувати роботу навіть якщо зовні нічого не приходить
//     System.out.println(tmin+", "+(1.0/(numEvents) + 0.5/numEvents*(-1.0+2.0*random.nextDouble())));


    }

    public int noNext() {
        return (!isNext()) ? 1 : 0;
    }

    public int tlocLessTmod() {
        return (tloc < tmod) ? 1 : 0;
    }

    public int tlocLessLim() {
        return (tloc < getTlim()) ? 1 : 0;
    }

    public int tlocEQLimAndPrev() {
//         System.out.println("tloc==Lim and prev not null"+((tloc == getTlim())&&isPrev()));
        return ((tloc == getTlim()) && isPrev()) ? 1 : 0;
    }

    public int tlimBiggerTmod() {
        return (tlim >= getTmod()) ? 1 : 0;
    }

    public int tMinLessLim() {
        return (tmin < getTlim()) ? 1 : 0;
    }

    public int bufferNotEmpty() {
        if (isPrev()) {
            return (getBuffersize() > 0) ? 1 : 0;
        } else {
            return 0;
        }
    }


    public int bufferNotMax() {

        return (getBuffersize() < maxsize) ? 1 : 0;
    }

    public int bufferMax() {

        return (getBuffersize() >= maxsize) ? 1 : 0;
    }

    /**
     * @return the tloc
     */
    public double getTloc() {
        return tloc;
    }

    /**
     * @param tloc the tloc to set
     */
    public void setTloc(double tloc) {
        this.tloc = tloc;
        if (isPrintable()) {
            System.out.println("set tloc --->> ");

            printState();
        }
    }

    /**
     * @return the tMod
     */
    public double getTmod() {
        return tmod;
    }

    /**
     * @param tMod the tMod to set
     */
    public void settMod(double tMod) {
        this.tmod = tMod;
        if (isPrintable()) {
            System.out.println("set tmod --->> ");
            printState();
        }
    }

    /**
     * @return the tMin
     */
    public double getTmin() {
        return tmin;
    }

    /**
     * @return the buffersize
     */
    public int getBuffersize() {
        if (bufferList.isEmpty()) {
            return 0;
        } else {
            return getBufferList().size();
        }
    }

    /**
     * @return the maximum size of buffer of external events
     */
    public int getMaxsize() {
        return maxsize;
    }

    /**
     * @param maxsize the maximum size of buffer of external events
     */
    public void setMaxsize(int maxsize) {
        this.maxsize = maxsize;
    }

    /**
     * @return the tfirst
     */
    public double getTfirst() {
        //return tfirst;
        if (!bufferList.isEmpty()) {
            return getBufferList().get(0);
        } else {
            return -1;
        }
    }

    /**
     * @return the tlim
     */
    public double getTlim() {
        return tlim;
    }

    /**
     * @param limit the limit to set
     */
    public void setTlim(double limit) {

        this.tlim = limit;

        if (tlim >= tmod) {
            tlim = tmod;
        }
//        System.out.println("set tlim --->> ");
//        printState();
    }

    public void printState() {

        System.out.println(name + ":   tloc " + tloc + ", tlim = " + tlim + ", tmin = " + tmin);
        if (!bufferList.isEmpty()) {
            System.out.println("\t\t\tbuffer size = " + getBuffersize() + ", tfirst = " + this.getTfirst() + ", tlast = " + this.getBufferList().get(this.getBuffersize() - 1));
        } else {
            System.out.println("\t\t\tbuffer size = " + getBuffersize());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the bufferList
     */
    public ArrayList<Double> getBufferList() {
        return bufferList;
    }

    /**
     * @param bufferList the bufferList to set
     */
    public void setBufferList(ArrayList<Double> bufferList) {
        this.bufferList = bufferList;
    }

    /**
     * @return the printable
     */
    public boolean isPrintable() {
        return printable;
    }

    /**
     * @param printable the printable to set
     */
    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

}
