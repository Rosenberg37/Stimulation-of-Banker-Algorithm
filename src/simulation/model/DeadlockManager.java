package simulation.model;

import simulation.controller.SimulationController;
import simulation.support.DataInter;
import simulation.support.Vector;
import simulation.view.datatable.DataTable;

public interface DeadlockManager
{
    int getNumProgresses();

    int getNumResources();

    void setDataFromInter(DataInter inter);

    DataTable getDataTable();

    void setController(SimulationController controller);

    void requestResources(Vector request, int PID);

    void returnResources(Vector vec, int PID);

    Vector checkStatus();
}
