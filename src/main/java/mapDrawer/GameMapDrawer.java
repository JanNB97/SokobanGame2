package mapDrawer;

import enums.Property;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.*;
import tools.PropertyMan;

import java.io.File;

public class GameMapDrawer
{
    private final VBox mainVBox;
    private final GameMap gameMap;

    private StackPane[][] stackPanes;

    private final Image boxImage = new Image(((File)PropertyMan.getProperty(Property.BOXIMAGE)).toURI().toString());
    private final Image boxDestinationImage = new Image(((File)PropertyMan.getProperty(Property.BOXDESTINATIONIMAGE)).toURI().toString());
    private final Image boxOnFinishImage = new Image(((File)PropertyMan.getProperty(Property.BOXONFINISHIMAGE)).toURI().toString());
    private final Image meadowImage = new Image(((File)PropertyMan.getProperty(Property.MEADOWIMAGE)).toURI().toString());
    private final Image playerImage = new Image(((File)PropertyMan.getProperty(Property.PLAYERIMAGE)).toURI().toString());
    private final Image wallImage = new Image(((File)PropertyMan.getProperty(Property.WALLIMAGE)).toURI().toString());
    private final Image nothingImage = new Image(((File)PropertyMan.getProperty(Property.NOTHINGIMAGE)).toURI().toString());

    public GameMapDrawer(VBox mainVBox, GameMap gameMap)
    {
        this.mainVBox = mainVBox;
        mainVBox.getChildren().remove(0, mainVBox.getChildren().size());
        this.gameMap = gameMap;
        initialize();
    }

    public void draw()
    {
        for(int i = 0; i < gameMap.getMaxHeight(); i++)
        {
            for(int j = 0; j < gameMap.getMaxWidth(); j++)
            {
                Block block = gameMap.getBlock(j, i);

                if(block instanceof OutsideBlock)
                {
                    drawOutsideBlock((OutsideBlock)block);
                }
                else if(block instanceof Wall)
                {
                    drawWall((Wall)block);
                }
                else if(block instanceof Meadow)
                {
                    drawMeadow((Meadow)block);
                }
            }
        }
    }

    private void drawOutsideBlock(OutsideBlock outsideBlock)
    {
        clearStackPane(outsideBlock.getX(), outsideBlock.getY());
        stackPanes[outsideBlock.getY()][outsideBlock.getX()].getChildren().add(new ImageView(nothingImage));
    }

    private void drawWall(Wall wall)
    {
        clearStackPane(wall.getX(), wall.getY());
        stackPanes[wall.getY()][wall.getX()].getChildren().add(new ImageView(wallImage));
    }

    private void drawMeadow(Meadow meadow)
    {
        StackPane stackPane = stackPanes[meadow.getY()][meadow.getX()];

        clearStackPane(meadow.getX(), meadow.getY());
        stackPane.getChildren().add(new ImageView(meadowImage));

        if(meadow.getBoxDestination() != null && meadow.getBox() != null)
        {
            stackPane.getChildren().add(new ImageView(boxOnFinishImage));
        }
        else if(meadow.getBoxDestination() != null)
        {
            stackPane.getChildren().add(new ImageView(boxDestinationImage));
        }
        else if(meadow.getBox() != null)
        {
            stackPane.getChildren().add(new ImageView(boxImage));
        }

        if(meadow.getPlayer() != null)
        {
            stackPane.getChildren().add(new ImageView(playerImage));
        }
    }

    private void clearStackPane(int x, int y)
    {
        stackPanes[y][x].getChildren().remove(0, stackPanes[y][x].getChildren().size());
    }

    private void initialize()
    {
        stackPanes = new StackPane[gameMap.getMaxHeight()][gameMap.getMaxWidth()];

        for(int i = 0; i < gameMap.getMaxHeight(); i++)
        {
            HBox hBox = new HBox();
            mainVBox.getChildren().add(hBox);

            for(int j = 0; j < gameMap.getMaxWidth(); j++)
            {
                StackPane stackPane = new StackPane();
                hBox.getChildren().add(stackPane);
                stackPanes[i][j] = stackPane;
            }
        }
    }
}
