package com.ruegnerlukas.taskmanager.utils.uielements;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AnchorUtils {


	public static void setAnchors(Node node, double top, double right, double bottom, double left) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setRightAnchor(node, right);
		AnchorPane.setBottomAnchor(node, bottom);
		AnchorPane.setLeftAnchor(node, left);
	}




	public static void setAnchors(Node node, Number top, Number right, Number bottom, Number left) {
		if (top != null) AnchorPane.setTopAnchor(node, top.doubleValue());
		if (right != null) AnchorPane.setRightAnchor(node, right.doubleValue());
		if (bottom != null) AnchorPane.setBottomAnchor(node, bottom.doubleValue());
		if (left != null) AnchorPane.setLeftAnchor(node, left.doubleValue());
	}


}
