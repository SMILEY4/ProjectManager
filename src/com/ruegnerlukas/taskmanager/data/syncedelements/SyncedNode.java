package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;

import java.util.ArrayList;
import java.util.List;

public class SyncedNode {


	public final String identifier;
	public final SyncedNode parent;

	private SyncedElement managedElement;




	public SyncedNode(String identifier, SyncedNode parent) {
		this.identifier = identifier;
		this.parent = parent;
		if (parent == null) {
			DataHandler.registerSyncedNode(this);
		}
	}




	protected void onManagedElementChanged(DataChange change) {
		if (parent == null) {
			DataHandler.onLocalChange(change);
		} else {
			List<SyncedNode> path = new ArrayList<>();
			SyncedNode p = this;
			while (p != null) {
				path.add(0, p);
				p = p.parent;
			}
			DataHandler.onLocalChange(createChangeFromPath(path, change));
		}
	}




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
		DataHandler.deregisterSyncedNode(this);
	}


}