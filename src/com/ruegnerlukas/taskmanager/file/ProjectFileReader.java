package com.ruegnerlukas.taskmanager.file;

import com.ruegnerlukas.taskmanager.data.Project;

import java.io.File;

public interface ProjectFileReader {


	Project readProject(File file);

	String getReaderVersion();

}
