package simulation.view.datatable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class AvoidanceDataTable extends DataTable
{
    public AvoidanceDataTable(int numProgresses, int numResources, JTable tableClaim, JTable tableNeed, JTable tableAllocation, JTable tableAvailable)
    {
        super(numProgresses, numResources, tableAllocation, tableAvailable, new String[]{"PID", "Claim", "Need", "Allocation", "Available"});
        this.tableClaim = tableClaim;
        this.tableNeed = tableNeed;

    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        if (row == 0)
        {
            switch (Title.contains(title[column]))
            {
                case PID -> {
                    return (table, value, isSelected, hasFocus, row1, column1) -> super.tablePID;
                }
                case CLAIM -> {
                    return (table, value, isSelected, hasFocus, row12, column12) -> tableClaim;
                }
                case ALLOCATION -> {
                    return (table, value, isSelected, hasFocus, row12, column12) -> tableAllocation;
                }
                case NEED -> {
                    return (table, value, isSelected, hasFocus, row13, column13) -> tableNeed;
                }
                case AVAILABLE -> {
                    return (table, value, isSelected, hasFocus, row14, column14) -> tableAvailable;
                }
                default -> throw new IllegalArgumentException();
            }
        }
        return null;
    }

    protected JTable tableClaim;
    protected JTable tableNeed;
}
