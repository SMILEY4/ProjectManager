package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;

public class SyncedProperty<T> extends CustomProperty<T> {


	public final String identifier;




	public SyncedProperty(String identifier) {
		this.identifier = identifier;
		ExternalDataHandler.registerSyncedProperty(this);
	}




	public void dispose() {
		ExternalDataHandler.deregisterSyncedProperty(this);
	}


}
