package com.ruegnerlukas.taskmanager.data;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedElement;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataHandler {


	private Map<String, SyncedNode> syncedNodes = new HashMap<>();
	private ExternalDataHandler externalDataHandler;

	private Project project;




	public DataHandler(Project project) {
		this(project, null);
	}




	public DataHandler(Project project, ExternalDataHandler externalDataHandler) {
		this.project = project;
		this.externalDataHandler = externalDataHandler;
		if (this.externalDataHandler != null) {
			this.externalDataHandler.addChangeListener(this::onExternalChange);
		}
	}




	public ExternalDataHandler getExternalDataHandler() {
		return externalDataHandler;
	}




	public void registerSyncedNode(SyncedNode node) {
		syncedNodes.put(node.identifier, node);
	}




	public void deregisterSyncedNode(SyncedNode node) {
		syncedNodes.remove(node.identifier);
	}




	public Set<String> getHandledIdentifiers() {
		return syncedNodes.keySet();
	}




	public void onExternalChange(DataChange change) {
		SyncedNode node = syncedNodes.get(change.getIdentifier());
		if (node != null) {
			SyncedElement element = node.getManagedElement();
			if (element != null) {
				element.applyChange(change);
			}
		}
	}




	public void onLocalChange(DataChange change) {
		if (getExternalDataHandler() != null && change != null) {
			getExternalDataHandler().applyChange(change, project);
		}
	}


}
