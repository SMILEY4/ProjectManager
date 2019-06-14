package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ValueDataChange;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;

public class SyncedProperty<T> extends CustomProperty<T> implements SyncedElement {


	public final String identifier;
	public final Class<T> type;




	public SyncedProperty(String identifier, Class<T> type) {
		super();
		this.identifier = identifier;
		this.type = type;
		DataHandler.registerSyncedElement(this);
	}




	public SyncedProperty(String identifier, Class<T> type, T initialValue) {
		super(initialValue);
		this.identifier = identifier;
		this.type = type;
		DataHandler.registerSyncedElement(this);
	}




	@Override
	public void applyChange(DataChange change) {
		if (change instanceof ValueDataChange) {
			applyChange((ValueDataChange) change);
		}
	}




	private void applyChange(ValueDataChange change) {
		if (change.newValue == null) {
			this.set(null);
		} else {
			if (change.newValue.getClass().isAssignableFrom(type)) {
				this.set((T) change.newValue);
			}
		}
	}




	@Override
	public String getIdentifier() {
		return identifier;
	}




	public Class<?> getType() {
		return type;
	}




	@Override
	public void dispose() {
		DataHandler.deregisterSyncedElement(this);
	}


}
