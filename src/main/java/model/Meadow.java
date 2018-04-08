package model;

import javafx.scene.image.Image;

public class Meadow extends Block
{
    private Box box;
    private BoxDestination boxDestination;
    private Player player;

    public Meadow(int x, int y)
    {
        super(x, y);
    }

    // --- Getter and Setter ---

    public Box getBox()
    {
        return box;
    }

    public void setBox(Box box)
    {
        if(box != this.box)
        {
            Box buffer = this.box;
            this.box = box;

            if(buffer != null && buffer.getMeadow() != null)
            {
                buffer.setMeadow(null);
            }

            if(box != null && box.getMeadow() != this)
            {
                box.setMeadow(this);
            }
        }
    }

    public BoxDestination getBoxDestination()
    {
        return boxDestination;
    }

    public void setBoxDestination(BoxDestination boxDestination)
    {
        this.boxDestination = boxDestination;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        if(player != this.player)
        {
            Player buffer = this.player;
            this.player = player;

            if(buffer != null && buffer.getMeadow() != null)
            {
                buffer.setMeadow(null);
            }

            if(player != null && player.getMeadow() != this)
            {
                player.setMeadow(this);
            }
        }
    }
}
