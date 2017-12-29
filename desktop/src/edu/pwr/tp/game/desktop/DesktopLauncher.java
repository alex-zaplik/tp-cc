package edu.pwr.tp.game.desktop;

import edu.pwr.tp.game.desktop.views.fx.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DesktopLauncher extends Application {

	public static MainView mainView;
	public static HelpView helpView;
	public static LoginView loginView;
	public static PartyView partyView;

	public static void changeRoot(Stage stage, Pane root) {
		stage.hide();
		stage.getScene().setRoot(root);
		stage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainView = new MainView(primaryStage);
		helpView = new HelpView(primaryStage);
		loginView = new LoginView(primaryStage);
		partyView = new PartyView(primaryStage);

		primaryStage.setTitle("mojeChineseCheckers");
		//primaryStage.setScene(new Scene(DesktopLauncher.mainView));
		primaryStage.setScene(new Scene(new GameView(primaryStage, 3))); //TODO: testing only. remove the line and uncomment line above
		primaryStage.show();
	}

	public static void main (String[] args) {
		launch(args);
	}
}
