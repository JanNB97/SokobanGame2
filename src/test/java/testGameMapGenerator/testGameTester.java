package testGameMapGenerator;

import gameMapGenerator.GameMapGenerator;
import gameMapGenerator.GameMapTester;
import model.*;
import org.junit.Assert;
import org.junit.Test;

public class testGameTester
{
    @Test
    public void solutionExists()
    {
        GameMap gameMap = new GameMap(7, 7);

        gameMap.addBlock(new Meadow(1, 2));
        gameMap.addBlock(new Meadow(2, 2));
        gameMap.addBlock(new Meadow(3, 1));
        gameMap.addBlock(new Meadow(4, 1));
        gameMap.addBlock(new Meadow(3, 2));
        gameMap.addBlock(new Meadow(4, 2));
        gameMap.addBlock(new Meadow(4, 3));
        gameMap.addBlock(new Meadow(3, 4));
        gameMap.addBlock(new Meadow(4, 4));
        gameMap.addBlock(new Meadow(5, 4));
        gameMap.addBlock(new Meadow(4, 5));
        gameMap.addBlock(new Meadow(5, 3));

        Meadow boxMeadow = (Meadow)gameMap.getBlock(3, 2);
        boxMeadow.setBox(new Box(boxMeadow));

        Meadow playerMeadow = (Meadow)gameMap.getBlock(1, 2);
        Player player = new Player(playerMeadow);
        playerMeadow.setPlayer(player);
        gameMap.setPlayer(player);

        Meadow boxDestinationMeadow = (Meadow)gameMap.getBlock(3, 4);
        boxDestinationMeadow.setBoxDestination(new BoxDestination(boxDestinationMeadow));

        GameMapGenerator.builtWallAroundMeadow(gameMap);

        Assert.assertTrue(GameMapTester.solutionExists(gameMap));
    }
}
