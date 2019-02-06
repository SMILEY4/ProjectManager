package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.BoolValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;



public class BoolItem extends SidebarItem {


	protected BoolItem(Task task, TaskAttribute attribute) {
		super(task, attribute);

		// left - label
		VBox boxLeft = new VBox();
		boxLeft.setMinSize(0, 32);
		boxLeft.setPrefSize(10000, 32);
		boxLeft.setMaxSize(10000, 32);
		this.getChildren().add(boxLeft);

		Label label = new Label(attribute.name);
		label.setAlignment(Pos.CENTER_RIGHT);
		label.setMinSize(0, 32);
		label.setPrefSize(-1, 32);
		label.setMaxSize(10000, 32);
		boxLeft.getChildren().add(label);

		// right - checkbox
		VBox boxRight = new VBox();
		boxRight.setMinSize(0, 32);
		boxRight.setPrefSize(10000, 32);
		boxRight.setMaxSize(10000, 32);
		this.getChildren().add(boxRight);

		CheckBox checkbox = new CheckBox("");
		checkbox.setAlignment(Pos.CENTER_LEFT);
		checkbox.setMinSize(0, 32);
		checkbox.setPrefSize(-1, 32);
		checkbox.setMaxSize(10000, 32);
		boxRight.getChildren().add(checkbox);

		Logic.tasks.getAttributeValue(task, attribute.name, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				if(response.getValue() instanceof NoValue) {
					checkbox.setSelected(false);
				} else {
					checkbox.setSelected( ((BoolValue)response.getValue()).getBoolValue() );
				}
			}
		});

		checkbox.setOnAction(event -> {
			Logic.tasks.setAttributeValue(task, attribute, new BoolValue(checkbox.isSelected()));
		});

	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
