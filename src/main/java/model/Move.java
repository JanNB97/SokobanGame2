package model;

import enums.Direction;
import javafx.stage.DirectoryChooser;

import java.util.logging.Logger;

public class Move
{
    private final int x;
    private final int y;
    private final int xEnd;
    private final int yEnd;

    private final Direction direction;

    public Move(int x, int y, int xEnd, int yEnd)
    {
        this.x = x;
        this.y = y;
        this.xEnd = xEnd;
        this.yEnd = yEnd;

        assert Math.abs(x - xEnd) == 1 && Math.abs(y - yEnd) == 0
                || Math.abs(x - xEnd) == 0 && Math.abs(y - yEnd) == 1;

        if(x > xEnd)
        {
            direction = Direction.WEST;
        }
        else if(xEnd > x)
        {
            direction = Direction.EAST;
        }
        else if(yEnd > y)
        {
            direction = Direction.SOUTH;
        }
        else if(y > yEnd)
        {
            direction = Direction.NORTH;
        }
        else
        {
            Logger.getGlobal().severe("No direction found");
            direction = null;
        }
    }

    public boolean isPossibleOn(GameMap gameMap)
    {
        Block startBlock = gameMap.getBlock(x, y);
        Block endBlock = gameMap.getBlock(xEnd, yEnd);
        Block secondBlock = gameMap.getBlockInDirection(x, y, direction, 2);

        switch (direction)
        {
            case NORTH:
                secondBlock = gameMap.getBlock(x, y - 2);
                break;
            case EAST:
                secondBlock = gameMap.getBlock(x + 2, y);
                break;
            case SOUTH:
                secondBlock = gameMap.getBlock(x, y + 2);
                break;
            case WEST:
                secondBlock = gameMap.getBlock(x - 2, y);
                break;
        }

        return isFreeMove(startBlock, endBlock) || isPushMove(startBlock, endBlock, secondBlock);
    }

    public boolean isPushMoveOn(GameMap gameMap)
    {
        return isPushMove(gameMap.getBlock(x, y),
                gameMap.getBlockInDirection(x, y, direction, 1),
                gameMap.getBlockInDirection(x, y, direction, 2));
    }

    // returns true if possible move
    public boolean doOn(GameMap gameMap)
    {
        Block startBlock = gameMap.getBlock(x, y);
        Block endBlock = gameMap.getBlock(xEnd, yEnd);

        if(isFreeMove(startBlock, endBlock))
        {
            ((Meadow)endBlock).setPlayer(gameMap.getPlayer());
            return true;
        }

        Block secondBlock = gameMap.getBlockInDirection(x, y, direction, 2);

        if(isPushMove(startBlock, endBlock, secondBlock))
        {
            ((Meadow)secondBlock).setBox(((Meadow)endBlock).getBox());
            ((Meadow)endBlock).setPlayer(gameMap.getPlayer());

            return true;
        }

        return false;
    }

    // --- Static ---

    public static Move getMove(int x, int y, Direction direction)
    {
        switch (direction)
        {

            case NORTH:
                return new Move(x, y, x, y - 1);
            case EAST:
                return new Move(x, y, x + 1, y);
            case SOUTH:
                return new Move(x, y, x, y + 1);
            case WEST:
                return new Move(x, y, x - 1, y);
        }

        Logger.getGlobal().severe("No direction selected");
        return null;
    }

    // --- Overritten ---

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ") -> (" + xEnd + "," + yEnd + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Move)
        {
            return x == ((Move) obj).getX() && y == ((Move) obj).getY()
                    && xEnd == ((Move) obj).getxEnd() && yEnd == ((Move) obj).getyEnd();
        }
        else
        {
            return false;
        }
    }

    // --- Supporting methods ---

    private boolean isFreeMove(Block startBlock, Block endBlock)
    {
        if(startBlock instanceof Meadow && ((Meadow) startBlock).getPlayer() != null
                && endBlock instanceof Meadow)
        {
            return ((Meadow) endBlock).isFree();
        }
        else
        {
            return false;
        }
    }

    private boolean isPushMove(Block startBlock, Block endBlock, Block secondBlock)
    {
        return startBlock.isFree() && endBlock instanceof Meadow && ((Meadow) endBlock).isFree() == false && secondBlock.isFree();
    }

    // --- Getter and Setter ---

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getxEnd()
    {
        return xEnd;
    }

    public int getyEnd()
    {
        return yEnd;
    }

    public Direction getDirection()
    {
        return direction;
    }
}
