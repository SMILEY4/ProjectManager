package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;

import java.util.ArrayList;
import java.util.List;

public class POJOPresetGroup {


	public String presetName;
	public String customHeader;
	public List<String> attributes;




	public POJOPresetGroup(String presetName, TaskGroupData data) {
		this.presetName = presetName;
		this.customHeader = data.customHeaderString;
		this.attributes = new ArrayList<>();
		for (TaskAttribute attribute : data.attributes) {
			this.attributes.add(attribute.name.get());
		}


	}

}
