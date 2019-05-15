package com.ruegnerlukas.taskmanager_old.ui.taskview.taskcard.cardattributes;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.ui.taskview.taskcard.cardattributes.CardAttribute;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ChoiceCardAttribute extends CardAttribute {


	protected ChoiceCardAttribute(Task task, TaskAttribute attribute) {
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
		if (!(value instanceof NoValue)) {
			Label label = new Label(((TextValue) value).getText().substring(0, Math.min(5, super.attribute.name.length())));

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
