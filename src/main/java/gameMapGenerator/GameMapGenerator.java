package gameMapGenerator;

import javafx.application.Platform;
import model.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class GameMapGenerator
{
    public static GameMap generate(int maxWidth, int maxHeight, int noOfBoxes, int noOfMeadows)
    {
        GameMap gameMap = new GameMap(maxWidth, maxHeight);

        builtWallBorder(gameMap);
        ArrayList<Meadow> allMeadows = addMeadows(gameMap, noOfMeadows);
        removeWallBorder(gameMap);

        builtWallAroundMeadow(gameMap);

        setPlayer(gameMap, allMeadows);

        setBoxAndDestinations(gameMap, allMeadows, noOfBoxes);

        return gameMap;
    }

    private static ArrayList<Meadow> addMeadows(GameMap gameMap, int noOfMeadows)
    {
        int maxHeight = gameMap.getMaxHeight();
        int maxWidth = gameMap.getMaxWidth();

        ArrayList<Meadow> allMeadows = new ArrayList<Meadow>();

        Meadow startMeadow = new Meadow(maxWidth / 2, maxHeight / 2);
        gameMap.addBlock(startMeadow);
        allMeadows.add(startMeadow);

        for(int i = 0; i < noOfMeadows - 1; i++)
        {
            ArrayList<OutsideBlock> allBorderOutsideBlocks = gameMap.getBorderOutsideBlocks();

            Random random = new Random();

            OutsideBlock outsideBlock = allBorderOutsideBlocks.get(random.nextInt(allBorderOutsideBlocks.size()));

            Meadow newMeadow = new Meadow(outsideBlock.getX(), outsideBlock.getY());
            gameMap.addBlock(newMeadow);

            allMeadows.add(newMeadow);
        }

        return allMeadows;
    }

    private static void builtWallBorder(GameMap gameMap)
    {
        int maxHeight = gameMap.getMaxHeight();
        int maxWidth = gameMap.getMaxWidth();

        for(int i = 0; i < maxWidth; i++)
        {
            gameMap.addBlock(new Wall(i, 0));
            gameMap.addBlock(new Wall(i, maxHeight - 1));
        }

        for(int i = 0; i < maxHeight; i++)
        {
            gameMap.addBlock(new Wall(0, i));
            gameMap.addBlock(new Wall(maxHeight - 1, i));
        }
    }

    private static void removeWallBorder(GameMap gameMap)
    {
        int maxHeight = gameMap.getMaxHeight();
        int maxWidth = gameMap.getMaxWidth();

        for(int i = 0; i < maxWidth; i++)
        {
            gameMap.addBlock(new OutsideBlock(i, 0));
            gameMap.addBlock(new OutsideBlock(i, maxHeight - 1));
        }

        for(int i = 0; i < maxHeight; i++)
        {
            gameMap.addBlock(new OutsideBlock(0, i));
            gameMap.addBlock(new OutsideBlock(maxHeight - 1, i));
        }
    }

    public static void builtWallAroundMeadow(GameMap gameMap)
    {
        int maxHeight = gameMap.getMaxHeight();
        int maxWidth = gameMap.getMaxWidth();

        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                Block block = gameMap.getBlock(j, i);

                if(block instanceof OutsideBlock && gameMap.meadowIsInRange(block))
                {
                    gameMap.addBlock(new Wall(block.getX(), block.getY()));
                }

            }
        }
    }

    private static void setPlayer(GameMap gameMap, ArrayList<Meadow> allMeadows)
    {
        Random random = new Random();
        int i = random.nextInt(allMeadows.size());

        Meadow meadow = allMeadows.get(i);

        Player player = new Player(meadow);
        gameMap.setPlayer(player);
    }

    private static void setBoxAndDestinations(GameMap gameMap, ArrayList<Meadow> allMeadows, int noOfBoxes)
    {
        ArrayList<Meadow> possibleMeadows = new ArrayList<>(allMeadows);
        possibleMeadows.remove(gameMap.getPlayer().getMeadow());

        Random random = new Random();

        for(int i = 0; i < noOfBoxes; i++)
        {
            Meadow boxMeadow = null;
            Meadow boxDesMeadow = null;

            ArrayList<Integer> rList = new ArrayList<>();

            do {
                if(boxMeadow != null)
                {
                    boxMeadow.setBox(null);
                    boxDesMeadow.setBoxDestination(null);
                }

                if(rList.size() == possibleMeadows.size() * (possibleMeadows.size() - 1))
                {
                    Logger.getGlobal().severe("For box " + (i + 1) + " was no place");
                    return;
                }

                int r1 = random.nextInt(possibleMeadows.size());

                boxMeadow = possibleMeadows.get(r1);
                boxMeadow.setBox(new Box(boxMeadow));

                int r2;
                do {
                    r2 = random.nextInt(possibleMeadows.size());
                }
                while (r2 == r1);

                boxDesMeadow = possibleMeadows.get(r2);
                boxDesMeadow.setBoxDestination(new BoxDestination(boxDesMeadow));

                if(rList.contains(r1*possibleMeadows.size() + r2))
                {
                    continue;
                }
                else
                {
                    rList.add(r1 * possibleMeadows.size() + r2);
                }
            }
            while(GameMapTester.solutionExists(gameMap) == false);

            possibleMeadows.remove(boxMeadow);
            possibleMeadows.remove(boxDesMeadow);
        }
    }
}
