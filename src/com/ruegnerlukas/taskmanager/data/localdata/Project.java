package com.ruegnerlukas.taskmanager.data.localdata;

public class Project {


	public final ProjectSettings settings = new ProjectSettings();
	public final ProjectData data = new ProjectData();
	public final ProjectDataTemporary temporaryData = new ProjectDataTemporary();




	public void dispose() {
		settings.dispose();
		data.dispose();
		temporaryData.dispose();
	}

}