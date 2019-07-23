package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.*;
import com.ruegnerlukas.taskmanager.data.localdata.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalFileHandler implements ExternalDataHandler {


	private Map<String, FileAction> fileActionMap = new HashMap<>();

	private List<DataChangeListener> listeners = new ArrayList<>();
	private FileHandler handler;




	public ExternalFileHandler() {
		this("D:\\LukasRuegner\\Programmieren\\Java\\Workspace\\SimpleTaskManager\\data\\Test_Project_1"); // TODO TEMP
	}




	public ExternalFileHandler(String directory) {
		handler = new FileHandler(directory);
		fileActionMap.put(Identifiers.SETTINGS_PROJECT_NAME, new ActionSettingsName());
		fileActionMap.put(Identifiers.SETTINGS_ATTRIBUTES_LOCKED, new ActionSettingsAttribsLock());
		fileActionMap.put(Identifiers.SETTINGS_IDCOUNTER, new ActionSettingsIDCounter());
		fileActionMap.put(Identifiers.DATA_ATTRIBUTE_LIST, new ActionListAttributes());
		fileActionMap.put(Identifiers.DATA_TASK_LIST, new ActionListTasks());
		fileActionMap.put(Identifiers.DATA_PRESETS_SORT, new ActionPresetsSort());
		fileActionMap.put(Identifiers.DATA_PRESETS_GROUP, new ActionPresetsGroup());
		fileActionMap.put(Identifiers.DATA_PRESETS_MASTER, new ActionPresetsMaster());
		fileActionMap.put(Identifiers.DATA_PRESETS_FILTER, new ActionPresetsFilter());

	}




	private void onChange(File file) {
		// TODO
		// create DataChange
		// send change to datahandler
	}




	@Override
	public void applyChange(DataChange change, Project project) {
		System.out.println("local change: " + change);
		// TODO write change to file (without triggering event from directory observer)

		FileAction action = fileActionMap.get(change.getIdentifier());
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
