package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import javafx.scene.control.ComboBox;


public class ItemFlag extends SimpleSidebarItem {


	private ComboBox<TaskFlag> choiceFlag;




	public ItemFlag(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void create() {
		choiceFlag = new ComboBox<>();
		choiceFlag.setButtonCell(ComboboxUtils.createListCellFlag());
		choiceFlag.setCellFactory(param -> ComboboxUtils.createListCellFlag());
		choiceFlag.getItems().addAll(AttributeLogic.FLAG_LOGIC.getFlagList(getAttribute()));
		choiceFlag.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new FlagValue(newValue));
		});
		this.setValueNode(choiceFlag);
		this.setEmpty(false);
		this.setShowButton(false);
	}




	@Override
	protected void refresh() {
		final TaskFlag flag = ((FlagValue) TaskLogic.getValueOrDefault(getTask(), getAttribute())).getValue();
		choiceFlag.getSelectionModel().select(flag);
	}




	@Override
	public void dispose() {
		super.dispose();
	}




	@Override
	protected void onSetEmpty(boolean empty) {
		this.setEmpty(false);
	}

}
