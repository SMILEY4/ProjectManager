package com.ruegnerlukas.taskmanager.data.externaldata;


import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.localdata.Project;

public interface ExternalDataHandler {


	/**
	 * @return a new {@link Project} created with the data handled by this {@link ExternalDataHandler}.
	 */
	Project loadProject();


	/**
	 * Applies given {@link DataChange} change to the external data-storage.
	 */
	void applyChange(DataChange change, Project project);


	/**
	 * Adds the given {@link DataChangeListener}. It will be notified of {@link DataChange}s in the external data-storage.
	 */
	void addChangeListener(DataChangeListener listener);


	/**
	 * Removes the given {@link DataChangeListener}.
	 */
	void removeChangeListener(DataChangeListener listener);


}
