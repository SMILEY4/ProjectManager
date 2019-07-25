package com.ruegnerlukas.taskmanager.utils.uielements;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class VBoxOrder {


	/**
	 * Moves the given {@link Node} in the given {@link VBox} one step in the given direction
	 */
	public static void moveItem(VBox vbox, Node node, int dir) {

		ObservableList<Node> nodes = vbox.getChildren();

		int index = nodes.indexOf(node);
		if (index == -1) {
			return;
		}

		int indexDst = index + dir;

		if (0 <= indexDst && indexDst <= nodes.size()) {
			nodes.remove(index);
			nodes.add(indexDst, node);
		}
	}


}
