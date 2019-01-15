package com.ruegnerlukas.taskmanager.utils.uielements.button;

import com.ruegnerlukas.taskmanager.utils.SVGIcon;
import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;

public class ButtonUtils {

	
	public static void makeIconButton(Button button, SVGIcon icon, double scale, String iconColor) {
		SVGPath svg = new SVGPath();
		svg.setContent(icon.data);
		button.setGraphic(svg);
		button.setText("");
		svg.scaleXProperty().bind(button.widthProperty().divide(icon.width/scale));
		svg.scaleYProperty().bind(button.heightProperty().divide(icon.height/scale));
	}
	
}
