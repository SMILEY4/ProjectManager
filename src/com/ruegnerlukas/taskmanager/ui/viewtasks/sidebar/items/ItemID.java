package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import javafx.scene.control.Label;


public class ItemID extends SimpleSidebarItem {


	private Label label;




	public ItemID(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void create() {
		label = new Label();
		this.setValueNode(label);
		this.setEmpty(false);
		this.setShowButton(false);
	}




	@Override
	protected void refresh() {
		final int id = ((IDValue) TaskLogic.getValueOrDefault(getTask(), getAttribute())).getValue();
		label.setText(Integer.toString(id));
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
