package com.ruegnerlukas.taskmanager.utils.uielements;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class LabelUtils {


	/**
	 * Adds button-functionality to the given {@link Label}
	 *
	 * @param label        the label
	 * @param styleDefault the default css-style of the label
	 * @param styleHover   the css-style when a mouse is over the given label
	 * @param eventHandler notified when the label was clicked
	 */
	public static void makeAsButton(Label label, String styleDefault, String styleHover, EventHandler<MouseEvent> eventHandler) {
		label.setOnMouseClicked(eventHandler);
		label.setStyle(styleDefault);
		label.setOnMouseEntered(event -> label.setStyle(styleHover));
		label.setOnMouseExited(event -> label.setStyle(styleDefault));

	}


}
