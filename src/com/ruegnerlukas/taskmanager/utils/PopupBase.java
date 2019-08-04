package com.ruegnerlukas.taskmanager.utils;

import com.ruegnerlukas.taskmanager.TaskManager;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class PopupBase extends AnchorPane {


	/**
	 * Opens the given {@link PopupBase} in a new window.
	 *
	 * @param wait whether or not to wait for the popup to close.
	 */
	public static void openPopup(PopupBase popup, boolean wait) {
		Stage stage = new Stage();
		popup.setStage(stage);
		popup.create();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(TaskManager.getPrimaryStage());
		Scene scene = new Scene(popup, popup.getPopupWidth(), popup.getPopupHeight());
		scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
			if (ke.getCode() == KeyCode.R) {
				UIDataHandler.styleReloadAll();
				ke.consume();
			}
		});
		stage.setScene(scene);
		if (wait) {
			stage.showAndWait();
		} else {
			stage.show();
		}
	}




	private Stage stage;
	private int width;
	private int height;




	/**
	 * @param width  the initial width of the popup-window
	 * @param height the initial height of the popup-window
	 */
	public PopupBase(int width, int height) {
		this.width = width;
		this.height = height;
	}




	/**
	 * Use this method to setup and create all content and logic of this popup.
	 */
	public abstract void create();




	/**
	 * @return the specified initial width of this popup
	 */
	public int getPopupWidth() {
		return this.width;
	}




	/**
	 * @return the specified initial height of this popup
	 */
	public int getPopupHeight() {
		return this.height;
	}




	/**
	 * sets the stage of this popup
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}




	/**
	 * @return the stage of this popup
	 */
	public Stage getStage() {
		return this.stage;
	}


}
