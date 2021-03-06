package model;

import enums.Direction;
import java.util.ArrayList;
import java.util.List;
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

    public boolean isFinished()
    {
        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                Block block = getBlock(j, i);

                if(block instanceof Meadow && ((Meadow) block).getBoxDestination() == null && ((Meadow) block).getBox() != null)
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

    public ArrayList<Move> getAllMoves()
    {
        ArrayList<Move> allMoves = new ArrayList<>();

        for(Meadow meadow : getReachableMeadows(player.getMeadow()))
        {
            addAllMoves(meadow, Direction.NORTH, allMoves);
            addAllMoves(meadow, Direction.EAST, allMoves);
            addAllMoves(meadow, Direction.SOUTH, allMoves);
            addAllMoves(meadow, Direction.WEST, allMoves);
        }

        return allMoves;
    }

    // --- Get meadows or blocks ---

    public ArrayList<Meadow> getReachableMeadows(Block block)
    {
        ArrayList<Meadow> a = new ArrayList<>();
        getReachableMeadows(a, block);
        return a;
    }

    public ArrayList<Meadow> getMeadowsWithBoxDestination()
    {
        ArrayList<Meadow> allMeadowsWithBoxDestination = new ArrayList<>();

        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                Block block = getBlock(j, i);

                if(block instanceof Meadow)
                {
                    if(((Meadow) block).getBoxDestination() != null)
                    {
                        allMeadowsWithBoxDestination.add((Meadow)block);
                    }
                }
            }
        }

        return allMeadowsWithBoxDestination;
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

    public Block getBlockInDirection(Block block, Direction direction, int fields)
    {
        return getBlockInDirection(block.getX(), block.getY(), direction, fields);
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

    public List<Position> getAllBoxPos()
    {
        ArrayList<Position> allPositions = new ArrayList<>();

        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                Block block = getBlock(j, i);

                if(block instanceof Meadow && ((Meadow) block).getBox() != null)
                {
                    allPositions.add(new Position(block.getX(), block.getY()));
                }
            }
        }

        return allPositions;
    }


    // --- Overritten methods ---

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof GameMap)
        {
            Player objPlayer = ((GameMap) obj).getPlayer();

            for(int i = 0; i < maxHeight; i++)
            {
                for(int j = 0; j < maxWidth; j++)
                {
                    // Other blocks are of no importance
                    if(allBlocks[i][j] instanceof Meadow)
                    {
                        Meadow block = (Meadow)allBlocks[i][j];
                        Meadow objBlock = (Meadow)((GameMap) obj).getBlock(j, i);

                        // Box Destinations are final
                        if(block.getBox() != null && objBlock.getBox() == null
                                || block.getBox() == null && objBlock.getBox() != null)
                        {
                            return false;
                        }
                    }
                }
            }

            GameMap objGameMap = (GameMap)obj;

            if(player.equals(objPlayer) == false && getReachableMeadows(player.getMeadow()).contains(objPlayer.getMeadow()) == false)
            {
                return false;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < maxHeight; i++)
        {
            for(int j = 0; j < maxWidth; j++)
            {
                Block block = getBlock(j, i);

                if(block instanceof OutsideBlock)
                {
                    builder.append(" ");
                }
                else if(block instanceof Wall)
                {
                    builder.append("O");
                }
                else if(block instanceof Meadow)
                {
                    if(((Meadow) block).getBox() != null)
                    {
                        builder.append("k");
                    }
                    else if(((Meadow) block).getPlayer() != null)
                    {
                        builder.append("p");
                    }
                    else if(((Meadow) block).getBoxDestination() != null)
                    {
                        builder.append(".");
                    }
                    else
                    {
                        builder.append(" ");
                    }
                }
                else
                {
                    builder.append(" ");
                }
            }

            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    // --- Supporting methods ---

    private void getReachableMeadows(ArrayList<Meadow> reachableMeadows, Block block)
    {
        if(block.isFree() && reachableMeadows.contains(block) == false)
        {
            reachableMeadows.add((Meadow)block);

            addReachableMeadows(reachableMeadows, block, Direction.NORTH);
            addReachableMeadows(reachableMeadows, block, Direction.EAST);
            addReachableMeadows(reachableMeadows, block, Direction.SOUTH);
            addReachableMeadows(reachableMeadows, block, Direction.WEST);
        }
    }

    private void addReachableMeadows(ArrayList<Meadow> reachableMeadows, Block block, Direction direction)
    {
        Block blockInDirection = getBlockInDirection(block, direction);
        getReachableMeadows(reachableMeadows, blockInDirection);
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
