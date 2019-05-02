package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.TaskFlagAttributeLogic;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import javafx.scene.control.ComboBox;


public class ItemFlag extends SimpleSidebarItem {


	public ItemFlag(TaskAttribute attribute, Task task) {
		super(attribute, task);

		final TaskFlag flag = (TaskFlag) TaskLogic.getValue(task, attribute);

		ComboBox<TaskFlag> choiceFlag = new ComboBox<>();
		choiceFlag.setButtonCell(ComboboxUtils.createListCellFlag());
		choiceFlag.setCellFactory(param -> ComboboxUtils.createListCellFlag());
		choiceFlag.getItems().addAll(TaskFlagAttributeLogic.getFlagList(attribute));
		choiceFlag.getSelectionModel().select(flag);
		choiceFlag.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), task, attribute, newValue);
		});
		this.setValueNode(choiceFlag);

		this.setEmpty(false);
		this.setShowButton(false);
	}




	@Override
	protected void onSetEmpty(boolean empty) {
		this.setEmpty(false);
	}

}
