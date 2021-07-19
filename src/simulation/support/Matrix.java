package simulation.support;

import simulation.view.MatrixTable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Matrix implements JTabulationable
{
    private final List<Vector> vectors;

    public Matrix(int numRow, int numCol)
    {
        vectors = new ArrayList<>(numRow);
        for (int i = 0; i < numRow; i++)
            vectors.add(new Vector(numCol));
    }

    public Vector get(int index)
    {
        return vectors.get(index);
    }

    public Integer get(int indexRow, int indexCol)
    {
        return vectors.get(indexRow).get(indexCol);
    }

    public Vector set(int index, Vector vector)
    {
        return vectors.set(index, vector.clone());
    }

    public int numRows()
    {
        return vectors.size();
    }

    public int numCols()
    {
        return vectors.get(0).size();
    }

    public void print()
    {
        for (int i = 0; i < numRows(); i++)
            System.out.println(get(i).toString());
    }

    @Override
    public JTable toJTable()
    {
        JTable returnTable = new JTable(new MatrixTable(this));
        return returnTable;
    }
}
