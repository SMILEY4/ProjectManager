package com.ruegnerlukas.taskmanager_old.utils.uielements;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AnchorUtils {

	public static void setAnchors(Node node, double top, double right, double bottom, double left) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setRightAnchor(node, right);
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setLeftAnchor(node, left);
	}
	
}
