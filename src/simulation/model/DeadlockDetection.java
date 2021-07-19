package simulation.model;

import simulation.controller.SimulationController;
import simulation.support.DataInter;
import simulation.support.Matrix;
import simulation.support.Vector;
import simulation.view.datatable.DataTable;
import simulation.view.datatable.DetectionDataTable;

public class DeadlockDetection implements DeadlockManager
{
    public DeadlockDetection()
    {
    }

    public int getNumProgresses()
    {
        return numProgresses;
    }

    public int getNumResources()
    {
        return numResources;
    }

    public void setDataFromInter(DataInter inter)
    {
        setData(inter.getNumProgresses(), inter.getNumResources(), inter.getAvailable(), inter.getAllocation(), new Matrix(inter.getNumProgresses(), inter.getNumResources()));
    }

    @Override
    public DataTable getDataTable()
    {
        return new DetectionDataTable(numProgresses, numResources, allocation.toJTable(), requests.toJTable(), available.toJTable());
    }

    public void setController(SimulationController controller)
    {
        this.controller = controller;
    }

    @Override
    public Vector checkStatus()
    {
        Vector work = available.clone();
        Vector finish = new Vector(numProgresses);
        Vector deadLockSeries = new Vector();
        for (int i = 0; i < numProgresses; i++)
        {
            if (allocation.get(i).equals(0))
            {
                finish.set(i, 1);
            }
        }
        while (true)
        {
            int i = 0;
            for (i = 0; i < numProgresses; i++)
            {
                if (finish.get(i) == 0 && requests.get(i).lessEqual(work)) break;
            }
            if (i >= numProgresses) break;
            work.plus(allocation.get(i));
            finish.set(i, 1);
        }
        for (int i = 0; i < numProgresses; i++)
        {
            if (finish.get(i) == 0) deadLockSeries.add(i);
        }
        if (deadLockSeries.size() > 0)
        {
            outputMessage("Deadlock occurred!");
            outputMessage("Participating process includes:");
            outputMessage(deadLockSeries.toString());
        }
        else outputMessage("Deadlock not occurred!");
        return deadLockSeries;
    }

    public void requestResources(Vector request, int PID)
    {
        requests.get(PID).plus(request);
        recoverFromDeadlock(checkStatus());
    }

    public void allocateResources(Vector vec, int PID)
    {
        if (!vec.lessEqual(requests.get(PID)))
        {
            outputMessage("Allocation more than request!");
            return;
        }
        if (!vec.lessEqual(available))
        {
            outputMessage("Allocation more than available!");
            return;
        }
        requests.get(PID).substract(vec);
        allocation.get(PID).plus(vec);
        available.substract(vec);
        outputMessage("Allocation successes!");
    }

    @Override
    public void returnResources(Vector vec, int PID)
    {
        if (!vec.lessEqual(allocation.get(PID)))
        {
            outputMessage("You don't have so many resources to return!");
            return;
        }
        allocation.get(PID).substract(vec);
        available.plus(vec);
    }

    public void recoverFromDeadlock(Vector series)
    {
        if (series.size() == 0) return;
        for (int i = 0; i < series.size(); i++)
        {
            returnResources(allocation.get(series.get(i)), series.get(i));
        }
        outputMessage("The process involved in the deadlock has been deprived of its resources!");
    }

    private void setData(int numProgresses, int numResources, Vector available, Matrix allocation, Matrix requests)
    {
        this.numProgresses = numProgresses;
        this.numResources = numResources;
        this.available = available;
        this.allocation = allocation;
        this.requests = requests;
    }

    private void outputMessage(String message)
    {
        System.out.println(message);
        controller.printMessage(message);
    }

    private SimulationController controller;
    private int numProgresses;
    private int numResources;
    private Vector available;
    private Matrix allocation;
    private Matrix requests;
}
