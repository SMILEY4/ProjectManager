package com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.SyncRequest;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.TaskCreatedEvent;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.Sidebar;
import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DependencyItem extends SidebarItem {


	private VBox boxTasksDep;
	private VBox boxTasksPre;




	protected DependencyItem(Task task, TaskAttribute attribute, Sidebar sidebar) {
		super(task, attribute, sidebar);
		this.setId("item_dependency");
	}




	@Override
	protected boolean isComplexItem() {
		return true;
	}




	@Override
	protected double getFieldHeight() {
		return 0;
	}




	@Override
	protected Parent createValueField(Task task, TaskAttribute attribute) {

		VBox content = new VBox();
		content.setMinWidth(0);
		content.setSpacing(5);
		content.setPadding(new Insets(0, 0, 0, 10));

		// label - depends on
		Label labelDep = new Label("Depends On");
		content.getChildren().add(labelDep);

		// box tasks - depends on
		boxTasksDep = new VBox();
		boxTasksDep.setMinWidth(0);
		boxTasksDep.setSpacing(3);
		boxTasksDep.setPadding(new Insets(0, 0, 0, 10));
		content.getChildren().add(boxTasksDep);


		// box add depends on
		HBox boxAdd = new HBox();
		boxAdd.setSpacing(2);
		boxAdd.setMinWidth(0);
		boxTasksDep.setPadding(new Insets(0, 0, 0, 10));
		content.getChildren().add(boxAdd);

		// choice add depends on
		ComboBox<Task> choiceTask = new ComboBox<>();
		choiceTask.setButtonCell(ComboboxUtils.createListCellTask());
		choiceTask.setCellFactory(param -> ComboboxUtils.createListCellTask());
		boxAdd.getChildren().add(choiceTask);
		choiceTask.setPrefWidth(85);
		setPossibleTasks(choiceTask);

		EventManager.registerListener(this, event -> {
			setPossibleTasks(choiceTask);
		}, TaskCreatedEvent.class);


		// button add depends on
		Button btnAdd = new Button();
		btnAdd.setPrefWidth(85);
		btnAdd.setText("Add Task");
		boxAdd.getChildren().add(btnAdd);

		btnAdd.setOnAction(event -> {
			Logic.dependencies.createDependency(task, choiceTask.getValue(), attribute);
			addDependsOn(choiceTask.getValue());
			setPossibleTasks(choiceTask);
		});


		// label - as prerequisite
		Label labelPre = new Label("As Prerequisite");
		content.getChildren().add(labelPre);

		// box tasks - as prerequisite
		boxTasksPre = new VBox();
		boxTasksPre.setMinWidth(0);
		boxTasksPre.setSpacing(3);
		boxTasksPre.setPadding(new Insets(0, 0, 0, 10));
		content.getChildren().add(boxTasksPre);

		// fill
		for(Task t : Logic.dependencies.getPrerequisitesOfInternal(task, attribute)) {
			addDependsOn(t);
		}
		for(Task t : Logic.dependencies.getDependentOnInternal(task, attribute)) {
			addAsPrerequisite(t);
		}

		return content;
	}




	private void addDependsOn(Task task) {

		HBox box = new HBox();
		box.setMinWidth(0);
		box.setSpacing(10);
		boxTasksDep.getChildren().add(box);

		Label labelID = new Label();
		labelID.setText("T-" + task.getID());
		box.getChildren().add(labelID);

		labelID.setOnMouseClicked(event -> {
			sidebar.taskView.onTaskSelected(task, true, true);
		});

	}




	private void addAsPrerequisite(Task task) {

		HBox box = new HBox();
		box.setMinWidth(0);
		box.setSpacing(10);
		boxTasksPre.getChildren().add(box);

		Label labelID = new Label();
		labelID.setText("T-" + task.getID());
		box.getChildren().add(labelID);

		labelID.setOnMouseClicked(event -> {
			sidebar.taskView.onTaskSelected(task, true, true);
		});

	}




	private List<Task> getDependencies() {
		List<Task> list = new ArrayList<>();

		for (Node node : boxTasksDep.getChildren()) {
			if (node instanceof HBox) {
				HBox box = (HBox) node;
				if (box.getChildren().get(0) instanceof Label) {
					Label label = (Label) box.getChildren().get(0);
					int id = Integer.parseInt(label.getText().trim().split("-")[1]);
					SyncRequest<Task> request = new SyncRequest<>();
					Logic.tasks.getTaskByID(id, request);
					Response<Task> response = request.getResponse();
					if (response.getState() == Response.State.SUCCESS) {
						list.add(response.getValue());
					}
				}
			}
		}

		return list;
	}




	private void setPossibleTasks(ComboBox<Task> choiceTask) {

		choiceTask.getItems().clear();

		List<Task> exclude = getDependencies();

		Logic.tasks.getTasks(new Request<List<Task>>(true) {
			@Override
			public void onResponse(Response<List<Task>> response) {
				for (Task t : response.getValue()) {
					if (t != task && !exclude.contains(t)) {
						choiceTask.getItems().add(t);
					}
				}
			}
		});

		choiceTask.getSelectionModel().select(0);
	}




	@Override
	protected TaskAttributeValue getValue() {
		return new TaskArrayValue(); // TODO : all prerequisites of this task
	}




	@Override
	public void dispose() {
		EventManager.deregisterListeners(this);
	}




	@Override
	protected void showValue() {
	}




	@Override
	protected void emptyValue() {
	}

}
