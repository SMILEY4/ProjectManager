package com.ruegnerlukas.taskmanager.utils;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class PopupBase extends AnchorPane {


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
