package PetriObj;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * This class for creating the transition of Petri net
 *
 * @author Стеценко Інна
 */
public class PetriT extends PetriMainElement implements Cloneable, Serializable { // inheritance added by Katya 20.11.2016

    private static double timeModeling = Double.MAX_VALUE - 1;

    /**
     * @return the timeModeling
     */
    public static double getTimeModeling() {
        return timeModeling;
    }

    /**
     * @param aTimeModeling the timeModeling to set
     */
    public static void setTimeModeling(double aTimeModeling) {
        timeModeling = aTimeModeling;
    }

    private String name;

    private int buffer;
    private int priority;
    private double probability;

    private double minTime;
    private double timeServ;
    private double parametr; //середнє значення часу обслуговування
    private double paramDeviation; //середнє квадратичне відхилення часу обслуговування
    private String distribution;
    private ArrayList<Double> timeOut = new ArrayList<Double>();
    private ArrayList<Integer> inP = new ArrayList<Integer>();
    private ArrayList<Integer> inPwithInf = new ArrayList<Integer>();
    private ArrayList<Integer> quantIn = new ArrayList<Integer>();
    private ArrayList<Integer> quantInwithInf = new ArrayList<Integer>();
    private ArrayList<Integer> outP = new ArrayList<Integer>();
    private ArrayList<Integer> quantOut = new ArrayList<Integer>();

    private int num;  // номер каналу багатоканального переходу, що відповідає найближчий події
    private int number; // номер переходу за списком
    private double mean;  // спостережуване середнє значення кількості активних каналів переходу
    private int observedMax;
    private int observedMin;
    private static int next = 0; //додано 1.10.2012
    
    // whether parametr, distribution, priority & probability are parameters; added by Katya 08.12.2016
    private boolean parametrIsParam = false;
    private boolean distributionIsParam = false;
    private boolean priorityIsParam = false;
    private boolean probabilityIsParam = false;
    // param names
    private String parametrParamName = null;
    private String distributionParamName = null;
    private String priorityParamName = null;
    private String probabilityParamName = null;
    
    /**
     *
     * @param n name of transition
     * @param tS timed delay
     */
    public PetriT(String n, double tS) {
        name = n;
        parametr = tS;
        paramDeviation = 0;
        timeServ = parametr;
        buffer = 0;

        minTime = Double.MAX_VALUE; // не очікується вихід маркерів переходу
        num = 0;
        mean = 0;
        observedMax = buffer;
        observedMin = buffer;
        priority = 0;
        probability = 1.0;
        distribution = null;
        number = next;
        next++;
        timeOut.add(Double.MAX_VALUE); // не очікується вихід маркерів з каналів переходу
        this.minEvent();
    }

    /**
     *
     * @param n name of transition
     * @param tS timed delay
     * @param prior value of priority
     */
    public PetriT(String n, double tS, int prior) {
        name = n;
        parametr = tS;
        paramDeviation = 0;
        timeServ = parametr;
        buffer = 0;
        
        minTime = Double.MAX_VALUE;
        num = 0;
        mean = 0;
        observedMax = buffer;
        observedMin = buffer;
        priority = prior;
        probability = 1;
        distribution = null;
        number = next;
        next++;
        timeOut.add(Double.MAX_VALUE);
        this.minEvent();
    }

    /**
     *
     * @param n name of transition
     * @param tS timed delay
     * @param prob value of probability
     */
    public PetriT(String n, double tS, double prob) {
        name = n;
        parametr = tS;
        paramDeviation = 0;
        timeServ = parametr;
        buffer = 0;
        
        minTime = Double.MAX_VALUE;
        num = 0;
        mean = 0;
        observedMax = buffer;
        observedMin = buffer;
        probability = prob;
        priority = 0;
        distribution = null;
        number = next;
        next++;
        timeOut.add(Double.MAX_VALUE);
        this.minEvent();
    }

    /**
     *
     * @param n name of transition
     */
    public PetriT(String n) {
        name = n;
        parametr = 0.0; //за замовчуванням нульова затримка!!!
        timeServ = parametr;
        buffer = 0;
        
        minTime = Double.MAX_VALUE;
        num = 0;
        mean = 0;
        observedMax = buffer;
        observedMin = buffer;

        priority = 0;
        probability = 1;
        distribution = null;
        number = next;
        next++;
        timeOut.add(Double.MAX_VALUE);
        this.minEvent();
    }

    public boolean parametrIsParam() {
        return parametrIsParam;
    }
    
    public boolean distributionIsParam() {
        return distributionIsParam;
    }
    
    public boolean priorityIsParam() {
        return priorityIsParam;
    }
    
    public boolean probabilityIsParam() {
        return probabilityIsParam;
    }
    
    public String getParametrParamName() {
        return parametrParamName;
    }
    
    public String getDistributionParamName() {
        return distributionParamName;
    }
    
    public String getPriorityParamName() {
        return priorityParamName;
    }
    
    public String getProbabilityParamName() {
        return probabilityParamName;
    }
    
    public void setParametrParam(String paramName) {
        if (paramName == null) {
            parametrIsParam = false;
            parametrParamName = null;
        } else {
            parametrIsParam = true;
            parametrParamName = paramName;
            parametr = 0.0;
        }
    }
    
    public void setDistributionParam(String paramName) {
        if (paramName == null) {
            distributionIsParam = false;
            distributionParamName = null;
        } else {
            distributionIsParam = true;
            distributionParamName = paramName;
            distribution = null;
        }
    }
    
    public void setPriorityParam(String paramName) {
        if (paramName == null) {
            priorityIsParam = false;
            priorityParamName = null;
        } else {
            priorityIsParam = true;
            priorityParamName = paramName;
            priority = 0;
        }
    }
    
    public void setProbabilityParam(String paramName) {
        if (paramName == null) {
            probabilityIsParam = false;
            probabilityParamName = null;
        } else {
            probabilityIsParam = true;
            probabilityParamName = paramName;
            probability = 1;
        }
    }
    
    /**
     * Set the counter of transitions to zero.
     */
    public static void initNext() { //ініціалізація лічильника нульовим значенням
         next = 0;
    }

    /**
     * Recalculates the mean value
     *
     * @param a value for recalculate of mean value (value equals product of
     * buffer and time divided by time modeling)
     */
    public void changeMean(double a) {//if(buffer>0)
        // mean=mean+buffer*a;
        mean = mean + (buffer - mean) * a;
    }

    /**
     *
     * @return mean value of quantity of markers
     */
    public double getMean() {
        return mean;
    }

    public double getObservedMax() {
        return observedMax;
    }

    public double getObservedMin() {
        return observedMin;
    }

    /**
     *
     * @return the value of priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set the new value of priority
     *
     * @param r - the new value of priority
     */
    public void setPriority(int r) {
        priority = r;
    }

    /**
     *
     * @return the value of priority
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Set the new value of probability
     *
     * @param v the value of probability
     */
    public void setProbability(double v) {
        probability = v;
    }

    /**
     *
     * @return the numbers of planed moments of markers outputs
     */
    public int getBuffer() {
        return buffer;
    }

    /**
     * This method sets the distribution of service time
     *
     * @param s the name of distribution as "exp", "norm", "unif". If <i>s</i>
     * equals null then the service time is determine value
     * @param param - the mean value of service time. If s equals null then the
     * service time equals <i>param</i>.
     */
    public void setDistribution(String s, double param) {
        distribution = s;
        parametr = param;
        timeServ = parametr; // додано 26.12.2011, тоді, якщо s==null, то передається час обслуговування
    }

    /**
     *
     * @return current value of service time
     */
    public double getTimeServ() {
        double a = timeServ;
        if (distribution != null) { 
          try {
                a = generateTimeServ();  
            } catch (ExceptionInvalidTimeDelay ex) {
                Logger.getLogger(PetriT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return a;

    }

    /**
     *
     * @return mean value of service time
     */
    public double getParametr() {
        return parametr;

    }

    /**
     * Set distribution parameter of service time
     *
     * @param p the mean value of service time
     */
    public void setParametr(double p) {
        parametr = p;  // додано 26.12.2011
        timeServ = parametr; // 20.11.2012 

    }

    /**
     * Generating the value of service time
     *
     * @return value of service time which has been generated
     */
    public double generateTimeServ() throws ExceptionInvalidTimeDelay {
        if (distribution != null) {
            if (distribution.equalsIgnoreCase("exp")) {
                timeServ = FunRand.exp(parametr);
            } else if (distribution.equalsIgnoreCase("unif")) {
                timeServ = FunRand.unif(parametr - paramDeviation, parametr + paramDeviation);// 18.01.2013
            } else if (distribution.equalsIgnoreCase("norm")) {
                timeServ = FunRand.norm(parametr, paramDeviation);// added 18.01.2013
            } else;
        } else {
            timeServ = parametr; // 20.11.2012 тобто детерміноване значення
        }
        return timeServ;
    }

    /**
     *
     * @return the name of transition
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param s name of transition
     */
    public void setName(String s) {
        name = s;
    }

    /**
     *
     * @return the time of nearest event
     */
    public double getMinTime() {
        this.minEvent();
        return minTime;
    }

    /**
     *
     * @return num the channel number of transition accordance to nearest event
     */
    public int getNum() {
        return num;
    }

    /**
     *
     * @return the number of transition
     */
    public int getNumber() {
        return number;
    }

    /**
     *
     * @param n - the number of place that is added to input places of
     * transition
     */
    public void addInP(int n) {
        inP.add(n);
    }

    /**
     *
     * @param n - the number of place that is added to output places of
     * transition
     */
    public void addOutP(int n) {
        outP.add(n);
    }

    /**
     * This method determines the places which is input for the transition. <br>
     * The class PetriNet use this method for creating net with given arrays of
     * places, transitions, input arcs and output arcs.
     *
     * @param inPP array of places
     * @param arcs array of input arcs
     * @throws PetriObj.ExceptionInvalidNetStructure if Petri net has invalid structure
     */
    public void createInP(PetriP[] inPP, ArcIn[] arcs) throws ExceptionInvalidNetStructure {
        inPwithInf.clear();    //додано 28.11.2012  список має бути порожнім!!!
        quantInwithInf.clear(); //додано 28.11.2012
        inP.clear();            //додано 28.11.2012
        quantIn.clear();        //додано 28.11.2012
        for (ArcIn arc: arcs) {
            if (arc.getNumT() == this.getNumber()) {
                if (arc.getIsInf() == true) {
                    inPwithInf.add(arc.getNumP());
                    quantInwithInf.add(arc.getQuantity());
                } else {
                    //if (arcs[j].getQuantity() > 0) { //вхідна позиція додається у разі позитивної кількості зв'язків, 9.11.2015
                    inP.add(arc.getNumP());
                    quantIn.add(arc.getQuantity());
                   // }
                }
            }
        }
        if (inP.isEmpty()) {
            throw new ExceptionInvalidNetStructure("Transition " + this.getName() + " hasn't input positions!");
        }

    }

    /**
     * This method determines the places which is output for the transition.
     * <br>
     * The class PetriNet use this method for creating net with given arrays of
     * places, transitions, input arcs and output arcs.
     *
     * @param inPP array of places
     * @param arcs array of output arcs
     * @throws PetriObj.ExceptionInvalidNetStructure if Petri net has invalid structure
     */
    public void createOutP(PetriP[] inPP, ArcOut[] arcs) throws ExceptionInvalidNetStructure {
        outP.clear(); //додано 28.11.2012
        quantOut.clear();   //додано 28.11.2012
        for (ArcOut arc: arcs) {
            if ( arc.getNumT() == this.getNumber()) {
                outP.add(arc.getNumP());
                quantOut.add(arc.getQuantity());
            }
        }
        if (outP.isEmpty()) {
            throw new ExceptionInvalidNetStructure("Transition " + this.getName() + " hasn't output positions!");
        }
    }

    /**
     *
     * @param n the channel number of transition accordance to nearest event
     */
    public void setNum(int n) {
        num = n;

    }

    /**
     *
     * @param n number of transition
     */
    public void setNumber(int n) {
        number = n;

    }

    /**
     * This method determines is firing condition of transition true.<br>
     * Condition is true if for each input place the quality of tokens in ....
     *
     * @param pp array of places of Petri net
     * @return true if firing condition is executed
     */
    public boolean condition(PetriP[] pp) { //Нумерація позицій тут відносна!!!  inP.get(i) - номер позиції у списку позицій, який побудований при конструюванні мережі Петрі, 

        boolean a = true;
        boolean b = true;  // Саме тому при з"єднанні спільних позицій зміна номера не призводить до трагічних наслідків (руйнування зв"язків)!!! 
        for (int i = 0; i < inP.size(); i++) {
            if (pp[inP.get(i)].getMark() < quantIn.get(i)) {
                a = false;
                break;
            }
        }
        for (int i = 0; i < inPwithInf.size(); i++) {
            if (pp[inPwithInf.get(i)].getMark() < quantInwithInf.get(i)) {
                b = false;
                break;
            }
        }
        return a == true && b == true;

    }

    /**
     * The firing transition consists of two actions - tokens input and
     * output.<br>
     * This method provides tokens input in the transition.
     *
     * @param pp array of Petri net places
     * @param currentTime current time
     */
    public void actIn(PetriP[] pp, double currentTime) {
    	/*try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        if (this.condition(pp) == true) {
            for (int i = 0; i < inP.size(); i++) {
                pp[inP.get(i)].decreaseMark(quantIn.get(i));
            }
            if (buffer == 0) {
                timeOut.set(0, currentTime + this.getTimeServ());
            } else {
                timeOut.add(currentTime + this.getTimeServ());
            }

            buffer++;
            if (observedMax < buffer) {
                observedMax = buffer;
            }

            this.minEvent();

        } else {
            //  System.out.println("Condition not true");
        }
    }

    /**
     * The firing transition consists of two actions - tokens input and
     * output.<br>
     * This method provides tokens output in the transition.
     *
     * @param pp array of Petri net places
     */
    public void actOut(PetriP[] pp) {
    	/*try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        if (buffer > 0) {
            for (int j = 0; j < outP.size(); j++) {
                pp[outP.get(j)].increaseMark(quantOut.get(j));
            }
            if (num == 0 && (timeOut.size() == 1)) {
                timeOut.set(0, Double.MAX_VALUE);
            } else {
                timeOut.remove(num);
            }

            buffer--;
            if (observedMin > buffer) {
                observedMin = buffer;
            }
        } else {
            // System.out.println("Buffer is null");
        }

    }

    /**
     * Determines the transition nearest event among the events of its tokens
     * outputs. and the number of transition channel
     */
    public final void minEvent() {
        minTime = Double.MAX_VALUE;
        if (timeOut.size() > 0) {
            for (int i = 0; i < timeOut.size(); i++) {
                if (timeOut.get(i) < minTime) {
                    minTime = timeOut.get(i);
                    num = i;
                }
            }
        }

    }

    /**
     *
     */
    public void print() {
        for (double time: timeOut) {
            System.out.println(time + "   " + this.getName());
        }
    }

    public void printParameters() {
        System.out.println("Transition " + name + " has such parameters: \n"
                + " number " + number + ", probability " + probability + ", priority " + priority
                + "\n parameter " + parametr + ", distribution " + distribution
                + ", time of service (generate) " + this.getTimeServ());
        System.out.println("This transition has input places with such numbers: ");
        for (Integer in : inP) {
            System.out.print(in.toString() + "  ");
        }
        System.out.println("\n and output places with such numbers: ");
        for (Integer out : outP) {
            System.out.print(out.toString() + "  ");
        }
        System.out.println("\n");
    }

    /**
     *
     * @return list of transition input places
     */
    public ArrayList<Integer> getInP() {
        return inP;
    }

    /**
     *
     * @return list of transition output places
     */
    ArrayList<Integer> getOutP() {
        return outP;
    }

    /**
     *
     * @return true if list of input places is empty
     */
    public boolean isEmptyInputPlacesList() {
        return inP.isEmpty();

    }

    /**
     *
     * @return true if list of output places is empty
     */
    public boolean isEmptyOutputPlacesList() {
        return outP.isEmpty();

    }

    /**
     *
     * @return PetriT object with parameters which copy current parameters of
     * this transition
     * @throws java.lang.CloneNotSupportedException if Petri net has invalid structure
     */
    @Override
    public PetriT clone() throws CloneNotSupportedException { // 30.11.2015
    
        super.clone();
        PetriT T = new PetriT(name, parametr);
        T.setDistribution(distribution, parametr);
        T.setPriority(priority);
        T.setProbability(probability);
        T.setNumber(number); //номер зберігається для відтворення зв"язків між копіями позицій та переходів
        T.setBuffer(buffer);
        T.setParamDeviation(paramDeviation);

        return T;

    }

    public void setBuffer(int buff) {
        buffer = buff;
    }

    public String getDistribution() {
        return distribution;
    }

    public double getParamDeviation() {
        return paramDeviation;
    }

    public void setParamDeviation(double parameter) {
        paramDeviation = parameter;
    }

}
