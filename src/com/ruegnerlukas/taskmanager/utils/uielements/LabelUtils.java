package com.ruegnerlukas.taskmanager.utils.uielements;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class LabelUtils {

	public static void makeAsButton(Label label, String styleDefault, String styleHover, EventHandler<MouseEvent> eventHandler) {
	
		// make clickable
		label.setOnMouseClicked(eventHandler);

		// set styles
		label.setStyle(styleDefault);

		label.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				label.setStyle(styleHover);
			}
		});
		
		label.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				label.setStyle(styleDefault);
			}
		});
		
	}
	
	
	
}
