package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.LastUpdatedValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ItemLastUpdated extends SimpleSidebarItem {


	private Label label;
	private FXMapEntryChangeListener<TaskAttributeData, TaskValue<?>> listener;




	public ItemLastUpdated(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
		this.getStyleClass().add("sidebar-item-last-updated");
	}




	@Override
	protected void create() {
		label = new Label();
		label.getStyleClass().add("sidebar-item-value");
		this.setValueNode(label);
		this.setEmpty(false);
		this.setShowButton(false);
		listener = new FXMapEntryChangeListener<TaskAttributeData, TaskValue<?>>(getTask().getValues(), getAttribute()) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends TaskAttributeData, ? extends TaskValue<?>> c) {
				refresh();
			}
		};
	}




	@Override
	protected void refresh() {
		final LocalDateTime lastUpdated = ((LastUpdatedValue) TaskLogic.getValueOrDefault(getTask(), getAttribute())).getValue();
		label.setText(lastUpdated.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")));
	}





	@Override
	public void dispose() {
		listener.removeFromAll();
		super.dispose();
	}




	@Override
	protected void onSetEmpty(boolean empty) {
		this.setEmpty(false);
	}

}
