package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.externaldata.DataChange;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import javafx.beans.value.ChangeListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataHandler {


	private static Map<String, SyncedProperty<?>> syncedProperties = new HashMap<>();

	private static ChangeListener<Object> changeListener = (observable, oldValue, newValue)
			-> DataHandler.onLocalChange(new DataChange(((SyncedProperty<Object>) observable).identifier, oldValue, newValue));




	public static Set<String> getHandledIdentifiers() {
		return Collections.unmodifiableSet(syncedProperties.keySet());
	}




	public static void registerSyncedProperty(SyncedProperty<?> property) {
		syncedProperties.put(property.identifier, property);
		property.addListener(changeListener);
	}




	public static void deregisterSyncedProperty(SyncedProperty<?> property) {
		syncedProperties.remove(property.identifier);
		property.removeListener(changeListener);
	}




	private static void onLocalChange(DataChange change) {
		if (Data.projectProperty.get() != null) {
			Data.projectProperty.get().temporaryData.externalDataInterface.applyChange(change);
		}
	}




	public static void onExternalChange(DataChange change) {
		SyncedProperty<?> property = syncedProperties.get(change.identifier);
		if (property != null) {
			property.applyChange(change);
		}
	}


}
