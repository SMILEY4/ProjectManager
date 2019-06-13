package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.externaldata.DataChange;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;

public class SyncedProperty<T> extends CustomProperty<T> {


	public final String identifier;
	public final Class<T> type;




	public SyncedProperty(String identifier, Class<T> type) {
		super();
		this.identifier = identifier;
		this.type = type;
		DataHandler.registerSyncedProperty(this);
	}




	public SyncedProperty(String identifier, Class<T> type, T initialValue) {
		super(initialValue);
		this.identifier = identifier;
		this.type = type;

		DataHandler.registerSyncedProperty(this);
	}




	public boolean applyChange(DataChange change) {
		if (change.newValue == null) {
			this.set(null);
			return true;
		} else {
			if (change.newValue.getClass().isAssignableFrom(type)) {
				this.set((T) change.newValue);
				return true;
			} else {
				return false;
			}
		}
	}




	public void dispose() {
		DataHandler.deregisterSyncedProperty(this);
	}


}
