package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.views.fx.game_view_elements.GUIBoard;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class GameView extends FXView {

    private int playerIndex;

    @Override
    protected void createWindow() {
        GUIBoard guiBoard = new GUIBoard(6, playerIndex); //TODO: 6 is for testing only. change that later
        add(guiBoard,0,0); //TODO: found BUG: createWindow starts earlier than constructor initializates playerIndex!!
        guiBoard.startPlayerTurn();
    }

    @Override
    public void handleInput(String msg) {

    }

    public GameView(Stage stage, int playerIndex){
        super(stage);
        this.playerIndex=playerIndex;
    }

}
