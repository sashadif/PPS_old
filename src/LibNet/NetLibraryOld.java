package LibNet;

import PetriObj.ExceptionInvalidNetStructure;
import PetriObj.PetriNet;
import PetriObj.PetriP;
import PetriObj.PetriT;
import PetriObj.ArcIn;
import PetriObj.ArcOut;
import PetriObj.ExceptionInvalidTimeDelay;
import java.util.ArrayList;
import java.util.Random;

public class NetLibraryOld {

    /**
     * Creates Petri net that describes the dynamics of system of the mass
     * service (with unlimited queue)
     *
     * @param numChannel the quantity of devices
     * @param timeMean the mean value of service time of unit
     * @param name the individual name of SMO
     * @throws ExceptionInvalidTimeDelay if one of net's transitions has no
     * input position.
     * @return Petri net dynamics of which corresponds to system of mass service
     * with given parameters
     */
    public static PetriNet CreateNetSMOwithoutQueue(int numChannel, double timeMean, String name) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", numChannel));
        d_P.add(new PetriP("P3", 0));
        d_T.add(new PetriT("T1", timeMean, Double.MAX_VALUE));
        d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(0.0);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet("SMOwithoutQueue" + name, d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    /**
     * Creates Petri net that describes the dynamics of arrivals of demands for
     * service
     *
     * @param timeMean mean value of interval between arrivals
     * @return Petri net dynamics of which corresponds to generator
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid
     * structure
     */
    public static PetriNet CreateNetGenerator(double timeMean) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0));
        d_T.add(new PetriT("T1", timeMean, Double.MAX_VALUE));
        d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(0.0);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(0), 1));
        PetriNet d_Net = new PetriNet("Generator", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    /**
     * Creates Petri net that describes the route choice with given
     * probabilities
     *
     * @param p1 the probability of choosing the first route
     * @param p2 the probability of choosing the second route
     * @param p3 the probability of choosing the third route
     * @return Petri net dynamics of which corresponds to fork of routs
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid
     * structure
     */
    public static PetriNet CreateNetFork(double p1, double p2, double p3) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_T.add(new PetriT("T1", 0.0, Double.MAX_VALUE));
        d_T.get(0).setProbability(p1);
        d_T.add(new PetriT("T2", 0.0, Double.MAX_VALUE));
        d_T.get(1).setProbability(p2);
        d_T.add(new PetriT("T3", 0.0, Double.MAX_VALUE));
        d_T.get(2).setProbability(p3);
        d_T.add(new PetriT("T4", 0.0, Double.MAX_VALUE));
        d_T.get(3).setProbability(1 - p1 - p2 - p3);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        PetriNet d_Net = new PetriNet("Fork", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    /**
     * Creates Petri net that describes the route choice with given
     * probabilities
     *
     * @param numOfWay quantity of possibilities in choice ("ways")
     * @param probabilities set of values probabilities for each "way"
     * @return Petri net dynamics of which corresponds to fork of routs
     * @throws PetriObj.ExceptionInvalidTimeDelay if Petri net has invalid
     * structure
     */
    public static PetriNet CreateNetFork(int numOfWay, double[] probabilities) throws ExceptionInvalidTimeDelay, ExceptionInvalidNetStructure {

        ArrayList<PetriP> d_P = new ArrayList<PetriP>();
        ArrayList<PetriT> d_T = new ArrayList<PetriT>();
        ArrayList<ArcIn> d_In = new ArrayList<ArcIn>();
        ArrayList<ArcOut> d_Out = new ArrayList<ArcOut>();

        d_P.add(new PetriP("P0", 0));
        for (int j = 0; j < numOfWay; j++) {
            d_P.add(new PetriP("P" + (j + 1), 0));
        }

        for (int j = 0; j < numOfWay; j++) {
            d_T.add(new PetriT("вибір маршруту " + (j + 1), 0));
        }
        for (int j = 0; j < numOfWay; j++) {
            d_T.get(j).setProbability(probabilities[j]);
        }

        for (int j = 0; j < numOfWay; j++) {
            d_In.add(new ArcIn(d_P.get(0), d_T.get(j), 1));
        }

        for (int j = 0; j < numOfWay; j++) {
            d_Out.add(new ArcOut(d_T.get(j), d_P.get(j + 1), 1));
        }

        PetriNet d_Net = new PetriNet("Fork ", d_P, d_T, d_In, d_Out);

        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }


    public static PetriNet CreateNetAdmin() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("User", 1));
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("Damaged", 0));
        d_P.add(new PetriP("Harmless", 0));
        d_P.add(new PetriP("Executed", 0));
        d_P.add(new PetriP("NewPacket", 0));
        d_P.add(new PetriP("Admin", 1));
        d_P.add(new PetriP("FileServer", 0));
        d_P.add(new PetriP("Alarm", 0));
        d_P.add(new PetriP("P10", 0));
        d_T.add(new PetriT("DoTest", 1.0));
        d_T.add(new PetriT("ControlTime", 10.0));
        d_T.add(new PetriT("T3", 0.0));
        d_T.add(new PetriT("T4", 0.0));
        d_T.add(new PetriT("RepairResources", 0.0));
        d_T.add(new PetriT("T2", 0.0));
        d_T.add(new PetriT("T3", 0.0));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(10), 1));
        PetriNet d_Net = new PetriNet("admin", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNettest3() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 100));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_T.add(new PetriT("T1", 0.0));
        d_T.add(new PetriT("T2", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        PetriNet d_Net = new PetriNet("test3", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetGenerator2(double d) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0));
        d_T.add(new PetriT("T1", d));
        d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(0.0);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(0), 1));
        PetriNet d_Net = new PetriNet("Generator", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetThread3() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("bow{", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("lockA", 1));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("P6", 0));
        d_P.add(new PetriP("failure++", 0));
        d_P.add(new PetriP("lockB", 1));
        d_P.add(new PetriP("bowA++", 0));
        d_P.add(new PetriP("P10", 0));
        d_P.add(new PetriP("bowB++", 0));
        d_P.add(new PetriP("P15", 0));
        d_P.add(new PetriP("bowLoop{", 100));
        d_P.add(new PetriP("bow", 0));
        d_P.add(new PetriP("Core", 1));
        d_T.add(new PetriT("imp{", 0.1));
        d_T.add(new PetriT("tryLockA", 0.0));
        d_T.add(new PetriT("0&?", 0.0));
        d_T.add(new PetriT("tryLockB", 0.0));
        d_T.add(new PetriT("bowBack{", 0.1));
        d_T.add(new PetriT("unlockA", 0.0));
        d_T.add(new PetriT("0&1", 0.0));
        d_T.add(new PetriT("failure", 0.0));
        d_T.add(new PetriT("unlockAB", 0.1));
        d_T.add(new PetriT("unlockB", 0.1));
        d_T.add(new PetriT("for{", 0.0));
        d_T.add(new PetriT("for", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(6), 1));

        PetriNet d_Net = new PetriNet("friendThread", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetConsistency(double forDelay, double readDelay, double modifyDelay, double writeDelay) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1000000));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 1));
        d_P.add(new PetriP("readPermission", 1));
        d_P.add(new PetriP("writePermission", 1));
        d_P.add(new PetriP("Cores", 2));
        d_T.add(new PetriT("for", forDelay));
        d_T.get(0).setDistribution("norm", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(0.1);
        d_T.add(new PetriT("read", readDelay));

        d_T.add(new PetriT("modify", modifyDelay));

        d_T.add(new PetriT("write", writeDelay));

        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));

        PetriNet d_Net = new PetriNet("Consistency", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetFriend() throws ExceptionInvalidNetStructure {
        double delay = 100.0;
        double x = 0.00000001;
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        Random r = new Random();
        d_P.add(new PetriP("bow[", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("lock", 1));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("P6", 0));
        d_P.add(new PetriP("failure++", 0));
        d_P.add(new PetriP("lockOther", 1));
        d_P.add(new PetriP("bowA++", 0));
        d_P.add(new PetriP("P10", 0));
        d_P.add(new PetriP("bowB++", 0));
        d_P.add(new PetriP("P15", 0));
        d_P.add(new PetriP("bowLoop[", 10));
        d_P.add(new PetriP("bow]", 0));
        d_P.add(new PetriP("Core", 1));
        d_T.add(new PetriT("imp[", delay * x));// was delay
        d_T.get(0).setDistribution("norm", d_T.get(0).getTimeServ() * 0.1);
        d_T.add(new PetriT("tryLockA", delay * x, 1));//priority = 1
        d_T.get(1).setDistribution("norm", d_T.get(1).getTimeServ() * 0.1);
        d_T.add(new PetriT("0&?", 0.0));
        d_T.add(new PetriT("tryLockB", delay * x, 1));//priority = 1
        d_T.get(3).setDistribution("norm", d_T.get(3).getTimeServ() * 0.1);
        d_T.add(new PetriT("bowBack[]", delay)); //delay*x
        d_T.get(4).setDistribution("norm", d_T.get(4).getTimeServ() * 0.1);
        d_T.add(new PetriT("unlockA", 0.0));
        d_T.add(new PetriT("0&1", 0.0, 1)); //priority = 1
        d_T.add(new PetriT("failure", 0.0));
        d_T.add(new PetriT("unlockAB", 0.0));
        d_T.add(new PetriT("unlockB", 0.0));
        d_T.add(new PetriT("for[", delay)); //sleep
        d_T.get(10).setDistribution("norm", d_T.get(10).getTimeServ() * 0.1);
        d_T.add(new PetriT("for]", 0.0 + r.nextDouble()));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(6), 1));
        PetriNet d_Net = new PetriNet("Friend ", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetFriendUsingCores() throws ExceptionInvalidNetStructure {
        double delay = 100.0;
        double x = 0.8;
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        Random r = new Random();
        d_P.add(new PetriP("bow[", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("lock", 1));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("P6", 0));
        d_P.add(new PetriP("failure++", 0));
        d_P.add(new PetriP("lockOther", 1));
        d_P.add(new PetriP("bowA++", 0));
        d_P.add(new PetriP("P10", 0));
        d_P.add(new PetriP("bowB++", 0));
        d_P.add(new PetriP("P15", 0));
        d_P.add(new PetriP("bowLoop[", 10));
        d_P.add(new PetriP("bow]", 0));
        d_P.add(new PetriP("Core", 10));
        d_T.add(new PetriT("imp[", delay * x));// was delay
        d_T.get(0).setDistribution("norm", d_T.get(0).getTimeServ() * 0.1);
        d_T.add(new PetriT("tryLockA", delay * x, 1));//priority = 1
        d_T.get(1).setDistribution("norm", d_T.get(1).getTimeServ() * 0.1);
        d_T.add(new PetriT("0&?", 0.0));
        d_T.add(new PetriT("tryLockB", delay * x, 1));//priority = 1
        d_T.get(3).setDistribution("norm", d_T.get(3).getTimeServ() * 0.1);
        d_T.add(new PetriT("bowBack[]", delay)); //delay*x
        d_T.get(4).setDistribution("norm", d_T.get(4).getTimeServ() * 0.1);
        d_T.add(new PetriT("unlockA", 0.0));
        d_T.add(new PetriT("0&1", 0.0, 1)); //priority = 1
        d_T.add(new PetriT("failure", 0.0));
        d_T.add(new PetriT("unlockAB", 0.0));
        d_T.add(new PetriT("unlockB", 0.0));
        d_T.add(new PetriT("for[", delay)); //sleep
        d_T.get(10).setDistribution("norm", d_T.get(10).getTimeServ() * 0.1);
        d_T.add(new PetriT("for]", 0.0 + r.nextDouble()));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(9), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(6), 1));

        PetriNet d_Net = new PetriNet("Friend ", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetNewPool() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("tasksQueue", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("end", 0));
        d_P.add(new PetriP("P7", 0));
        d_P.add(new PetriP("numWorkers", 0));
        d_T.add(new PetriT("T1", 0.1));
        d_T.add(new PetriT("newRunnable", 0.1));
        d_T.add(new PetriT("invoke", 0.1));
        d_T.add(new PetriT("run", 0.2));
        d_T.add(new PetriT("T5", 0.1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 5));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(6), 2));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(7), 5));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(1), 1));
        PetriNet d_Net = new PetriNet("NewPool", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetNewThread() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("P6", 0));
        d_P.add(new PetriP("P7", 0));
        d_P.add(new PetriP("P8", 0));
        d_P.add(new PetriP("P9", 0));
        d_T.add(new PetriT("new", 0.1));
        d_T.add(new PetriT("start", 0.1));
        d_T.add(new PetriT("run[", 0.1));
        d_T.add(new PetriT("run]", 0.1));
        d_T.add(new PetriT("end", 0.1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(8), 1));
        PetriNet d_Net = new PetriNet("NewThread", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetUntitled() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 0));
        d_T.add(new PetriT("T1", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        PetriNet d_Net = new PetriNet("Untitled", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetkjkhhjkhh() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 0));
        d_T.add(new PetriT("T1", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        PetriNet d_Net = new PetriNet("kjkhhjkhh", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNethhh() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 0));
        d_T.add(new PetriT("T1", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        PetriNet d_Net = new PetriNet("hhh", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetLock() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("lock", 0));
        d_P.add(new PetriP("unlock", 1));
        d_P.add(new PetriP("P6", 0));
        d_P.add(new PetriP("P7", 0));
        d_P.add(new PetriP("P8", 0));
        d_T.add(new PetriT("tryLock", 0.1));
        d_T.get(0).setPriority(3);
        d_T.add(new PetriT("unlock", 0.1));
        d_T.get(1).setPriority(3);
        d_T.add(new PetriT("catch", 0.0));
        d_T.add(new PetriT("false", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(3), 1));
        PetriNet d_Net = new PetriNet("Lock", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetGuardedBlock() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("signal to another thread", 0));
        d_P.add(new PetriP("signal from another thread", 0));
        d_P.add(new PetriP("P8", 1));
        d_P.add(new PetriP("lock", 0));
        d_T.add(new PetriT("lock", 0.0));
        d_T.add(new PetriT("T2", 0.0));
        d_T.get(1).setPriority(4);
        d_T.add(new PetriT("wait", 0.0));
        d_T.add(new PetriT("notify", 0.0));
        d_T.add(new PetriT("notify other", 0.0));
        d_T.add(new PetriT("unlock", 0.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(7), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(1), 1));
        PetriNet d_Net = new PetriNet("GuardedBlock", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetConsistency() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("readPermission", 1));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("writePermission", 1));
        d_P.add(new PetriP("P1", 1000000));
        d_P.add(new PetriP("P5", 1));
        d_P.add(new PetriP("P2", 0));
        d_T.add(new PetriT("read", 0.1));
        d_T.add(new PetriT("modify", 0.1));
        d_T.add(new PetriT("write", 0.1));
        d_T.add(new PetriT("for", 0.2));
        d_T.get(3).setDistribution("norm", d_T.get(3).getTimeServ());
        d_T.get(3).setParamDeviation(0.1);
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        PetriNet d_Net = new PetriNet("Consistency", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetPool() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("numThreads", 0));
        d_P.add(new PetriP("numWorkers", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P7", 0));
        d_P.add(new PetriP("P8", 0));
        d_T.add(new PetriT("invoke", 0.1));
        d_T.get(0).setDistribution("exp", d_T.get(0).getTimeServ());
        d_T.get(0).setParamDeviation(0.0);
        d_T.add(new PetriT("newThreadPool", 0.1));
        d_T.add(new PetriT("newRunnable", 0.1));
        d_T.add(new PetriT("run()", 0.2));
        d_T.get(3).setDistribution("exp", d_T.get(3).getTimeServ());
        d_T.get(3).setParamDeviation(0.0);
        d_T.add(new PetriT("end", 0.1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(7), 1));
        PetriNet d_Net = new PetriNet("Pool", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
    
    //!!!!додано звільнення локера після wait і знову захопленння його коли приходить сигнал
    // додано очікування локера після getSignal
      public static PetriNet CreateNetWaitParallel() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("lock", 0)); //P[1] not used  inversed to unlock
        d_P.add(new PetriP("unlock", 1)); //P[2] 
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("signalFromPrevObj", 0)); //P[4]
        d_P.add(new PetriP("bufferNotEmpty", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("waitStart", 0)); //P[7]
        d_P.add(new PetriP("waitFinish", 0)); //P[8]
         d_P.add(new PetriP("was not waiting when signal arrived", 0)); //P[9]
           d_P.add(new PetriP("waiting locker", 0)); //P[10] 

        d_T.add(new PetriT("lock", 1.0));    
        d_T.add(new PetriT("set", 1.0)); // setLim
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("wait", 1.0));
        d_T.add(new PetriT("getSignal", 1.0)); //get signal notEmpty
        d_T.get(3).setPriority(2);  //
        d_T.add(new PetriT("unlock", 1.0));
        d_T.add(new PetriT("is not waiting", 1.0)); // сигнал який надійшов, скинути
        d_T.add(new PetriT("lock again",1.0)); //  додано 11.02.2021 блокування об'єкта для notify
        d_In.add(new ArcIn(d_P.get(0), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.get(3).setInf(true);
        d_In.add(new ArcIn(d_P.get(0), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(1), 1));
        d_In.get(5).setInf(true);
//        d_In.add(new ArcIn(d_P.get(1), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(6), 1)); // знову захоплюємо локер??
        d_In.add(new ArcIn(d_P.get(10), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(6), 1));
        
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(0), 1));
//        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(2), 1)); // Звільнити локер
         d_Out.add(new ArcOut(d_T.get(3), d_P.get(10), 1));
         
        PetriNet d_Net = new PetriNet("WaitParallel", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }
      
    public static PetriNet CreateNetRunParallel() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P1", 1));
        d_P.add(new PetriP("P2", 0)); // while
        d_P.add(new PetriP("if", 0));
        d_P.add(new PetriP("runFin", 0)); //P[3]
        d_P.add(new PetriP("tLoc<tMod", 0));
        d_P.add(new PetriP("prevObj!=null", 0)); // prev==1 if previous object not null
        d_P.add(new PetriP("waitStart", 0)); //P[6]
        d_P.add(new PetriP("waitFinish", 0)); //P[7]
        d_P.add(new PetriP("if", 0));
        d_P.add(new PetriP("goUntilStart", 0)); //P[9]
        d_P.add(new PetriP("tLoc<lim", 0));
        d_P.add(new PetriP("toWhile", 0));
        d_P.add(new PetriP("goUntilFinish", 0)); //P[12]
        d_P.add(new PetriP("runSt", 0)); //P[13]
        d_T.add(new PetriT("runStart", 1.0));
        d_T.add(new PetriT("while", 1.0));
        d_T.get(1).setPriority(1);
        d_T.add(new PetriT("runFinish", 1.0));
        d_T.add(new PetriT("T3", 1.0));
        d_T.get(3).setPriority(1);
        d_T.add(new PetriT("lim=tMod", 1.0));
        d_T.add(new PetriT("waiting finished", 1.0));
        d_T.add(new PetriT("T6", 1.0));
        d_T.get(6).setPriority(1);
        d_T.add(new PetriT("return", 1.0));
        d_T.add(new PetriT("T8", 1.0));
        d_T.add(new PetriT("T9", 1.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(1), 1));
        d_In.get(3).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(3), 1));
        d_In.get(5).setInf(true);
        d_In.add(new ArcIn(d_P.get(2), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(6), 1));
        d_In.get(9).setInf(true);
        d_In.add(new ArcIn(d_P.get(8), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(8), 1));
        d_In.add(new ArcIn(d_P.get(11), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(0), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(3), 1)); // виправлено...4.02.2021
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(1), 1));
        PetriNet d_Net = new PetriNet("RunParallel", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetOutputParallel(int listTLength) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("outputStart", 0)); //P[0]
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("if", 0));
        d_P.add(new PetriP("P4", 1)); // for 
        d_P.add(new PetriP("nextLock", 0));  // state lock
        d_P.add(new PetriP("no cond (nextObj_not_null_and_getOutTContainsTr)", 0));//P[6] but only no nexObj is implemented
        d_P.add(new PetriP("wait3Start", 0)); //P[7]
        d_P.add(new PetriP("signalNextObjWait0", 0));
        d_P.add(new PetriP("wait3Finish", 0)); //P[9]
        d_P.add(new PetriP("P10", 0));
        d_P.add(new PetriP("outputFinish", 0)); //P[11]
        d_P.add(new PetriP("nextObjUnlock", 1)); //P[12] додано 7.02.2021
        
        d_P.add(new PetriP("nextObjLock", 0)); //P[13] додано 7.02.2021 // від'єднано стан зберігається у P[5]
        d_P.add(new PetriP("endfor", 0)); //P[14] додано 7.02.2021
        d_P.add(new PetriP("signalNextObjWait2", 0));  //додати для WAIt??
        
        d_T.add(new PetriT("start", 1.0)); // 
        d_T.add(new PetriT("for", 1.0));
        d_T.add(new PetriT("trActOut", 1.0));
        // make decision by probability
        d_T.get(2).setPriority(10);  // виправлено 7.02.2021
        double prob =1.0 / listTLength; 
        d_T.get(2).setProbability(prob); //додано 7.02.2021
        d_T.add(new PetriT("T3", 1.0));
        d_T.get(3).setPriority(10);  // виправлено 17.02.2021 щоб сума ймовірностей = 1
        d_T.get(3).setProbability(1.0-prob); //додано 17.02.2021
        //
        d_T.add(new PetriT("lock", 1.0));
        d_T.add(new PetriT("T5", 1.0));
        d_T.get(5).setPriority(1);
        d_T.add(new PetriT("endwhile", 1.0));
        d_T.add(new PetriT("endfor", 1.0));
        d_T.add(new PetriT("unlock", 1.0)); //додано 7.02.2021
        d_T.add(new PetriT("outputFinish", 1.0)); //додано 7.02.2021
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(5), 1)); // 13.02.2021
        d_In.get(7).setInf(true);
        d_In.add(new ArcIn(d_P.get(12), d_T.get(4), 1));  //додано 7.02.2021
        d_In.add(new ArcIn(d_P.get(9), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(10), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(8), 1));//додано 7.02.2021
        d_In.add(new ArcIn(d_P.get(14), d_T.get(9), listTLength));//додано 7.02.2021
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), listTLength));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(2), 1)); // to while
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(4), 1)); // to for
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(12), 1)); //додано 7.02.2021
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(7), 1)); //додано 7.02.2021
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(8), 1)); //додано 7.02.2021
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(11), 1)); //додано 7.02.2021
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(15), 1)); //додано 8.02.2021
        
        PetriNet d_Net = new PetriNet("OutputParallel", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetMainParallel(int n, int c) throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("P0", 1));
        d_P.add(new PetriP("P1", 0));
        d_P.add(new PetriP("P2", 0));
        d_P.add(new PetriP("P3", 0));
        d_P.add(new PetriP("P4", 0));
        d_P.add(new PetriP("P5", 1));
        d_P.add(new PetriP("runStart", 0)); //P[6]
        d_P.add(new PetriP("runFinish", 0)); //P[7]
        d_P.add(new PetriP("P8", 0));
        d_P.add(new PetriP("P9", 0));
        d_T.add(new PetriT("main()", 1.0));
        d_T.add(new PetriT("getModel", 0.01 * c));
        d_T.add(new PetriT("for", 1.0));
        d_T.add(new PetriT("newThread", 1.0));
        d_T.add(new PetriT("startThread", 1.0));
        d_T.add(new PetriT("joinThread", 1.0));
        d_T.add(new PetriT("printRes", 1.0));
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(2), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(5), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), n));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(1), d_P.get(2), n));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(4), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(9), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(5), 1));
      
        PetriNet d_Net = new PetriNet("MainParallel", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

    public static PetriNet CreateNetGoUntilParallel() throws ExceptionInvalidNetStructure {
        ArrayList<PetriP> d_P = new ArrayList<>();
        ArrayList<PetriT> d_T = new ArrayList<>();
        ArrayList<ArcIn> d_In = new ArrayList<>();
        ArrayList<ArcOut> d_Out = new ArrayList<>();
        d_P.add(new PetriP("goUntilStart", 0));
        d_P.add(new PetriP("P1", 0)); //P[1]
        d_P.add(new PetriP("P2", 0));  // після P[17] тепер
        d_P.add(new PetriP("if1", 0));
        d_P.add(new PetriP("tMin<lim", 0)); // P[4]
        d_P.add(new PetriP("P5", 0));
        d_P.add(new PetriP("if2", 0));
        d_P.add(new PetriP("lim>=tMod", 0)); //P[7]
        d_P.add(new PetriP("if3", 0));
        d_P.add(new PetriP("prevObj!=null", 0));// P[9]
        d_P.add(new PetriP("P10", 0));
        d_P.add(new PetriP("outputStart", 0));
        d_P.add(new PetriP("outputFinish", 0));
        d_P.add(new PetriP("P13", 0));
        d_P.add(new PetriP("P14", 0));
        d_P.add(new PetriP("condSignalNextWait0", 0));//P[15]
        d_P.add(new PetriP("wait2Start", 0));
        d_P.add(new PetriP("if(wait2Finish)", 0));
        d_P.add(new PetriP("first>tMod", 0)); //P[18]
        d_P.add(new PetriP("P19", 0));
        d_P.add(new PetriP("P20", 0));
        d_P.add(new PetriP("if4", 0));
        d_P.add(new PetriP("prevlock", 0));
        d_P.add(new PetriP("prebObjUnlock", 1)); //unlock
        d_P.add(new PetriP("prevObjLock", 0)); // від'єднуємо
        d_P.add(new PetriP("signalPrevObj", 0));
        d_P.add(new PetriP("bufferSize>=MaxSize", 0)); //P[26]  13.02.2021
        d_P.add(new PetriP("P27", 0));
        d_P.add(new PetriP("P28", 0));
        d_P.add(new PetriP("nextObj==null", 0)); //P[29] 14.02.2021
        d_P.add(new PetriP("nextlock", 0));
        d_P.add(new PetriP("nextObjUnlock", 1)); //unlock
        d_P.add(new PetriP("nextObjLock", 0)); // від'єднали, цей стан зберігає Р[30]
        d_P.add(new PetriP("while", 0));
        d_P.add(new PetriP("goUntilFinish", 0));
        d_P.add(new PetriP("tLoc<lim", 0)); // P[35]
        d_P.add(new PetriP("condSignalNextWait2", 0)); // P[36]
        
        d_T.add(new PetriT("goUntilStart", 0.0)); //   виправлена назва Т1 на goUntilStart
        d_T.add(new PetriT("input", 0.0));
        d_T.add(new PetriT("T2", 0.0)); //tmin less then tlim???
        d_T.get(2).setPriority(1);
        d_T.add(new PetriT("T3", 0.0));
        d_T.add(new PetriT("settLoc=tMin", 0.0));
        d_T.add(new PetriT("T5", 0.0));
        d_T.get(5).setPriority(1);
        d_T.add(new PetriT("T6", 0.0));
        d_T.add(new PetriT("prevNotNull", 0.0)); // prev not null
        d_T.get(7).setPriority(1);
        d_T.add(new PetriT("tLoc=lim", 0.0));
        d_T.add(new PetriT("T9", 0.0));
        d_T.add(new PetriT("outputFinish", 0.0)); //output finish T11
        d_T.add(new PetriT("settLoc=tMod", 0.0));
        d_T.add(new PetriT("toWhile", 0.0));  //  виправлена назва Т13 на toWhile
        d_T.add(new PetriT("T13", 0.0));
        d_T.get(13).setPriority(1);
        d_T.add(new PetriT("settLoc=limit", 0.0));
        d_T.add(new PetriT("reinstate", 0.0));
        d_T.add(new PetriT("remove", 0.0));
        d_T.add(new PetriT("T17", 0.0));
//        d_T.get(17).setPriority(1); //13.02.2021
        d_T.add(new PetriT("T18", 0.0));
        d_T.add(new PetriT("T19", 0.0));
         d_T.get(19).setPriority(1); //13.02.2021
        d_T.add(new PetriT("T20", 0.0));
        d_T.add(new PetriT("lockNextObj", 0.0)); //nextObjlock
//        d_T.get(21).setPriority(1); //14.02.2021
        d_T.add(new PetriT("unlockNextObj", 0.0));
        d_T.add(new PetriT("T23", 0.0));
        d_T.get(23).setPriority(1); //14.02.2021
        d_T.add(new PetriT("T24", 0.0));
        d_T.get(24).setPriority(1);
        d_T.add(new PetriT("goUntilFinish", 0.0));
        d_T.add(new PetriT("updateTfirst", 0.0));
        
        d_In.add(new ArcIn(d_P.get(0), d_T.get(0), 1));
        d_In.add(new ArcIn(d_P.get(1), d_T.get(1), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(2), 1));
        d_In.add(new ArcIn(d_P.get(3), d_T.get(3), 1));
        d_In.add(new ArcIn(d_P.get(4), d_T.get(2), 1));
        d_In.get(4).setInf(true);
        d_In.add(new ArcIn(d_P.get(5), d_T.get(4), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(5), 1));
        d_In.add(new ArcIn(d_P.get(6), d_T.get(6), 1));
        d_In.add(new ArcIn(d_P.get(7), d_T.get(5), 1));
        d_In.get(8).setInf(true);
        d_In.add(new ArcIn(d_P.get(8), d_T.get(7), 1));
        d_In.add(new ArcIn(d_P.get(8), d_T.get(8), 1)); //??
        d_In.add(new ArcIn(d_P.get(9), d_T.get(7), 1));
        d_In.get(11).setInf(true);
        d_In.add(new ArcIn(d_P.get(10), d_T.get(9), 1));
        d_In.add(new ArcIn(d_P.get(12), d_T.get(10), 1));
        d_In.add(new ArcIn(d_P.get(14), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(13), d_T.get(12), 1));
        d_In.add(new ArcIn(d_P.get(17), d_T.get(14), 1));
        d_In.add(new ArcIn(d_P.get(18), d_T.get(13), 1));
        d_In.get(17).setInf(true);
        d_In.add(new ArcIn(d_P.get(19), d_T.get(15), 1));
        d_In.add(new ArcIn(d_P.get(20), d_T.get(16), 1));
        d_In.add(new ArcIn(d_P.get(21), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(22), d_T.get(18), 1));
        d_In.add(new ArcIn(d_P.get(23), d_T.get(17), 1));
        d_In.add(new ArcIn(d_P.get(21), d_T.get(19), 1));
        d_In.add(new ArcIn(d_P.get(26), d_T.get(19), 1)); //13.02.2021
        d_In.get(24).setInf(true);
        d_In.add(new ArcIn(d_P.get(27), d_T.get(20), 1));
        d_In.add(new ArcIn(d_P.get(28), d_T.get(21), 1));
        d_In.add(new ArcIn(d_P.get(29), d_T.get(23), 1)); //14.02.2021
        d_In.get(27).setInf(true);
        d_In.add(new ArcIn(d_P.get(30), d_T.get(22), 1));
        d_In.add(new ArcIn(d_P.get(31), d_T.get(21), 1));
        d_In.add(new ArcIn(d_P.get(33), d_T.get(24), 1));
        d_In.add(new ArcIn(d_P.get(33), d_T.get(25), 1));
        d_In.add(new ArcIn(d_P.get(35), d_T.get(24), 1));
        d_In.get(32).setInf(true);
        d_In.add(new ArcIn(d_P.get(17), d_T.get(26), 1)); //15.02.2021
        d_In.add(new ArcIn(d_P.get(28), d_T.get(23), 1));
        d_In.add(new ArcIn(d_P.get(35), d_T.get(14), 1));
        d_In.add(new ArcIn(d_P.get(35), d_T.get(11), 1));
        d_In.add(new ArcIn(d_P.get(35), d_T.get(8), 1)); // added
        d_In.add(new ArcIn(d_P.get(2), d_T.get(13), 1)); //15.02.2021

        d_Out.add(new ArcOut(d_T.get(1), d_P.get(3), 1));
        d_Out.add(new ArcOut(d_T.get(2), d_P.get(5), 1));
        d_Out.add(new ArcOut(d_T.get(3), d_P.get(6), 1));
        d_Out.add(new ArcOut(d_T.get(6), d_P.get(8), 1));
        d_Out.add(new ArcOut(d_T.get(4), d_P.get(10), 1));
        d_Out.add(new ArcOut(d_T.get(9), d_P.get(11), 1));
        d_Out.add(new ArcOut(d_T.get(10), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(5), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(8), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(7), d_P.get(16), 1));
        d_Out.add(new ArcOut(d_T.get(14), d_P.get(19), 1));
        d_Out.add(new ArcOut(d_T.get(15), d_P.get(20), 1));
        d_Out.add(new ArcOut(d_T.get(16), d_P.get(21), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(25), 1));
        d_Out.add(new ArcOut(d_T.get(17), d_P.get(22), 1)); // 
//        d_Out.add(new ArcOut(d_T.get(17), d_P.get(24), 1)); //
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(23), 1));
        d_Out.add(new ArcOut(d_T.get(18), d_P.get(27), 1));
        d_Out.add(new ArcOut(d_T.get(19), d_P.get(27), 1));
        d_Out.add(new ArcOut(d_T.get(20), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(11), d_P.get(28), 1));
        d_Out.add(new ArcOut(d_T.get(21), d_P.get(30), 1));
        d_Out.add(new ArcOut(d_T.get(22), d_P.get(15), 1));
        d_Out.add(new ArcOut(d_T.get(22), d_P.get(31), 1));
        d_Out.add(new ArcOut(d_T.get(22), d_P.get(13), 1)); //??
        d_Out.add(new ArcOut(d_T.get(0), d_P.get(33), 1));
        d_Out.add(new ArcOut(d_T.get(24), d_P.get(1), 1));
        d_Out.add(new ArcOut(d_T.get(25), d_P.get(34), 1));
        d_Out.add(new ArcOut(d_T.get(12), d_P.get(33), 1));
        d_Out.add(new ArcOut(d_T.get(13), d_P.get(14), 1));
        d_Out.add(new ArcOut(d_T.get(23), d_P.get(13), 1));
        d_Out.add(new ArcOut(d_T.get(22), d_P.get(36), 1));
        d_Out.add(new ArcOut(d_T.get(26), d_P.get(2), 1)); //15.02.2021
        
        PetriNet d_Net = new PetriNet("GoUntilParallel", d_P, d_T, d_In, d_Out);
        PetriP.initNext();
        PetriT.initNext();
        ArcIn.initNext();
        ArcOut.initNext();

        return d_Net;
    }

}
