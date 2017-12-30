package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.DesktopLauncher;
import edu.pwr.tp.game.desktop.net.Client;
import edu.pwr.tp.game.desktop.views.IView;
import edu.pwr.tp.game.desktop.views.fx.game_view_elements.GUIBoard;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Map;

public class GameView extends GridPane implements IView {

    private Stage stage;

    private int playerCount;
    private int playerIndex;

    private boolean isMoving = false;
    private GUIBoard board;

    public GameView(Stage stage, int playerCount, int playerIndex) {
        this.stage = stage;

        setPadding(new Insets(25, 25, 25, 25));
        setVgap(10);
        setHgap(10);
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));

        this.playerCount = playerCount;
        this.playerIndex = playerIndex;

        createWindow();
    }

    protected void createWindow() {
        // TODO: Get the index instead of a color
        board = new GUIBoard(playerCount, playerIndex);
        if (isMoving) board.startPlayerTurn();

        add(board,0,0);

        Client.getInstance().sendDone(true);
    }

    @Override
    public void handleInput(String msg) {
        System.out.println("GameView: " + msg);

        Map<String, Object> response = Client.getInstance().parser.parse(msg);
        if (response.containsKey("s_move")) {
            // TODO: Handle jumps and skips

            if (board != null) {
                board.startPlayerTurn();
            } else {
                isMoving = true;
            }
        } else if (response.containsKey("b_valid")) {
                board.getLastPawnMovedByMe().confirmMove((boolean) response.get("b_valid"));
                if((boolean) response.get("b_valid")) board.startEnemyTurn();
        } else if (response.containsKey("i_action")) {
            board.movePawn((int) response.get("i_fx"), (int) response.get("i_fy"), (int) response.get("i_tx"), (int) response.get("i_ty"));
        } else if (response.containsKey("s_disc")) {
            Client.getInstance().disconnect();
            DesktopLauncher.changeRoot(stage, DesktopLauncher.loginView);
        }
    }
}
