package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import javafx.scene.layout.AnchorPane;

public class SidebarItem extends AnchorPane {


	public static SidebarItem createItem(TaskAttribute attribute, Task task) {
		switch (attribute.type.get()) {
			case ID:
				return new ItemID(attribute, task);
			case DESCRIPTION:
				return new ItemDescription(attribute, task);
			case CREATED:
				return new ItemCreated(attribute, task);
			case LAST_UPDATED:
				return new ItemLastUpdated(attribute, task);
			case FLAG:
				return new ItemFlag(attribute, task);
			case TEXT:
				return new ItemText(attribute, task);
			case NUMBER:
				return new ItemNumber(attribute, task);
			case BOOLEAN:
				return new ItemBoolean(attribute, task);
			case CHOICE:
				return new ItemChoice(attribute, task);
			case DATE:
				return new ItemDate(attribute, task);
			case DEPENDENCY:
				return null; // TODO
			default:
				return null;
		}
	}




	private final TaskAttribute attribute;
	private final Task task;




	public SidebarItem(TaskAttribute attribute, Task task) {
		this.attribute = attribute;
		this.task = task;
	}




	public TaskAttribute getAttribute() {
		return attribute;
	}




	public Task getTask() {
		return task;
	}

}
