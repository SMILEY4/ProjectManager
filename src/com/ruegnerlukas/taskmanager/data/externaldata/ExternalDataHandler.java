package com.ruegnerlukas.taskmanager.data.externaldata;


import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.localdata.Project;

public interface ExternalDataHandler {

	void applyChange(DataChange change, Project project);

	void addChangeListener(DataChangeListener listener);

	void removeChangeListener(DataChangeListener listener);


}
