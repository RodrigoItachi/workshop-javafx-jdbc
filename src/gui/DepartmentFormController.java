package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entityDepartment;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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

	public void setEntityDepartment(Department entityDepartment) {
		this.entityDepartment = entityDepartment;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener changeListener) {
		dataChangeListeners.add(changeListener);
	}
	
	public void onBtnSaveAction(ActionEvent event) {
		if (entityDepartment == null) {
			throw new IllegalStateException("Entity was null!"); 
		}
		if (departmentService == null) {
			throw new IllegalStateException("Service was null!");
		}
		try {
			entityDepartment = getFormData();
			departmentService.saveOrUpdate(entityDepartment);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}catch (DbException e) {
			Alerts.showAlerts("Error saving object!", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener dataChangeListener : dataChangeListeners) {
			dataChangeListener.onDataChange();
		}
	}

	public void onbtnCancelAction(ActionEvent event ) {
		Utils.currentStage(event).close();
	}

	private Department getFormData() {

		Department department = new Department();
		department.setId(Utils.tryParseToInt(txtId.getText()));
		department.setName(txtName.getText());
		return department;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void updateFormData() {
		if (entityDepartment == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entityDepartment.getId()));
		txtName.setText(entityDepartment.getName());
	}
}