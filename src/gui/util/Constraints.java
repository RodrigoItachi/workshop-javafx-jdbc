package gui.util;

import javafx.scene.control.TextField;

public class Constraints {
	public void setTextField(TextField txt) {
		txt.textProperty().addListener((obs, newValue, oldValue) -> {
			if (newValue != null && !newValue.matches("\\d*")) {
				txt.setText(oldValue);
			}
		});
	}

	public void setTextFieldMax(TextField txt, int max) {
		txt.textProperty().addListener((obs, newValue, oldValue) -> {
			if (newValue != null && newValue.length() > max) {
				txt.setText(oldValue);
			}
		});
	}

	public void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, newValue, oldValue) -> {
			if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
				txt.setText(oldValue);
			}
		});
	}
}