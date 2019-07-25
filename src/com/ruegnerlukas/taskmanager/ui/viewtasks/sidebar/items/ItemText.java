package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TextValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.MultiTextField;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class ItemText extends SidebarItem {


	private MultiTextField area;
	private FXMapEntryChangeListener<TaskAttribute, TaskValue<?>> listener;




	public ItemText(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
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

		Label label = new Label(getAttribute().name.get() + ":");
		box.getChildren().add(label);

		area = new MultiTextField();
		area.setMultiline(AttributeLogic.TEXT_LOGIC.getMultiline(getAttribute()));
		setValueHeight();
		box.getChildren().add(area);

		area.textProperty().addListener(((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new TextValue(area.getText()));
		}));

		setOnAttribNameChanged(e -> {
			label.setText(getAttribute().name.get() + ":");
		});

		listener = new FXMapEntryChangeListener<TaskAttribute, TaskValue<?>>(getTask().values, getAttribute()) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends TaskAttribute, ? extends TaskValue<?>> c) {
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
			area.setText(((TextValue) objValue).getValue());
		} else {
			area.setText("");
		}
	}




	@Override
	public void dispose() {
		listener.removeFromAll();
		super.dispose();
	}




	/**
	 * Sets the required height of this item / the text area.
	 * */
	private void setValueHeight() {
		final double height = AttributeLogic.TEXT_LOGIC.getMultiline(getAttribute()) ? MultiTextField.calculateOptimalHeight(5) : 32;
		area.setMinSize(60, height);
		area.setPrefSize(10000, height);
		area.setMaxSize(10000, height);
	}

}
