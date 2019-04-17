package com.ruegnerlukas.taskmanager_old.data.taskAttributes;


import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.*;

public class TaskAttribute {


	public String name;
	public TaskAttributeData data;




	public TaskAttribute(String name, TaskAttributeType type) {
		this.name = name;
		createNewData(type);
	}




	public boolean createNewData(TaskAttributeType type) {
		if(type == null) {
			return false;
		}
		switch (type) {
			case ID: {
				data = new IDAttributeData();
				return true;
			}
			case FLAG: {
				data = new FlagAttributeData();
				return true;
			}
			case DESCRIPTION: {
				data = new DescriptionAttributeData();
				return true;
			}
			case TEXT: {
				data = new TextAttributeData();
				return true;
			}
			case NUMBER: {
				data = new NumberAttributeData();
				return true;
			}
			case BOOLEAN: {
				data = new BoolAttributeData();
				return true;
			}
			case CHOICE: {
				data = new ChoiceAttributeData();
				return true;
			}
			case DEPENDENCY: {
				data = new DependencyAttributeData();
				return true;
			}
			default: {
				return false;
			}
		}
	}


}