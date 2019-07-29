package com.ruegnerlukas.taskmanager.data.externaldata.files;

import com.ruegnerlukas.taskmanager.data.Identifiers;
import com.ruegnerlukas.taskmanager.data.change.DataChange;
import com.ruegnerlukas.taskmanager.data.change.DataChangeListener;
import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.read.ReadActionAttributes;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.read.ReadActionSettings;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.read.ReadActionTasks;
import com.ruegnerlukas.taskmanager.data.externaldata.files.actions.write.*;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOFlag;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOSettings;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOTask;
import com.ruegnerlukas.taskmanager.data.externaldata.files.utils.POJOTaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.io.File;
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
		fileActionMap.put(Identifiers.SETTINGS_PROJECT_NAME, new WriteActionSettingsName());
		fileActionMap.put(Identifiers.SETTINGS_ATTRIBUTES_LOCKED, new WriteActionSettingsAttribsLock());
		fileActionMap.put(Identifiers.SETTINGS_TASK_IDCOUNTER, new WriteActionSettingsTaskIDCounter());
		fileActionMap.put(Identifiers.SETTINGS_ATTRIBUTE_IDCOUNTER, new WriteActionSettingsAttributeIDCounter());
		fileActionMap.put(Identifiers.DATA_ATTRIBUTE_LIST, new WriteActionListAttributes());
		fileActionMap.put(Identifiers.DATA_TASK_LIST, new WriteActionListTasks());
		fileActionMap.put(Identifiers.DATA_PRESETS_SORT, new WriteActionPresetsSort());
		fileActionMap.put(Identifiers.DATA_PRESETS_GROUP, new WriteActionPresetsGroup());
		fileActionMap.put(Identifiers.DATA_PRESETS_MASTER, new WriteActionPresetsMaster());
		fileActionMap.put(Identifiers.DATA_PRESETS_FILTER, new WriteActionPresetsFilter());

	}




	private void onChange(File file) {
		// TODO
		// create DataChange
		// send change to datahandler
	}




	@Override
	public Project createProject() {
		Project project = new Project(this);

		// TODO prevent changes from generating -> unwanted "feedback"

		// settings
		POJOSettings pojoSettings = new ReadActionSettings().readSettings(project, handler);
		project.settings.name.set(pojoSettings.projectName);
		project.settings.attributesLocked.set(pojoSettings.attributesLocked);
		project.settings.attIDCounter.set(pojoSettings.attributeIDCounter);
		project.settings.taskIDCounter.set(pojoSettings.taskIDCounter);

		// attributes
		List<POJOTaskAttribute> pojoAttributes = new ReadActionAttributes().readAttributes(project, handler);
		for (POJOTaskAttribute pojoAttribute : pojoAttributes) {
			TaskAttribute attribute = pojoAttribute.toAttribute(project);
			if (attribute != null) {
				project.data.attributes.add(attribute);
			}
		}

		// tasks
		List<POJOTask> pojoTasks = new ReadActionTasks().readTasks(project, handler);

		for (POJOTask pojoTask : pojoTasks) {
			Task task = pojoTask.toTask(project);
			if (task != null) {
				project.data.tasks.add(task);
			}
		}

		// fix task flags
		TaskAttribute flagAttribute = AttributeLogic.findAttributeByType(project, AttributeType.FLAG);

		FlagValue defaultValue = AttributeLogic.FLAG_LOGIC.getDefaultValue(flagAttribute);
		TaskFlag defaultFlag = new POJOFlag(defaultValue.getValue()).findFlag(project);
		flagAttribute.values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(new FlagValue(defaultFlag)));

		for(Task task : project.data.tasks) {
			TaskValue<?> flagValue = TaskLogic.getValueOrDefault(task, flagAttribute);
			if (flagValue != null && flagValue.getAttType() == AttributeType.FLAG) {
				TaskFlag flag = new POJOFlag(((FlagValue) flagValue).getValue()).findFlag(project);
				task.values.put(flagAttribute, new FlagValue(flag));
			}
		}

		// presets
		// TODO

		return project;
	}




	@Override
	public void applyChange(DataChange change, Project project) {
		// TODO write change to file (without triggering event from directory observer)
		WriteFileAction action = fileActionMap.get(change.getIdentifier());
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
