package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.externaldata.ExternalDataHandler;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RawProject {


	public RawSettings settings;

	public List<RawTaskAttribute> attributes = new ArrayList<>();

	public List<RawTask> tasks = new ArrayList<>();

	public List<RawPresetFilter> presetsFilter = new ArrayList<>();
	public List<RawPresetGroup> presetsGroup = new ArrayList<>();
	public List<RawPresetSort> presetsSort = new ArrayList<>();
	public List<RawPresetMaster> presetsMaster = new ArrayList<>();




	public static RawProject toRaw(Project project) {
		RawProject raw = new RawProject();
		raw.settings = RawSettings.toRaw(project.settings);
		for (TaskAttribute attribute : project.data.attributes) {
			raw.attributes.add(RawTaskAttribute.toRaw(attribute));
		}
		for (Task task : project.data.tasks) {
			raw.tasks.add(RawTask.toRaw(task));
		}
		for (Map.Entry<String, FilterCriteria> entry : project.data.presetsFilter.entrySet()) {
			raw.presetsFilter.add(RawPresetFilter.toRaw(entry.getKey(), entry.getValue()));
		}
		for (Map.Entry<String, TaskGroupData> entry : project.data.presetsGroup.entrySet()) {
			raw.presetsGroup.add(RawPresetGroup.toRaw(entry.getKey(), entry.getValue()));
		}
		for (Map.Entry<String, SortData> entry : project.data.presetsSort.entrySet()) {
			raw.presetsSort.add(RawPresetSort.toRaw(entry.getKey(), entry.getValue()));
		}
		for (Map.Entry<String, MasterPreset> entry : project.data.presetsMaster.entrySet()) {
			raw.presetsMaster.add(RawPresetMaster.toRaw(entry.getKey(), entry.getValue()));
		}
		return raw;
	}




	public static Project fromRaw(RawProject rawProject, ExternalDataHandler externalDataHandler) {
		Project project = new Project(externalDataHandler);

		project.dataHandler.setMuted(true);

		// settings
		project.settings.name.set(rawProject.settings.name);
		project.settings.attributesLocked.set(rawProject.settings.attributesLocked);
		project.settings.attIDCounter.set(rawProject.settings.idCounterAttributes);
		project.settings.taskIDCounter.set(rawProject.settings.idCounterTasks);

		// attributes
		for (RawTaskAttribute rawAttribute : rawProject.attributes) {
			AttributeType t = rawAttribute.type;
			TaskAttribute attribute = RawTaskAttribute.baseFromRaw(rawAttribute, project);
			project.data.attributes.add(attribute);
			RawTaskAttribute.fromRaw(attribute, rawAttribute, rawProject, project);
		}

		// tasks
		for (RawTask rawTask : rawProject.tasks) {
			if (TaskLogic.findTaskByID(project, rawTask.id) == null) {
				Task task = new Task(rawTask.id, project);
				project.data.tasks.add(task);
				RawTask.fromRaw(task, rawTask, rawProject, project);
			}
		}

		// presets filter
		for (RawPresetFilter rawPreset : rawProject.presetsFilter) {
			project.data.presetsFilter.put(rawPreset.presetName, RawPresetFilter.fromRaw(rawPreset, project));
		}

		// presets group
		for (RawPresetGroup rawPreset : rawProject.presetsGroup) {
			project.data.presetsGroup.put(rawPreset.presetName, RawPresetGroup.fromRaw(rawPreset, project));
		}

		// presets sort
		for (RawPresetSort rawPreset : rawProject.presetsSort) {
			project.data.presetsSort.put(rawPreset.presetName, RawPresetSort.fromRaw(rawPreset, project));
		}

		// presets master
		for (RawPresetMaster rawPreset : rawProject.presetsMaster) {
			project.data.presetsMaster.put(rawPreset.presetName, RawPresetMaster.fromRaw(rawPreset));
		}

		project.dataHandler.setMuted(false);
		return project;
	}

}
