package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.views.IView;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class FXView extends GridPane implements IView {

	Stage stage;

	FXView(Stage stage) {
		this.stage = stage;

		setPadding(new Insets(25, 25, 25, 25));
		setVgap(10);
		setHgap(10);
		setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));

		createWindow();
	}

	protected abstract void createWindow();
}
