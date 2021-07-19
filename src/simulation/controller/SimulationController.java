package simulation.controller;

import simulation.model.DeadlockAvoidance;
import simulation.model.DeadlockDetection;
import simulation.model.DeadlockManager;
import simulation.support.DataInter;
import simulation.support.Rand;
import simulation.support.Vector;
import simulation.view.GraphicInterface;

import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;


public class SimulationController
{
    SimulationController()
    {
        this.avoidanceManager = new DeadlockAvoidance();
        avoidanceManager.setController(this);
        this.detectionManager = new DeadlockDetection();
        detectionManager.setController(this);
        this.GUI = new GraphicInterface();
        GUI.setController(this);
    }

    public void acceptCommandText(String requestText)
    {
        Object[] analysis = analyzeCommandText(requestText);
        if (analysis[0] == Command.NONE) GUI.printMessage("Command Error!!!");
        else if (analysis[0] == Command.CLEAR) GUI.clearInfoArea();
        else if (analysis[0] == Command.MODE)
        {
            switch ((int) analysis[1])
            {
                case 0 -> {
                    managerMode = Mode.AVOIDANCE;
                    if (dataInitialized) GUI.setDataTable(avoidanceManager.getDataTable());
                    GUI.printMessage("Switch to deadlock avoidance mode");
                }
                case 1 -> {
                    managerMode = Mode.DETECTION;
                    if (dataInitialized) GUI.setDataTable(detectionManager.getDataTable());
                    GUI.printMessage("Switch to deadlock detection mode");
                }
                default -> GUI.printMessage("Mode Code Error !!!");
            }
        }
        else
        {
            switch (managerMode)
            {
                case AVOIDANCE -> {
                    switch ((Command) analysis[0])
                    {
                        case REQUEST -> avoidanceManager.requestResources((Vector) analysis[2], (int) analysis[1]);
                        case RETURN -> avoidanceManager.returnResources(null, (int) analysis[1]);
                        case CHECK -> avoidanceManager.checkStatus();
                        case ALLOCATE, RECOVER -> GUI.printMessage("Current Mode Unsupported Command!");
                        case RAND -> randStimulate(avoidanceManager);
                        default -> GUI.printMessage("Command Error!!!");
                    }
                }
                case DETECTION -> {
                    switch ((Command) analysis[0])
                    {
                        case REQUEST -> detectionManager.requestResources((Vector) analysis[2], (int) analysis[1]);
                        case RETURN -> detectionManager.returnResources((Vector) analysis[2], (int) analysis[1]);
                        case CHECK -> detectionManager.checkStatus();
                        case ALLOCATE -> detectionManager.allocateResources((Vector) analysis[2], (int) analysis[1]);
                        case RAND -> randStimulate(detectionManager);
                        case RECOVER -> detectionManager.recoverFromDeadlock(detectionManager.checkStatus());
                        default -> GUI.printMessage("Command Error!!!");
                    }
                }
            }
        }
        GUI.repaint();
    }

    public void setDataFromInter(DataInter inter)
    {

        try
        {
            avoidanceManager.setDataFromInter(inter);
            detectionManager.setDataFromInter(inter);
            GUI.setDataTable(switch (managerMode)
                                     {
                                         case AVOIDANCE -> avoidanceManager.getDataTable();
                                         case DETECTION -> detectionManager.getDataTable();
                                     });
            dataInitialized = true;
        }
        catch (NullPointerException e)
        {
            GUI.printMessage("Data Setting Failed!!!");
        }
    }

    public void printMessage(String message)
    {
        GUI.printMessage(message);
    }

    private Object[] analyzeCommandText(String requestText)
    {
        Scanner scanner = new Scanner(new StringReader(requestText));
        Object[] result = new Object[0];
        try
        {
            if (!dataInitialized) throw new NoSuchElementException();
            String commandText = scanner.next();
            switch (Command.contains(commandText))
            {
                case REQUEST -> {
                    result = new Object[3];
                    result[0] = Command.REQUEST;
                    result[1] = scanner.nextInt();
                    Vector request = new Vector(avoidanceManager.getNumResources());
                    for (int i = 0; i < request.size(); i++)
                        request.set(i, scanner.nextInt());
                    if (scanner.hasNext()) throw new NoSuchElementException();
                    result[2] = request;
                }
                case RETURN -> {
                    result = new Object[3];
                    result[0] = Command.RETURN;
                    result[1] = scanner.nextInt();
                    if (managerMode == Mode.DETECTION)
                    {
                        Vector returnResources = new Vector(detectionManager.getNumResources());
                        for (int i = 0; i < returnResources.size(); i++)
                            returnResources.set(i, scanner.nextInt());
                        if (scanner.hasNext()) throw new NoSuchElementException();
                        result[2] = returnResources;
                    }
                }
                case CHECK -> {
                    result = new Object[1];
                    result[0] = Command.CHECK;
                }
                case MODE -> {
                    result = new Object[2];
                    result[0] = Command.MODE;
                    result[1] = scanner.nextInt();
                }
                case ALLOCATE -> {
                    result = new Object[3];
                    result[0] = Command.ALLOCATE;
                    result[1] = scanner.nextInt();
                    Vector allocation = new Vector(detectionManager.getNumResources());
                    for (int i = 0; i < allocation.size(); i++)
                        allocation.set(i, scanner.nextInt());
                    if (scanner.hasNext()) throw new NoSuchElementException();
                    result[2] = allocation;
                }
                case RAND -> {
                    result = new Object[1];
                    result[0] = Command.RAND;
                    if (scanner.hasNext()) throw new NoSuchElementException();
                }
                case CLEAR -> {
                    result = new Object[1];
                    result[0] = Command.CLEAR;
                    if (scanner.hasNext()) throw new NoSuchElementException();
                }
                case RECOVER -> {
                    result = new Object[1];
                    result[0] = Command.RECOVER;
                    if (scanner.hasNext()) throw new NoSuchElementException();
                }
                default -> {
                    result = new Object[1];
                    result[0] = Command.NONE;
                }
            }
        }
        catch (NoSuchElementException e)
        {
            result = new Object[1];
            result[0] = Command.NONE;
        }
        finally
        {
            return result;
        }
    }

    private void randStimulate(DeadlockManager manager)
    {
        if (manager.getNumProgresses() == 0)
        {
            printMessage("There aren't any progresses!!!");
            return;
        }

        Vector[] needs = new Vector[manager.getNumProgresses()];
        if (manager instanceof DeadlockAvoidance)
        {
            for (int i = 0; i < manager.getNumProgresses(); i++)
                needs[i] = ((DeadlockAvoidance) manager).getNeed(i);
        }
        else
        {
            for (int i = 0; i < manager.getNumProgresses(); i++)
                needs[i] = Rand.getRandomVector(manager.getNumResources(), 5);
        }

        for (int i = 0; i < manager.getNumProgresses(); i++)
        {
            GUI.printMessage("Progress " + i + ":");
            GUI.printMessage("Need :" + needs[i]);
        }
        GUI.printMessage("------------------------");

        ExecutorService es = Executors.newFixedThreadPool(manager.getNumProgresses());
        for (int i = 0; i < manager.getNumProgresses(); i++)
            es.submit(new SimulationThread(i, needs[i], manager));

        es.shutdown();
    }

    class SimulationThread implements Runnable
    {
        static int terminatedThreadsNum = 0;

        private final int PID;
        private final Vector need;
        private final DeadlockManager manager;
        private final int requestsNum;

        SimulationThread(int PID, Vector need, DeadlockManager manager)
        {
            this.need = need;
            this.PID = PID;
            this.manager = manager;
            requestsNum = Rand.r.nextInt(manager.getNumResources()) + 1;
        }

        @Override
        public void run()
        {
            Vector[] requests = Rand.randomSplitVector(need, requestsNum);
            int requestID = 0;
            for (int i = 0; i < requests.length; i++)
            {
                if (requests[i].isAllZero()) continue;
                try
                {
                    sleep(500 + Rand.r.nextInt(1500 * manager.getNumProgresses() + 1000 * manager.getNumResources()));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                synchronized (GUILock)
                {
                    GUI.printMessage("Progress " + PID + " requesting:");
                    GUI.printMessage("Request " + requestID++ + ":" + requests[i]);
                    manager.requestResources(requests[i], PID);
                    GUI.printMessage("");
                    if (manager instanceof DeadlockDetection && Rand.r.nextInt(3) == 2)
                    {
                        GUI.printMessage("Progress " + PID + " allocating:");
                        ((DeadlockDetection) manager).allocateResources(requests[i], PID);
                        GUI.printMessage("");
                    }
                    GUI.repaint();
                }
            }
            if (manager instanceof DeadlockAvoidance)
            {
                synchronized (GUILock)
                {
                    GUI.printMessage("Progress " + PID + " returning:");
                    manager.returnResources(null, PID);
                    GUI.printMessage("");
                    GUI.repaint();
                }
            }
            if (++terminatedThreadsNum == manager.getNumProgresses())
            {
                GUI.printMessage("Random Stimulation End.");
                terminatedThreadsNum = 0;
            }
        }
    }

    enum Command
    {
        REQUEST, RETURN, RECOVER, CHECK, ALLOCATE, MODE, RAND, CLEAR, NONE;

        public static Command contains(String name)
        {
            Command result = null;
            try
            {
                result = Command.valueOf(name);
            }
            catch (IllegalArgumentException e)
            {
                result = Command.NONE;
            }
            finally
            {
                return result;
            }
        }
    }

    enum Mode
    {
        AVOIDANCE, DETECTION
    }

    private final DeadlockAvoidance avoidanceManager;
    private final DeadlockDetection detectionManager;
    private final GraphicInterface GUI;
    private Mode managerMode = Mode.AVOIDANCE;
    private boolean dataInitialized = false;
    private static final Object GUILock = new Object();

    public static void main(String[] args)
    {
        SimulationController controller = new SimulationController();
    }
}


