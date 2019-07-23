package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.DataHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;

public class Project {


	public final DataHandler dataHandler;

	public final ProjectSettings settings;
	public final ProjectData data;
	public final ProjectDataTemporary temporaryData;




	public Project(ExternalDataHandler externalDataHandler) {
		this.dataHandler = new DataHandler(this, externalDataHandler);
		this.settings = new ProjectSettings(this);
		this.data = new ProjectData(this);
		this.temporaryData = new ProjectDataTemporary(this);
	}




	public void dispose() {
		settings.dispose();
		data.dispose();
		temporaryData.dispose();
	}

}
