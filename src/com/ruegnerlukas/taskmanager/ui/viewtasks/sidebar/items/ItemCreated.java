package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.CreatedValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ItemCreated extends SimpleSidebarItem {


	public ItemCreated(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void create() {
		final LocalDateTime created = ((CreatedValue) TaskLogic.getValueOrDefault(getTask(), getAttribute())).getValue();
		Label label = new Label(created.format(DateTimeFormatter.ISO_DATE_TIME));
		this.setValueNode(label);
		this.setEmpty(false);
		this.setShowButton(false);
	}




	@Override
	protected void refresh() {
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
