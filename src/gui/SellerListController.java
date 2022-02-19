package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService sellerService;

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;

	@FXML
	private TableColumn<Seller, String> tableColumnEmail;

	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;

	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	private ObservableList<Seller> observableList;

	@FXML
	private Button btnNewSeller;

	public void onBtNewSellerAction(ActionEvent event) {
		System.out.println("onBtNewSellerAction");
	}

	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	@Override
	public void onDataChange() {
		updateTableView();
	}

	@Override
	public void initialize(URL uri, ResourceBundle resourceBundle) {
		initilizeNodes();
	}

	public void updateTableView() {
		if (sellerService == null) {
			throw new IllegalStateException("Service was null.");
		}
		List<Seller> list = sellerService.findAll();
		observableList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(observableList);
		initEditButtons();
		initRemoveButtons();
	}

	private void initilizeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
//		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
//		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("BirthDate"));
//		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("BaseSalary"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Edit");

			@Override
			protected void updateItem(Seller seller, boolean empty) {
				super.updateItem(seller, empty);

				if (seller == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> {
				});
			}

		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Remove");

			@Override
			protected void updateItem(Seller seller, boolean empty) {
				super.updateItem(seller, empty);

				if (seller == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(seller));
			}

		});
	}

	protected void removeEntity(Seller seller) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		if (result.get() == ButtonType.OK) {
			if (sellerService == null) {
				throw new IllegalStateException("Service was null.");
			}
			try {
				sellerService.remove(seller);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlerts("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}