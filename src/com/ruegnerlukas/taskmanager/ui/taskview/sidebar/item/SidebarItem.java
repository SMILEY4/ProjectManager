package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public abstract class SidebarItem extends HBox {


	public static SidebarItem createItem(Task task, TaskAttribute attribute) {
		if (attribute.data.getType() == TaskAttributeType.BOOLEAN) {
			return new BoolItem(task, attribute);
		}
		if (attribute.data.getType() == TaskAttributeType.CHOICE) {
			return new ChoiceItem(task, attribute);
		}
		if (attribute.data.getType() == TaskAttributeType.NUMBER) {
			return new NumberItem(task, attribute);
		}
		if (attribute.data.getType() == TaskAttributeType.TEXT) {
			return new TextItem(task, attribute);
		}
		return null;
	}




	public Task task;
	public TaskAttribute attribute;

	private Label noValue;
	private Parent valueNode;
	private Button button;




	protected SidebarItem(Task task, TaskAttribute attribute) {
		this.attribute = attribute;
		this.task = task;
		this.setMinSize(0, 32);
		this.setPrefSize(10000, 32);
		this.setMaxSize(10000, 10000);
		this.setSpacing(10);
		create(task, attribute);
	}




	private void create(Task task, TaskAttribute attribute) {

		// left - label
		Label label = new Label(attribute.name);
		label.setAlignment(Pos.CENTER_RIGHT);
		label.setMinSize(150, 32);
		label.setPrefSize(150, 32);
		label.setMaxSize(150, 32);
		this.getChildren().add(label);

		// right
		HBox boxRight = new HBox();
		this.getChildren().add(boxRight);

		// center - value
		valueNode = createValueField(task, attribute);

		// center - NoValue
		noValue = new Label("Empty");
		noValue.setAlignment(Pos.TOP_CENTER);
		noValue.setPrefSize(10000, 32);

		// pane value
		AnchorPane paneValue = new AnchorPane();
		paneValue.setMaxWidth(10000);
		paneValue.setPrefWidth(10000);
		paneValue.setMinHeight(32);
		paneValue.setMaxHeight(10000);
		paneValue.setPrefHeight(getFieldHeight());
		AnchorUtils.setAnchors(valueNode, 0, 0, 0, 0);
		AnchorUtils.setAnchors(noValue, 0, 0, 0, 0);
		paneValue.getChildren().add(valueNode);
		paneValue.getChildren().add(noValue);
		boxRight.getChildren().add(paneValue);

		// right - button
		button = new Button();
		button.setText("-");
		button.setMinSize(32, 32);
		button.setPrefSize(32, 32);
		button.setMaxSize(32, 32);
		boxRight.getChildren().add(button);

		button.setOnAction(event -> {
			if (noValue.isVisible()) {
				showValue();
			} else {
				emptyValue();
			}
		});

		if(attribute.data.usesDefault()) {
			button.setDisable(true);
			button.setVisible(false);
		}

		Logic.tasks.getAttributeValue(task, attribute.name, new Request<TaskAttributeValue>() {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				if(response.getValue() instanceof NoValue) {
					noValue.setVisible(true);
					valueNode.setVisible(false);
					button.setText("+");
				} else {
					noValue.setVisible(false);
					valueNode.setVisible(true);
					button.setText("-");
				}
			}
		});
	}




	private void showValue() {
		noValue.setVisible(false);
		valueNode.setVisible(true);
		button.setText("-");
		Logic.tasks.setAttributeValue(task, attribute, getValue());
	}




	private void emptyValue() {
		noValue.setVisible(true);
		valueNode.setVisible(false);
		button.setText("+");
		Logic.tasks.removeAttribute(task, attribute);
	}




	protected abstract double getFieldHeight();


	protected abstract Parent createValueField(Task task, TaskAttribute attribute);


	protected abstract TaskAttributeValue getValue();


	public abstract void dispose();


}
