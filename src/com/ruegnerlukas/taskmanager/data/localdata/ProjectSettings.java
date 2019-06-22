package com.ruegnerlukas.taskmanager.data.localdata;


import com.ruegnerlukas.taskmanager.data.syncedelements.SyncedProperty;

public class ProjectSettings {


	public final SyncedProperty<String> name = new SyncedProperty<>("project.settings.project_name", null);
	public final SyncedProperty<Boolean> attributesLocked = new SyncedProperty<>("project.settings.attributes_locked", null);
	public final SyncedProperty<Integer> idCounter = new SyncedProperty<>("project.settings.id_counter", null);




	public ProjectSettings() {
		attributesLocked.set(false);
		idCounter.set(0);
	}




	public void dispose() {
		name.dispose();
		attributesLocked.dispose();
		idCounter.dispose();
	}

}
