package main;

import gameMapGenerator.GameMapGenerator;
import gameMapGenerator.GameMapTester;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import mapDrawer.GameMapDrawer;
import model.GameMap;
import model.Meadow;
import model.Move;
import model.Wall;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    @FXML
    private VBox mainVBox;

    private GameMapDrawer gameMapDrawer;
    private GameMap gameMap;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        gameMap = GameMapGenerator.generate(15, 15, 2, 40);
        gameMapDrawer = new GameMapDrawer(mainVBox, gameMap);
        gameMapDrawer.draw();
        new Thread(() -> System.out.println(GameMapTester.solutionExists(gameMap))).start();
    }

    // --- Handle Clicks and Keys ---

    @FXML
    private void handleClickedOnRestart()
    {
        initialize(null, null);
    }

    @FXML
    private void handleClickedOnUndo()
    {
        //TODO
    }

    @FXML
    private void handleTypedOnKey(KeyEvent keyEvent)
    {
        Move move = null;

        int x = gameMap.getPlayer().getX();
        int y = gameMap.getPlayer().getY();

        if(keyEvent.getCharacter().equals("w"))
        {
            move = new Move(x, y, x, y - 1);
        }
        else if(keyEvent.getCharacter().equals("a"))
        {
            move = new Move(x, y, x - 1, y);
        }
        else if(keyEvent.getCharacter().equals("s"))
        {
            move = new Move(x, y, x, y + 1);
        }
        else if(keyEvent.getCharacter().equals("d"))
        {
            move = new Move(x, y, x + 1, y);
        }
        else
        {
            return;
        }

        move.doOn(gameMap);
        gameMapDrawer.draw();
    }
}
