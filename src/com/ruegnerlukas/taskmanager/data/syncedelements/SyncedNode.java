package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;

import java.util.ArrayList;
import java.util.List;

public class SyncedNode {


	private DataHandler dataHandler;

	public final String identifier;
	public final SyncedNode parent;

	private SyncedElement managedElement;




	public SyncedNode(String identifier, SyncedNode parent, DataHandler dataHandler) {
		this.identifier = identifier;
		this.parent = parent;
		this.dataHandler = dataHandler;
		if (parent == null && dataHandler != null) {
			dataHandler.registerSyncedNode(this);
		}
	}




	/**
	 * Called when the {@link SyncedElement} connected to this node was changed.
	 */
	protected void onManagedElementChanged(DataChange change) {
		if (dataHandler == null) {
			return;
		}
		if (parent == null) {
			dataHandler.onLocalChange(change);
		} else {
			List<SyncedNode> path = new ArrayList<>();
			SyncedNode p = this;
			while (p != null) {
				path.add(0, p);
				p = p.parent;
			}
			dataHandler.onLocalChange(createChangeFromPath(path, change));
		}
	}




	/**
	 * @return a created {@link DataChange}. If the given list is not empty, a {@link NestedChange} will be created from the givne list and change.
	 */
	private DataChange createChangeFromPath(List<SyncedNode> path, DataChange change) {
		if (path.size() == 1) {
			return change;
		} else {
			return new NestedChange(path.remove(0).identifier, createChangeFromPath(path, change));
		}
	}




	public void setManagedElement(SyncedElement element) {
		this.managedElement = element;
	}




	public SyncedElement getManagedElement() {
		return managedElement;
	}




	public void dispose() {
		if (dataHandler != null) {
			dataHandler.deregisterSyncedNode(this);
		}
	}


}
