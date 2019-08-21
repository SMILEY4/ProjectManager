package com.ruegnerlukas.taskmanager.data.localdata;


import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;

public class ProjectSettings {


	public final SyncedProperty<String> name;
	public final SyncedProperty<Boolean> attributesLocked;

	public final SyncedProperty<Integer> attIDCounter;
	public final SyncedProperty<Integer> taskIDCounter;
	public final SyncedProperty<Integer> docIDCounter;




	ProjectSettings(Project project) {
		name = new SyncedProperty<>(Identifiers.SETTINGS_PROJECT_NAME, null, project.dataHandler, "");
		attributesLocked = new SyncedProperty<>(Identifiers.SETTINGS_ATTRIBUTES_LOCKED, null, project.dataHandler, false);
		attIDCounter = new SyncedProperty<>(Identifiers.SETTINGS_ATTRIBUTE_IDCOUNTER, null, project.dataHandler, 0);
		taskIDCounter = new SyncedProperty<>(Identifiers.SETTINGS_TASK_IDCOUNTER, null, project.dataHandler, 0);
		docIDCounter = new SyncedProperty<>(Identifiers.SETTINGS_DOC_IDCOUNTER, null, project.dataHandler, 0);
	}




	public void dispose() {
		name.dispose();
		attributesLocked.dispose();
		attIDCounter.dispose();
		taskIDCounter.dispose();
		docIDCounter.dispose();
	}

}
