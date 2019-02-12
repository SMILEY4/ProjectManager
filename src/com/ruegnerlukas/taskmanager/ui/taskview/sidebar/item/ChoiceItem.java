package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;

public class ChoiceItem extends SidebarItem {


	private ChoiceBox<String> choiceBox;




	protected ChoiceItem(Task task, TaskAttribute attribute) {
		super(task, attribute);
	}




	@Override
	protected double getFieldHeight() {
		return 32;
	}




	@Override
	protected Parent createValueField(Task task, TaskAttribute attribute) {

		choiceBox = new ChoiceBox<>();
		ChoiceAttributeData data = (ChoiceAttributeData) attribute.data;
		choiceBox.getItems().addAll(data.values);
		choiceBox.setMinSize(0, 32);
		choiceBox.setPrefSize(200, 32);
		choiceBox.setMaxSize(200, 32);

		Logic.tasks.getAttributeValue(task, attribute.name, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				if (response.getValue() instanceof NoValue) {
					choiceBox.getSelectionModel().select(null);
				} else {
					choiceBox.getSelectionModel().select(((TextValue) response.getValue()).getText());
				}
			}
		});

		choiceBox.setOnAction(event -> {
			Logic.tasks.setAttributeValue(task, attribute, new TextValue(choiceBox.getSelectionModel().getSelectedItem()));
		});

		return choiceBox;

	}




	@Override
	protected TaskAttributeValue getValue() {
		return new TextValue(choiceBox.getSelectionModel().getSelectedItem());
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}

}
