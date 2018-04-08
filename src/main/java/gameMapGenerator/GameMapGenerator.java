package gameMapGenerator;

import model.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

public class GameMapGenerator
{
    public static GameMap generate(int maxWidth, int maxHeight, int noOfBoxes, int noOfMeadows)
    {
        GameMap gameMap = new GameMap(maxWidth, maxHeight);

        builtWallBorder(gameMap, maxWidth, maxHeight);
        ArrayList<Meadow> allMeadows = addMeadows(gameMap, noOfMeadows, maxWidth, maxHeight);
        removeWallBorder(gameMap, maxWidth, maxHeight);

        builtWallAroundMeadow(gameMap, maxWidth, maxHeight);

        setPlayer(gameMap, allMeadows);
        setBoxAndDestinations(gameMap, maxWidth, maxHeight, allMeadows, noOfBoxes);

        return gameMap;
    }

    private static ArrayList<Meadow> addMeadows(GameMap gameMap, int noOfMeadows, int maxWidth, int maxHeight)
    {
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

    private static void builtWallBorder(GameMap gameMap, int maxWidth, int maxHeight)
    {
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

    private static void removeWallBorder(GameMap gameMap, int maxWidth, int maxHeight)
    {
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

    private static void builtWallAroundMeadow(GameMap gameMap, int maxWidth, int maxHeight)
    {
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

    private static void setBoxAndDestinations(GameMap gameMap, int maxWidth, int maxHeight, ArrayList<Meadow> allMeadows, int noOfBoxes)
    {
        ArrayList<Meadow> possibleMeadows = new ArrayList<>(allMeadows);
        possibleMeadows.remove(gameMap.getPlayer().getMeadow());

        Random random = new Random();

        for(int i = 0; i < noOfBoxes; i++)
        {
            int r = random.nextInt(possibleMeadows.size());

            Meadow meadow = possibleMeadows.get(r);
            meadow.setBox(new Box(meadow));

            possibleMeadows.remove(meadow);

            r = random.nextInt(possibleMeadows.size());
            meadow = possibleMeadows.get(r);
            meadow.setBoxDestination(new BoxDestination(meadow));

            possibleMeadows.remove(meadow);
        }
    }
}
