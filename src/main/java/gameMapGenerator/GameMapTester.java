package gameMapGenerator;

import model.GameMap;
import model.Move;

import java.util.ArrayList;

public class GameMapTester
{
    public static boolean solutionExists(GameMap gameMap)
    {
        return foundSolution(new ArrayList<>(), gameMap, 0);
    }

    private static boolean foundSolution(ArrayList<GameMap> visitedGameMaps, GameMap gameMap, int noOfMoves)
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
                while (visitedGameMaps.size() > noOfMoves)
                {
                    visitedGameMaps.remove(visitedGameMaps.size() - 1);
                }

                visitedGameMaps.add(gameMapClone);

                if(foundSolution(visitedGameMaps, gameMapClone, noOfMoves + 1))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
