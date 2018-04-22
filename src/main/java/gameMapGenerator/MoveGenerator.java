package gameMapGenerator;

import model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class MoveGenerator
{
    public static GameMap generate(int maxWidth, int maxHeight, int noOfBoxes, int noOfMeadows, int noOfMoves)
    {
        GameMap gameMap = new GameMap(maxWidth, maxHeight);

        GeneratorTool.builtWallBorder(gameMap);
        ArrayList<Meadow> allMeadows = GeneratorTool.addMeadows(gameMap, noOfMeadows);
        GeneratorTool.removeWallBorder(gameMap);

        GeneratorTool.builtWallAroundMeadow(gameMap);

        GeneratorTool.setPlayer(gameMap, allMeadows);

        GeneratorTool.addBoxesSmart(gameMap, noOfBoxes, allMeadows);

        setBoxDestinations(gameMap, getBoxDestinationPositions(gameMap, noOfMoves));

        return gameMap;
    }

    public static void setBoxDestinations(GameMap gameMap, List<Position> positions)
    {
        if(positions == null)
        {
            Logger.getGlobal().severe("No gameMap found");
            return;
        }

        for(Position position : positions)
        {
            Meadow meadow = (Meadow)gameMap.getBlock(position.getX(), position.getY());
            meadow.setBoxDestination(new BoxDestination(meadow));
        }
    }

    public static List<Position> getBoxDestinationPositions(GameMap gameMap, int noOfMoves)
    {
        ArrayList<GameMap> visitedGameMaps = new ArrayList<>();
        visitedGameMaps.add(gameMap);

        return getBoxDestinationPositions(gameMap, visitedGameMaps, noOfMoves, 0);
    }

    private static List<Position> getBoxDestinationPositions(GameMap gameMap, ArrayList<GameMap> visitedGameMaps, int noOfMoves, int m)
    {
        if(m == noOfMoves)
        {
            return gameMap.getAllBoxPos();
        }

        List<Position> boxDestinationPositions;

        List<Move> allMoves = gameMap.getAllMoves();
        Collections.shuffle(allMoves);

        for(Move move : allMoves)
        {
            GameMap gameMapClone = new GameMap(gameMap);
            gameMapClone.doMove(move);

            if(visitedGameMaps.contains(gameMapClone) == false)
            {
                visitedGameMaps.add(gameMapClone);
            }
            else
            {
                continue;
            }

            boxDestinationPositions = getBoxDestinationPositions(gameMapClone, visitedGameMaps, noOfMoves, m + 1);

            if(boxDestinationPositions != null)
            {
                return boxDestinationPositions;
            }
            else
            {
                while(visitedGameMaps.size() - 1 != m)
                {
                    visitedGameMaps.remove(visitedGameMaps.size() - 1);
                }
            }
        }

        return null;
    }
}
