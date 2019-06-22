package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataHandler {


	private static Map<String, SyncedNode> syncedNodes = new HashMap<>();




	public static void registerSyncedNode(SyncedNode node) {
		syncedNodes.put(node.identifier, node);
	}




	public static void deregisterSyncedNode(SyncedNode node) {
		syncedNodes.remove(node.identifier);
	}


	public static Set<String> getHandledIdentifiers() {
		return syncedNodes.keySet();
	}



	public static void onExternalChange(DataChange change) {
		System.out.println("EXTERNAL CHANGE: " + change.toString());
		SyncedNode node = syncedNodes.get(change.getIdentifier());
		if(node != null) {
			SyncedElement element = node.getManagedElement();
			if (element != null) {
				element.applyChange(change);
			}
		}
	}


	public static void onLocalChange(DataChange change) {
		System.out.println("LOCAL CHANGE: " + change.toString());
	}



}
