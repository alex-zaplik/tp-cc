package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.net.Client;
import edu.pwr.tp.game.desktop.views.fx.game_view_elements.GUIBoard;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PartyView extends FXView {

	public PartyView(Stage stage) {
		super(stage);
	}

	@Override
	protected void createWindow() {
		Button start = new Button("Start");
		start.setMinWidth(200);
		start.setOnAction(e -> {
			Client.getInstance().startGame();
			//TODO: wait for all players and get from server number of players and ID of player (0-5)
			int mockPlayersCount = 6;
			int mockPlayerIndex = 0;

			GUIBoard board = new GUIBoard(mockPlayersCount, mockPlayerIndex);
			add(board,0,0);

			// 		 Client.getInstance().sendDone(true);
		});
		add(start, 0, 0);
	}

	@Override
	public void handleInput(String msg) {
		System.out.println(msg);
	}
}
