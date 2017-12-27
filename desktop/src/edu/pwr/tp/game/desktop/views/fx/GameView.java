package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.views.fx.game_view_elements.GUIBoard;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class GameView extends FXView {

    private Paint playerColor;

    @Override
    protected void createWindow() {
        GUIBoard guiBoard = new GUIBoard(6, playerColor); //TODO: 6 is for testing only. change that later
        add(guiBoard,0,0);
    }

    @Override
    public void handleInput(String msg) {

    }

    public GameView(Stage stage, Paint playerColor){
        super(stage);
    }

}
