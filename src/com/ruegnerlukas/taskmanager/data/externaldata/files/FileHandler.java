package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHandler {


	private static final String FILENAME_SETTINGS = "settings.json";
	private static final String FILENAME_ATTRIBUTE_DIRECTORY = "attributes";
	private static final String FILENAME_TASK_DIRECTORY = "tasks";
	private static final String FILENAME_PRESETS_SORT_DIRECTORY = "presets_sort";
	private static final String FILENAME_PRESETS_GROUP_DIRECTORY = "presets_group";
	private static final String FILENAME_PRESETS_MASTER_DIRECTORY = "presets_master";
	private static final String FILENAME_PRESETS_FILTER_DIRECTORY = "presets_filter";

	private final File rootDirectory;




	FileHandler(String pathRoot) {
		this.rootDirectory = new File(pathRoot);
	}




	public File getSettingsFile(boolean createIfNeccessary) {
		return getFile(FILENAME_SETTINGS, createIfNeccessary);
	}




	public File getAttributeFile(String attributeName, boolean createIfNeccessary) {
		return getFile(FILENAME_ATTRIBUTE_DIRECTORY + "/" + attributeName + ".json", createIfNeccessary);
	}




	public List<File> getAttributeFiles() {
		return getFiles(FILENAME_ATTRIBUTE_DIRECTORY);
	}




	public File getTaskFile(String taskID, boolean createIfNeccessary) {
		return getFile(FILENAME_TASK_DIRECTORY + "/" + taskID + ".json", createIfNeccessary);
	}




	public List<File> getTaskFiles() {
		return getFiles(FILENAME_TASK_DIRECTORY);
	}




	public File getPresetFilterFile(String name, boolean createIfNeccessary) {
		return getFile(FILENAME_PRESETS_FILTER_DIRECTORY + "/" + name + ".json", createIfNeccessary);
	}




	public List<File> getPresetFilterFiles() {
		return getFiles(FILENAME_PRESETS_FILTER_DIRECTORY);
	}




	public File getPresetGroupFile(String name, boolean createIfNeccessary) {
		return getFile(FILENAME_PRESETS_GROUP_DIRECTORY + "/" + name + ".json", createIfNeccessary);
	}




	public List<File> getPresetGroupFiles() {
		return getFiles(FILENAME_PRESETS_GROUP_DIRECTORY);
	}




	public File getPresetSortFile(String name, boolean createIfNeccessary) {
		return getFile(FILENAME_PRESETS_SORT_DIRECTORY + "/" + name + ".json", createIfNeccessary);
	}




	public List<File> getPresetSortFiles() {
		return getFiles(FILENAME_PRESETS_SORT_DIRECTORY);
	}




	public File getPresetMasterFile(String name, boolean createIfNeccessary) {
		return getFile(FILENAME_PRESETS_MASTER_DIRECTORY + "/" + name + ".json", createIfNeccessary);
	}




	public List<File> getPresetMasterFiles() {
		return getFiles(FILENAME_PRESETS_MASTER_DIRECTORY);
	}




	private List<File> getFiles(String pathDir) {
		List<File> files = new ArrayList<>();

		File dir = getFile(pathDir, false);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return files;
		}

		File[] arr = dir.listFiles();
		if (arr != null) {
			files.addAll(Arrays.asList(arr));
		}

		return files;
	}




	private File getFile(String pathFile, boolean createIfNeccessary) {
		File file = new File(rootDirectory.getAbsolutePath() + "/" + pathFile);
		if (!file.exists() && createIfNeccessary) {
			try {
				if (file.isDirectory()) {
					file.mkdirs();
				} else {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
			} catch (IOException e) {
				Logger.get().error(e);
			}
		}
		if (!file.exists()) {
			return null;
		} else {
			return file;
		}
	}


}
