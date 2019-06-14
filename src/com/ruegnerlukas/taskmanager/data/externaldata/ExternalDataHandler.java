package com.ruegnerlukas.taskmanager.data.externaldata;

import com.ruegnerlukas.taskmanager.data.change.DataChange;

import java.util.List;

public interface ExternalDataHandler {


	boolean checkChanges();

	List<DataChange> getAllChanges();

	void applyChange(DataChange change);

}
