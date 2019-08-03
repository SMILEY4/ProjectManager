package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskFlag;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.FlagValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.mutableelements.MutableCombobox;
import javafx.application.Platform;


public class ItemFlag extends SimpleSidebarItem {


	private MutableCombobox<TaskFlag> choiceFlag;




	public ItemFlag(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void create() {
		choiceFlag = new MutableCombobox<>();
		choiceFlag.setButtonCell(ComboboxUtils.createListCellFlag());
		choiceFlag.setCellFactory(param -> ComboboxUtils.createListCellFlag());
		choiceFlag.getItems().addAll(AttributeLogic.FLAG_LOGIC.getFlagList(getAttribute()));
		choiceFlag.addMutableSelectedItemListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new FlagValue(newValue));
			}
		});
		this.setValueNode(choiceFlag);
		this.setEmpty(false);
		this.setShowButton(true);
	}




	@Override
	protected void refresh() {
		choiceFlag.setMuted(true);

		final TaskFlag[] flags = AttributeLogic.FLAG_LOGIC.getFlagList(getAttribute());
		final TaskFlag flag = ((FlagValue) TaskLogic.getValueOrDefault(getTask(), getAttribute())).getValue();

		Platform.runLater(() -> {
			choiceFlag.setMuted(true);
			choiceFlag.getItems().setAll(flags);
			choiceFlag.getSelectionModel().select(flag);
			choiceFlag.setMuted(false);
		});
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
			final TaskFlag[] flagList = AttributeLogic.FLAG_LOGIC.getFlagList(getAttribute());

			if (flagList.length == 0) {
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NoValue());
				Platform.runLater(() -> {
					choiceFlag.setMuted(true);
					choiceFlag.getSelectionModel().clearSelection();
					choiceFlag.setMuted(false);
				});

			} else {
				TaskFlag flag = AttributeLogic.FLAG_LOGIC.getDefaultValue(getAttribute()).getValue();
				TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new FlagValue(flag));
				Platform.runLater(() -> {
					choiceFlag.setMuted(true);
					choiceFlag.getSelectionModel().select(flag);
					choiceFlag.setMuted(false);
				});
			}
		}

		this.setEmpty(empty);
	}

}
