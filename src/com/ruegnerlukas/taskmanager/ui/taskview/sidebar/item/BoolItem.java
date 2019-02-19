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
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;


public class BoolItem extends SidebarItem {


	private CheckBox checkbox;




	protected BoolItem(Task task, TaskAttribute attribute) {
		super(task, attribute);
	}




	@Override
	protected boolean isComplexItem() {
		return false;
	}




	@Override
	protected double getFieldHeight() {
		return 32;
	}




	@Override
	protected Parent createValueField(Task task, TaskAttribute attribute) {

		checkbox = new CheckBox("");
		checkbox.setAlignment(Pos.CENTER_LEFT);
		checkbox.setMinSize(0, 32);
		checkbox.setPrefSize(-1, 32);
		checkbox.setMaxSize(10000, 32);

		Logic.tasks.getAttributeValue(task, attribute.name, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				if (response.getValue() instanceof NoValue) {
					checkbox.setSelected(false);
				} else {
					checkbox.setSelected(((BoolValue) response.getValue()).getBoolValue());
				}
			}
		});

		checkbox.setOnAction(event -> {
			Logic.tasks.setAttributeValue(task, attribute, new BoolValue(checkbox.isSelected()));
		});

		return checkbox;
	}




	@Override
	protected TaskAttributeValue getValue() {
		return new BoolValue(checkbox.isSelected());
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
