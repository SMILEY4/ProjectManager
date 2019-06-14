package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.ValueDataChange;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedList;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataHandler {


	private static Map<String, SyncedElement> syncedElements = new HashMap<>();

	private static ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
		DataHandler.onLocalChange(new ValueDataChange(((SyncedProperty<Object>) observable).identifier, newValue));
	};

	private static ListChangeListener<Object> listChangeListener = c -> {
		// TODO
	};




	public static Set<String> getHandledIdentifiers() {
		return Collections.unmodifiableSet(syncedElements.keySet());
	}




	private static void onLocalChange(DataChange change) {
		if (Data.projectProperty.get() != null) {
			Data.projectProperty.get().temporaryData.externalDataInterface.applyChange(change);
		}
	}




	public static void onExternalChange(DataChange change) {
		SyncedElement element = syncedElements.get(change.identifier);
		if (element != null) {
			element.applyChange(change);
		}
	}




	public static void registerSyncedElement(SyncedProperty<?> property) {
		syncedElements.put(property.identifier, property);
		property.addListener(changeListener);
	}




	public static void deregisterSyncedElement(SyncedProperty<?> property) {
		syncedElements.remove(property.identifier);
		property.removeListener(changeListener);
	}




	public static void registerSyncedElement(SyncedList<?> list) {
		syncedElements.put(list.identifier, list);
		list.addListener(listChangeListener);
	}




	public static void deregisterSyncedElement(SyncedList<?> list) {
		syncedElements.remove(list.identifier);
		list.removeListener(listChangeListener);
	}


}
