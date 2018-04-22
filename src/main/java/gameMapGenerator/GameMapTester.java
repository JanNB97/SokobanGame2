package gameMapGenerator;

import enums.Direction;
import model.*;

import java.util.ArrayList;

public class GameMapTester
{
    public static boolean solutionExists(GameMap gameMap)
    {
        return foundSolution(new ArrayList<>(), gameMap);

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

            if(DeadSituationDetector.boxNowInDeadSituation(gameMapClone, box) == false && visitedGameMaps.contains(gameMapClone) == false)
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
}
