package simulation.view;

import simulation.support.Vector;

import javax.swing.table.AbstractTableModel;

public class VectorTable extends AbstractTableModel
{
    Vector data;

    public VectorTable(Vector data)
    {
        this.data = data;
    }

    @Override
    public int getRowCount()
    {
        return 1;
    }

    @Override
    public int getColumnCount()
    {
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data.get(columnIndex);
    }

}
