package com.ruegnerlukas.taskmanager.utils;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class FXNodeSilencer {


	public static final EventHandler MUTE_FILTER = Event::consume;




	/**
	 * Prevents given {@link Node}s from firing events.
	 */
	public static void muteNodes(Node... nodes) {
		for (Node node : nodes) {
			muteNode(node);
		}
	}




	/**
	 * Prevents given {@link Node} from firing events.
	 */
	public static void muteNode(Node node) {
		node.addEventFilter(ActionEvent.ACTION, MUTE_FILTER);
	}




	/**
	 * Allows given {@link Node}s to fire events.
	 */
	public static void unmuteNodes(Node... nodes) {
		for (Node node : nodes) {
			unmuteNode(node);
		}
	}




	/**
	 * Allows given {@link Node} to fire events.
	 */
	public static void unmuteNode(Node node) {
		node.removeEventFilter(ActionEvent.ACTION, MUTE_FILTER);
	}

}
