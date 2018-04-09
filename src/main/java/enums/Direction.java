package enums;

public enum Direction
{
    NORTH, EAST, SOUTH, WEST;

    public Direction getClockwise()
    {
        int newOrdinal = ordinal() + 1;

        if(newOrdinal > 3)
        {
            newOrdinal = 0;
        }

        return values()[newOrdinal];
    }

    public Direction getAgainstClockwise()
    {
        int newOrdinal = ordinal() - 1;

        if(newOrdinal < 0)
        {
            newOrdinal = 3;
        }

        return values()[newOrdinal];
    }
}
