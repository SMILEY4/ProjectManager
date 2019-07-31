package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;

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


}
