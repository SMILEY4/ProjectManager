package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.google.gson.Gson;
import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write.*;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.raw.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalFileHandler implements ExternalDataHandler {


	private Map<String, WriteFileAction> fileActionMap = new HashMap<>();

	private List<DataChangeListener> listeners = new ArrayList<>();
	private FileHandler handler;




	public ExternalFileHandler(String directory) {
		handler = new FileHandler(directory);
		fileActionMap.put(Identifiers.SETTINGS_PROJECT_NAME, new WriteActionSettings());
		fileActionMap.put(Identifiers.SETTINGS_ATTRIBUTES_LOCKED, new WriteActionSettings());
		fileActionMap.put(Identifiers.SETTINGS_TASK_IDCOUNTER, new WriteActionSettings());
		fileActionMap.put(Identifiers.SETTINGS_DOC_IDCOUNTER, new WriteActionSettings());
		fileActionMap.put(Identifiers.SETTINGS_ATTRIBUTE_IDCOUNTER, new WriteActionSettings());
		fileActionMap.put(Identifiers.DATA_ATTRIBUTE_LIST, new WriteActionListAttributes());
		fileActionMap.put(Identifiers.DATA_TASK_LIST, new WriteActionListTasks());
		fileActionMap.put(Identifiers.DATA_PRESETS_SORT, new WriteActionPresetsSort());
		fileActionMap.put(Identifiers.DATA_PRESETS_GROUP, new WriteActionPresetsGroup());
		fileActionMap.put(Identifiers.DATA_PRESETS_MASTER, new WriteActionPresetsMaster());
		fileActionMap.put(Identifiers.DATA_PRESETS_FILTER, new WriteActionPresetsFilter());
		fileActionMap.put(Identifiers.DATA_DOC_FILE_LIST, new WriteActionListDocumentation());
	}




	@Override
	public Project loadProject() {

		handler.createBackup();

		RawProject rawProject = loadRawProject();
		if (rawProject == null) {
			return null;
		}

		return RawProject.fromRaw(rawProject, this);
	}




	public RawProject loadRawProject() {

		// get files
		File fileSettings = handler.getSettingsFile(false);
		List<File> filesAttributes = handler.getAttributeFiles();
		List<File> filesTasks = handler.getTaskFiles();
		List<File> filesDoc = handler.getDocFiles();
		List<File> filesPresetFilter = handler.getPresetFilterFiles();
		List<File> filesPresetGroup = handler.getPresetGroupFiles();
		List<File> filesPresetSort = handler.getPresetSortFiles();
		List<File> filesPresetMaster = handler.getPresetMasterFiles();

		// stop, no settings-file found
		if (fileSettings == null) {
			Logger.get().error("Could not load Project from file." + System.lineSeparator() + "No Settings-File found in " + this.handler.rootDirectory.getAbsolutePath());
			return null;
		}

		// create raw project from json-files
		try {

			Gson gson = JsonUtils.getGson();

			RawProject rawProject = new RawProject();

			// settings
			FileReader readerSettings = new FileReader(fileSettings);
			rawProject.settings = gson.fromJson(readerSettings, RawSettings.class);
			readerSettings.close();

			// attributes
			for (File fileAttribute : filesAttributes) {
				FileReader readerAttribute = new FileReader(fileAttribute);
				rawProject.attributes.add(gson.fromJson(readerAttribute, RawTaskAttribute.class));
				readerAttribute.close();
			}

			// tasks
			for (File fileTask : filesTasks) {
				FileReader readerTask = new FileReader(fileTask);
				rawProject.tasks.add(gson.fromJson(readerTask, RawTask.class));
				readerTask.close();
			}

			// docs
			for (File fileDoc : filesDoc) {
				FileReader readerDoc = new FileReader(fileDoc);
				rawProject.docFiles.add(gson.fromJson(readerDoc, RawDocumentationFile.class));
				readerDoc.close();
			}

			// presets
			for (File filePresetFilter : filesPresetFilter) {
				FileReader readerFilter = new FileReader(filePresetFilter);
				rawProject.presetsFilter.add(gson.fromJson(readerFilter, RawPresetFilter.class));
				readerFilter.close();
			}
			for (File filePresetGroup : filesPresetGroup) {
				FileReader readerGroup = new FileReader(filePresetGroup);
				rawProject.presetsGroup.add(gson.fromJson(readerGroup, RawPresetGroup.class));
				readerGroup.close();
			}
			for (File filePresetSort : filesPresetSort) {
				FileReader readerSort = new FileReader(filePresetSort);
				rawProject.presetsSort.add(gson.fromJson(readerSort, RawPresetSort.class));
				readerSort.close();
			}
			for (File filePresetMaster : filesPresetMaster) {
				FileReader readerMaster = new FileReader(filePresetMaster);
				rawProject.presetsMaster.add(gson.fromJson(readerMaster, RawPresetMaster.class));
				readerMaster.close();
			}

			return rawProject;

		} catch (IOException e) {
			Logger.get().error("Error while loading project from file." + System.lineSeparator() + "Directory: " + this.handler.rootDirectory.getAbsolutePath(), e);
		}

		return null;
	}




	@Override
	public void applyChange(DataChange change, Project project) {
		WriteFileAction action = fileActionMap.get(change.getIdentifier());
		System.out.println("ACTION " + change.getIdentifier() + "  " + action);
		if (action != null) {
			action.onChange(change, project, handler);
		}

	}




	@Override
	public void addChangeListener(DataChangeListener listener) {
		listeners.add(listener);
	}




	@Override
	public void removeChangeListener(DataChangeListener listener) {
		listeners.remove(listener);
	}


}
