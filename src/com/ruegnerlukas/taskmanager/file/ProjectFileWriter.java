package com.ruegnerlukas.taskmanager.file;

import com.ruegnerlukas.taskmanager.data.Project;

import java.io.File;

public interface ProjectFileWriter {


	String asJsonString(Project project);

	void writeToFile(Project project, File file);

	String getWriterVersion();


}
