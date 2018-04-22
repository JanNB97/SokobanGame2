package testGameMapGenerator;

import gameMapGenerator.DeadSituationDetector;
import gameMapGenerator.TestGenerator;
import model.*;
import org.junit.Assert;
import org.junit.Test;

public class testDeadSituationDetector
{
    @Test
    public void testBlockIsAtDeadWall()
    {
        GameMap gameMap = new GameMap(4, 5);

        gameMap.addBlock(new Meadow(1, 1));
        gameMap.addBlock(new Meadow(2, 1));
        gameMap.addBlock(new Meadow(1, 2));
        gameMap.addBlock(new Meadow(1, 3));
        gameMap.addBlock(new Meadow(2, 3));

        Meadow boxMeadow = new Meadow(2, 2);
        Box box = new Box(boxMeadow);
        gameMap.addBlock(boxMeadow);

        TestGenerator.builtWallAroundMeadow(gameMap);

        Assert.assertTrue(DeadSituationDetector.boxNowInDeadSituation(gameMap, boxMeadow));
    }

    @Test
    public void testBlockIsNotAtDeadWall()
    {
        GameMap gameMap = new GameMap(5, 5);

        gameMap.addBlock(new Meadow(1, 1));
        gameMap.addBlock(new Meadow(2, 1));
        gameMap.addBlock(new Meadow(1, 2));
        gameMap.addBlock(new Meadow(1, 3));
        gameMap.addBlock(new Meadow(2, 3));
        gameMap.addBlock(new Meadow(3, 1));
        gameMap.addBlock(new Meadow(3, 2));
        gameMap.addBlock(new Meadow(3, 3));

        Meadow boxMeadow = new Meadow(2, 2);
        Box box = new Box(boxMeadow);
        gameMap.addBlock(boxMeadow);

        TestGenerator.builtWallAroundMeadow(gameMap);

        Assert.assertFalse(DeadSituationDetector.boxNowInDeadSituation(gameMap, boxMeadow));

        gameMap.addBlock(new Wall(3, 2));
        Assert.assertFalse(DeadSituationDetector.boxNowInDeadSituation(gameMap, boxMeadow));

        gameMap.addBlock(new Wall(3, 1));
        Assert.assertFalse(DeadSituationDetector.boxNowInDeadSituation(gameMap, boxMeadow));

        gameMap.addBlock(new Wall(3, 3));
        Meadow boxDestMeadow = (Meadow)gameMap.getBlock(2, 1);
        boxDestMeadow.setBoxDestination(new BoxDestination(boxDestMeadow));
        Assert.assertFalse(DeadSituationDetector.boxNowInDeadSituation(gameMap, boxMeadow));
    }
}
