package simulation.support;

import java.util.Random;

public class Rand
{
    public static Random r = new Random();

    public static final Vector getRandomVector(int size, int maxValue)
    {
        Vector result = new Vector(size);
        for (int i = 0; i < size; i++)
            result.set(i, r.nextInt(maxValue));
        return result;
    }

    public static final Vector[] randomSplitVector(Vector vector, int numParts)
    {
        Vector[] result = new Vector[numParts];
        for (int i = 0; i < numParts; i++)
            result[i] = new Vector(vector.size());
        for (int i = 0; i < vector.size(); i++)
        {
            int[] splitNum = randomSplitInteger(vector.get(i), numParts);
            for (int j = 0; j < splitNum.length; j++)
                result[j].set(i, splitNum[j]);
        }
        return result;
    }

    public static final int[] randomSplitInteger(int num, int numParts)
    {
        int left = num;
        int[] returnArray = new int[numParts];

        for (int i = 0; i < numParts - 1; i++)
        {
            int r = (int) Math.round(Math.random() * left);
            left -= r;
            returnArray[i] = r;
        }
        returnArray[numParts - 1] = left;

        return returnArray;
    }
}
