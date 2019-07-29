package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.read;

import com.google.gson.Gson;
import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.JsonUtils;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOTaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.Project;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadActionAttributes {


	public List<POJOTaskAttribute> readAttributes(Project project, FileHandler handler) {

		List<POJOTaskAttribute> attributes = new ArrayList<>();
		List<File> files = handler.getAttributeFiles();

		for (File file : files) {
			POJOTaskAttribute attribute = readAttribute(project, file);
			if (attribute != null) {
				attributes.add(attribute);
			}
		}

		return attributes;
	}




	public POJOTaskAttribute readAttribute(Project project, File file) {

		try {

			Gson gson = JsonUtils.getGson(project);
			FileReader reader = new FileReader(file);

			POJOTaskAttribute data = gson.fromJson(reader, POJOTaskAttribute.class);
			reader.close();
			return data;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}