package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.net.Client;
import edu.pwr.tp.game.desktop.views.IView;
import edu.pwr.tp.game.desktop.views.fx.game_view_elements.GUIBoard;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class GameView extends GridPane implements IView {

    private Paint playerColor;

    private Stage stage;

    private int playerCount;
    private int playerIndex;

    public GameView(Stage stage, int playerCount, int playerIndex) {
        this.stage = stage;

        setPadding(new Insets(25, 25, 25, 25));
        setVgap(10);
        setHgap(10);
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));

        this.playerCount = playerCount;
        this.playerIndex = playerIndex;
        this.playerColor = playerColor;

        createWindow();
    }

    protected void createWindow() {
        // TODO: Get the index instead of a color
        GUIBoard guiBoard = new GUIBoard(playerCount, Color.grayRgb(100));
        add(guiBoard,0,0);

        Client.getInstance().sendDone(true);
    }

    @Override
    public void handleInput(String msg) {
        System.out.println("GameView: " + msg);
    }
}
