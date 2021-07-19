package simulation.support;

import simulation.view.VectorTable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Vector implements Cloneable, JTabulationable
{
    private List<Integer> list;

    public Vector()
    {
        list = new ArrayList<Integer>();
    }

    public Vector(int size)
    {
        list = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            list.add(0);
    }

    public void add(int element)
    {
        list.add(element);
    }

    //remove the last element of a Vector
    public void del()
    {
        list.remove(size() - 1);
    }

    public Integer get(int index)
    {
        return list.get(index);
    }

    public Integer set(int index, int element)
    {
        return list.set(index, element);
    }

    public void set(int element)
    {
        for (int i = 0; i < size(); i++)
            set(i, element);
    }

    public int size()
    {
        return list.size();
    }

    public Vector plus(Vector rhs)
    {
        if (size() != rhs.size()) throw new IllegalArgumentException("Size Unmatched");
        for (int i = 0; i < rhs.size(); i++)
            set(i, get(i) + rhs.get(i));
        return this;
    }

    public Vector substract(Vector rhs)
    {
        if (size() != rhs.size()) throw new IllegalArgumentException("Size Unmatched");
        for (int i = 0; i < rhs.size(); i++)
            set(i, get(i) - rhs.get(i));
        return this;
    }

    public boolean lessThan(Vector rhs)
    {
        if (size() != rhs.size()) throw new IllegalArgumentException("Size Unmatched");
        boolean isLessThan = true;
        for (int i = 0; i < rhs.size(); i++)
        {
            if (get(i) >= rhs.get(i))
            {
                isLessThan = false;
                break;
            }
        }
        return isLessThan;
    }

    public boolean lessEqual(Vector rhs)
    {
        if (size() != rhs.size()) throw new IllegalArgumentException("Size Unmatched");
        boolean isLessEqual = true;
        for (int i = 0; i < rhs.size(); i++)
        {
            if (get(i) > rhs.get(i))
            {
                isLessEqual = false;
                break;
            }
        }
        return isLessEqual;
    }

    public boolean moreThan(Vector rhs)
    {
        if (size() != rhs.size()) throw new IllegalArgumentException("Size Unmatched");
        boolean isMoreThan = true;
        for (int i = 0; i < rhs.size(); i++)
        {
            if (get(i) <= rhs.get(i))
            {
                isMoreThan = false;
                break;
            }
        }
        return isMoreThan;
    }

    public boolean moreEqual(Vector rhs)
    {
        if (size() != rhs.size()) throw new IllegalArgumentException("Size Unmatched");
        boolean isMoreEqual = true;
        for (int i = 0; i < rhs.size(); i++)
        {
            if (get(i) < rhs.get(i))
            {
                isMoreEqual = false;
                break;
            }
        }
        return isMoreEqual;
    }

    public boolean equals(int x)
    {
        for (int i = 0; i < size(); i++)
        {
            if (get(i) != x) return false;
        }
        return true;
    }

    public boolean equals(Vector rhs)
    {
        for (int i = 0; i < size(); i++)
        {
            if (get(i) != rhs.get(i)) return false;
        }
        return true;
    }

    public boolean isAllZero()
    {
        boolean zero = true;
        for (Integer integer : list)
        {
            if (integer != 0)
            {
                zero = false;
                break;
            }
        }
        return zero;
    }

    @Override
    public Vector clone()
    {
        Vector returnVector = null;
        try
        {
            returnVector = (Vector) super.clone();
            returnVector.list = (List<Integer>) ((ArrayList<Integer>) list).clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return returnVector;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < size(); i++)
        {
            s.append(list.get(i));
            s.append(", ");
        }
        s.deleteCharAt(s.length() - 1);
        s.setCharAt(s.length() - 1, ']');
        return new String(s);
    }

    public JTable toJTable()
    {
        JTable returnTable = new JTable(new VectorTable(this));
        return returnTable;
    }

    public static void main(String[] args)
    {
        var test = new Vector(5);
        for (int i = 0; i < test.size(); i++)
            test.set(i, i);
//        Integer temp = test.get(1);
//        temp = temp + 999;
//        //Packaging class will unpack it self and assign, so temp actually get a new object.
//        System.out.println(test.get(1));
//        var testClone = test.clone();
//        testClone.set(2, 100);
//        System.out.println(test.get(2));
//    	  System.out.println(test.equals(0));
        test.del();
        System.out.print(test.size());
        test.add(100);
//    	  System.out.println(test.equals(0));
        System.out.println(test);
        test.del();
        System.out.println(test);
        test.del();
        System.out.println(test);
    }
}
