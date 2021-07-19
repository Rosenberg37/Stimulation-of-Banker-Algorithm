package simulation.model;

import simulation.controller.SimulationController;
import simulation.support.DataInter;
import simulation.support.Matrix;
import simulation.support.Vector;
import simulation.view.datatable.AvoidanceDataTable;
import simulation.view.datatable.DataTable;

public class DeadlockAvoidance implements DeadlockManager
{
    public DeadlockAvoidance()
    {
    }

    @Override
    public int getNumProgresses()
    {
        return numProgresses;
    }

    @Override
    public int getNumResources()
    {
        return numResources;
    }

    @Override
    public void setDataFromInter(DataInter inter)
    {
        setData(inter.getNumProgresses(), inter.getNumResources(), inter.getAvailable(), inter.getClaim(), inter.getNeed(), inter.getAllocation());
    }

    @Override
    public DataTable getDataTable()
    {
        return new AvoidanceDataTable(numProgresses, numResources, claim.toJTable(), need.toJTable(), allocation.toJTable(), available.toJTable());
    }

    @Override
    public void setController(SimulationController controller)
    {
        this.controller = controller;
    }

    private void setData(int numProgresses, int numResources, Vector available, Matrix claim, Matrix need, Matrix allocation)
    {
        this.numProgresses = numProgresses;
        this.numResources = numResources;
        this.available = available;
        this.claim = claim;
        this.need = need;
        this.allocation = allocation;
    }

    @Override
    public void requestResources(Vector request, int PID)
    {
        if (responseRequest(request, PID)) outputMessage("Request accepted!");
        else outputMessage("Request denied!");
    }

    @Override
    public void returnResources(Vector vec, int PID)
    {
        if (claim.get(PID).equals(allocation.get(PID)))
        {
            outputMessage("Process" + PID + " finished!");
            available.plus(allocation.get(PID));
            allocation.get(PID).set(0);
        }
        else
        {
            outputMessage("Resources not satisfied!");
        }
    }

    @Override
    public Vector checkStatus()
    {
        if (checkSecurity())
        {
            outputMessage("The status is safe!");
            outputMessage("All of the security series:");
            printSecuritySeries();
        }
        else
        {
            outputMessage("The status is not safe!");
        }
		return new Vector();
    }

    public Vector getNeed(int PID)
    {
        return need.get(PID);
    }

    private boolean responseRequest(Vector request, int PID)
    {
        if (PID >= numProgresses) throw new IndexOutOfBoundsException("PID out of progress number.");
        if (request.size() != numResources) throw new IndexOutOfBoundsException("Request size does not match.");
        if (!request.lessEqual(need.get(PID)))
        {
            outputMessage("Request resource more than the claim");
            return false;
        }
        if (!request.lessEqual(available))
        {
            outputMessage("Request resource more than the available");
            return false;
        }
        available.substract(request);
        allocation.get(PID).plus(request);
        need.get(PID).substract(request);
        boolean isSafe = checkSecurity();
        if (!isSafe)
        {
            available.plus(request);
            allocation.get(PID).substract(request);
            need.get(PID).plus(request);
        }
        return isSafe;
    }

    private boolean checkSecurity()
    {
        Vector work = available.clone();
        Vector finish = new Vector(numProgresses);
        while (true)
        {
            int i = 0;
            for (i = 0; i < numProgresses; i++)
                if (finish.get(i) == 0 && need.get(i).lessEqual(work)) break;
            if (i >= numProgresses) break;
            work.plus(allocation.get(i));
            finish.set(i, 1);
        }
        for (int i = 0; i < numProgresses; i++)
            if (finish.get(i) == 0) return false;
        return true;
    }

    private void printSecuritySeries()
    {
        Vector work = available.clone();
        Vector finish = new Vector(numProgresses);
        Vector res = new Vector();
        int index = 0;
        securityRecurrence(work, finish, res, index);
    }

    private void securityRecurrence(Vector work, Vector finish, Vector res, int index)
    {
        if (index == numProgresses)
        {
            outputMessage(res.toString());
            return;
        }
        for (int i = 0; i < numProgresses; i++)
        {
            if (finish.get(i) == 0 && need.get(i).lessEqual(work))
            {
                work.plus(allocation.get(i));
                finish.set(i, 1);
                res.add(i);
                index++;
                securityRecurrence(work, finish, res, index);
                work.substract(allocation.get(i));
                finish.set(i, 0);
                res.del();
                index--;
            }
        }
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
    private Matrix claim;
    private Matrix need;
    private Matrix allocation;
}
