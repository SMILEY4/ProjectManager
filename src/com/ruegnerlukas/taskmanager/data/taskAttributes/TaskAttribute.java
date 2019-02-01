package com.ruegnerlukas.taskmanager.data.taskAttributes;


import com.ruegnerlukas.taskmanager.data.taskAttributes.data.*;

public class TaskAttribute {


	public String name;
	public TaskAttributeData data;
	
	
	public TaskAttribute(String name, TaskAttributeType type) {
		this.name = name;
		createRequirement(type);
	}






	public void createRequirement(TaskAttributeType type) {
		if(type == TaskAttributeType.ID) {
			data = new IDAttributeData();
		}
		if(type == TaskAttributeType.FLAG) {
			data = new FlagAttributeData();
		}
		if(type == TaskAttributeType.DESCRIPTION) {
			data = new DescriptionAttributeData();
		}
		if(type == TaskAttributeType.TEXT) {
			data = new TextAttributeData();
		}
		if(type == TaskAttributeType.NUMBER) {
			data = new NumberAttributeData();
		}
		if(type == TaskAttributeType.BOOLEAN) {
			data = new BoolAttributeData();
		}
		if(type == TaskAttributeType.CHOICE) {
			data = new ChoiceAttributeData();
		}
	}

	
	
	
}
