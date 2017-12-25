package edu.pwr.tp.game.desktop.views.fx;

import javafx.stage.Stage;

public class PartyView extends FXView {

	public PartyView(Stage stage) {
		super(stage);
	}

	@Override
	protected void createWindow() {

	}

	@Override
	public void handleInput(String msg) {
		System.out.println(msg);
	}
}
