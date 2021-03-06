package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class FXEventAdapter {


	private final List<ObservableValue> observableValues = new ArrayList<>();
	private final List<ObservableList> observableLists = new ArrayList<>();
	private final List<ObjectProperty<EventHandler>> properties = new ArrayList<>();

	private final EventHandler eventHandler = this::onHandleEvent;
	private final ChangeListener changeListener = this::onHandleValueChange;
	private final ListChangeListener listChangeListener = this::onHandleListChange;




	/**
	 * Adds this listener to the given {@link ObservableValue}.
	 */
	public FXEventAdapter addTo(ObservableValue observable) {
		observable.addListener(changeListener);
		observableValues.add(observable);
		return this;
	}




	/**
	 * Adds this listener to the given {@link ObservableList}.
	 */
	public FXEventAdapter addTo(ObservableList list) {
		list.addListener(listChangeListener);
		observableLists.add(list);
		return this;
	}




	/**
	 * Adds this listener to the given {@link ObjectProperty}.
	 */
	public FXEventAdapter addTo(ObjectProperty<EventHandler> property) {
		property.set(eventHandler);
		properties.add(property);
		return this;
	}




	/**
	 * Removes this listener from the given {@link ObservableValue}.
	 */
	public FXEventAdapter removeFrom(ObservableValue observable) {
		observable.removeListener(changeListener);
		observableValues.remove(observable);
		return this;
	}




	/**
	 * Removes this listener from the given {@link ObservableList}.
	 */
	public FXEventAdapter removeFrom(ObservableList list) {
		list.removeListener(listChangeListener);
		observableLists.remove(list);
		return this;
	}




	/**
	 * Removes this listener from the given {@link ObjectProperty}.
	 */
	public FXEventAdapter removeFrom(ObjectProperty<EventHandler> property) {
		if (property.get() == eventHandler) {
			property.set(null);
		}
		properties.remove(property);
		return this;
	}




	/**
	 * Removes this listener from all registered objects.
	 */
	public FXEventAdapter removeFromAll() {
		for (ObservableValue observable : observableValues) {
			observable.removeListener(changeListener);
		}
		for (ObservableList list : observableLists) {
			list.removeListener(listChangeListener);
		}
		for (ObjectProperty<EventHandler> property : properties) {
			if (property.get() == eventHandler) {
				property.set(null);
			}
		}
		observableValues.clear();
		observableLists.clear();
		properties.clear();
		return this;
	}




	/**
	 * @return the {@link ChangeListener} used by this listener.
	 */
	public ChangeListener getChangeListener() {
		return this.changeListener;
	}




	/**
	 * @return the {@link ListChangeListener} used by this listener.
	 */
	public ListChangeListener getListChangeListener() {
		return this.listChangeListener;
	}




	/**
	 * @return the {@link EventHandler} used by this listener.
	 */
	public EventHandler getEventHandler() {
		return eventHandler;
	}




	private void onHandleEvent(Event event) {
		handle(new ActionEvent());
	}




	private void onHandleValueChange(ObservableValue observable, Object oldValue, Object newValue) {
		handle(new ActionEvent());
	}




	private void onHandleListChange(ListChangeListener.Change change) {
		handle(new ActionEvent());
	}




	/**
	 * This method is called when an event is fired by a registered object or when a registered object changed.
	 */
	public abstract void handle(ActionEvent event);

}
