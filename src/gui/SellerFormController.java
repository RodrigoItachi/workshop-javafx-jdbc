package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entitySeller;

	private SellerService sellerService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private Date txtBirthDate;

	@FXML
	private Double txtBaseSalary;

	@FXML
	private Label labelMsgErrorName;

	@FXML
	private Label labelMsgErrorEmail;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	public void setEntitySeller(Seller entitySeller) {
		this.entitySeller = entitySeller;
	}

	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	public void subscribeDataChangeListener(DataChangeListener dataChangeListener) {
		dataChangeListeners.add(dataChangeListener);
	}

	public void onbtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void onBtnSaveAction(ActionEvent event) {
		if (entitySeller == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (sellerService == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entitySeller = getFormData();
			sellerService.saveOrUpdate(entitySeller);
			notifyDataChangeListeners();
			Utils.currentStage(event);
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlerts("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private Seller getFormData() {
		Seller seller = new Seller();
		final String msgErrorFields = "Field can't be empty!";
		ValidationException exception = new ValidationException("Validation Error");

		seller.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
			exception.addError("name", msgErrorFields);
		}
		seller.setName(txtName.getText());

//		if (txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
//			exception.addError("email", msgErrorFields);
//		}
//		seller.setEmail(txtEmail.getText());

		return seller;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void notifyDataChangeListeners() {
		for (DataChangeListener dataChangeListener : dataChangeListeners) {
			dataChangeListener.onDataChange();
		}
	}

	public void onBtnActionCancel(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void updateFormData() {
		if (entitySeller == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entitySeller.getId()));
		txtName.setText(entitySeller.getName());
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelMsgErrorName.setText(errors.get("name"));
		}
		if (fields.contains("email")) {
			labelMsgErrorEmail.setText(errors.get("email"));
		}
	}

}