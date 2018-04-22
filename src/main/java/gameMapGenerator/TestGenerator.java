package gameMapGenerator;

import javafx.application.Platform;
import model.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class TestGenerator
{
    public static GameMap generate(int maxWidth, int maxHeight, int noOfBoxes, int noOfMeadows)
    {
        GameMap gameMap = new GameMap(maxWidth, maxHeight);

        GeneratorTool.builtWallBorder(gameMap);
        ArrayList<Meadow> allMeadows = GeneratorTool.addMeadows(gameMap, noOfMeadows);
        GeneratorTool.removeWallBorder(gameMap);

        GeneratorTool.builtWallAroundMeadow(gameMap);

        GeneratorTool.setPlayer(gameMap, allMeadows);

        setBoxAndDestinations(gameMap, allMeadows, noOfBoxes);

        return gameMap;
    }

    private static void setBoxAndDestinations(GameMap gameMap, ArrayList<Meadow> allMeadows, int noOfBoxes)
    {
        ArrayList<Meadow> possibleMeadows = new ArrayList<>(allMeadows);
        possibleMeadows.remove(gameMap.getPlayer().getMeadow());

        Random random = new Random();

        for(int i = 0; i < noOfBoxes; i++)
        {
            Meadow boxMeadow = null;
            Meadow boxDesMeadow = null;

            ArrayList<Integer> rList = new ArrayList<>();

            do {
                if(boxMeadow != null)
                {
                    boxMeadow.setBox(null);
                    boxDesMeadow.setBoxDestination(null);
                }

                if(rList.size() == possibleMeadows.size() * (possibleMeadows.size() - 1))
                {
                    Logger.getGlobal().severe("For box " + (i + 1) + " was no place");
                    return;
                }

                int r1 = random.nextInt(possibleMeadows.size());

                boxMeadow = possibleMeadows.get(r1);
                boxMeadow.setBox(new Box(boxMeadow));

                int r2;
                do {
                    r2 = random.nextInt(possibleMeadows.size());
                }
                while (r2 == r1);

                boxDesMeadow = possibleMeadows.get(r2);
                boxDesMeadow.setBoxDestination(new BoxDestination(boxDesMeadow));

                if(rList.contains(r1*possibleMeadows.size() + r2))
                {
                    continue;
                }
                else
                {
                    rList.add(r1 * possibleMeadows.size() + r2);
                }

                if(DeadSituationDetector.boxNowInDeadSituation(gameMap, boxMeadow))
                {
                    continue;
                }
            }
            while(GameMapTester.solutionExists(gameMap) == false);

            possibleMeadows.remove(boxMeadow);
            possibleMeadows.remove(boxDesMeadow);
        }
    }
}
