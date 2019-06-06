package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.ChoiceValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import javafx.scene.control.ComboBox;


public class ItemChoice extends SimpleSidebarItem {


	private ComboBox<String> choice;




	public ItemChoice(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void create() {
		choice = new ComboBox<>();
		choice.getItems().addAll(AttributeLogic.CHOICE_LOGIC.getValueList(getAttribute()));
		choice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new ChoiceValue(newValue));
		});
		this.setValueNode(choice);
		this.setShowButton(true);
	}




	@Override
	protected void refresh() {
		final TaskValue<?> objValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());
		if (objValue != null && objValue.getAttType() != null) {
			choice.getSelectionModel().select(((ChoiceValue) objValue).getValue());
		}
	}




	@Override
	public void dispose() {
		super.dispose();
	}




	@Override
	protected void onSetEmpty(boolean empty) {
		if (empty) {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NoValue());
		} else {
			final String[] valueList = AttributeLogic.CHOICE_LOGIC.getValueList(getAttribute());
			if (valueList.length == 0) {
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NoValue());
				choice.getSelectionModel().clearSelection();
			} else {
				final String value = valueList[0];
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new ChoiceValue(value));
				choice.getSelectionModel().select(value);
			}
		}
		this.setEmpty(empty);
	}

}
