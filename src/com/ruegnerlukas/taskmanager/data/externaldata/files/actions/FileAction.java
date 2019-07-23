package com.ruegnerlukas.taskmanager.data.externaldata.files.actions;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.localdata.Project;

public abstract class FileAction {


	public abstract void onChange(DataChange change, Project project, FileHandler fileHandler);

}
