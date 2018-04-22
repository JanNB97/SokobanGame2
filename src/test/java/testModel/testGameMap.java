package testModel;

import model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class testGameMap
{
    @Test
    public void getReachableMeadows()
    {
        GameMap gameMap = new GameMap(4, 5);

        gameMap.addBlock(new Meadow(1, 1));
        gameMap.addBlock(new Meadow(2, 1));
        gameMap.addBlock(new Meadow(1, 2));
        gameMap.addBlock(new Meadow(1, 3));

        Meadow boxMeadow = (Meadow)gameMap.getBlock(1, 2);
        boxMeadow.setBox(new Box(boxMeadow));

        Meadow playerMeadow = (Meadow)gameMap.getBlock(2, 1);
        Player player = new Player(playerMeadow);
        playerMeadow.setPlayer(player);
        gameMap.setPlayer(player);

        ArrayList<Meadow> reachableMeadows = gameMap.getReachableMeadows(playerMeadow);

        ArrayList<Meadow> excepted = new ArrayList<>();
        excepted.add(new Meadow(2, 1));
        excepted.add(new Meadow(1, 1));

        Assert.assertEquals(excepted, reachableMeadows);
    }

    @Test
    public void getAllMoves()
    {
        GameMap gameMap = new GameMap(4, 5);

        gameMap.addBlock(new Meadow(1, 1));
        gameMap.addBlock(new Meadow(2, 1));
        gameMap.addBlock(new Meadow(1, 2));
        gameMap.addBlock(new Meadow(1, 3));

        Meadow boxMeadow = (Meadow)gameMap.getBlock(1, 2);
        boxMeadow.setBox(new Box(boxMeadow));

        Meadow playerMeadow = (Meadow)gameMap.getBlock(2, 1);
        Player player = new Player(playerMeadow);
        playerMeadow.setPlayer(player);
        gameMap.setPlayer(player);

        ArrayList<Move> possilbeMoves = gameMap.getAllMoves();

        ArrayList<Move> expectedMoves = new ArrayList<>();
        expectedMoves.add(new Move(1, 1, 1, 2));

        Assert.assertEquals(expectedMoves, possilbeMoves);
    }
}
