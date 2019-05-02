package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.NoValue;
import com.ruegnerlukas.taskmanager.data.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


public class ItemDescription extends SidebarItem {


	private TextArea area;




	public ItemDescription(TaskAttribute attribute, Task task) {
		super(attribute, task);

		VBox box = new VBox();
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().add(box);

		Label label = new Label("Description:");
		box.getChildren().add(label);

		area = new TextArea();
		area.setMinSize(0, 40);
		area.setPrefSize(10000, 200);
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


}
