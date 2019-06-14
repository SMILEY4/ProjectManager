package com.ruegnerlukas.taskmanager.data.syncedelements;

import com.ruegnerlukas.taskmanager.data.change.DataChange;

public interface SyncedElement {


	void applyChange(DataChange change);

	String getIdentifier();

	void dispose();

}
