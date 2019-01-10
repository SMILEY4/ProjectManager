package com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes;


import com.ruegnerlukas.taskmanager.logic_v2.data.taskAttributes.requirements.*;

public class TaskAttribute {


	public String name;
	public TaskAttributeRequirement data;
	
	
	public TaskAttribute(String name, TaskAttributeType type) {
		this.name = name;
		createRequirement(type);
	}






	public void createRequirement(TaskAttributeType type) {
		if(type == TaskAttributeType.ID) {
			data = new IDAttributeRequirement();
		}
		if(type == TaskAttributeType.FLAG) {
			data = new FlagAttributeRequirement();
		}
		if(type == TaskAttributeType.DESCRIPTION) {
			data = new DescriptionAttributeRequirement();
		}
		if(type == TaskAttributeType.TEXT) {
			data = new TextAttributeRequirement();
		}
		if(type == TaskAttributeType.NUMBER) {
			data = new NumberAttributeRequirement();
		}
		if(type == TaskAttributeType.BOOLEAN) {
			data = new BoolAttributeRequirement();
		}
		if(type == TaskAttributeType.CHOICE) {
			data = new ChoiceAttributeRequirement();
		}
	}

	
	
	
}
