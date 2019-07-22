package com.ruegnerlukas.taskmanager.data.externaldata;


import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.change.DataChange;

public interface ExternalDataHandler {

	void applyChange(DataChange change);

	void addChangeListener(DataChangeListener listener);

	void removeChangeListener(DataChangeListener listener);


}
