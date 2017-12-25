package edu.pwr.tp.game.desktop.views.fx;

import edu.pwr.tp.game.desktop.net.Client;
import edu.pwr.tp.game.desktop.net.Party;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.Map;

public class PartyView extends FXView {

	private ObservableList<Party> parties;
	private TableView<Party> partyTable;
	private boolean noAvailable = false;

	public PartyView(Stage stage) {
		super(stage);
	}

	@Override
	protected void createWindow() {
		parties = FXCollections.observableArrayList(p -> new Observable[] {p.nameValueProperty(), p.leftValueProperty(), p.maxValueProperty()});

		setUpTable();

		Button join = new Button("Join");
		join.setOnAction(event -> {
			Client.getInstance().joinParty(partyTable.getSelectionModel().getSelectedItem().getNameValue());
			System.out.println("Joining " + partyTable.getSelectionModel().getSelectedItem().getNameValue() + "...");
		});
		add(join, 0, 1);

		Button create = new Button("Create");
		// TODO: Creating a party (new modal stage window?)
		add(create, 1, 1);
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

		add(partyTable, 0, 0, 2, 1);
	}

	@Override
	public void handleInput(String msg) {
		Map<String, Object> responseMap = Client.getInstance().getParser().parse(msg);

		if (!responseMap.containsKey("s_msg")) {
			String name = (String) responseMap.get("s_name");
			int left = (int) responseMap.get("i_left");
			int max = (int) responseMap.get("i_max");

			parties.add(new Party(name, left, max));
			partyTable.refresh();
		} else if (partyTable != null) {
			partyTable.setPlaceholder(new Label("No parties available :("));
		} else {
			noAvailable = true;
		}
	}
}
