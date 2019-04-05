package com.ruegnerlukas.taskmanager.utils;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class FXEvents {


	public static final EventHandler MUTE_FILTER = Event::consume;




	public static void muteNode(Node node) {
		node.addEventFilter(ActionEvent.ACTION, MUTE_FILTER);
	}




	public static void unmuteNode(Node node) {
		node.removeEventFilter(ActionEvent.ACTION, MUTE_FILTER);
	}

}
