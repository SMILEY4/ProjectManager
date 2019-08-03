package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.DescriptionValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


public class ItemDescription extends SidebarItem {


	private TextArea area;
	private FXMapEntryChangeListener<TaskAttributeData, TaskValue<?>> listener;




	public ItemDescription(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
		create();
		refresh();
	}




	@Override
	protected void onAttChangedEvent() {
		refresh();
	}




	private void create() {
		VBox box = new VBox();
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().setAll(box);

		Label label = new Label("Description:");
		box.getChildren().add(label);

		area = new TextArea();
		area.setMinSize(0, 40);
		area.setPrefSize(10000, 200);
		box.getChildren().add(area);

		area.textProperty().addListener(((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new DescriptionValue(area.getText()));
		}));

		setOnAttribNameChanged(e -> {
			label.setText(getAttribute().name.get() + ":");
		});

		listener = new FXMapEntryChangeListener<TaskAttributeData, TaskValue<?>>(getTask().getValues(), getAttribute()) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends TaskAttributeData, ? extends TaskValue<?>> c) {
				refresh();
			}
		};
	}




	/**
	 * Updates the displayed {@link TaskValue}.
	 */
	private void refresh() {
		final TaskValue<?> objValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());
		if (objValue != null && objValue.getAttType() != null) {
			area.setText(((DescriptionValue) objValue).getValue());
		} else {
			area.setText("");
		}
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
		super.dispose();
	}


}
