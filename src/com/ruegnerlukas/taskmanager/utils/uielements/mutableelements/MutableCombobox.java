package com.ruegnerlukas.taskmanager.utils.uielements.mutableelements;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class MutableCombobox<T> extends ComboBox<T> {


	private boolean muted = false;
	private List<ChangeListener<? super T>> listeners = new ArrayList<>();




	public MutableCombobox() {
		super();
		setup();
	}




	public MutableCombobox(ObservableList<T> items) {
		super(items);
		setup();
	}




	private void setup() {
		this.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (!isMuted()) {
				for (ChangeListener<? super T> listener : listeners) {
					listener.changed(observable, oldValue, newValue);
				}
			}
		}));
	}




	public void addMutableSelectedItemListener(ChangeListener<? super T> listener) {
		listeners.add(listener);
	}




	public void setMuted(boolean muted) {
		this.muted = muted;
	}




	public boolean isMuted() {
		return muted;
	}


}
