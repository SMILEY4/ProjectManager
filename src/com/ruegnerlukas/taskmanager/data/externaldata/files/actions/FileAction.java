package com.ruegnerlukas.taskmanager.data.externaldata.files.actions;

import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;

public abstract class FileAction {


	protected final FileHandler fileHandler;




	public FileAction(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}




	public abstract void onChange(DataChange change);

}
