package com.ruegnerlukas.taskmanager.utils.uielements;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VBoxDragAndDrop {


	protected static ObjectProperty<Node> draggingNode = new SimpleObjectProperty<Node>();




	/**
	 * Enables drag and drop for new{@link Node}s inside the given {@link VBox}.
	 */
	public static void enableDragAndDrop(VBox box) {

		for (Node node : box.getChildren()) {
			new DraggableNode(box, node);
		}

		box.getChildren().addListener((ListChangeListener<Node>) c -> {
			while (c.next()) {
				if (c.wasAdded()) {
					List<? extends Node> addedNodes = c.getAddedSubList();
					for (Node node : addedNodes) {
						new DraggableNode(box, node);
					}
				}
			}

		});

	}

}






class DraggableNode {


	public static final String KEY = "DraggableNode";

	public final VBox parent;
	public final Node node;




	public DraggableNode(VBox parent, Node node) {
		this.parent = parent;
		this.node = node;

		node.setOnDragDetected(event -> {

			Dragboard dragboard = node.startDragAndDrop(TransferMode.MOVE);
			dragboard.setDragView(node.snapshot(null, null));

			ClipboardContent cpContent = new ClipboardContent();
			cpContent.putString(KEY);
			dragboard.setContent(cpContent);

			VBoxDragAndDrop.draggingNode.set(node);

			event.consume();

		});

		node.setOnDragOver(event -> {
			final Dragboard dragboard = event.getDragboard();
			if (dragboard.hasString() && KEY.equals(dragboard.getString()) && VBoxDragAndDrop.draggingNode.get() != null) {
				event.acceptTransferModes(TransferMode.MOVE);
				event.consume();
			}
		});

		node.setOnDragExited(event -> {
			final Dragboard dragboard = event.getDragboard();
			if (dragboard.hasString() && KEY.equals(dragboard.getString()) && VBoxDragAndDrop.draggingNode.get() != null) {
				event.consume();
			}
		});

		node.setOnDragDropped(event -> {
			final Dragboard dragboard = event.getDragboard();
			boolean success = false;

			if (dragboard.hasString()) {

				Object source = event.getGestureSource();
				int srcIndex = parent.getChildren().indexOf(source);
				int dstIndex = parent.getChildren().indexOf(node);

				List<Node> nodes = new ArrayList<>(parent.getChildren());
				if (srcIndex < dstIndex) {
					Collections.rotate(nodes.subList(srcIndex, dstIndex + 1), -1);
				} else {
					Collections.rotate(nodes.subList(dstIndex, srcIndex + 1), +1);
				}
				parent.getChildren().clear();
				parent.getChildren().addAll(nodes);
				success = true;
			}
			event.setDropCompleted(success);
			event.consume();
		});

	}

}
