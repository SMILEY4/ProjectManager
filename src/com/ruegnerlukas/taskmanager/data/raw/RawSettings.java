package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.ProjectSettings;

public class RawSettings {


	public String name;
	public boolean attributesLocked;
	public int idCounterAttributes;
	public int idCounterTasks;
	public int idCounterDoc;




	public static RawSettings toRaw(ProjectSettings settings) {
		RawSettings raw = new RawSettings();
		raw.name = settings.name.get();
		raw.attributesLocked = settings.attributesLocked.get();
		raw.idCounterAttributes = settings.attIDCounter.get();
		raw.idCounterTasks = settings.taskIDCounter.get();
		raw.idCounterDoc = settings.docIDCounter.get();
		return raw;
	}


}
