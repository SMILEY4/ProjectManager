package com.ruegnerlukas.taskmanager.ui.taskview.sidebar;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeCreatedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeRemovedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeTypeChangedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.AttributeUpdatedEvent;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.DescriptionAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.IDAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.NumberValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.TaskView;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item.SidebarItem;
import com.ruegnerlukas.taskmanager.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sidebar extends AnchorPane {


	public Task currentTask = null;
	public TaskView taskView;

	private Breadcrumb breadcrumb;
	@FXML private AnchorPane paneBreadcrumb;

	@FXML private VBox boxContent;
	@FXML private TextArea fieldDesc;
	@FXML private Label labelID;
	@FXML private ComboBox<TaskFlag> choiceFlag;
	@FXML private VBox boxAttribs;

	@FXML private Button btnDeleteTask;
	private int deleteTaskStep = 0;

	private List<SidebarItem> items = new ArrayList<>();
	private boolean suppressFlagRefesh = false;




	public Sidebar(TaskView taskView) {
		this.taskView = taskView;

		try {
			Parent root = UIDataHandler.loadFXML(UIModule.ELEMENT_SIDEBAR, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading Sidebar-FXML: " + e);
		}

		this.setPrefSize(10000, 200);

		create();

	}




	private void create() {

		// breadcrumb
		breadcrumb = new Breadcrumb() {
			@Override
			public boolean onStepBack(Task task) {
				taskView.onTaskSelected(task, true, false);
				return true;
			}




			@Override
			public boolean onJumpBack(Task task) {
				taskView.onTaskSelected(task, true, false);
				return true;
			}
		};
		AnchorUtils.setAnchors(breadcrumb, 0, 0, 0, 0);
		paneBreadcrumb.getChildren().add(breadcrumb);


		// description
		fieldDesc.setText("");
		fieldDesc.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (currentTask != null && !newValue) {
				Logic.tasks.setAttributeValue(currentTask, DescriptionAttributeData.NAME, new TextValue(fieldDesc.getText()));
			}
		});


		// id
		labelID.setText("-");


		// flag
		choiceFlag.setButtonCell(ComboboxUtils.createListCellFlag());
		choiceFlag.setCellFactory(param -> ComboboxUtils.createListCellFlag());
		choiceFlag.getItems().addAll(Logic.taskFlags.getAllFlags().getValue());
		choiceFlag.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(oldValue != newValue && oldValue != null && newValue != null) {
				suppressFlagRefesh = true;
				Logic.tasks.setAttributeValue(currentTask, FlagAttributeData.NAME, new FlagValue(choiceFlag.getValue()));
				suppressFlagRefesh = false;
			}
		});


		// listen for taskAttribute-changes
		EventManager.registerListener(this, event -> {
			refresh();
		}, AttributeRemovedEvent.class, AttributeCreatedEvent.class, AttributeTypeChangedEvent.class, AttributeUpdatedEvent.class);


		// Delete Task
		btnDeleteTask.getStyleClass().add("delete-task-default");
		btnDeleteTask.setOnAction(event -> {
			if (currentTask != null) {
				switch (deleteTaskStep) {
					case 0: {
						deleteTaskStep = 1;
						btnDeleteTask.setText("Delete Task ?");
						btnDeleteTask.getStyleClass().removeAll("delete-task-confirm", "delete-task-default");
						btnDeleteTask.getStyleClass().add("delete-task-confirm");
						break;
					}
					case 1: {
						Logic.tasks.deleteTask(currentTask);
						deleteTaskStep = 0;
						btnDeleteTask.setText("Delete Task");
						btnDeleteTask.getStyleClass().removeAll("delete-task-confirm", "delete-task-default");
						btnDeleteTask.getStyleClass().add("delete-task-default");
						break;
					}

				}

			}
		});

	}




	public void refresh() {

		// delete task style
		deleteTaskStep = 0;
		btnDeleteTask.setText("Delete Task");
		btnDeleteTask.getStyleClass().removeAll("delete-task-confirm", "delete-task-default");
		btnDeleteTask.getStyleClass().add("delete-task-default");

		if (currentTask == null) {
			return;
		}

		// description
		TextValue valueDesc = (TextValue) Logic.tasks.getAttributeValue(currentTask, DescriptionAttributeData.NAME).getValue();
		fieldDesc.setText(valueDesc.getText());

		// id
		NumberValue valueID = (NumberValue) Logic.tasks.getAttributeValue(currentTask, IDAttributeData.NAME).getValue();
		labelID.setText("T-" + valueID.getInt());

		// flag
		if (!suppressFlagRefesh) {
			FlagValue valueFlag = (FlagValue) Logic.tasks.getAttributeValue(currentTask, FlagAttributeData.NAME).getValue();
			choiceFlag.getItems().setAll(Logic.taskFlags.getAllFlags().getValue());
			choiceFlag.getSelectionModel().select(valueFlag.getFlag());
		}

		// task attributes
		for (SidebarItem item : items) {
			item.dispose();
		}
		boxAttribs.getChildren().clear();
		items.clear();

		List<TaskAttribute> attributes = Logic.attribute.getAttributes().getValue();
		for (int i = 0; i < attributes.size(); i++) {
			TaskAttribute attribute = attributes.get(i);
			SidebarItem item = SidebarItem.createItem(currentTask, attribute, Sidebar.this);
			if (item != null) {
				items.add(item);
				boxAttribs.getChildren().add(item);
			}
		}
	}




	public void showTask(Task task) {
		if (task == null) {
			this.currentTask = null;
			this.setVisible(false);
		} else {
			this.currentTask = task;
			this.setVisible(true);
			refresh();
		}
	}




	public Breadcrumb getBreadcrumb() {
		return breadcrumb;
	}


}
