package com.ruegnerlukas.taskmanager_old.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.ChoiceAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NoValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.Sidebar;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item.SidebarItem;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;

public class ChoiceItem extends SidebarItem {


	private ComboBox<String> choiceBox;




	protected ChoiceItem(Task task, TaskAttribute attribute, Sidebar sidebar) {
		super(task, attribute, sidebar);
		this.setId("item_choice");
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

		choiceBox = new ComboBox<>();
		ChoiceAttributeData data = (ChoiceAttributeData) attribute.data;
		choiceBox.getItems().addAll(data.values);
		choiceBox.setMinSize(0, 32);
		choiceBox.setPrefSize(200, 32);
		choiceBox.setMaxSize(200, 32);

		Response<TaskAttributeValue> response = Logic.tasks.getAttributeValue(task, attribute.name);
		if (response.getValue() instanceof NoValue) {
			choiceBox.getSelectionModel().select(null);
		} else {
			choiceBox.getSelectionModel().select(((TextValue) response.getValue()).getText());
		}

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