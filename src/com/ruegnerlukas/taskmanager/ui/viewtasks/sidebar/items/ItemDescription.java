package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.DescriptionValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.events.AttributeValueChangeEvent;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


public class ItemDescription extends SidebarItem {


	private TextArea area;




	public ItemDescription(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
		setupControls();
		setupInitialValue();
		setupLogic();
	}




	@Override
	protected void onAttChangedEvent(AttributeValueChangeEvent e) {

	}




	private void setupControls() {
		VBox box = new VBox();
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().setAll(box);

		Label label = new Label("Description:");
		box.getChildren().add(label);

		area = new TextArea();
		area.setMinSize(0, 40);
		area.setPrefSize(10000, 200);
		box.getChildren().add(area);

	}




	private void setupInitialValue() {
		final TaskValue<?> objValue = TaskLogic.getValueOrDefault(getTask(), getAttribute());
		if (objValue != null && objValue.getAttType() != null) {
			area.setText(((DescriptionValue) objValue).getValue());
		} else {
			area.setText("");
		}
	}




	private void setupLogic() {
		area.textProperty().addListener(((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), getTask(), getAttribute(), new DescriptionValue(area.getText()));
		}));
	}




	@Override
	public void dispose() {
		super.dispose();
	}


}
