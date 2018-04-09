package model;

public abstract class Block
{
    private final int x;
    private final int y;

    public Block(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Block)
        {
            return x == ((Block) obj).getX() && y == ((Block) obj).getY();
        }
        else
        {
            return false;
        }
    }

    public boolean hasFinishedBox()
    {
        return this instanceof Meadow
                && ((Meadow) this).getBoxDestination() != null
                && ((Meadow) this).getBox() != null;
    }

    public boolean isFree()
    {
        return this instanceof Meadow && ((Meadow)this).getBox() == null;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
