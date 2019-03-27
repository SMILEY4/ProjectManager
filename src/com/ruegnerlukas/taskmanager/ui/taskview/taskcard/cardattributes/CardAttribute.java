package com.ruegnerlukas.taskmanager.ui.taskview.taskcard.cardattributes;

import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public abstract class CardAttribute extends HBox {


	public static CardAttribute create(Task task, TaskAttribute attribute) {
		if (attribute.data.getType() == TaskAttributeType.BOOLEAN) {
			return new BoolCardAttribute(task, attribute);
		} else if (attribute.data.getType() == TaskAttributeType.CHOICE) {
			return new ChoiceCardAttribute(task, attribute);
		} else if (attribute.data.getType() == TaskAttributeType.DEPENDENCY) {
			return new DependencyCardAttribute(task, attribute);
		} else if (attribute.data.getType() == TaskAttributeType.DESCRIPTION) {
			return new DescriptionCardAttribute(task, attribute);
		} else if (attribute.data.getType() == TaskAttributeType.FLAG) {
			return new FlagCardAttribute(task, attribute);
		} else if (attribute.data.getType() == TaskAttributeType.ID) {
			return new IDCardAttribute(task, attribute);
		} else if (attribute.data.getType() == TaskAttributeType.NUMBER) {
			return new NumberCardAttribute(task, attribute);
		} else if (attribute.data.getType() == TaskAttributeType.TEXT) {
			return new TextCardAttribute(task, attribute);
		}
		return null;
	}




	protected Task task;
	protected TaskAttribute attribute;




	protected CardAttribute(Task task, TaskAttribute attribute) {
		this.task = task;
		this.attribute = attribute;

		this.setMinSize(0, 25);
		this.setPrefSize(10000, 25);
		this.setMaxSize(10000, 25);
		this.setSpacing(10);

		Label labelName = new Label(attribute.name + ":");
		labelName.setMinSize(0, 25);
		labelName.setPrefSize(10000, 25);
		labelName.setMaxSize(10000, 25);
		labelName.setAlignment(Pos.CENTER_RIGHT);
		this.getChildren().add(labelName);

		Region nodeValue = getValueNode(Logic.tasks.getAttributeValue(task, attribute.name).getValue());
		nodeValue.setMinSize(0, 25);
		nodeValue.setPrefSize(10000, 25);
		nodeValue.setMaxSize(10000, 25);
		this.getChildren().add(nodeValue);
	}




	public Region getIconNode() {
		return this.getIconNode(Logic.tasks.getAttributeValue(task, attribute.name).getValue());
	}




	protected abstract Region getValueNode(TaskAttributeValue value);


	protected abstract Region getIconNode(TaskAttributeValue value);


}
