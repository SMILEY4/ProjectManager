package com.ruegnerlukas.taskmanager.utils;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;


public class CustomPropertyCollection implements ObservableValue {


	private final CustomProperty[] properties;
	private final List<ChangeListener> listeners = new ArrayList<>();




	public CustomPropertyCollection(CustomProperty... properties) {
		this.properties = new CustomProperty[properties.length];
		for (int i = 0; i < properties.length; i++) {
			CustomProperty property = properties[i];
			property.addListener(((observable, oldValue, newValue) -> fireValueChangedEvent(property, oldValue, newValue)));
			this.properties[i] = property;
		}
	}




	private void fireValueChangedEvent(CustomProperty property, Object oldValue, Object newValue) {
		for (ChangeListener listener : listeners) {
			listener.changed(property, oldValue, newValue);
		}
	}




	@Override
	public void addListener(ChangeListener listener) {
		listeners.add(listener);
	}




	@Override
	public void removeListener(ChangeListener listener) {
		listeners.remove(listener);
	}




	@Override
	public void addListener(InvalidationListener listener) {
	}




	@Override
	public void removeListener(InvalidationListener listener) {
	}




	@Override
	public Object getValue() {
		throw new UnsupportedOperationException();
	}

}
