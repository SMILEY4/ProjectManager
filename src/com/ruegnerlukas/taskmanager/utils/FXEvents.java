package com.ruegnerlukas.taskmanager.utils;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class FXEvents {


	public static final EventHandler MUTE_FILTER = Event::consume;




	public static void muteNodes(Node... nodes) {
		for (Node node : nodes) {
			muteNode(node);
		}
	}




	public static void muteNode(Node node) {
		System.out.println("MUTE " + node.getClass().getSimpleName() + "@" + Integer.toHexString(node.hashCode()));
		node.addEventFilter(ActionEvent.ACTION, MUTE_FILTER);
	}




	public static void unmuteNodes(Node... nodes) {
		for (Node node : nodes) {
			unmuteNode(node);
		}
	}




	public static void unmuteNode(Node node) {
		System.out.println("UNMUTE " + node.getClass().getSimpleName() + "@" + Integer.toHexString(node.hashCode()));
		node.removeEventFilter(ActionEvent.ACTION, MUTE_FILTER);
	}

}
