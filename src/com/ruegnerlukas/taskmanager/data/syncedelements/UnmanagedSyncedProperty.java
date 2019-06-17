package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ValueChange;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;

public class UnmanagedSyncedProperty<T> extends CustomProperty<T> implements SyncedElement {


	public final String identifier;




	public UnmanagedSyncedProperty(String identifier, T initialValue) {
		super(initialValue);
		this.identifier = identifier;
	}




	public UnmanagedSyncedProperty(String identifier) {
		super();
		this.identifier = identifier;
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof ValueChange) {
			ValueChange valueChange = (ValueChange) change;
			if (get().getClass().isAssignableFrom(valueChange.getNewValue().getClass())) {
				this.set((T) valueChange.getNewValue());
			} else {
				// incompatible types
			}
		} else {
			// unexpected change-type
		}
	}




	@Override
	public String getIdentifier() {
		return identifier;
	}




	@Override
	public void dispose() {
	}


}
