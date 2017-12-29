package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.net.Client;
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

			// TODO: Set up the actual game and do this after finishing:
			// 		 Client.getInstance().sendDone(true);
		});
		add(start, 0, 0);
	}

	@Override
	public void handleInput(String msg) {
		System.out.println(msg);
	}
}
