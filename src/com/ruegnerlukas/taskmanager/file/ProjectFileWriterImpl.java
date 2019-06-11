package com.ruegnerlukas.taskmanager.file;

import com.google.gson.Gson;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.file.plaindataobjects.PDOProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProjectFileWriterImpl implements ProjectFileWriter {


	@Override
	public String asJsonString(Project project) {
		PDOProject pojoProject = PDOProject.fromProject(project);
		Gson gson = FileHandler.buildGson();
		return gson.toJson(pojoProject);
	}




	@Override
	public void writeToFile(Project project, File file) {
		try {
			PDOProject pojoProject = PDOProject.fromProject(project);
			Gson gson = FileHandler.buildGson();
			FileWriter writer = new FileWriter(file);
			gson.toJson(pojoProject, writer);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	@Override
	public String getWriterVersion() {
		return "0.1";
	}

}




