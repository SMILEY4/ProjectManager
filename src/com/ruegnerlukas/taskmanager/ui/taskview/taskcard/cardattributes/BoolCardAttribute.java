package com.ruegnerlukas.taskmanager.ui.taskview.taskcard.cardattributes;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class BoolCardAttribute extends CardAttribute {


	protected BoolCardAttribute(Task task, TaskAttribute attribute) {
		super(task, attribute);
	}




	@Override
	protected Region getValueNode(TaskAttributeValue value) {
		Label label = new Label();
		if (value instanceof NoValue) {
			label.setText("No Value");
			label.setDisable(true);
		} else {
			label.setText(((BoolValue) value).getBoolValue() ? "True" : "False");
		}
		return label;
	}




	@Override
	protected Region getIconNode(TaskAttributeValue value) {
		if (!(value instanceof NoValue) && ((BoolValue) value).getBoolValue()) {
			Label label = new Label(super.attribute.name.substring(0, Math.min(5, super.attribute.name.length())));

			VBox tmpBox = new VBox();
			Scene scene = new Scene(tmpBox, 400, 50);
			tmpBox.getChildren().add(label);
			tmpBox.applyCss();
			tmpBox.layout();
			double width = Math.max(20, Math.min(50, label.getWidth()));
			tmpBox.getChildren().clear();

			label.setMinWidth(width);
			label.setMaxWidth(width);

			return label;
		} else {
			return null;
		}
	}

}
