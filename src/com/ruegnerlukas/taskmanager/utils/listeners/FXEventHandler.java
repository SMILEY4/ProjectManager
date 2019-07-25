package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class FXEventHandler<T extends Event> {


	private final EventHandler<T> handler = this::handle;
	private final List<ObjectProperty<EventHandler<T>>> properties = new ArrayList<>();




	/**
	 * Adds this listener to the given property.
	 */
	public FXEventHandler addTo(ObjectProperty<EventHandler<T>> property) {
		property.set(handler);
		properties.add(property);
		return this;
	}




	/**
	 * Removes this listener to the given property.
	 */
	public FXEventHandler removeFrom(ObjectProperty<EventHandler<T>> property) {
		if (property.get() == handler) {
			property.set(null);
		}
		properties.remove(property);
		return this;
	}




	/**
	 * Removes this listener from all registered properties
	 */
	public FXEventHandler removeFromAll() {
		for (ObjectProperty<EventHandler<T>> property : properties) {
			if (property.get() == handler) {
				property.set(null);
			}
		}
		properties.clear();
		return this;
	}




	/**
	 * @return the {@link EventHandler} used by this handler.
	 */
	public EventHandler<T> getHandler() {
		return this.handler;
	}




	/**
	 * This method is called when an event is fired by an registered property.
	 */
	public abstract void handle(T event);


}
