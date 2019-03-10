package com.ruegnerlukas.taskmanager.ui.taskview.sidebar;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
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
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TextValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.TaskView;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.item.SidebarItem;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
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

	private List<SidebarItem> items = new ArrayList<>();




	public Sidebar(TaskView taskView) {
		this.taskView = taskView;

		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_sidebar.fxml"), this);
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
		Logic.taskFlags.getAllFlags(new Request<TaskFlag[]>(true) {
			@Override
			public void onResponse(Response<TaskFlag[]> response) {
				choiceFlag.getItems().addAll(response.getValue());
			}
		});
		choiceFlag.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			Logic.tasks.setAttributeValue(currentTask, FlagAttributeData.NAME, new FlagValue(choiceFlag.getValue()));
		});


		// listen for taskAttribute-changes
		EventManager.registerListener(this, event -> {
			refresh();
		}, AttributeRemovedEvent.class, AttributeCreatedEvent.class, AttributeTypeChangedEvent.class, AttributeUpdatedEvent.class);


		// Delete Task
		btnDeleteTask.setOnAction(event -> {
			if (currentTask != null) {
				Logic.tasks.deleteTask(currentTask);
			}
		});

	}




	public void refresh() {

		if (currentTask == null) {
			return;
		}

		// description
		Logic.tasks.getAttributeValue(currentTask, DescriptionAttributeData.NAME, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				TextValue value = (TextValue) response.getValue();
				fieldDesc.setText(value.getText());
			}
		});

		// id
		Logic.tasks.getAttributeValue(currentTask, IDAttributeData.NAME, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				NumberValue value = (NumberValue) response.getValue();
				labelID.setText("T-" + value.getInt());
			}
		});

		// flag
		Logic.taskFlags.getAllFlags(new Request<TaskFlag[]>(true) {
			@Override
			public void onResponse(Response<TaskFlag[]> response) {
				choiceFlag.getItems().setAll(response.getValue());
			}
		});
		Logic.tasks.getAttributeValue(currentTask, FlagAttributeData.NAME, new Request<TaskAttributeValue>(true) {
			@Override
			public void onResponse(Response<TaskAttributeValue> response) {
				FlagValue value = (FlagValue) response.getValue();
				choiceFlag.getSelectionModel().select(value.getFlag());
			}
		});

		// task attributes
		for (SidebarItem item : items) {
			item.dispose();
		}
		boxAttribs.getChildren().clear();
		items.clear();
		Logic.attribute.getAttributes(new Request<List<TaskAttribute>>(true) {
			@Override
			public void onResponse(Response<List<TaskAttribute>> response) {
				List<TaskAttribute> attributes = response.getValue();
				for (int i = 0; i < attributes.size(); i++) {
					TaskAttribute attribute = attributes.get(i);
					SidebarItem item = SidebarItem.createItem(currentTask, attribute, Sidebar.this);
					if (item != null) {
						items.add(item);
						boxAttribs.getChildren().add(item);
					}
				}
			}
		});
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
