package gameMapGenerator;

import enums.Direction;
import model.*;

import java.util.ArrayList;

public class GameMapTester
{
    public static boolean solutionExists(GameMap gameMap)
    {
        if(isDeadAtStart(gameMap))
        {
            return false;
        }
        else
        {
            return foundSolution(new ArrayList<>(), gameMap);
        }
    }

    private static boolean foundSolution(ArrayList<GameMap> visitedGameMaps, GameMap gameMap)
    {
        if(gameMap.isFinished())
        {
            return true;
        }

        for(Move move : gameMap.getAllMoves())
        {
            GameMap gameMapClone = new GameMap(gameMap);

            gameMapClone.doMove(move);

            Block box = gameMapClone.getBlockInDirection(move.getX(), move.getY(), move.getDirection(), 2);

            if(boxNowInDeadSituation(gameMapClone, box) == false && visitedGameMaps.contains(gameMapClone) == false)
            {
                visitedGameMaps.add(gameMapClone);

                if(foundSolution(visitedGameMaps, gameMapClone))
                {
                    return true;
                }
            }
        }

        return false;
    }

    // Dead situation with static objects
    public static boolean isDeadAtStart(GameMap gameMap)
    {
        ArrayList<Meadow> boxDestinationMeadows = gameMap.getMeadowsWithBoxDestination();

        /*for(Meadow meadow : boxDestinationMeadows)
        {
            if(meadow.getBox() == null)
            {
                if(gameMap.getBlockInDirection(meadow, Direction.NORTH) instanceof Wall
                        && gameMap.getBlockInDirection(meadow, Direction.SOUTH) instanceof Wall)
                {
                    if(gameMap.getBlockInDirection(meadow, Direction.WEST) instanceof Wall
                            && gameMap.getBlockInDirection(meadow, Direction.EAST).isFree()
                            && gameMap.getBlockInDirection(meadow, Direction.EAST, 2) instanceof Wall)
                    {
                        return true;
                        //TODO
                    }
                }
            }
        }*/

        for(int i = 0; i < gameMap.getMaxHeight(); i++)
        {
            for(int j = 0; j < gameMap.getMaxWidth(); j++)
            {
                Block block = gameMap.getBlock(j, i);

                if(block instanceof Meadow && ((Meadow) block).getBox() != null)
                {
                    if(boxNowInDeadSituation(gameMap, block))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean boxNowInDeadSituation(GameMap gameMap, Block block)
    {
        if(boxInWallTrianlge(gameMap, block, Direction.NORTH)
                || boxInWallTrianlge(gameMap, block, Direction.EAST)
                || boxInWallTrianlge(gameMap, block, Direction.SOUTH)
                || boxInWallTrianlge(gameMap, block, Direction.WEST))
        {
            return true;
        }

        if(boxInWallReactangle(gameMap, block, Direction.NORTH)
                || boxInWallReactangle(gameMap, block, Direction.EAST)
                || boxInWallReactangle(gameMap, block, Direction.SOUTH)
                || boxInWallReactangle(gameMap, block, Direction.WEST))
        {
            return true;
        }

        //TODO: Box at long wall

        return false;
    }

    private static boolean boxInWallTrianlge(GameMap gameMap, Block block, Direction direction)
    {
        if((block.hasFinishedBox() == false
                && gameMap.getBlockInDirection(block, direction) instanceof Wall
                && gameMap.getBlockInDirection(block, direction.getClockwise()) instanceof Wall))
        {
            return true;
        }

        return false;
    }

    private static boolean boxInWallReactangle(GameMap gameMap, Block block, Direction direction)
    {
        Block[] blocks = new Block[3];

        blocks[0] = gameMap.getBlockInDirection(block, direction);
        blocks[1] = gameMap.getBlockInDirection(blocks[0], direction.getClockwise());
        blocks[2] = gameMap.getBlockInDirection(block, direction.getClockwise());

        boolean finishedBox = block.hasFinishedBox();
        boolean unfinishedBox = false;

        for(Block b : blocks)
        {
            if(b.isFree())
            {
                return false;
            }

            if(b.hasFinishedBox())
            {
                finishedBox = true;
            }
            else if(b instanceof Meadow)
            {
                unfinishedBox = true;
            }
        }

        if(finishedBox == true && unfinishedBox == false)
        {
            return false;
        }

        return true;
    }
}
