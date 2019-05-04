package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.NumberAttributeLogic;
import javafx.scene.control.CheckBox;


public class ItemBoolean extends SimpleSidebarItem {


	private CheckBox checkBox;




	public ItemBoolean(TaskAttribute attribute, Task task) {
		super(attribute, task);

		checkBox = new CheckBox();
		checkBox.setText("");
		checkBox.setOnAction(event -> {
			TaskLogic.setValue(Data.projectProperty.get(), task, attribute, checkBox.isSelected());
		});

		this.setValueNode(checkBox);
		this.setShowButton(!NumberAttributeLogic.getUseDefault(attribute));

		final Object objValue = TaskLogic.getValue(task, attribute);
		if (objValue != null && !(objValue instanceof NoValue)) {
			checkBox.setSelected((Boolean) objValue);
			this.setEmpty(false);
		} else {
			setEmpty(true);
		}

	}




	@Override
	public void dispose() {

	}




	@Override
	protected void onSetEmpty(boolean empty) {
		this.setEmpty(empty);
		if (empty) {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new NoValue());
		} else {
			final boolean value = false;
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), value);
			checkBox.setSelected(value);
		}
	}

}
