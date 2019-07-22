package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExternalFileHandler implements ExternalDataHandler {


	private List<DataChangeListener> listeners = new ArrayList<>();




	public ExternalFileHandler() {
		this("D:\\LukasRuegner\\Programmieren\\Java\\Workspace\\SimpleTaskManager\\data\\Test_Project_1"); // TODO TEMP
	}




	public ExternalFileHandler(String directory) {
	}




	private void onChange(File file) {
		// TODO
		// create DataChange
		// send change to datahandler
	}




	@Override
	public void applyChange(DataChange change) {
		System.out.println("local change: " + change);
		// TODO
		// write change to file
		// update checksums
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
