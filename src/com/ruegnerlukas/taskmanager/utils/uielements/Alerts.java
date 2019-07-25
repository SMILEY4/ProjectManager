package com.ruegnerlukas.taskmanager.utils.uielements;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {


	/**
	 * Shows an confirmation alert with the given header- and content-text and the buttons "Yes", "No" and "Cancel"
	 */
	public static ButtonType confirmation(String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		return alert.getResult();
	}




	/**
	 * Shows an confirmation alert with the given header-text and the buttons "Yes", "No" and "Cancel".
	 *
	 * @return the clicked {@code ButtonType}
	 */
	public static ButtonType confirmation(String text) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
		return alert.getResult();
	}




	/**
	 * Shows an confirmation alert with the given header- and content-text and the given buttons.
	 *
	 * @return the clicked {@code ButtonType}
	 */
	public static ButtonType confirmation(String header, String content, ButtonType... buttonTypes) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", buttonTypes);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		return alert.getResult();
	}




	/**
	 * Shows an confirmation alert with the given header-text and the given buttons.
	 *
	 * @return the clicked {@code ButtonType}
	 */
	public static ButtonType confirmation(String text, ButtonType... buttonTypes) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", buttonTypes);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
		return alert.getResult();
	}




	/**
	 * Shows an error alert with the given header-text.
	 */
	public static void error(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}




	/**
	 * Shows an error alert with the given content-text.
	 */
	public static void error(String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}




	/**
	 * Shows an info alert with the given header- and content-text.
	 */
	public static void info(String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}




	/**
	 * Shows an info alert with the given and content-text.
	 */
	public static void info(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}

}
