package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {

	private Department entityDepartment;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelMsgErrorName;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	public void onBtnSaveAction() {
		System.out.println("onBtnSaveAction");
	}

	public void onbtnCancelAction() {
		System.out.println("onbtnCancelAction");
	}

	public TextField getTxtId() {
		return txtId;
	}

	public TextField getTxtName() {
		return txtName;
	}

	public Label getLabelMsgErrorName() {
		return labelMsgErrorName;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setEntityDepartment(Department entityDepartment) {
		this.entityDepartment = entityDepartment;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void updateFormDate() {
		if (entityDepartment == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entityDepartment.getId()));
		txtName.setText(entityDepartment.getName());
	}
}