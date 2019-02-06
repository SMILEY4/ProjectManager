package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import javafx.scene.layout.HBox;

public abstract class SidebarItem extends HBox {


	public TaskAttribute attribute;



	protected SidebarItem(Task task, TaskAttribute attribute) {
		this.attribute = attribute;
		this.setMinSize(0, 32);
		this.setPrefSize(10000, 32);
		this.setMaxSize(10000, 10000);
		this.setSpacing(10);
	}




	public abstract void dispose();




	public static SidebarItem create(Task task, TaskAttribute attribute) {
		if(attribute.data.getType() == TaskAttributeType.BOOLEAN) {
			return new BoolItem(task, attribute);
		}
		if(attribute.data.getType() == TaskAttributeType.CHOICE) {
			return new ChoiceItem(task, attribute);
		}
		if(attribute.data.getType() == TaskAttributeType.NUMBER) {
			return new NumberItem(task, attribute);
		}
		if(attribute.data.getType() == TaskAttributeType.TEXT) {
			return new TextItem(task, attribute);
		}
		return null;
	}

}
