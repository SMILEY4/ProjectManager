package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.LastUpdatedValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ItemLastUpdated extends SimpleSidebarItem {


	private Label label;




	public ItemLastUpdated(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
	}




	@Override
	protected void setupControls() {
		label = new Label();
		this.setValueNode(label);

		this.setEmpty(false);
		this.setShowButton(false);
	}




	@Override
	protected void setupInitialValue() {
		final LocalDateTime lastUpdated = ((LastUpdatedValue) TaskLogic.getValueOrDefault(getTask(), getAttribute())).getValue();
		label.setText(lastUpdated.format(DateTimeFormatter.ISO_DATE_TIME));
	}




	@Override
	protected void setupLogic() {

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
