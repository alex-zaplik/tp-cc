package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.DesktopLauncher;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainView extends FXView {

	public MainView(Stage stage) {
		super(stage);
	}

	protected void createWindow() {
		// TODO: Get the image to show up
		ImageView logoView = new ImageView(new Image("file:/test.png"));
		logoView.setCache(true);
		add(logoView, 0, 0, 1, 3);

		Button start = new Button("Start");
		start.setMinWidth(100);
		start.setOnAction(event -> DesktopLauncher.changeRoot(stage, DesktopLauncher.loginView));
		add(start, 1, 0);

		Button help = new Button("Help");
		help.setMinWidth(100);
		help.setOnAction(event -> DesktopLauncher.changeRoot(stage, DesktopLauncher.helpView));
		add(help, 1, 1);

		Button exit = new Button("Exit");
		exit.setMinWidth(100);
		exit.setOnAction(event -> Platform.exit());
		add(exit, 1, 2);
	}

	@Override
	public void handleInput(String msg) {

	}
}
