package simulation.view.datatable;

public enum Title
{
    PID, CLAIM, ALLOCATION, NEED, AVAILABLE, REQUEST;

    public static Title contains(String name)
    {
        return switch (name)
                {
                    case "PID" -> PID;
                    case "Claim" -> CLAIM;
                    case "Allocation" -> ALLOCATION;
                    case "Need" -> NEED;
                    case "Available" -> AVAILABLE;
                    case "Request" -> REQUEST;
                    default -> throw new IllegalArgumentException();
                };
    }
}
