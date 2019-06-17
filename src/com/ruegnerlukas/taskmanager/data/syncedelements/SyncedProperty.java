package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;

public class SyncedProperty<T> extends UnmanagedSyncedProperty<T> {


	public SyncedProperty(String identifier, T initialValue) {
		super(identifier, initialValue);
		DataHandler.registerSyncedElement(this);
	}




	public SyncedProperty(String identifier) {
		super(identifier);
		DataHandler.registerSyncedElement(this);
	}




	@Override
	public void dispose() {
		DataHandler.deregisterSyncedElement(this);
	}


}
