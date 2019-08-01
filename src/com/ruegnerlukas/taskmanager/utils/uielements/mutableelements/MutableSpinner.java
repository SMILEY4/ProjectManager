package com.ruegnerlukas.taskmanager.utils.uielements.mutableelements;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MutableSpinner<T> extends Spinner<T> {


	private boolean muted = false;
	private List<ChangeListener<? super T>> listeners = new ArrayList<>();




	public MutableSpinner() {
		super();
		setup();
	}




	public MutableSpinner(int min, int max, int initialValue) {
		super(min, max, initialValue);
		setup();
	}




	public MutableSpinner(int min, int max, int initialValue, int amountToStepBy) {
		super(min, max, initialValue, amountToStepBy);
		setup();
	}




	public MutableSpinner(double min, double max, double initialValue) {
		super(min, max, initialValue);
		setup();
	}




	public MutableSpinner(double min, double max, double initialValue, double amountToStepBy) {
		super(min, max, initialValue, amountToStepBy);
		setup();
	}




	private void setup() {
		this.valueProperty().addListener(((observable, oldValue, newValue) -> {
			if (!isMuted()) {
				for (ChangeListener<? super T> listener : listeners) {
					listener.changed(observable, oldValue, newValue);
				}
			}
		}));
	}




	public void addMutableValueListener(ChangeListener<? super T> listener) {
		listeners.add(listener);
	}




	public void setMuted(boolean muted) {
		this.muted = muted;
	}




	public boolean isMuted() {
		return muted;
	}


}
