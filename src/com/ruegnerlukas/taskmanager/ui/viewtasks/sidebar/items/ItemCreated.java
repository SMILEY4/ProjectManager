package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ItemCreated extends SimpleSidebarItem {


	public ItemCreated(TaskAttribute attribute, Task task) {
		super(attribute, task);

		final LocalDateTime created = (LocalDateTime) TaskLogic.getValue(task, attribute);
		Label label = new Label(created.format(DateTimeFormatter.ISO_DATE_TIME));
		this.setValueNode(label);

		this.setEmpty(false);
		this.setShowButton(false);
	}




	@Override
	public void dispose() {

	}




	@Override
	protected void onSetEmpty(boolean empty) {
		this.setEmpty(false);
	}

}
