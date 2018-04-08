package gameMapGenerator;

import model.GameMap;
import model.Move;

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

            if(visitedGameMaps.contains(gameMapClone) == false)
            {
                ArrayList<GameMap> visitedGameMapsClone = new ArrayList<>(visitedGameMaps);
                visitedGameMapsClone.add(gameMapClone);

                if(foundSolution(visitedGameMapsClone, gameMapClone))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
