package model;

import javafx.scene.image.Image;

public class BoxDestination
{
    private final Meadow meadow;

    public BoxDestination(Meadow meadow)
    {
        this.meadow = meadow;
    }

    public Meadow getMeadow()
    {
        return meadow;
    }
}
