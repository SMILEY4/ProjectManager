package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public abstract class FXChangeListener<T> implements CustomListener<ObservableValue<T>> {


	private final ChangeListener<T> listener = this::onValueChanged;
	private final List<ObservableValue<T>> observables = new ArrayList<>();
	private boolean isSilenced = false;




	public FXChangeListener(ObservableValue<T>... observables) {
		for (ObservableValue<T> observable : observables) {
			addTo(observable);
		}
	}




	@Override
	public FXChangeListener<T> addTo(ObservableValue<T> observable) {
		observable.addListener(listener);
		observables.add(observable);
		return this;
	}




	@Override
	public FXChangeListener<T> removeFrom(ObservableValue<T> observable) {
		observable.removeListener(listener);
		observables.remove(observable);
		return this;
	}




	@Override
	public FXChangeListener<T> removeFromAll() {
		for (ObservableValue<T> observable : observables) {
			observable.removeListener(listener);
		}
		observables.clear();
		return this;
	}




	@Override
	public void setSilenced(boolean silenced) {
		this.isSilenced = silenced;
	}




	@Override
	public boolean isSilenced() {
		return this.isSilenced;
	}




	public ChangeListener<T> getListener() {
		return this.listener;
	}




	private void onValueChanged(ObservableValue<? extends T> observable, T oldValue, T newValue) {
		if (!isSilenced()) {
			changed(observable, oldValue, newValue);
		}
	}




	public abstract void changed(ObservableValue<? extends T> observable, T oldValue, T newValue);


}
