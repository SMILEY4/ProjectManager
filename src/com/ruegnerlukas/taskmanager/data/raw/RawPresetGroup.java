package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

import java.util.ArrayList;
import java.util.List;

public class RawPresetGroup {


	public String presetName;
	public String customHeader;
	public List<Integer> attributes = new ArrayList<>();




	public static RawPresetGroup toRaw(String presetName, TaskGroupData data) {
		RawPresetGroup raw = new RawPresetGroup();
		raw.presetName = presetName;
		raw.customHeader = data.customHeaderString;
		for (TaskAttribute attribute : data.attributes) {
			raw.attributes.add(attribute.id);
		}
		return raw;
	}




	public static TaskGroupData fromRaw(RawPresetGroup rawPreset, Project project) {
		TaskAttribute[] attributes = new TaskAttribute[rawPreset.attributes.size()];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = AttributeLogic.findAttributeByID(project, rawPreset.attributes.get(i));
		}
		return new TaskGroupData(rawPreset.customHeader, attributes);
	}

}
