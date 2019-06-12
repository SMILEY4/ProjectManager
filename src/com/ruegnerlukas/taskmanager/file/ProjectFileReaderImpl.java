package com.ruegnerlukas.taskmanager.file;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.file.plaindataobjects.PDOProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ProjectFileReaderImpl implements ProjectFileReader {


	@Override
	public Project readProject(File file) {
		try {
			Gson gson = FileHandler.buildGson();
			JsonReader reader = new JsonReader(new FileReader(file));
			PDOProject pojoProject = gson.fromJson(reader, PDOProject.class);
			return PDOProject.toProject(pojoProject);
		} catch (FileNotFoundException e) {
			Logger.get().error(e);
		}

		return null;
	}




	@Override
	public String getReaderVersion() {
		return "0.1";
	}

}
