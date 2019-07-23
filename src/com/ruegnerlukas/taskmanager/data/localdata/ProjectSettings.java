package com.ruegnerlukas.taskmanager.data.localdata;


import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;

public class ProjectSettings {

	public final SyncedProperty<String> name;
	public final SyncedProperty<Boolean> attributesLocked;
	public final SyncedProperty<Integer> idCounter;




	ProjectSettings(Project project) {
		name = new SyncedProperty<>(Identifiers.SETTINGS_PROJECT_NAME, null, project.dataHandler);
		attributesLocked = new SyncedProperty<>(Identifiers.SETTINGS_ATTRIBUTES_LOCKED, null, project.dataHandler);
		idCounter = new SyncedProperty<>(Identifiers.SETTINGS_IDCOUNTER, null, project.dataHandler);
	}




	public void dispose() {
		name.dispose();
		attributesLocked.dispose();
		idCounter.dispose();
	}

}
