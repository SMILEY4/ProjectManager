package com.ruegnerlukas.taskmanager.utils;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class PopupBase extends AnchorPane {


	private Stage stage;
	private int width;
	private int height;




	public PopupBase(int width, int height) {
		this.width = width;
		this.height = height;
	}




	public abstract void create();




	public int getPopupWidth() {
		return this.width;
	}




	public int getPopupHeight() {
		return this.height;
	}




	public void setStage(Stage stage) {
		this.stage = stage;
	}




	public Stage getStage() {
		return this.stage;
	}


}
