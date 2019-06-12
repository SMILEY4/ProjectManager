package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.BoolValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.NoValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import javafx.scene.control.CheckBox;


public class ItemBoolean extends SimpleSidebarItem {


	private CheckBox checkBox;




	public ItemBoolean(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void create() {
		checkBox = new CheckBox();
		checkBox.setText("");
		checkBox.setOnAction(event -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new BoolValue(checkBox.isSelected()));
		});
		this.setValueNode(checkBox);
		this.setShowButton(true);
	}




	@Override
	protected void refresh() {
		final TaskValue<?> objValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());
		if (objValue != null && objValue.getAttType() != null) {
			checkBox.setSelected(((BoolValue) objValue).getValue());
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
			final boolean value = false;
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new BoolValue(value));
			checkBox.setSelected(value);
		}
		this.setEmpty(empty);
	}

}
