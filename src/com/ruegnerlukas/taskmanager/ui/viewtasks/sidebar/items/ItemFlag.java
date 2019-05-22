package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.TaskFlagAttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import javafx.scene.control.ComboBox;


public class ItemFlag extends SimpleSidebarItem {


	private ComboBox<TaskFlag> choiceFlag;




	public ItemFlag(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void setupControls() {
		choiceFlag = new ComboBox<>();
		choiceFlag.setButtonCell(ComboboxUtils.createListCellFlag());
		choiceFlag.setCellFactory(param -> ComboboxUtils.createListCellFlag());
		choiceFlag.getItems().addAll(TaskFlagAttributeLogic.getFlagList(getAttribute()));
		this.setValueNode(choiceFlag);

		this.setEmpty(false);
		this.setShowButton(false);
	}




	@Override
	protected void setupInitialValue() {
		final TaskFlag flag = ((FlagValue) TaskLogic.getValueOrDefault(getTask(), getAttribute())).getValue();
		choiceFlag.getSelectionModel().select(flag);
	}




	@Override
	protected void setupLogic() {
		choiceFlag.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new FlagValue(newValue));
		});
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
