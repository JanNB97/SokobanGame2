package gameMapGenerator;

import enums.Direction;
import enums.SimpleDirection;
import model.Block;
import model.GameMap;
import model.Meadow;
import model.Wall;

import java.util.ArrayList;

public class DeadSituationDetector
{
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

        //TODO - test me
        if(boxIsAtDeadStairs(gameMap, block, Direction.NORTH, SimpleDirection.LEFT)
                || boxIsAtDeadStairs(gameMap, block, Direction.EAST, SimpleDirection.LEFT)
                || boxIsAtDeadStairs(gameMap, block, Direction.SOUTH,SimpleDirection.LEFT)
                || boxIsAtDeadStairs(gameMap, block, Direction.WEST, SimpleDirection.LEFT)
                || boxIsAtDeadStairs(gameMap, block, Direction.NORTH, SimpleDirection.RIGHT)
                || boxIsAtDeadStairs(gameMap, block, Direction.EAST, SimpleDirection.RIGHT)
                || boxIsAtDeadStairs(gameMap, block, Direction.SOUTH, SimpleDirection.RIGHT)
                || boxIsAtDeadStairs(gameMap, block, Direction.WEST, SimpleDirection.RIGHT))
        {
            return true;
        }

        if(boxIsAtDeadWall(gameMap, block, Direction.NORTH)
                || boxIsAtDeadWall(gameMap, block, Direction.EAST)
                || boxIsAtDeadWall(gameMap, block, Direction.SOUTH)
                || boxIsAtDeadWall(gameMap, block, Direction.WEST))
        {
            return true;
        }

        return false;
    }

    public static boolean boxNowInDeadSituationWithoutWall(GameMap gameMap, Block block)
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

        //TODO - test me
        if(boxIsAtDeadStairs(gameMap, block, Direction.NORTH, SimpleDirection.LEFT)
                || boxIsAtDeadStairs(gameMap, block, Direction.EAST, SimpleDirection.LEFT)
                || boxIsAtDeadStairs(gameMap, block, Direction.SOUTH,SimpleDirection.LEFT)
                || boxIsAtDeadStairs(gameMap, block, Direction.WEST, SimpleDirection.LEFT)
                || boxIsAtDeadStairs(gameMap, block, Direction.NORTH, SimpleDirection.RIGHT)
                || boxIsAtDeadStairs(gameMap, block, Direction.EAST, SimpleDirection.RIGHT)
                || boxIsAtDeadStairs(gameMap, block, Direction.SOUTH, SimpleDirection.RIGHT)
                || boxIsAtDeadStairs(gameMap, block, Direction.WEST, SimpleDirection.RIGHT))
        {
            return true;
        }

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

    private static boolean boxIsAtDeadWall(GameMap gameMap, Block boxBlock, Direction direction)
    {
        if(boxBlock.hasFinishedBox()
                || gameMap.getBlockInDirection(boxBlock, direction) instanceof Wall == false)
        {
            return false;
        }
        else
        {
            return boxIsAtDeadWall(gameMap, boxBlock, direction, SimpleDirection.LEFT)
                    && boxIsAtDeadWall(gameMap, boxBlock, direction, SimpleDirection.RIGHT);
        }
    }

    private static boolean boxIsAtDeadWall(GameMap gameMap, Block block, Direction direction, SimpleDirection simpleDirection)
    {
        if(block instanceof Wall)
        {
            return true;
        }
        else
        {
            Meadow meadow = (Meadow)block;

            if(meadow.getBoxDestination() != null && meadow.getBox() == null)
            {
                return false;
            }

            Block wallBlock = gameMap.getBlockInDirection(meadow, direction);

            if(wallBlock instanceof Wall)
            {
                switch (simpleDirection)
                {
                    case LEFT:
                        return boxIsAtDeadWall(gameMap, gameMap.getBlockInDirection(block, direction.getAgainstClockwise()), direction, simpleDirection);
                    case RIGHT:
                        return boxIsAtDeadWall(gameMap, gameMap.getBlockInDirection(block, direction.getClockwise()), direction, simpleDirection);
                    default:
                        return false;
                }
            }
            else
            {
                return false;
            }
        }
    }

    private static boolean boxIsAtDeadStairs(GameMap gameMap, Block block, Direction direction, SimpleDirection simpleDirection)
    {
        Block otherBox = gameMap.getBlockInDirection(block, direction);

        if(otherBox instanceof Meadow && ((Meadow) otherBox).getBox() != null)
        {
            if(otherBox.hasFinishedBox() && ((Meadow)block).hasFinishedBox())
            {
                return false;
            }

            // At least one box is not finished

            Block wall1;
            Block wall2;

            if(simpleDirection == SimpleDirection.LEFT)
            {
                wall1 = gameMap.getBlockInDirection(block, direction.getClockwise());
                wall2 = gameMap.getBlockInDirection(otherBox, direction.getAgainstClockwise());
            }
            else
            {
                wall1 = gameMap.getBlockInDirection(block, direction.getClockwise());
                wall2 = gameMap.getBlockInDirection(otherBox, direction.getAgainstClockwise());
            }

            if(wall1 instanceof Wall && wall2 instanceof Wall)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
