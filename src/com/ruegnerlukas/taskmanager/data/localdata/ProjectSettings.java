package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;

public class ProjectSettings {


	public final SyncedProperty<String> name = new SyncedProperty<>("project.settings.project_name");
	public final SyncedProperty<Boolean> attributesLocked = new SyncedProperty<>("project.settings.attributes_locked", false);
	public final SyncedProperty<Integer> idCounter = new SyncedProperty<>("project.settings.id_counter", 1);




	public void dispose() {
		name.dispose();
		attributesLocked.dispose();
		idCounter.dispose();
	}

}
