package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write.*;
import com.ruegnerlukas.taskmanager.data.localdata.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalFileHandler implements ExternalDataHandler {


	private Map<String, WriteFileAction> fileActionMap = new HashMap<>();

	private List<DataChangeListener> listeners = new ArrayList<>();
	private FileHandler handler;




	public ExternalFileHandler(String directory) {
		handler = new FileHandler(directory);
		fileActionMap.put(Identifiers.SETTINGS_PROJECT_NAME, new WriteActionSettings());
		fileActionMap.put(Identifiers.SETTINGS_ATTRIBUTES_LOCKED, new WriteActionSettings());
		fileActionMap.put(Identifiers.SETTINGS_TASK_IDCOUNTER, new WriteActionSettings());
		fileActionMap.put(Identifiers.SETTINGS_ATTRIBUTE_IDCOUNTER, new WriteActionSettings());
		fileActionMap.put(Identifiers.DATA_ATTRIBUTE_LIST, new WriteActionListAttributes());
		fileActionMap.put(Identifiers.DATA_TASK_LIST, new WriteActionListTasks());
		fileActionMap.put(Identifiers.DATA_PRESETS_SORT, new WriteActionPresetsSort());
		fileActionMap.put(Identifiers.DATA_PRESETS_GROUP, new WriteActionPresetsGroup());
		fileActionMap.put(Identifiers.DATA_PRESETS_MASTER, new WriteActionPresetsMaster());
		fileActionMap.put(Identifiers.DATA_PRESETS_FILTER, new WriteActionPresetsFilter());

	}




	private void onChange(File file) {
		// TODO
		// create DataChange
		// send change to datahandler
	}




	@Override
	public Project createProject() {
		return null; // TODO
	}




	@Override
	public void applyChange(DataChange change, Project project) {
		System.out.println("apply change " + change.getIdentifier());
		// TODO write change to file (without triggering event from directory observer)
		WriteFileAction action = fileActionMap.get(change.getIdentifier());
		if (action != null) {
			action.onChange(change, project, handler);
		}

	}




	@Override
	public void addChangeListener(DataChangeListener listener) {
		listeners.add(listener);
	}




	@Override
	public void removeChangeListener(DataChangeListener listener) {
		listeners.remove(listener);
	}


}
