package com.ruegnerlukas.taskmanager.ui.taskview.taskcard.cardattributes;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class DependencyCardAttribute extends CardAttribute {


	protected DependencyCardAttribute(Task task, TaskAttribute attribute) {
		super(task, attribute);
	}




	@Override
	protected Region getValueNode(TaskAttributeValue value) {
		Label label = new Label();
		if (value instanceof NoValue) {
			label.setText("No Value");
			label.setDisable(true);
		} else {
			StringBuilder str = new StringBuilder();
			if (((TaskArrayValue) value).getTasks().length == 0) {
				str.append(" - ");
			} else {
				for (Task task : ((TaskArrayValue) value).getTasks()) {
					str.append("T-").append(task.getID()).append(", ");
				}
			}
			label.setText(str.toString());
		}
		return label;
	}




	@Override
	protected Region getIconNode(TaskAttributeValue value) {
		if (!(value instanceof NoValue) && ((TaskArrayValue) value).getTasks().length != 0) {
			Label label = new Label("D");
			label.setMinWidth(20);
			label.setMaxWidth(20);
			return label;
		} else {
			return null;
		}
	}

}
