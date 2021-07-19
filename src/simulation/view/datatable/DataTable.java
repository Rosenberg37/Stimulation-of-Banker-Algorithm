package simulation.view.datatable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DataTable extends JTable
{
    public DataTable()
    {
        super(new DefaultTableModel(1, 2));
        title = new String[]{"Allocation", "Available"};
        ((DefaultTableModel) getModel()).setColumnIdentifiers(title);
    }

    protected DataTable(int numProgresses, int numResources, JTable tableAllocation, JTable tableAvailable, String[] title)
    {
        super(new DefaultTableModel(1, title.length));
        ((DefaultTableModel) getModel()).setColumnIdentifiers(title);
        this.title = title;
        this.numProgresses = numProgresses;
        this.numResources = numResources;
        this.tableAllocation = tableAllocation;
        this.tableAvailable = tableAvailable;
        initialTablePID();
        setRowHeight(0, getRowHeight() * numProgresses);
    }

    @Override
    public final boolean isCellEditable(int row, int column)
    {
        return false;
    }

    protected final void initialTablePID()
    {
        var dataTableModel = new DefaultTableModel(numProgresses, 1);
        for (int i = 0; i < numProgresses; i++)
            dataTableModel.setValueAt(i, i, 0);
        this.tablePID = new JTable(dataTableModel);
    }

    protected String[] title;
    protected int numProgresses;
    protected int numResources;
    protected JTable tablePID;
    protected JTable tableAllocation;
    protected JTable tableAvailable;
}