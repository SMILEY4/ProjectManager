package com.ruegnerlukas.taskmanager.data.externaldata;

import com.ruegnerlukas.taskmanager.data.SyncedProperty;

import java.util.ArrayList;
import java.util.List;

public class ExternalDataHandler {


	private static List<SyncedProperty<?>> syncedProperties = new ArrayList<>();




	public static void registerSyncedProperty(SyncedProperty<?> property) {
		syncedProperties.add(property);
	}




	public static void deregisterSyncedProperty(SyncedProperty<?> property) {
		syncedProperties.remove(property);
	}


}
