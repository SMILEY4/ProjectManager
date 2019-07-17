package com.ruegnerlukas.taskmanager.utils.files;

import java.io.File;
import java.nio.file.WatchEvent;

public interface DirectoryListener {


	default boolean onEvent(WatchEvent.Kind<?> kind, File file) {
		return false;
	}

	boolean onCreate(File file);

	boolean onDelete(File file);

	boolean onModify(File file);

}
