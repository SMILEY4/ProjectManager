package com.ruegnerlukas.taskmanager.utils.uielements.menu;

import javafx.geometry.Pos;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class ColorMenuFunction extends MenuFunction {
	
	
	private Color color;
	private boolean displayText = false;
	
	
	
	public ColorMenuFunction(Color color, String... path) {
		super(path);
		this.color = color;
	}
	
	
	public ColorMenuFunction setColor(Color color) {
		this.color = color;
		return this;
	}
	
	
	
	
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
		colorPane.setStyle("-fx-background-radius: 5; -fx-background-color: rgba(" + (int)(255*color.getRed()) +","+ (int)(255*color.getGreen()) +","+ (int)(255*color.getBlue()) + ",255);");
		box.getChildren().add(colorPane);
		
		if(displayText) {
			Label label = new Label(this.text);
			box.getChildren().add(label);
		}
		
		CustomMenuItem item = new CustomMenuItem();
		item.setContent(box);
		return item;
	}
	
}
