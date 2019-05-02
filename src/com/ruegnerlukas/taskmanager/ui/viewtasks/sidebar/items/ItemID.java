package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import javafx.scene.control.Label;


public class ItemID extends SimpleSidebarItem {


	public ItemID(TaskAttribute attribute, Task task) {
		super(attribute, task);

		final int id = (Integer) TaskLogic.getValue(task, attribute);
		Label label = new Label(Integer.toString(id));
		this.setValueNode(label);

		this.setEmpty(false);
		this.setShowButton(false);
	}




	@Override
	protected void onSetEmpty(boolean empty) {
		this.setEmpty(false);
	}

}
