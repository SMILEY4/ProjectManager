package com.ruegnerlukas.taskmanager.utils.uielements.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {

	
	
	public static ButtonType confirmation(String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		return alert.getResult();
	}
	
	
	
	
	public static ButtonType confirmation(String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
		return alert.getResult();
	}
	
	
	
	
	public static void error(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	
	
	
	public static void error(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}
	
	
	
	
	public static void info(String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	
	
	
	public static void info(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}
	
}
