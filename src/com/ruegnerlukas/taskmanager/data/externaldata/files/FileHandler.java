package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;

import java.io.File;
import java.io.IOException;

public class FileHandler {


	private static final String FILENAME_SETTINGS = "settings.json";

	private final File rootDirectory;




	FileHandler(String pathRoot) {
		this.rootDirectory = new File(pathRoot);
	}




	public File getSettingsFile() {
		File file = new File(rootDirectory.getAbsolutePath() + "/" + FILENAME_SETTINGS);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Logger.get().error(e);
			}
		}
		return file;
	}


}
