package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.change.NestedChange;
import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.ActionSettingsAttribsLock;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.ActionSettingsIDCounter;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.ActionSettingsName;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.FileAction;

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
		fileActionMap.put(Identifiers.SETTINGS_PROJECT_NAME, new ActionSettingsName(handler));
		fileActionMap.put(Identifiers.SETTINGS_ATTRIBUTES_LOCKED, new ActionSettingsAttribsLock(handler));
		fileActionMap.put(Identifiers.SETTINGS_IDCOUNTER, new ActionSettingsIDCounter(handler));
	}




	private void onChange(File file) {
		// TODO
		// create DataChange
		// send change to datahandler
	}




	@Override
	public void applyChange(DataChange change) {
		System.out.println("local change: " + change);
		// TODO write change to file (without triggering event from directory observer)


		if (change.getType() != DataChange.ChangeType.NESTED) {
			FileAction action = fileActionMap.get(change.getIdentifier());
			if (action != null) {
				action.onChange(change);
			}

		} else {
			NestedChange nestedChange = (NestedChange) change;
			// todo ...
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
