package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.DataHandler;

public class Project {

	public static final DataHandler DATA_HANDLER = new DataHandler();

	public final ProjectSettings settings = new ProjectSettings();
	public final ProjectData data = new ProjectData();
	public final ProjectDataTemporary temporaryData = new ProjectDataTemporary();


	public void dispose() {
		settings.dispose();
		data.dispose();
		temporaryData.dispose();
	}

}
