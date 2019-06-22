package com.ruegnerlukas.taskmanager.utils.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.List;

public abstract class FXGenericChangeListener implements CustomListener<ObservableValue> {


	private final ChangeListener listener = this::onChangedValue;
	private final List<ObservableValue> observables = new ArrayList<>();
	private boolean isSilenced = false;




	public FXGenericChangeListener(ObservableValue... observables) {
		for (ObservableValue observable : observables) {
			addTo(observable);
		}
	}




	@Override
	public FXGenericChangeListener addTo(ObservableValue observable) {
		observable.addListener(listener);
		observables.add(observable);
		return this;
	}




	@Override
	public FXGenericChangeListener removeFrom(ObservableValue observable) {
		observable.removeListener(listener);
		observables.remove(observable);
		return this;
	}




	@Override
	public FXGenericChangeListener removeFromAll() {
		for (ObservableValue observable : observables) {
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




	public ChangeListener getListener() {
		return this.listener;
	}




	private void onChangedValue(ObservableValue observable, Object oldValue, Object newValue) {
		if (!isSilenced) {
			changed(observable, oldValue, newValue);
		}
	}




	public abstract void changed(ObservableValue observable, Object oldValue, Object newValue);


}
