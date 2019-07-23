package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items.dependency;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.*;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class PopupDependency extends PopupBase {


	@FXML private VBox boxTasks;
	@FXML private Button btnCancel;
	@FXML private Button btnAdd;

	private final Task selectedTask;
	private final TaskAttribute attribute;




	public PopupDependency(Task selectedTask, TaskAttribute attribute) {
		super(400, 450);
		this.selectedTask = selectedTask;
		this.attribute = attribute;
	}




	@Override
	public void create() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_DEPENDENCY, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading DependencyPopup-FXML: " + e);
		}


		// button cancel / accept
		btnAdd.setOnAction(event -> onAccept());
		btnCancel.setOnAction(event -> onCancel());


		// load initial data
		List<Task> tasks = Data.projectProperty.get().data.tasks;
		Set<Task> dependencyList = new HashSet<>();

		TaskValue<?> valueDep = TaskLogic.getValueOrDefault(selectedTask, attribute);
		if (valueDep.getAttType() == AttributeType.DEPENDENCY) {
			dependencyList.addAll(Arrays.asList(((DependencyValue) valueDep).getValue()));
		}

		boxTasks.getChildren().clear();
		for (int i = 0, n = tasks.size(); i < n; i++) {
			Task task = tasks.get(i);
			boolean isSelected = dependencyList.contains(task);
			TaskItem item = new TaskItem(task, isSelected, task == selectedTask);
			boxTasks.getChildren().add(item);
		}

	}




	private void onCancel() {
		this.getStage().close();
	}




	private void onAccept() {

		List<Task> listSelected = new ArrayList<>();
		for (Node node : boxTasks.getChildren()) {
			TaskItem item = (TaskItem) node;
			if (item.isSelected) {
				listSelected.add(item.task);
			}
		}

		if (listSelected.isEmpty()) {
			TaskLogic.setValue(Data.projectProperty.get(), selectedTask, attribute, new NoValue());
		} else {
			Task[] arraySelected = new Task[listSelected.size()];
			for (int i = 0; i < listSelected.size(); i++) {
				arraySelected[i] = listSelected.get(i);
			}
			TaskLogic.setValue(Data.projectProperty.get(), selectedTask, attribute, new DependencyValue(arraySelected));
		}

		this.getStage().close();
	}


}






class TaskItem extends HBox {


	public final Task task;
	public boolean isSelected;




	public TaskItem(Task task, boolean isSelected, boolean disable) {
		this.task = task;
		this.isSelected = isSelected;

		this.setMinSize(0, 26);
		this.setPrefSize(100000, 26);
		this.setMaxSize(100000, 26);
		this.setSpacing(5);
		this.setPadding(new Insets(0, 5, 0, 5));
		this.setAlignment(Pos.CENTER_LEFT);

		CheckBox checkbox = new CheckBox("");
		checkbox.setSelected(isSelected);
		checkbox.setOnAction(e -> this.isSelected = checkbox.isSelected());
		this.getChildren().add(checkbox);

		Label labelTask = new Label();
		labelTask.setMinSize(60, 22);
		labelTask.setMaxSize(60, 22);
		this.getChildren().add(labelTask);

		Label labelDescr = new Label();
		labelDescr.setMinSize(0, 22);
		labelDescr.setMaxSize(100000, 22);
		this.getChildren().add(labelDescr);

		IDValue valueID = (IDValue) TaskLogic.getValueOrDefault(task, AttributeLogic.findAttributeByType(Data.projectProperty.get(), AttributeType.ID));
		DescriptionValue valueDescr = (DescriptionValue) TaskLogic.getValueOrDefault(task, AttributeLogic.findAttributeByType(Data.projectProperty.get(), AttributeType.DESCRIPTION));

		labelTask.setText("T-" + valueID.getValue());
		labelDescr.setText(valueDescr.getValue());

		this.setOnMouseClicked(e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				this.isSelected = !this.isSelected;
				checkbox.setSelected(this.isSelected);
			}
		});

		if(disable) {
			checkbox.setSelected(false);
			this.setDisable(true);
		}
	}


}



