package com.ruegnerlukas.taskmanager.data.externaldata.files.utils;

import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;

public class POJOFlag {


	public TaskFlag.FlagColor color;
	public String name;




	public POJOFlag(TaskFlag flag) {
		this.color = flag.color.get();
		this.name = flag.name.get();
	}




	public TaskFlag findFlag(Project project) {

		TaskAttribute attribute = AttributeLogic.findAttributeByType(project, AttributeType.FLAG);
		if (attribute == null) {
			return null;
		}

		TaskFlag[] flags = AttributeLogic.FLAG_LOGIC.getFlagList(attribute);
		if (flags == null) {
			return null;
		}

		for (TaskFlag flag : flags) {
			if (flag.name.get().equals(this.name)) {
				return flag;
			}
		}
		return null;

	}




	public TaskFlag toFlag() {
		return new TaskFlag(this.name, this.color);
	}


}
