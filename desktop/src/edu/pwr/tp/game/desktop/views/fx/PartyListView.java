package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.DesktopLauncher;
import edu.pwr.tp.game.desktop.net.Client;
import edu.pwr.tp.game.desktop.net.Party;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PartyListView extends FXView {

	private ObservableList<Party> parties;
	private TableView<Party> partyTable;

	private List<String> pending = new ArrayList<>();

	private boolean noAvailable = false;
	private int listSize = 0;
	private boolean gotList = false;
	private volatile boolean serverResponded = false;

	private Runnable waitForServer = () -> {
		long time = System.currentTimeMillis();
		while (!serverResponded) {
			if (time + 15000 < System.currentTimeMillis()) {
				Platform.runLater(() -> {
					Client.getInstance().disconnect();
					displayErrorMessage("Unable to join party");
					DesktopLauncher.changeRoot(stage, DesktopLauncher.loginView);
				});

				Client.getInstance().stopJoining();
					return;
			}
		}

		Platform.runLater(() -> {
			DesktopLauncher.partyView.resolvePending(pending);
			Client.getInstance().changeView(DesktopLauncher.partyView);
				DesktopLauncher.changeRoot(stage, DesktopLauncher.partyView);
		});
	};

	public PartyListView(Stage stage) {
		super(stage);
	}

	@Override
	protected void createWindow() {
		parties = FXCollections.observableArrayList(p -> new Observable[] {p.nameValueProperty(), p.leftValueProperty(), p.maxValueProperty()});

		setUpTable();

		Button join = new Button("Join");
		join.setOnAction(event -> {
			if (!Client.getInstance().isJoining() && partyTable.getSelectionModel().getSelectedItem() != null) {
				Client.getInstance().joinParty(partyTable.getSelectionModel().getSelectedItem().getNameValue());

				new Thread(waitForServer).start();
			}
		});
		add(join, 0, 1);

		Button create = new Button("Create");
		create.setOnAction(event -> {
			if (!Client.getInstance().isJoining()) {
				TextInputDialog dialog = new TextInputDialog("name");
				dialog.setTitle("Create a party");
				dialog.setHeaderText("Choose a name for your party");
				dialog.setContentText("Name:");

				Optional<String> result = dialog.showAndWait();
				result.ifPresent(name -> Client.getInstance().sendPartySettings(name, 6));

				new Thread(waitForServer).start();
			}
		});
		add(create, 1, 1);

		Button disconnect = new Button("Disconnect");
		disconnect.setOnAction(event -> {
			Client.getInstance().disconnect();
			DesktopLauncher.changeRoot(stage, DesktopLauncher.loginView);
		});
		add(disconnect, 2, 1);
	}

	private void setUpTable() {
		partyTable = new TableView<>();

		if (!noAvailable) partyTable.setPlaceholder(new Label("Loading..."));
		else partyTable.setPlaceholder(new Label("No parties available :("));

		TableColumn<Party, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(p -> {
			if (p.getValue() != null) {
				return new SimpleStringProperty(p.getValue().getNameValue());
			} else {
				return new SimpleStringProperty("<no name>");
			}
		});

		TableColumn<Party, Number> leftCol = new TableColumn<>("Left");
		leftCol.setCellValueFactory(p -> {
			if (p.getValue() != null) {
				return new SimpleIntegerProperty(p.getValue().getLeftValue());
			} else {
				return new SimpleIntegerProperty(0);
			}
		});

		TableColumn<Party, Number> maxCol = new TableColumn<>("Max");
		maxCol.setCellValueFactory(p -> {
			if (p.getValue() != null) {
				return new SimpleIntegerProperty(p.getValue().getMaxValue());
			} else {
				return new SimpleIntegerProperty(0);
			}
		});

		partyTable.getColumns().add(nameCol);
		partyTable.getColumns().add(leftCol);
		partyTable.getColumns().add(maxCol);

		partyTable.setItems(parties);

		add(partyTable, 0, 0, 3, 1);
	}

	@Override
	public void handleInput(String msg) {
		try {
			Map<String, Object> responseMap = Client.getInstance().getParser().parse(msg);

			if (!gotList) {
				if (!responseMap.containsKey("s_msg")) {
					if (listSize == 0) listSize = (int) responseMap.get("i_size");

					String name = (String) responseMap.get("s_name");
					int left = (int) responseMap.get("i_left");
					int max = (int) responseMap.get("i_max");

					parties.add(new Party(name, left, max));
					partyTable.refresh();

					listSize--;
				} else if (partyTable != null) {
					partyTable.setPlaceholder(new Label("No parties available :("));
				} else {
					noAvailable = true;
				}

				gotList = listSize <= 0;
			} else if (!serverResponded) {
				serverResponded = responseMap.containsKey("s_joined");
			} else {
				pending.add(msg);
			}
		} catch (NullPointerException e) {
			// TODO: Disconnect
			System.err.println("Server closed");
			Client.getInstance().disconnect();
		}
	}
}
