package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataHandler {


	private static Map<String, SyncedElement> syncedElements = new HashMap<>();




	public static void registerSyncedElement(SyncedElement element) {
		syncedElements.put(element.getIdentifier(), element);
	}




	public static void deregisterSyncedElement(SyncedElement element) {
		syncedElements.remove(element.getIdentifier());
	}




	public static void onExternalChange(DataChange change) {
		SyncedElement element = syncedElements.get(change.getIdentifier());
		if (element != null) {
			element.applyChange(change);
		}
	}




	public static Set<String> getHandledIdentifiers() {
		return syncedElements.keySet();
	}

}
