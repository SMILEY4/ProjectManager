package com.ruegnerlukas.taskmanager.utils.uielements;

import com.ruegnerlukas.taskmanager.utils.SVGIcon;
import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;

public class ButtonUtils {


	/**
	 * Sets the given {@link Button} up as a button with only the given {@link SVGIcon}. The icon can be scaled with the given scale-argument.
	 */
	public static void makeIconButton(Button button, SVGIcon icon, double scale) {
		SVGPath svg = new SVGPath();
		svg.setContent(icon.data);
		button.setGraphic(svg);
		button.setText("");
		svg.scaleXProperty().bind(button.widthProperty().divide(icon.width / scale));
		svg.scaleYProperty().bind(button.heightProperty().divide(icon.height / scale));
	}

}
