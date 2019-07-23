package com.ruegnerlukas.taskmanager.data.localdata;


import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;

public class ProjectSettings {


	public final SyncedProperty<String> name;
	public final SyncedProperty<Boolean> attributesLocked;

	public final SyncedProperty<Integer> attIDCounter;
	public final SyncedProperty<Integer> taskIDCounter;




	ProjectSettings(Project project) {
		name = new SyncedProperty<>(Identifiers.SETTINGS_PROJECT_NAME, null, project.dataHandler);
		attributesLocked = new SyncedProperty<>(Identifiers.SETTINGS_ATTRIBUTES_LOCKED, null, project.dataHandler);
		attIDCounter = new SyncedProperty<>(Identifiers.SETTINGS_ATTRIBUTE_IDCOUNTER, null, project.dataHandler);
		taskIDCounter = new SyncedProperty<>(Identifiers.SETTINGS_TASK_IDCOUNTER, null, project.dataHandler);
	}




	public void dispose() {
		name.dispose();
		attributesLocked.dispose();
		attIDCounter.dispose();
		taskIDCounter.dispose();
	}

}
