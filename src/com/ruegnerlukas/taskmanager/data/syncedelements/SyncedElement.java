package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.utils.listeners.CustomListener;

public interface SyncedElement {


	void applyChange(DataChange change);


	void dispose();


	SyncedNode getNode();


	CustomListener<?> getListener();


}
