package com.ruegnerlukas.taskmanager.data.externaldata;

import java.util.List;

public interface ExternalDataInterface {


	boolean checkChanges();

	List<DataChange> getAllChanges();

	void applyChange(DataChange change);

}
