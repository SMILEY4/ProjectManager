package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public abstract class FXGenericChangeListener {


	private final ChangeListener listener = this::changed;
	private final List<ObservableValue> observables = new ArrayList<>();




	public FXGenericChangeListener(ObservableValue... observables) {
		for (ObservableValue observable : observables) {
			addTo(observable);
		}
	}




	public FXGenericChangeListener addTo(ObservableValue observable) {
		observable.addListener(listener);
		observables.add(observable);
		return this;
	}




	public FXGenericChangeListener removeFrom(ObservableValue observable) {
		observable.removeListener(listener);
		observables.remove(observable);
		return this;
	}




	public FXGenericChangeListener removeFromAll() {
		for (ObservableValue observable : observables) {
			observable.removeListener(listener);
		}
		observables.clear();
		return this;
	}




	public ChangeListener getListener() {
		return this.listener;
	}




	public abstract void changed(ObservableValue observable, Object oldValue, Object newValue);


}
