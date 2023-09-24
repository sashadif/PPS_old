package PetriObj;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import graphpresentation.PetriNetsPanel;

import java.io.Serializable;
import java.util.*;

import javax.swing.JSlider;
import javax.swing.JTextArea;

/**
 * This class is Petri simulator. <br>
 * The object of this class recreates dynamics of functioning according to Petri
 * net, given in his data field. Such object is named Petri-object.
 *
 * @author Стеценко Інна
 */
public class PetriSim implements Serializable {
	
    private static double timeCurr=0;
    private static double timeMod = Double.MAX_VALUE - 1;
    
    
    private String name;
    private int numObj; //поточний номер створюваного об"єкта   
    private static int next = 1; //лічильник створених об"єктів  
    private int priority;
    private double timeMin;
 
    private int numP;
    private int numT;
    private int numIn;
    private int numOut;
    private PetriP[] listP = new PetriP[numP];
    private PetriT[] listT = new PetriT[numT];
    private ArcIn[] listIn = new ArcIn[numIn];
    private ArcOut[] listOut = new ArcOut[numOut];
    private PetriT eventMin;
    private PetriNet net;
    private ArrayList<PetriP> listPositionsForStatistica = new ArrayList<PetriP>();
    //..... з таким списком статистика спільних позицій працює правильно...
    
    private JSlider delaySlider = null;

    /**
     * Constructs the Petri simulator with given Petri net and time modeling
     *
     * @param pNet Petri net that describes the dynamics of object
     */    
    public PetriSim(PetriNet net, JSlider delaySlider) {
        this.delaySlider = delaySlider;
    	this.net = net;
        name = net.getName();
        numObj = next; 
        next++;        
        timeMin = Double.MAX_VALUE;
      
        listP = net.getListP();
        listT = net.getListT();
        listIn = net.getArcIn();
        listOut = net.getArcOut();
        numP = listP.length;
        numT = listT.length;
        numIn = listIn.length;
        numOut = listOut.length;
        eventMin = this.getEventMin();
        priority = 0;
        listPositionsForStatistica.addAll(Arrays.asList(listP));

    }
    
    /**
     * Constructs the Petri simulator with given Petri net and time modeling
     *
     * @param net Petri net that describes the dynamics of object
     */ 
    public PetriSim(PetriNet net) {
        this.delaySlider = null;
    	this.net = net;
        name = net.getName();
        numObj = next; 
        next++;        
        timeMin = Double.MAX_VALUE;
      
        listP = net.getListP();
        listT = net.getListT();
        listIn = net.getArcIn();
        listOut = net.getArcOut();
        numP = listP.length;
        numT = listT.length;
        numIn = listIn.length;
        numOut = listOut.length;
        eventMin = this.getEventMin();
        priority = 0;
        listPositionsForStatistica.addAll(Arrays.asList(listP));

    }

    /**
     *
     * @return PetriNet
     */
    public PetriNet getNet() {
        return net;
    }

    /**
     *
     * @return name of Petri-object
     */
    public String getName() {
        return name;
    }
    
    

    /**
     *
     * @return list of places for statistics which use for statistics
     */
    public ArrayList<PetriP> getListPositionsForStatistica() {
        return listPositionsForStatistica;
    }

    /**
     * Get priority of Petri-object
     *
     * @return value of priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     *
     * @return the number of object
     */
    public int getNumObj() {
        return numObj;
    }

    /**
     * Set priority of Petri-object
     *
     * @param a value of priority
     */
    public void setPriority(int a) {
        priority = a;
    }

    /**
     * Set current time of Petri-object in value, given a parameter a
     *
     * @param a a value of current time
     */
  

    /**
     * This method uses for describing other actions associated with transition
     * markers output.<br>
     * Such as the output markers into the other Petri-object.<br>
     * The method is overridden in subclass.
     */
    public void doT() {

    }

    /**
     * Determines the next event and its moment.
     */
    public void eventMin() {
        PetriT event = null; //пошук часу найближчої події
        // якщо усі переходи порожні, то це означає зупинку імітації, 
        // отже за null значенням eventMin можна відслідковувати зупинку імітації
        double min = Double.MAX_VALUE;
        for (PetriT transition : listT) {
            if (transition.getMinTime() < min) {
                event = transition;
                min = transition.getMinTime();
            }
        }
        timeMin = min;
        eventMin = event;
    }

    /**
     *
     * @return moment of next event
     */
    public double getTimeMin() {
        return timeMin;
    }

    /**
     * Finds the set of transitions for which the firing condition is true and
     * sorts it on priority value
     *
     * @return the sorted list of transitions with the true firing condition
     */
    public ArrayList<PetriT> findActiveT() {
        ArrayList<PetriT> aT = new ArrayList<>();

        for (PetriT transition : listT) {
            if ((transition.condition(listP) == true) && (transition.getProbability() != 0)) {
                aT.add(transition);

            }
        }

         if (aT.size() > 1) {
            aT.sort(new Comparator<PetriT>() { // сортування переходів за спаданням пріоритету
                @Override
                public int compare(PetriT o1, PetriT o2) {
                    if (o1.getPriority() < o2.getPriority()) {
                        return 1;
                    } else if (o1.getPriority() == o2.getPriority()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
        return aT;
    }

    /**
     * It does one step of simulation: transitions input markers, then finding next event moment, and then transitions input markers
     */
    public void step() { //один крок ,використовується для одного об'єкту мережа Петрі

        System.out.println("Next Step  " + "time=" + getTimeCurr());

        this.printMark();//друкувати поточне маркування
        ArrayList<PetriT> activeT = this.findActiveT();     //формування списку активних переходів

        if ((activeT.isEmpty() && isBufferEmpty() == true) || getTimeCurr() >= getTimeMod()) { //зупинка імітації за умови, що
                                                                                            //немає переходів, які запускаються,
          // і немає маркерів у переходах, або вичерпаний час моделювання
            System.out.println("STOP in Net  " + this.getName());
            timeMin = getTimeMod();
            for (PetriP p : listP) {
                p.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : listT) {
                transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(timeMin);         //просування часу
        } else {
            while (activeT.size() > 0) { //вхід маркерів в переходи доки можливо

                this.doConflikt(activeT).actIn(listP, getTimeCurr()); //розв'язання конфліктів
                doAfterStep();
                activeT = this.findActiveT(); //оновлення списку активних переходів

            }

            this.eventMin();//знайти найближчу подію та ії час
            
            for (PetriP position : listP) {
                position.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : listT) {
                transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(timeMin);         //просування часу
            output();

        }
    }

    public void step(JTextArea area) //один крок,використовується для одного об'єкту мережа Петрі(наприклад, покрокова імітація мережі Петрі в графічному редакторі)
    {
        area.append("\n Next event, current time = " + getTimeCurr());

        this.printMark();//друкувати поточне маркування
        ArrayList<PetriT> activeT =  this.findActiveT();     //формування списку активних переходів
        for (PetriT T : activeT) {
            area.append("\nList of transitions with a fulfilled activation condition " + T.getName());
        }
        if ((activeT.isEmpty() && isBufferEmpty() == true) || getTimeCurr() >= getTimeMod()) { //зупинка імітації за умови, що
            //не має переходів, які запускаються,
          //  stop = true;                              // і не має фішок в переходах або вичерпаний час моделювання
            area.append("\n STOP, there are no active transitions / transitions with a fulfilled activation condition " + this.getName());
            timeMin = getTimeMod();
            for (PetriP position : listP) {
                position.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : listT) {
                transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(timeMin);         //просування часу
        } else {

            while (activeT.size() > 0) {      //вхід маркерів в переходи доки можливо

                area.append("\n Choosing a transition to activate " + this.doConflikt(activeT).getName());
                this.doConflikt(activeT).actIn(listP, getTimeCurr()); //розв'язання конфліктів
                doAfterStep();
                activeT = this.findActiveT(); //оновлення списку активних переходів
            }
            area.append("\n Markers enter transitions:");
            this.printMark(area);//друкувати поточне маркування

            this.eventMin();//знайти найближчу подію та ії час
            for (PetriP position : listP) {
                position.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            for (PetriT transition : listT) {
                transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
            }

            setTimeCurr(timeMin);         //просування часу

            if (getTimeCurr() <= getTimeMod()) {

                area.append("\n current time =" + getTimeCurr() + "   " + eventMin.getName());
                //Вихід маркерів
                eventMin.actOut(listP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                doAfterStep();
                area.append("\n Markers leave a transition " + eventMin.getName());
                this.printMark(area);//друкувати поточне маркування

                if (eventMin.getBuffer() > 0) {

                    boolean u = true;
                    while (u == true) {
                        eventMin.minEvent();
                        if (eventMin.getMinTime() == getTimeCurr()) {
                            // System.out.println("MinTime="+TEvent.getMinTime());
                            eventMin.actOut(listP);
                            doAfterStep();
                            // this.printMark();//друкувати поточне маркування
                        } else {
                            u = false;
                        }
                    }
                    area.append("\n Markers leave a transition buffer " + eventMin.getName());
                    this.printMark(area);//друкувати поточне маркування
                }
                //Додано 6.08.2011!!!
                for (PetriT transition : listT) { //ВАЖЛИВО!!Вихід з усіх переходів, що час виходу маркерів == поточний момент час.
                    if (transition.getBuffer() > 0 && transition.getMinTime() == getTimeCurr()) {
                    	transition.actOut(listP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                    	doAfterStep();
                    	area.append("\n Markers leave a transition " + transition.getName());
                        this.printMark(area);//друкувати поточне маркування
                        if (transition.getBuffer() > 0) {
                            boolean u = true;
                            while (u == true) {
                                transition.minEvent();
                                if (transition.getMinTime() == getTimeCurr()) {
                                    // System.out.println("MinTime="+TEvent.getMinTime());
                                	transition.actOut(listP);
                                	doAfterStep();
                                    // this.printMark();//друкувати поточне маркування
                                } else {
                                    u = false;
                                }
                            }
                            area.append("\n Markers leave a transition buffer " + transition.getName());
                            this.printMark(area);//друкувати поточне маркування
                        }
                    }
                }
            }
        }
     
    }

    /**
     * It does the transitions input markers
     */
    public void input() {//вхід маркерів в переходи Петрі-об'єкта

        ArrayList<PetriT> activeT = this.findActiveT();     //формування списку активних переходів

        if (activeT.isEmpty() && isBufferEmpty() == true) { //зупинка імітації за умови, що
            //не має переходів, які запускаються,
            timeMin = Double.MAX_VALUE;
            // eventMin = null;
        } else {
            while (activeT.size() > 0) { //запуск переходів доки можливо
            	this.doConflikt(activeT).actIn(listP, getTimeCurr()); //розв'язання конфліктів
            	doAfterStep();
				activeT = this.findActiveT(); // оновлення списку активних переходів
			}

            this.eventMin();//знайти найближчу подію та ії час
        }
    }
    
    /**
     * It does the transitions output markers
     */
   
    public void output() {
        if (getTimeCurr() <= getTimeMod()) {
        	eventMin.actOut(listP);//здійснення події
        	doAfterStep();
            if (eventMin.getBuffer() > 0) {
                boolean u = true;
                while (u == true) {
                    eventMin.minEvent();
                    if (eventMin.getMinTime() == getTimeCurr()) {
                    	eventMin.actOut(listP);
                    	doAfterStep();
                    } else {
                        u = false;
                    }
                }
            }
            for (PetriT transition : listT) { //ВАЖЛИВО!!Вихід з усіх переходів, що час виходу маркерів == поточний момент час.

                if (transition.getBuffer() > 0 && transition.getMinTime() == getTimeCurr()) {
                	transition.actOut(listP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                	doAfterStep();
                	if (transition.getBuffer() > 0) {
                        boolean u = true;
                        while (u == true) {
                            transition.minEvent();
                            if (transition.getMinTime() == getTimeCurr()) {
                                transition.actOut(listP);
                                doAfterStep();
                            } else {
                                u = false;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * It does one event in current moment: the transitions output and input  markers
     */
    public void stepEvent() {  //один крок,вихід та вхід маркерів в переходи Петрі-об"єкта, використовується для множини Петрі-об'єктів
        if (isStop()) {
            timeMin = Double.MAX_VALUE;
       
            return; //зупинка імітації
        }
//        System.out.println("before output ");
//        this.printMark();
//        this.printBuffer();
        output();
//        System.out.println("after output ");
//        this.printMark();
//          this.printBuffer();
        input();   
//       System.out.println("after input ");
//        this.printMark();
//        this.printBuffer();
        
    }
    
    public void printState(String info){
        System.out.println(info); 
        this.printMark();
          this.printBuffer();
    }

    /**
     * Calculates mean value of quantity of markers in places and quantity of
     * active channels of transitions
     */
    public void doStatistica() {
        for (PetriP position : listP) {
            position.changeMean((timeMin - getTimeCurr()) / getTimeMod());
        }
        for (PetriT transition : listT) {
            transition.changeMean((timeMin - getTimeCurr()) / getTimeMod());
        }

    }

    /**
     *
     * @param dt - the time interval
     */
    public void doStatistica(double dt) {
        if (dt > 0) {
            for (PetriP position : listPositionsForStatistica) {
                position.changeMean(dt);
            }
        }
        if (dt > 0) {
            for (PetriT transition : listT) {
                transition.changeMean(dt);
            }
        }
    }

    /**
     * This method use for simulating Petri-object
     */
    public void go() {
        setTimeCurr(0);

        while (getTimeCurr() <= getTimeMod() && isStop() == false) {
            PetriSim.this.step();
            if (isStop() == true) {
                System.out.println("STOP in net  " + this.getName());
            }
            this.printMark();//друкувати поточне маркування
        }
    }

    /**
     * This method use for simulating Petri-object until current time less then
     * the momemt in parametr time
     *
     * @param time - the simulation time
     */
    public void go(double time) {

        while (getTimeCurr() < time && isStop() == false) {
            step();
            if (isStop() == true) {
                System.out.println("STOP in net  " + this.getName());
            }
            // this.printMark();//друкувати поточне маркування
        }
    }

    public void go(double time, JTextArea area) {

        while (getTimeCurr() < time && isStop() == false) {
            step(area);
            if (isStop() == true) {
                area.append("STOP in net  " + this.getName());
            }
            // this.printMark();//друкувати поточне маркування
        }
    }

    /**
     * Determines is all of transitions has empty buffer
     *
     * @return true if buffer is empty for all transitions of Petri net
     */
    public boolean isBufferEmpty() {
        boolean c = true;
        for (PetriT e : listT) {
            if (e.getBuffer() > 0) {
                c = false;
                break;
            }
        }
        return c;
    }

    /**
     * Do printing the current marking of Petri net
     */
    public void printMark() {
        System.out.print("Mark in Net  " + this.getName() + "   ");
        for (PetriP position : listP) {
            System.out.print(position.getMark() + "  ");
        }
        System.out.println();
    }
    public void printBuffer(){
    System.out.print("Buffer in Net  " + this.getName() + "   ");
        for (PetriT transition : listT) {
            System.out.print(transition.getBuffer() + "  ");
        }
        System.out.println();
    }
    public void printMark(JTextArea area) {
        area.append("\n Mark in Net  " + this.getName() + "   \n");
        for (PetriP position : listP) {
            area.append(position.getMark() + "  ");
        }
        area.append("\n");
    }

    /**
     *
     * @return time modeling
     */
  /*  public static double getTimeMod() {
        return timeMod;
    }
*/
    /**
     * @param aTimeMod the timeMod to set
     */
/*    public static void setTimeMod(double aTimeMod) {
        timeMod = aTimeMod;
    }
*/
    /**
     *
     * @return current time
     */
 /*   public double getTimeCurr() {
        return timeCurr;
    }
*/
   
    /**
     *
     * @return the nearest event
     */
    public final PetriT getEventMin() {
        this.eventMin();
        return eventMin;
    }

    /**
     * This method solves conflict between transitions given in parametr TT
     *
     * @param TT the list of transitions
     * @return the transition - winner of conflict
     * 
     */
    public PetriT doConflikt(ArrayList<PetriT> TT) {//
        PetriT aT = TT.get(0);
        if (TT.size() > 1) {
            aT = TT.get(0);
            double checkSum=0.0;
            int i = 0;
            while (i < TT.size() && TT.get(i).getPriority() == aT.getPriority()) {
                checkSum+=TT.get(i).getProbability();
                //System.out.println("--------------->>>>"+checkSum);
                i++;
               
            }
            if((checkSum!=1.0) && (checkSum!=i) )  // added 21.02.2021 it should be Exception!!
                System.out.println("Check parameters of transition: the sum of probability of conflict transitions is "+checkSum);
            if (i == 1)
             ; else {
                double r = Math.random();
                int j = 0;
                double sum = 0;
                double prob;
                while (j < TT.size() && TT.get(j).getPriority() == aT.getPriority()) {

                    if (TT.get(j).getProbability() == 1.0) {
                        prob = 1.0 / i;  // якщо рівноймовірно
                    } else {
                        prob = TT.get(j).getProbability(); //сума ймовірностей має бути = 1.0
                    }

                    sum = sum + prob;
                    if (r < sum) {
                        aT = TT.get(j);
                        break;
                    } //вибір переходу за значенням ймовірності
                    else {
                        j++;
                    }
                }
            }
        }

        return aT;

    }


    /**
     * @return the stop
     */
    public boolean isStop() {
        
        this.eventMin();
         
        return (eventMin==null);
    }
    
      /**
     * @return the timeCurr
     */
    public static double getTimeCurr() {
        return timeCurr;
    }

    /**
     * @param aTimeCurr the timeCurr to set
     */
    public static void setTimeCurr(double aTimeCurr) {
        timeCurr = aTimeCurr;
    }

    /**
     * @return the timeMod
     */
    public static double getTimeMod() {
        return timeMod;
    }

    /**
     * @param aTimeMod the timeMod to set
     */
    public static void setTimeMod(double aTimeMod) {
        timeMod = aTimeMod;
    }
 
    private void doAfterStep(){
    	try {
			if(delaySlider != null)Thread.sleep(delaySlider.getValue());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static Comparator<PetriSim> getComparatorByPriority() { //added by Inna 12.10.2017
        return new Comparator<PetriSim>() {
            @Override
            public int compare(PetriSim o1, PetriSim o2) {
                if (o1.getPriority() < o2.getPriority()) {
                    return 1;
                } else if (o1.getPriority() == o2.getPriority()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        };
    }
    
      void stepEvent(PetriNetsPanel panel) {
        if (isStop()) {
            timeMin = getTimeMod() + 1;
            // timeCurr = timeMin;        //просування часу
            return; //зупинка імітації
        }
        output(panel);
        input(panel);
    }

    public void output(PetriNetsPanel panel) {
        if (getTimeCurr() <= getTimeMod()) {
            panel.animateT(eventMin);
            panel.animateOut(eventMin);
            eventMin.actOut(listP);//здійснення події
            panel.animateP(eventMin.getOutP());
            doAfterStep();
            if (eventMin.getBuffer() > 0) {
                boolean u = true;
                while (u == true) {
                    eventMin.minEvent();
                    if (eventMin.getMinTime() == getTimeCurr()) {
                        panel.animateT(eventMin);
                        panel.animateOut(eventMin);
                        eventMin.actOut(listP);
                        panel.animateP(eventMin.getOutP());
                        
                    } else {
                        u = false;
                    }
                }
            }
            for (PetriT transition : listT) { //ВАЖЛИВО!!Вихід з усіх переходів, що час виходу маркерів == поточний момент час.

                if (transition.getBuffer() > 0 && transition.getMinTime() == getTimeCurr()) {
                    transition.actOut(listP);//Вихід маркерів з переходу, що відповідає найближчому моменту часу
                    if (transition.getBuffer() > 0) {
                        boolean u = true;
                        while (u == true) {
                            transition.minEvent();
                            if (transition.getMinTime() == getTimeCurr()) {
                                transition.actOut(listP);
                            } else {
                                u = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public void input(PetriNetsPanel panel) {//вхід маркерів в переходи Петрі-об'єкта

        ArrayList<PetriT> activeT = this.findActiveT();     //формування списку активних переходів

        if (activeT.isEmpty() && isBufferEmpty() == true) { //зупинка імітації за умови, що
            //не має переходів, які запускаються,
            timeMin = Double.MAX_VALUE;
            // eventMin = null;
        } else {
            while (activeT.size() > 0) { //запуск переходів доки можливо
                PetriT tr = this.doConflikt(activeT);
                panel.animateP(tr.getInP());
                panel.animateIn(tr);
                tr.actIn(listP, getTimeCurr()); //розв'язання конфліктів
                panel.animateT(tr);
                doAfterStep();
                activeT = this.findActiveT(); //оновлення списку активних переходів
            }

            this.eventMin();//знайти найближчу подію та ії час
        }
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
   
   

}
