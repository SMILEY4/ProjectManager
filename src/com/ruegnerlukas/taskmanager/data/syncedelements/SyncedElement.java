package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.utils.listeners.CustomListener;

public interface SyncedElement {


	/**
	 * Applies/Processes the given change to/by this element.
	 */
	void applyChange(DataChange change);


	/**
	 * @return the {@link SyncedNode} of this element (should not be null!)
	 */
	SyncedNode getNode();


	CustomListener<?> getListener();


	void dispose();

}
