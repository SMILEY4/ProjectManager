package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.TextAttributeLogic;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.MultiTextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class ItemText extends SidebarItem {


	private MultiTextField area;




	public ItemText(TaskAttribute attribute, Task task) {
		super(attribute, task);

		VBox box = new VBox();
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().add(box);

		Label label = new Label(attribute.name.get() + ":");
		box.getChildren().add(label);

		area = new MultiTextField();
		area.setMultiline(TextAttributeLogic.getMultiline(attribute));
		setValueHeight();
		area.textProperty().addListener(((observable, oldValue, newValue) -> {
			TaskLogic.setValue(Data.projectProperty.get(), task, attribute, area.getText());
		}));
		box.getChildren().add(area);

		final Object objValue = TaskLogic.getValue(task, attribute);
		if (objValue != null && !(objValue instanceof NoValue)) {
			area.setText((String) objValue);
		} else {
			area.setText("");
		}

	}




	@Override
	public void dispose() {

	}




	private void setValueHeight() {
		final double height = TextAttributeLogic.getMultiline(getAttribute()) ? MultiTextField.calculateOptimalHeight(5) : 32;
		area.setMinSize(60, height);
		area.setPrefSize(10000, height);
		area.setMaxSize(10000, height);
	}

}
