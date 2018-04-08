package model;

public class Box
{
    private final Meadow startMeadow;

    private Meadow meadow;

    public Box(Meadow startMeadow)
    {
        this.startMeadow = startMeadow;

        setMeadow(startMeadow);
    }

    public boolean isAtDestination()
    {
        return meadow.getBoxDestination() != null;
    }

    // --- Getter and Setter ---

    public Meadow getStartMeadow()
    {
        return startMeadow;
    }

    public Meadow getMeadow()
    {
        return meadow;
    }

    public void setMeadow(Meadow meadow)
    {
        if(meadow != this.meadow)
        {
            Meadow buffer = this.meadow;
            this.meadow = meadow;

            if(buffer != null && buffer.getBox() != null)
            {
                buffer.setBox(null);
            }

            if(meadow != null && meadow.getBox() != this)
            {
                meadow.setBox(this);
            }
        }
    }
}
