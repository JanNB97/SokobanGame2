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
