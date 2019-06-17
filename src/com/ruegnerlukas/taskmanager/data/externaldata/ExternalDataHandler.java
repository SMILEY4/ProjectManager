package com.ruegnerlukas.taskmanager.data.externaldata;


import com.ruegnerlukas.taskmanager.data.change.DataChange;

public interface ExternalDataHandler {


	boolean checkChanges();


	void applyChange(DataChange change);

}
