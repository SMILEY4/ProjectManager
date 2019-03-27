package com.ruegnerlukas.taskmanager.ui.taskview.taskcard.cardattributes;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class DescriptionCardAttribute extends CardAttribute {


	protected DescriptionCardAttribute(Task task, TaskAttribute attribute) {
		super(task, attribute);
	}




	@Override
	protected Region getValueNode(TaskAttributeValue value) {
		Label label = new Label();
		if (value instanceof NoValue) {
			label.setText("No Value");
			label.setDisable(true);
		} else {
			label.setText(((TextValue) value).getText());
		}
		return label;
	}




	@Override
	protected Region getIconNode(TaskAttributeValue value) {
		return null;
	}

}
