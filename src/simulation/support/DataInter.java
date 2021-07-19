package simulation.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DataInter
{
    private int numProgresses;
    private int numResources;
    private Vector available;
    private Matrix claim;
    private Matrix need;
    private Matrix allocation;

    public DataInter(String filename)
    {
        initialData(Paths.get(filename).toFile());
    }

    public DataInter(File readingFile)
    {
        initialData(readingFile);
    }

    public int getNumProgresses()
    {
        return numProgresses;
    }

    public int getNumResources()
    {
        return numResources;
    }

    public Vector getAvailable()
    {
        return available;
    }

    public Matrix getClaim()
    {
        return claim;
    }

    public Matrix getNeed()
    {
        return need;
    }

    public Matrix getAllocation()
    {
        return allocation;
    }

    private void initialData(File readingFile)
    {
        try
        {
            String DELIMITER = ",|\n|\r|\s";
            Scanner scanner = new Scanner(readingFile);
            scanner.useDelimiter(DELIMITER);

            scanner.next();
            numProgresses = scanner.nextInt();
            scanner.nextLine();
            scanner.next();
            numResources = scanner.nextInt();

            this.available = new Vector(numResources);
            this.claim = new Matrix(numProgresses, numResources);
            this.need = new Matrix(numProgresses, numResources);
            this.allocation = new Matrix(numProgresses, numResources);

            scanner.nextLine();
            scanner.nextLine();
            Vector temp = new Vector(numResources);
            for (int i = 0; i < numProgresses; i++)
            {
                for (int j = 0; j < numResources; j++)
                {
                    temp.set(j, scanner.nextInt());
                }
                claim.set(i, temp);
                scanner.nextLine();
                for (int j = 0; j < numResources; j++)
                {
                    temp.set(j, scanner.nextInt());
                }
                allocation.set(i, temp);
                scanner.nextLine();
                for (int j = 0; j < numResources; j++)
                {
                    temp.set(j, scanner.nextInt());
                }
                need.set(i, temp);
                scanner.nextLine();
                if (i == 0)
                {
                    for (int j = 0; j < numResources; j++)
                    {
                        available.set(j, scanner.nextInt());
                    }
                    scanner.nextLine();
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        DataInter in = new DataInter("testData.csv");
        for (int i = 0; i < 5; i++)
        {
            System.out.println(in.getClaim().get(i).toString());
        }
        System.out.println(in.getAvailable().toString());
    }
}
