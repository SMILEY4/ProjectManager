package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ValueChange;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import javafx.beans.value.ObservableValue;

public class SyncedProperty<T> extends CustomProperty<T> implements SyncedElement {


	private final SyncedNode node;
	private final FXChangeListener<T> listener;




	public SyncedProperty(String identifier, SyncedNode parent, DataHandler handler) {
		this.node = new SyncedNode(identifier, parent, handler);
		this.node.setManagedElement(this);
		this.listener = new FXChangeListener<T>(this) {
			@Override
			public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
				ValueChange change = new ValueChange(node.identifier, newValue);
				node.onManagedElementChanged(change);
			}
		};
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof ValueChange) {
			ValueChange valueChange = (ValueChange) change;
			if (get().getClass().isAssignableFrom(valueChange.getNewValue().getClass())) {
				this.listener.setMuted(true);
				this.set((T) valueChange.getNewValue());
				this.listener.setMuted(false);
			}
		}
	}




	@Override
	public void dispose() {
		node.dispose();
		listener.removeFromAll();
	}




	@Override
	public SyncedNode getNode() {
		return node;
	}




	@Override
	public FXChangeListener<T> getListener() {
		return this.listener;
	}


}
