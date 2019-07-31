//package com.ruegnerlukas.taskmanager.data.externaldata.files.actions.read;
//
//import com.google.gson.Gson;
//import com.ruegnerlukas.taskmanager.data.externaldata.files.FileHandler;
//import com.ruegnerlukas.taskmanager.data.externaldata.files.JsonUtils;
//import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOTask;
//import com.ruegnerlukas.taskmanager.data.localdata.Project;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReadActionTasks {
//
//
//	public List<POJOTask> readTasks(Project project, FileHandler handler) {
//
//		List<POJOTask> tasks = new ArrayList<>();
//		List<File> files = handler.getTaskFiles();
//
//		for (File file : files) {
//			POJOTask task = readTask(project, file);
//			if (task != null) {
//				tasks.add(task);
//			}
//		}
//
//		return tasks;
//	}
//
//
//
//
//	private POJOTask readTask(Project project, File file) {
//
//		try {
//
//			Gson gson = JsonUtils.getGson(project);
//			FileReader reader = new FileReader(file);
//
//			POJOTask data = gson.fromJson(reader, POJOTask.class);
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
//}
