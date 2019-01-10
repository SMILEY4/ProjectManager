package com.ruegnerlukas.taskmanager.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FXEvents {


	private static Map<ObservableValue, List<ChangeListener>> listenerMap = new HashMap<>();
	private static Map<ButtonBase, EventHandler<ActionEvent>> buttonBaseMap = new HashMap<>();
	private static Map<TextField, EventHandler<ActionEvent>> textFieldMap = new HashMap<>();
	private static Map<ComboBox, EventHandler<ActionEvent>> comboboxMap = new HashMap<>();




	public static <T> ChangeListener<T> register(ChangeListener<T> listener, ObservableValue observableValue) {
		if (!listenerMap.containsKey(observableValue)) {
			listenerMap.put(observableValue, new ArrayList<ChangeListener>());
		}
		listenerMap.get(observableValue).add(listener);
		return listener;
	}




	public static EventHandler<ActionEvent> register(EventHandler<ActionEvent> eventHandler, ButtonBase buttonBase) {
		buttonBaseMap.put(buttonBase, eventHandler);
		return eventHandler;
	}




	public static EventHandler<ActionEvent> register(EventHandler<ActionEvent> eventHandler, TextField textField) {
		textFieldMap.put(textField, eventHandler);
		return eventHandler;
	}




	public static EventHandler<ActionEvent> register(EventHandler<ActionEvent> eventHandler, ComboBox comboBox) {
		comboboxMap.put(comboBox, eventHandler);
		return eventHandler;
	}




	public static void mute(ObservableValue observableValue) {
		if (listenerMap.containsKey(observableValue)) {
			for (ChangeListener listener : listenerMap.get(observableValue)) {
				observableValue.removeListener(listener);
			}
		}
	}




	public static void mute(ButtonBase buttonBase) {
		buttonBase.setOnAction(null);
	}




	public static void mute(TextField textField) {
		textField.setOnAction(null);
	}




	public static void mute(ComboBox comboBox) {
		comboBox.setOnAction(null);
	}




	public static void unmute(ObservableValue observableValue) {
		if (listenerMap.containsKey(observableValue)) {
			for (ChangeListener listener : listenerMap.get(observableValue)) {
				observableValue.addListener(listener);
			}
		}
	}




	public static void unmute(ButtonBase buttonBase) {
		buttonBase.setOnAction(buttonBaseMap.get(buttonBase));
	}




	public static void unmute(TextField textField) {
		textField.setOnAction(textFieldMap.get(textField));
	}




	public static void unmute(ComboBox comboBox) {
		comboBox.setOnAction(comboboxMap.get(comboBox));
	}

}
