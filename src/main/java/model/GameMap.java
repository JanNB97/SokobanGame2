package model;

import enums.Direction;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GameMap
{
    private final int maxWidth;
    private final int maxHeight;

    private Block[][] allBlocks;
    private Player player;

    public GameMap(GameMap gameMap)
    {
        maxWidth = gameMap.getMaxWidth();
        maxHeight = gameMap.getMaxHeight();

        allBlocks = new Block[maxHeight][maxWidth];

        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                Block blockToCopy = gameMap.getBlock(j, i);

                if(blockToCopy instanceof Wall)
                {
                    allBlocks[i][j] = new Wall(j, i);
                }
                else if(blockToCopy instanceof Meadow)
                {
                    allBlocks[i][j] = new Meadow(j, i);

                    if(((Meadow) blockToCopy).getBox() != null)
                    {
                        ((Meadow)allBlocks[i][j]).setBox(new Box((Meadow)allBlocks[i][j]));
                    }

                    if(((Meadow)blockToCopy).getBoxDestination() != null)
                    {
                        ((Meadow)allBlocks[i][j]).setBoxDestination(new BoxDestination((Meadow)allBlocks[i][j]));
                    }
                }
            }
        }

        Meadow playerMeadow = (Meadow)getBlock(gameMap.getPlayer().getX(), gameMap.getPlayer().getY());
        Player player = new Player(playerMeadow);
        this.player = player;
    }

    public GameMap(int maxWidth, int maxHeight)
    {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;

        allBlocks = new Block[maxHeight][maxWidth];

        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                allBlocks[i][j] = new OutsideBlock(j, i);
            }
        }
    }

    public ArrayList<OutsideBlock> getBorderOutsideBlocks()
    {
        ArrayList<OutsideBlock> allBlocks = new ArrayList<>();

        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                Block block = getBlock(j, i);

                if(block instanceof OutsideBlock && meadowIsNeighboor(block))
                {
                    allBlocks.add((OutsideBlock)block);
                }
            }
        }

        return allBlocks;
    }

    public boolean meadowIsInRange(Block block)
    {
        return getBlock(block.getX() + 1, block.getY() + 1) instanceof Meadow
                || getBlock(block.getX() - 1, block.getY() - 1) instanceof Meadow
                || getBlock(block.getX() + 1, block.getY() - 1) instanceof Meadow
                || getBlock(block.getX() - 1, block.getY() + 1) instanceof Meadow
                || meadowIsNeighboor(block);
    }

    public boolean meadowIsNeighboor(Block block)
    {
        return getBlock(block.getX(), block.getY() + 1) instanceof Meadow
                || getBlock(block.getX() + 1, block.getY()) instanceof Meadow
                || getBlock(block.getX(), block.getY() - 1) instanceof Meadow
                || getBlock(block.getX() - 1, block.getY()) instanceof Meadow;
    }

    public Block getBlockInDirection(Block block, Direction direction)
    {
        switch (direction)
        {
            case NORTH:
                return getBlock(block.getX(), block.getY() - 1);
            case EAST:
                return getBlock(block.getX() + 1, block.getY());
            case SOUTH:
                return getBlock(block.getX(), block.getY() + 1);
            case WEST:
                return getBlock(block.getX() - 1, block.getY());
        }

        Logger.getGlobal().severe("No direction selected");
        return null;
    }

    public Block getBlockInDirection(int x, int y, Direction direction, int fields)
    {
        switch (direction)
        {
            case NORTH:
                return getBlock(x, y - fields);
            case EAST:
                return getBlock(x + fields, y);
            case SOUTH:
                return getBlock(x, y + fields);
            case WEST:
                return getBlock(x - fields, y);
        }

        Logger.getGlobal().severe("No direction selected");
        return null;
    }

    public boolean isFinished()
    {
        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                Block block = getBlock(j, i);

                if(block instanceof Meadow && ((Meadow) block).getBoxDestination() != null && ((Meadow) block).getBox() == null)
                {
                    return false;
                }
            }
        }

        return true;
    }

    // --- Moves ---

    public boolean isPossibleMove(Move move)
    {
        return move.isPossibleOn(this);
    }

    public boolean doMove(Move move)
    {
        return move.doOn(this);
    }

    public ArrayList<Meadow> getReachableMeadows(ArrayList<Meadow> reachableMeadows, Block block)
    {
        if(block.isFree())
        {
            reachableMeadows.add((Meadow)block);

            addReachableMeadows(reachableMeadows, block, Direction.NORTH);
            addReachableMeadows(reachableMeadows, block, Direction.EAST);
            addReachableMeadows(reachableMeadows, block, Direction.SOUTH);
            addReachableMeadows(reachableMeadows, block, Direction.WEST);
        }

        return reachableMeadows;
    }

    public ArrayList<Move> getAllMoves()
    {
        ArrayList<Move> allMoves = new ArrayList<>();

        for(Meadow meadow : getReachableMeadows(new ArrayList<>(), player.getMeadow()))
        {
            addAllMoves(meadow, Direction.NORTH, allMoves);
            addAllMoves(meadow, Direction.EAST, allMoves);
            addAllMoves(meadow, Direction.SOUTH, allMoves);
            addAllMoves(meadow, Direction.WEST, allMoves);
        }

        return allMoves;
    }

    // --- Overritten methods ---

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof GameMap)
        {
            if(player.equals(((GameMap) obj).getPlayer()) == false)
            {
                return false;
            }

            for(int i = 0; i < maxHeight; i++)
            {
                for(int j = 0; j < maxWidth; j++)
                {
                    // Other blocks are of no importance

                    if(allBlocks[i][j] instanceof Meadow)
                    {
                        Meadow block = (Meadow)allBlocks[i][j];
                        Meadow objBlock = (Meadow)((GameMap) obj).getBlock(j, i);

                        if(block.getBox() != null && objBlock.getBox() == null
                                || block.getBox() == null && objBlock.getBox() != null
                                || block.getBoxDestination() != null && objBlock.getBoxDestination() == null
                                || block.getBoxDestination() == null && objBlock.getBoxDestination() != null)
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }


    // --- Supporting methods ---

    private void addReachableMeadows(ArrayList<Meadow> reachableMeadows, Block block, Direction direction)
    {
        Block blockInDirection = getBlockInDirection(block, direction);

        if(reachableMeadows.contains(blockInDirection) == false)
        {
            reachableMeadows.addAll(getReachableMeadows(reachableMeadows, blockInDirection));
        }
    }

    private boolean isBorderMeadow(Meadow meadow)
    {
        return getBlock(meadow.getX() + 1, meadow.getY()) instanceof OutsideBlock
                || getBlock(meadow.getX() - 1, meadow.getY()) instanceof OutsideBlock
                || getBlock(meadow.getX(), meadow.getY() + 1) instanceof OutsideBlock
                || getBlock(meadow.getX(), meadow.getY() - 1) instanceof OutsideBlock;
    }

    private void addAllMoves(Meadow meadow, Direction direction, ArrayList<Move> allMoves)
    {
        int x = meadow.getX();
        int y = meadow.getY();

        Move nMove = Move.getMove(x, y, direction);

        if(nMove.isPushMoveOn(this))
        {
            allMoves.add(nMove);
        }
    }

    // --- Getter and Setter ---

    public void addBlock(Block block)
    {
        allBlocks[block.getY()][block.getX()] = block;
    }

    public Block getBlock(int x, int y)
    {
        if(x >= 0 && x <= maxWidth - 1 && y >= 0 && y <= maxHeight - 1)
        {
            return allBlocks[y][x];
        }
        else
        {
            return null;
        }
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public int getMaxWidth()
    {
        return maxWidth;
    }

    public int getMaxHeight()
    {
        return maxHeight;
    }
}
