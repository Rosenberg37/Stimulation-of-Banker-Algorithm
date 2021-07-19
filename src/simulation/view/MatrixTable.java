package simulation.view;

import simulation.support.Matrix;

import javax.swing.table.AbstractTableModel;

public class MatrixTable extends AbstractTableModel
{
    Matrix data;

    public MatrixTable(Matrix data)
    {
        this.data = data;
    }

    @Override
    public int getRowCount()
    {
        return data.numRows();
    }

    @Override
    public int getColumnCount()
    {
        return data.numCols();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data.get(rowIndex, columnIndex);
    }
}
