package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public abstract class FXChangeListener<T> {


	private final ChangeListener<T> listener = this::changed;
	private final List<ObservableValue<T>> observables = new ArrayList<>();




	public FXChangeListener(ObservableValue<T>... observables) {
		for (ObservableValue<T> observable : observables) {
			addTo(observable);
		}
	}




	public FXChangeListener addTo(ObservableValue<T> observable) {
		observable.addListener(listener);
		observables.add(observable);
		return this;
	}




	public FXChangeListener removeFrom(ObservableValue<T> observable) {
		observable.removeListener(listener);
		observables.remove(observable);
		return this;
	}




	public FXChangeListener removeFromAll() {
		for (ObservableValue<T> observable : observables) {
			observable.removeListener(listener);
		}
		observables.clear();
		return this;
	}




	public ChangeListener<T> getListener() {
		return this.listener;
	}




	public abstract void changed(ObservableValue<? extends T> observable, T oldValue, T newValue);


}
