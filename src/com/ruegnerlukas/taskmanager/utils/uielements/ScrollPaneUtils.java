package com.ruegnerlukas.taskmanager.utils.uielements;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class ScrollPaneUtils {


	public static void centerContent(ScrollPane pane, Node node) {

		double contentHeight = pane.getContent().getBoundsInLocal().getHeight();
		double viewHeight = pane.getViewportBounds().getHeight();
		double y = (node.getBoundsInParent().getMaxY() + node.getBoundsInParent().getMinY()) / 2;

		pane.setVvalue(pane.getVmax() * ((y - 0.5 * viewHeight) / (contentHeight - viewHeight)));

		double contentWidth = pane.getContent().getBoundsInLocal().getWidth();
		double viewWidth = pane.getViewportBounds().getWidth();
		double x = (node.getBoundsInParent().getMaxX() + node.getBoundsInParent().getMinX()) / 2;

		pane.setHvalue(pane.getHmax() * ((x - 0.5 * viewWidth) / (contentWidth - viewWidth)));

	}


}
