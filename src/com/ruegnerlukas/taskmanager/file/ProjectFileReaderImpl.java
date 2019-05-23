package com.ruegnerlukas.taskmanager.file;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.file.pojos.POJOProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ProjectFileReaderImpl implements ProjectFileReader {


	@Override
	public Project readProject(File file) {
		try {
			Gson gson = FileHandler.buildGson();
			JsonReader reader = new JsonReader(new FileReader(file));
			POJOProject pojoProject = gson.fromJson(reader, POJOProject.class);
			return POJOProject.toProject(pojoProject);
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
