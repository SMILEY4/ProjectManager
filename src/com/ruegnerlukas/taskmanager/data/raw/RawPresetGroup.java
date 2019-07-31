package com.ruegnerlukas.taskmanager.data.raw;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;

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


}
