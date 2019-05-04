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




	public void addTo(ObservableValue<T> observable) {
		observable.addListener(listener);
		observables.add(observable);
	}




	public void removeFrom(ObservableValue<T> observable) {
		observable.removeListener(listener);
		observables.remove(observable);
	}




	public void removeFromAll() {
		for (ObservableValue<T> observable : observables) {
			observable.removeListener(listener);
		}
		observables.clear();
	}




	public ChangeListener<T> getListener() {
		return this.listener;
	}




	public abstract void changed(ObservableValue<? extends T> observable, T oldValue, T newValue);


}
