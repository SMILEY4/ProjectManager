package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.dependency;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.IDValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.content.TaskContent;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.ItemDependency;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ItemDep extends HBox {


	public final ItemDependency parent;
	public final Task task;




	public ItemDep(ItemDependency parent, Task task) {
		this.parent = parent;
		this.task = task;
		this.getStyleClass().add("dependency-item");

		this.setMinSize(0, 22);
		this.setMaxSize(100000, 22);
		this.setSpacing(5);

		Label label0 = new Label("-");
		label0.getStyleClass().addAll("dependency-item-dash", "dependency-item-label");
		this.getChildren().add(label0);

		final IDValue valueID = (IDValue) TaskLogic.getValueOrDefault(task, AttributeLogic.findAttributeByType(Data.projectProperty.get(), AttributeType.ID));
		Label labelTask = new Label("T-" + valueID.getValue());
		labelTask.getStyleClass().addAll("dependency-item-id", "dependency-item-label");
		this.getChildren().add(labelTask);

		Button btnRemove = new Button("x");
		btnRemove.getStyleClass().add("dependency-item-remove");
		btnRemove.setMinSize(22, 22);
		btnRemove.setMaxSize(22, 22);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.5);
		this.getChildren().add(btnRemove);

		btnRemove.setOnAction(event -> parent.removeDependency(this));

		labelTask.setOnMouseClicked(e -> {
			parent.getSidebar().getTaskView().getContent().selectTask(task, TaskContent.SELECTION_LINK);
		});

	}


}
