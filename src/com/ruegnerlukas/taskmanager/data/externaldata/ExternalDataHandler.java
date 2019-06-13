package com.ruegnerlukas.taskmanager.data.externaldata;

import java.util.List;

public interface ExternalDataHandler {


	boolean checkChanges();

	List<DataChange> getAllChanges();

	void applyChange(DataChange change);

}
