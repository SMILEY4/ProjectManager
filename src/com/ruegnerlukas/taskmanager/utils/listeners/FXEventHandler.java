package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class FXEventHandler<T extends Event> {


	private final EventHandler<T> handler = this::handle;
	private final List<ObjectProperty<EventHandler<T>>> properties = new ArrayList<>();




	public FXEventHandler addTo(ObjectProperty<EventHandler<T>> property) {
		property.set(handler);
		properties.add(property);
		return this;
	}




	public FXEventHandler removeFrom(ObjectProperty<EventHandler<T>> property) {
		if (property.get() == handler) {
			property.set(null);
		}
		properties.remove(property);
		return this;
	}




	public FXEventHandler removeFromAll() {
		for (ObjectProperty<EventHandler<T>> property : properties) {
			if (property.get() == handler) {
				property.set(null);
			}
		}
		properties.clear();
		return this;
	}




	public EventHandler<T> getHandler() {
		return this.handler;
	}




	public abstract void handle(T event);


}
