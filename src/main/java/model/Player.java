package model;

import javafx.application.Platform;

public class Player
{
    private final Meadow startMeadow;

    private Meadow meadow;

    public Player(Meadow startMeadow)
    {
        this.startMeadow = startMeadow;

        setMeadow(startMeadow);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Player)
        {
            return meadow.getX() == ((Player) obj).getMeadow().getX() && meadow.getY() == ((Player) obj).getMeadow().getY();
        }
        else
        {
            return false;
        }
    }

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
        if(this.meadow != meadow)
        {
            Meadow buffer = this.meadow;
            this.meadow = meadow;

            if(buffer != null && buffer.getPlayer() != null)
            {
                buffer.setPlayer(null);
            }

            if(meadow != null && meadow.getPlayer() != this)
            {
                meadow.setPlayer(this);
            }
        }
    }

    public int getX()
    {
        return meadow.getX();
    }

    public int getY()
    {
        return meadow.getY();
    }
}
