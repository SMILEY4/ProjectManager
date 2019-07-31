//package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.read;
//
//import com.google.gson.Gson;
//import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
//import com.ruegnerlukas.taskmanager.data.externaldata.files.JsonUtils;
//import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOSettings;
//import com.ruegnerlukas.taskmanager.data.localdata.Project;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class ReadActionSettings {
//
//
//	public POJOSettings readSettings(Project project, FileHandler handler) {
//
//		File file = handler.getSettingsFile(false);
//		if (file == null) {
//			return null;
//		}
//
//		try {
//
//			Gson gson = JsonUtils.getGson(project);
//			FileReader reader = new FileReader(file);
//
//			POJOSettings data = gson.fromJson(reader, POJOSettings.class);
//			reader.close();
//			return data;
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//
//}
