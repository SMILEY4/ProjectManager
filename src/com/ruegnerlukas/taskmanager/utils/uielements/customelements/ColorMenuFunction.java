package com.ruegnerlukas.taskmanager.utils.uielements.customelements;

import javafx.geometry.Pos;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * A simple function for JavaFX menues. Contains a specified color.
 */
public abstract class ColorMenuFunction extends MenuFunction {


	private Color color;
	private boolean displayText = false;




	/**
	 * @param color the color represented by this function
	 * @param path  the path to this function. The last entry is the name of this function.
	 */
	public ColorMenuFunction(Color color, String... path) {
		super(path);
		this.color = color;
	}




	/**
	 * Set the color represented by this function.
	 *
	 * @return this function for chaining
	 */
	public ColorMenuFunction setColor(Color color) {
		this.color = color;
		return this;
	}




	/**
	 * Whether the name of this function should be shown.
	 *
	 * @return this function for chaining
	 */
	public ColorMenuFunction displayText(boolean displayText) {
		this.displayText = displayText;
		return this;
	}




	@Override
	protected MenuItem createItem() {

		HBox box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);
		box.setSpacing(5);

		Pane colorPane = new Pane();
		colorPane.setMinSize(60, 30);
		colorPane.setPrefSize(60, 30);
		colorPane.setMaxSize(60, 30);
		colorPane.setStyle("-fx-background-radius: 5; -fx-background-color: rgba(" + (int) (255 * color.getRed()) + "," + (int) (255 * color.getGreen()) + "," + (int) (255 * color.getBlue()) + ",255);");
		box.getChildren().add(colorPane);

		if (displayText) {
			Label label = new Label(this.text);
			box.getChildren().add(label);
		}

		CustomMenuItem item = new CustomMenuItem();
		item.setContent(box);
		return item;
	}

}
