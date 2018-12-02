package com.ruegnerlukas.taskmanager.utils.uielements.button;

import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;

public class ButtonUtils {

	
	public static void makeIconButton(Button button, String strSVG, float svgWidth, float svgHeight, String iconColor) {
		SVGPath svg = new SVGPath();
		svg.setContent(strSVG);
//		svg.setStyle("-fx-fill: " + iconColor + ";");
		button.setGraphic(svg);
		button.setText("");
//		button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
		svg.scaleXProperty().bind(button.widthProperty().divide(svgWidth));
		svg.scaleYProperty().bind(button.heightProperty().divide(svgHeight));
	}
	
}
