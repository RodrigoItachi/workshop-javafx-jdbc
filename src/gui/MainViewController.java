package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	@FXML 
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
<<<<<<< HEAD
		});
=======
		}); 
>>>>>>> 033341d0fa9cbf2e9ab2708c1c2e81e24d976822
	}

	@FXML
	public void onMenuAboutAction() {
<<<<<<< HEAD
		loadView("/gui/About.fxml", x -> {
		});
=======
		loadView("/gui/About.fxml", x -> {});
>>>>>>> 033341d0fa9cbf2e9ab2708c1c2e81e24d976822
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	public synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
<<<<<<< HEAD
=======

			T controller = loader.getController();
			initializingAction.accept(controller);
>>>>>>> 033341d0fa9cbf2e9ab2708c1c2e81e24d976822
			
			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlerts("IO Exception", "Error loanding view", e.getMessage(), AlertType.ERROR);
		}
	}

}