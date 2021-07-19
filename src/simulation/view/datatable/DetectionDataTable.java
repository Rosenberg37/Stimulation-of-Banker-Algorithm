package simulation.view.datatable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class DetectionDataTable extends DataTable
{

    public DetectionDataTable(int numProgresses, int numResources, JTable tableAllocation, JTable tableRequest, JTable tableAvailable)
    {
        super(numProgresses, numResources, tableAllocation, tableAvailable, new String[]{"PID", "Allocation", "Request", "Available"});
        this.tableRequest = tableRequest;
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        if (row == 0)
        {
            switch (Title.contains(title[column]))
            {
                case PID -> {
                    return (table, value, isSelected, hasFocus, row1, column1) -> tablePID;
                }
                case ALLOCATION -> {
                    return (table, value, isSelected, hasFocus, row12, column12) -> tableAllocation;
                }
                case AVAILABLE -> {
                    return (table, value, isSelected, hasFocus, row14, column14) -> tableAvailable;
                }
                case REQUEST -> {
                    return (table, value, isSelected, hasFocus, row14, column14) -> tableRequest;
                }
                default -> throw new IllegalArgumentException();
            }
        }
        return null;
    }

    protected JTable tableRequest;
}
