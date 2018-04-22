package gameMapGenerator;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneratorTool
{
    public static void setPlayer(GameMap gameMap, ArrayList<Meadow> allMeadows)
    {
        Random random = new Random();
        int i = random.nextInt(allMeadows.size());

        Meadow meadow = allMeadows.get(i);

        Player player = new Player(meadow);
        gameMap.setPlayer(player);
    }

    public static ArrayList<Meadow> addMeadows(GameMap gameMap, int noOfMeadows)
    {
        int maxHeight = gameMap.getMaxHeight();
        int maxWidth = gameMap.getMaxWidth();

        ArrayList<Meadow> allMeadows = new ArrayList<>();

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

    public static void addBoxes(GameMap gameMap, int noOfBoxes, ArrayList<Meadow> allMeadows)
    {
        List<Meadow> possibleMeadows = allMeadows.stream().filter(m -> m.getBox() == null
                && m.getPlayer() == null
                && m.getBoxDestination() == null).collect(Collectors.toList());

        Random random = new Random();

        for(int i = 0; i < noOfBoxes; i++)
        {
            Meadow meadow = possibleMeadows.get(random.nextInt(possibleMeadows.size()));
            new Box(meadow);

            possibleMeadows.remove(meadow);
        }
    }

    public static void addBoxesSmart(GameMap gameMap, int noOfBoxes, ArrayList<Meadow> allMeadows)
    {
        List<Meadow> possibleMeadows = allMeadows.stream().filter(m -> m.getBox() == null
                && m.getPlayer() == null
                && m.getBoxDestination() == null).collect(Collectors.toList());

        Random random = new Random();

        for(int i = 0; i < noOfBoxes; i++)
        {
            addBoxSmart(gameMap, possibleMeadows);
        }
    }

    private static void addBoxSmart(GameMap gameMap, List<Meadow> possibleMeadows)
    {
        Random random = new Random();
        ArrayList<Integer> usedRandoms = new ArrayList<>();

        Meadow meadow;

        do {
            if(usedRandoms.size() != 0)
            {
                possibleMeadows.get(usedRandoms.get(usedRandoms.size() - 1)).setBox(null);
            }

            int r = random.nextInt(possibleMeadows.size());
            usedRandoms.add(r);

            meadow = possibleMeadows.get(r);
            new Box(meadow);

        }while (DeadSituationDetector.boxNowInDeadSituationWithoutWall(gameMap, meadow) && usedRandoms.size() != possibleMeadows.size());

        possibleMeadows.remove(meadow);
    }

    public static void removeAllBoxes(GameMap gameMap, ArrayList<Meadow> allMeadows)
    {
        for(Meadow meadow : allMeadows)
        {
            meadow.setBox(null);
        }
    }

    public static void builtWallBorder(GameMap gameMap)
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

    public static void removeWallBorder(GameMap gameMap)
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
}
