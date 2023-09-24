/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LibNet;

import PetriObj.ArcIn;
import PetriObj.ArcOut;
import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author innastetsenko
 */
public class NetLibrary {
// часові затримки у мікросекундах
    private static final double d = 0.001;  // '+' is approximately 1000 ns = 1000*0.000001ms = 0.001 ms
    private static final double dlock = d*10; 
    private static final double dnumthread = d*1000; 
    private static final double dmodel = d*20; 

    public static PetriNet CreateNetMain(int numThreads, int complexity) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("mainStart", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("for", 1));
        d_P.add(new PetriP("runSt", 0)); //P[3]
        d_P.add(new PetriP("runFin", 0)); //P[4]
        d_P.add(new PetriP("P6", 0));
        d_T.add(new PetriT("start", d * complexity * 20));
        d_T.add(new PetriT("for", d * numThreads * 3000));
        d_T.add(new PetriT("finish", d * 10));
//        for (PetriT tr: d_T){
//            tr.setDistribution("unif", d);
//        }
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), numThreads));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), numThreads));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        PetriNet d_Net = new PetriNet("Untitled", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetOutput(int k) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0));
        d_P.add(new PetriP("for", 0));
        d_P.add(new PetriP("for", 1));
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("lockNext", 0)); //P[5]
        d_P.add(new PetriP("waitSt", 0)); //P[6]
        d_P.add(new PetriP("unlockNext", 1)); //P[7]
        d_P.add(new PetriP("signalNext", 0)); //P[8]
        d_P.add(new PetriP("signalNext2", 0)); //P[9]
        d_P.add(new PetriP("waitFin", 0)); //P[10]
        d_P.add(new PetriP("endFor", 0));
        d_P.add(new PetriP("finish", 0)); //P[12]
        d_P.add(new PetriP("noNext", 0)); //P[13]
        d_P.add(new PetriP("endWhile", 0));

        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("for", d));
        d_T.add(new PetriT("trActout", d * 2));
        d_T.get(2).setPriority(2);
        d_T.get(2).setProbability(0.1);  // тільки 1 з 2 мають час виходу = поточний час 
        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("unlock", d));
        d_T.add(new PetriT("noTrActout", d));
        d_T.get(5).setPriority(2);
        d_T.get(5).setProbability(0.9);
        d_T.add(new PetriT("doWhile", d));
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("noNext", d));
        d_T.get(8).setPriority(1); // added 23.02.2021
        d_T.add(new PetriT("doFor", d));
//        for (PetriT tr: d_T){
//            tr.setDistribution("exp", d);
//        }
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), k));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(8), 1));
        d_In.get(10).setInf(true);
        d_In.add(new ArcIn(d_P.get(4), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), k));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet("Output", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

// відділено спочатку not waiting потім захоплення/очікування локера
    public static PetriNet CreateNetWait() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0));
        d_P.add(new PetriP("unlock", 1));
        d_P.add(new PetriP("condCheck", 0));
        d_P.add(new PetriP("notCond", 0));
        d_P.add(new PetriP("waiting", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("finish", 0));
        d_P.add(new PetriP("getSignal", 0));
        d_P.add(new PetriP("P8", 0));
        d_P.add(new PetriP("P9", 0));

        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("check", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("wait", d));
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("getSignal", d));
        d_T.get(4).setPriority(1);
        d_T.add(new PetriT("isNotWaiting", d));
        d_T.add(new PetriT("catch locker", d * 10));
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.get(3).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet("wait", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetGoUntil(int threadComplexity) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0)); //P[0]
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("tlocLESStlim", 0));//P[2]
        d_P.add(new PetriP("finish", 0)); //P[3]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("if1", 0));
        d_P.add(new PetriP("outputST", 0));//P[6]
        d_P.add(new PetriP("outputFin", 0)); //P[7]
        d_P.add(new PetriP("tminLESStlim", 0));//P[8]
        d_P.add(new PetriP("if2", 0));
        d_P.add(new PetriP("P11", 0));
        d_P.add(new PetriP("tlimBIGGERtmod", 0)); //P[11]
        d_P.add(new PetriP("P13", 0));
        d_P.add(new PetriP("noNext", 0)); //P[13]
        d_P.add(new PetriP("lockNext", 0));
        d_P.add(new PetriP("unlockNext", 1)); //P[15]
        d_P.add(new PetriP("signalNext", 0)); //P[16]
        d_P.add(new PetriP("if3", 0));
        d_P.add(new PetriP("toRemove", 0)); //P[18]
        d_P.add(new PetriP("prev", 0));//P[19]
        d_P.add(new PetriP("P24", 0));
        d_P.add(new PetriP("P26", 0));
        d_P.add(new PetriP("lockPrev", 0)); //P[22]
        d_P.add(new PetriP("P28", 0));
        d_P.add(new PetriP("unlockPrev", 1)); //P[24]
        d_P.add(new PetriP("signalCondLimit", 0));//P[25]

        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("while", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("input", d * 0.5*threadComplexity)); //0.1*threadComplexity));
        d_T.add(new PetriT("settLoc=tMin", d));
        d_T.get(4).setPriority(1);
        d_T.add(new PetriT("doWhile", d));
        d_T.add(new PetriT("T7", d));
        d_T.add(new PetriT("T8", d));
        d_T.get(7).setPriority(1);
        d_T.add(new PetriT("settloc=tmod", d));
        d_T.add(new PetriT("T10", d));
        d_T.add(new PetriT("noNext", d));
        d_T.get(10).setPriority(1);
        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("unlock", d));
        d_T.add(new PetriT("T14", d));
        d_T.get(13).setPriority(1);
        d_T.add(new PetriT("T17", d));
        d_T.add(new PetriT("settloc=tlim", d));
        d_T.add(new PetriT("remove", d));
        d_T.add(new PetriT("lockPrev", d * 10));
        d_T.add(new PetriT("unlockPrev", d));
        d_T.add(new PetriT("T22", d)); //T[19]
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.get(0).setInf(true);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(4), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(5), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.get(11).setInf(true);
        d_In.add(new ArcIn(d_P.get(9), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(10), 1));
        d_In.get(15).setInf(true);
        d_In.add(new ArcIn(d_P.get(15), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(12), 1));
        d_In.add(new ArcIn(d_P.get(17), d_T.get(13), 1));
        d_In.add(new ArcIn(d_P.get(19), d_T.get(13), 1));
        d_In.get(19).setInf(true);
        d_In.add(new ArcIn(d_P.get(17), d_T.get(14), 1));
        d_In.add(new ArcIn(d_P.get(20), d_T.get(16), 1));
        d_In.add(new ArcIn(d_P.get(21), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(22), d_T.get(18), 1));
        d_In.add(new ArcIn(d_P.get(23), d_T.get(19), 1));
        d_In.add(new ArcIn(d_P.get(24), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(18), d_T.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(17), 1));
        d_Out.add(new ArcOut(d_T.get(14), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(13), d_P.get(18), 1));
        d_Out.add(new ArcOut(d_T.get(15), d_P.get(20), 1));
        d_Out.add(new ArcOut(d_T.get(16), d_P.get(21), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(22), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(23), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(24), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(25), 1));
        d_Out.add(new ArcOut(d_T.get(19), d_P.get(7), 1));
        PetriNet d_Net = new PetriNet("goUntil", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetRun() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 1));
        d_P.add(new PetriP("while", 0)); //P[2]
        d_P.add(new PetriP("tlocLESStmod", 0)); //P[3]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("waitSt", 0));
        d_P.add(new PetriP("waitFin", 0));
        d_P.add(new PetriP("runFinish", 0)); //P[7]
        d_P.add(new PetriP("prev", 0));  //P[8]
        d_P.add(new PetriP("tlocLESStlim", 0)); //P[9]
        d_P.add(new PetriP("goUntilSt", 0)); //P[10]
        d_P.add(new PetriP("goUntilFin", 0)); //P[11]
        d_P.add(new PetriP("condCheck", 0)); //P[12]
        d_P.add(new PetriP("tlocEQtlimAndPrev", 0)); //P[13]
        d_P.add(new PetriP("P14", 0));
        d_P.add(new PetriP("unlockPrev", 1)); // P[15]
        d_P.add(new PetriP("lockPrev", 0)); //P[16]
        d_P.add(new PetriP("signalCondLimit", 0)); // P[17]

        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("T2", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("waitSt", d));
        d_T.get(2).setPriority(1);
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("notWait", d));
        d_T.add(new PetriT("goUntilSt", d));
        d_T.get(5).setPriority(2);
        d_T.add(new PetriT("return", d));
        d_T.add(new PetriT("goUntilFin", d));
        d_T.add(new PetriT("updateCond", 0.0));
        d_T.add(new PetriT("remove", d));
        d_T.get(9).setPriority(1);
        d_T.add(new PetriT("lockPrev", d * 10));
        d_T.add(new PetriT("unlockPrev", d)); //T[11]
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.get(2).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(2), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(9), d_T.get(5), 1));
        d_In.get(8).setInf(true);
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(9), 1));
        d_In.get(14).setInf(true);
        d_In.add(new ArcIn(d_P.get(15), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(16), d_T.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(17), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(2), 1)); // edit
        PetriNet d_Net = new PetriNet("run", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetMain(int cores, int numThreads, int complexity) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("mainStart", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("for", 1));
        d_P.add(new PetriP("runSt", 0)); //P[3]
        d_P.add(new PetriP("runFin", 0)); //P[4]
        d_P.add(new PetriP("P6", 0));

        d_T.add(new PetriT("start", d * complexity * 20));
        d_T.add(new PetriT("createThread", d *numThreads * 500));
        d_T.add(new PetriT("finish", d * 10));
//        for (PetriT tr: d_T){
//            tr.setDistribution("unif", d);
//        }
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), numThreads));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), numThreads));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("mainCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetGoUntil(int cores, int threadComplexity) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0)); //P[0]
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("tlocLESStlim", 0));//P[2]
        d_P.add(new PetriP("finish", 0)); //P[3]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("if1", 0));
        d_P.add(new PetriP("outputST", 0));//P[6]
        d_P.add(new PetriP("outputFin", 0)); //P[7]
        d_P.add(new PetriP("tminLESStlim", 0));//P[8]
        d_P.add(new PetriP("if2", 0));
        d_P.add(new PetriP("P11", 0));
        d_P.add(new PetriP("tlimBIGGERtmod", 0)); //P[11]
        d_P.add(new PetriP("P13", 0));
        d_P.add(new PetriP("noNext", 0)); //P[13]
        d_P.add(new PetriP("lockNext", 0));
        d_P.add(new PetriP("unlockNext", 1)); //P[15]
        d_P.add(new PetriP("signalNext", 0)); //P[16]
        d_P.add(new PetriP("if3", 0));
        d_P.add(new PetriP("toRemove", 0)); //P[18]
        d_P.add(new PetriP("prev", 0));//P[19]
        d_P.add(new PetriP("P24", 0));
        d_P.add(new PetriP("P26", 0));
        d_P.add(new PetriP("lockPrev", 0)); //P[22]
        d_P.add(new PetriP("P28", 0));
        d_P.add(new PetriP("unlockPrev", 1)); //P[24]
        d_P.add(new PetriP("signalCondLimit", 0));//P[25]
        
        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("while", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("input", d*5*threadComplexity)); //0.1*threadComplexity));
        d_T.add(new PetriT("settLoc=tMin", d));
        d_T.get(4).setPriority(1);
        d_T.add(new PetriT("doWhile", d));
        d_T.add(new PetriT("T7", d));
        d_T.add(new PetriT("T8", d));
        d_T.get(7).setPriority(1);
        d_T.add(new PetriT("settloc=tmod", d));
        d_T.add(new PetriT("T10", d));
        d_T.add(new PetriT("noNext", d));
        d_T.get(10).setPriority(1);
        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("unlock", d));
        d_T.add(new PetriT("T14", d));
        d_T.get(13).setPriority(1);
        d_T.add(new PetriT("T17", d));
        d_T.add(new PetriT("settloc=tlim", d));
        d_T.add(new PetriT("remove", d));
        d_T.add(new PetriT("lockPrev", d * 10));
        d_T.add(new PetriT("unlockPrev", d));
        d_T.add(new PetriT("T22", d)); //T[19]
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.get(0).setInf(true);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(4), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(5), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.get(11).setInf(true);
        d_In.add(new ArcIn(d_P.get(9), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(10), 1));
        d_In.get(15).setInf(true);
        d_In.add(new ArcIn(d_P.get(15), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(12), 1));
        d_In.add(new ArcIn(d_P.get(17), d_T.get(13), 1));
        d_In.add(new ArcIn(d_P.get(19), d_T.get(13), 1));
        d_In.get(19).setInf(true);
        d_In.add(new ArcIn(d_P.get(17), d_T.get(14), 1));
        d_In.add(new ArcIn(d_P.get(20), d_T.get(16), 1));
        d_In.add(new ArcIn(d_P.get(21), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(22), d_T.get(18), 1));
        d_In.add(new ArcIn(d_P.get(23), d_T.get(19), 1));
        d_In.add(new ArcIn(d_P.get(24), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(18), d_T.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(17), 1));
        d_Out.add(new ArcOut(d_T.get(14), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(13), d_P.get(18), 1));
        d_Out.add(new ArcOut(d_T.get(15), d_P.get(20), 1));
        d_Out.add(new ArcOut(d_T.get(16), d_P.get(21), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(22), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(23), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(24), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(25), 1));
        d_Out.add(new ArcOut(d_T.get(19), d_P.get(7), 1));

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("goUntilCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetOutput(int cores, int threadComplexity) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0));
        d_P.add(new PetriP("for", 0));
        d_P.add(new PetriP("for", 1));
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("lockNext", 0)); //P[5]
        d_P.add(new PetriP("waitSt", 0)); //P[6]
        d_P.add(new PetriP("unlockNext", 1)); //P[7]
        d_P.add(new PetriP("signalNext", 0)); //P[8]
        d_P.add(new PetriP("signalNext2", 0)); //P[9]
        d_P.add(new PetriP("waitFin", 0)); //P[10]
        d_P.add(new PetriP("endFor", 0));
        d_P.add(new PetriP("finish", 0)); //P[12]
        d_P.add(new PetriP("noNext", 0)); //P[13]
        d_P.add(new PetriP("endWhile", 0));
        double probability  = 1.0/threadComplexity; // only 1 of all could perform act in the current moment
        if(probability>=1.0){
            System.out.println("The model has got the wrong value of probability" +probability);
        }
//        System.out.println("=========" +probability);
        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("for", d*2));
        d_T.add(new PetriT("trActout", d ));
        d_T.get(2).setPriority(2);
        d_T.get(2).setProbability(probability);  // тільки 1 з 2 мають час виходу = поточний час 
        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("unlock", d));
        d_T.add(new PetriT("noTrActout", d));
        d_T.get(5).setPriority(2);
        d_T.get(5).setProbability(1-probability);
        d_T.add(new PetriT("doWhile", d));
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("noNext", d));
        d_T.get(8).setPriority(1); // added 23.02.2021
        d_T.add(new PetriT("doFor", d));
//        for (PetriT tr: d_T){
//            tr.setDistribution("exp", d);
//        }
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), threadComplexity));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(8), 1));
        d_In.get(10).setInf(true);
        d_In.add(new ArcIn(d_P.get(4), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), threadComplexity));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(2), 1));

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("outputCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetRun(int cores) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 1));
        d_P.add(new PetriP("while", 0)); //P[2]
        d_P.add(new PetriP("tlocLESStmod", 0)); //P[3]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("waitSt", 0));
        d_P.add(new PetriP("waitFin", 0));
        d_P.add(new PetriP("runFinish", 0)); //P[7]
        d_P.add(new PetriP("prev", 0));  //P[8]
        d_P.add(new PetriP("tlocLESStlim", 0)); //P[9]
        d_P.add(new PetriP("goUntilSt", 0)); //P[10]
        d_P.add(new PetriP("goUntilFin", 0)); //P[11]
        d_P.add(new PetriP("condCheck", 0)); //P[12]
        d_P.add(new PetriP("tlocEQtlimAndPrev", 0)); //P[13]
        d_P.add(new PetriP("P14", 0));
        d_P.add(new PetriP("unlockPrev", 1)); // P[15]
        d_P.add(new PetriP("lockPrev", 0)); //P[16]
        d_P.add(new PetriP("signalCondLimit", 0)); // P[17]

        d_T.add(new PetriT("start", d));
        d_T.add(new PetriT("T2", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("waitSt", d));
        d_T.get(2).setPriority(1);
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("notWait", d));
        d_T.add(new PetriT("goUntilSt", d));
        d_T.get(5).setPriority(2);
        d_T.add(new PetriT("return", d));
        d_T.add(new PetriT("goUntilFin", d));
        d_T.add(new PetriT("updateCond", 0.0));
        d_T.add(new PetriT("remove", d));
        d_T.get(9).setPriority(1);
        d_T.add(new PetriT("lockPrev", d * 10));
        d_T.add(new PetriT("unlockPrev", d)); //T[11]
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.get(2).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(2), 1));
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(9), d_T.get(5), 1));
        d_In.get(8).setInf(true);
        d_In.add(new ArcIn(d_P.get(11), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(9), 1));
        d_In.get(14).setInf(true);
        d_In.add(new ArcIn(d_P.get(15), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(16), d_T.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(17), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(2), 1)); // edit

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("runCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetWait(int cores) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("start", 0));
        d_P.add(new PetriP("unlock", 1));
        d_P.add(new PetriP("condCheck", 0));
        d_P.add(new PetriP("notCond", 0));
        d_P.add(new PetriP("waiting", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("finish", 0));
        d_P.add(new PetriP("getSignal", 0));
        d_P.add(new PetriP("P8", 0));
        d_P.add(new PetriP("P9", 0));

        d_T.add(new PetriT("lock", d * 10));
        d_T.add(new PetriT("check", d));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("wait", d));
        d_T.add(new PetriT("finish", d));
        d_T.add(new PetriT("getSignal", d));
        d_T.get(4).setPriority(1);
        d_T.add(new PetriT("isNotWaiting", d));
        d_T.add(new PetriT("catch locker", d * 10));
//        for (PetriT tr: d_T){
//            tr.setParametr(d);
//        }
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.get(3).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(2), 1));

        d_P.add(new PetriP("core", cores));
        for (int j = 0; j < d_T.size(); j++) {
            d_In.add(new ArcIn(d_P.get(d_P.size() - 1), d_T.get(j), 1));
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(d_P.size() - 1), 1));
        }

        PetriNet d_Net = new PetriNet("waitCores", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

}
