package com.ruegnerlukas.taskmanager.data.localdata;

import com.ruegnerlukas.taskmanager.data.SyncedProperty;

public class ProjectSettings {


	public final SyncedProperty<String> name = new SyncedProperty<>("project.settings.projectname", String.class);
	public final SyncedProperty<Boolean> attributesLocked = new SyncedProperty<>("project.settings.attributeslocked", Boolean.class, false);
	public final SyncedProperty<Integer> idCounter = new SyncedProperty<>("project.settings.idcounter", Integer.class, 1);




	public void dispose() {
		name.dispose();
		attributesLocked.dispose();
		idCounter.dispose();
	}

}
