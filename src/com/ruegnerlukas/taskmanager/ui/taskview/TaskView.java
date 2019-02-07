package com.ruegnerlukas.taskmanager.ui.taskview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.SyncRequest;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.FilterCriteriaChangedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.GroupOrderChangedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.RefreshTaskDisplayRecommendationEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.SortElementsChangedEvent;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.Task;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroup;
import com.ruegnerlukas.taskmanager.data.groups.TaskGroupData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskFlag;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.FlagAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.data.TaskAttributeData;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagArrayValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.FlagValue;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.TabContent;
import com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.FilterPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.groupPopup.GroupByPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.Sidebar;
import com.ruegnerlukas.taskmanager.ui.taskview.sortPopup.SortPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.taskcard.TaskCard;
import com.ruegnerlukas.taskmanager.ui.taskview.tasklist.TaskList;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.label.LabelUtils;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class TaskView extends AnchorPane implements TabContent {


	public static final String TITLE = "Tasks";

	@FXML private AnchorPane rootTaskView;

	@FXML private AnchorPane paneHeader;
	@FXML private AnchorPane paneHeaderBadges;
	private Label badgeFilter;
	private Label badgeGroupBy;
	private Label badgeSort;

	@FXML private Button btnFilter;
	@FXML private Button btnGroup;
	@FXML private Button btnSort;
	@FXML private Button btnActions;

	@FXML private Label labelNTasks;

	@FXML private SplitPane splitContent;
	@FXML private AnchorPane paneContent;

	@FXML private ScrollPane scollTasks;
	@FXML private AnchorPane paneTasks;
	@FXML private HBox boxTasks;

	private Sidebar sidebar;
	@FXML private Label labelHideSidebar;
	@FXML private AnchorPane paneSidebar;
	private boolean sidebarHidden = true;

	private TaskGroupData lastTaskGroupData = null;
	private boolean taskViewVisible = false;
	private boolean refreshOnShow = false;




	public TaskView() {
		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_view_tasks.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading TaskView-FXML: " + e);
		}


		// FOR TESTING
		for (int i = 0; i < 40; i++) {
			Logic.tasks.createNewTask();
		}

		FlagArrayValue flags = new FlagArrayValue(
				new TaskFlag(TaskFlag.FlagColor.RED, "Bug Critical"),
				new TaskFlag(TaskFlag.FlagColor.ORANGE, "Bug"),
				new TaskFlag(TaskFlag.FlagColor.CYAN, "Feature"),
				new TaskFlag(TaskFlag.FlagColor.BLUE, "Feature Critical"),
				new TaskFlag(TaskFlag.FlagColor.GRAY, "Unassigned")
		);
		Logic.attribute.updateTaskAttribute(FlagAttributeData.NAME, TaskAttributeData.Var.FLAG_ATT_FLAGS, flags);

		Logic.tasks.getTasks(new Request<List<Task>>(true) {
			@Override
			public void onResponse(Response<List<Task>> response) {
				List<Task> tasks = response.getValue();
				for (Task task : tasks) {

					Logic.attribute.getAttribute(TaskAttributeType.FLAG, new Request<TaskAttribute>(false) {
						@Override
						public void onResponse(Response<TaskAttribute> response) {
							TaskAttribute flagAttribute = response.getValue();
							FlagValue value = new FlagValue(((FlagAttributeData) flagAttribute.data).flags[new Random().nextInt(((FlagAttributeData) flagAttribute.data).flags.length)]);
							Logic.tasks.setAttributeValue(task, flagAttribute, value);
						}
					});
				}
			}
		});

		// END TESTING


		setupListeners();
		createHeader();
		createSidebar();
		refreshTaskView();
	}




	private void createHeader() {

		// badges
		paneHeaderBadges.setMouseTransparent(true);
		badgeFilter = createButtonBadge(paneHeaderBadges, btnFilter);
		badgeGroupBy = createButtonBadge(paneHeaderBadges, btnGroup);
		badgeSort = createButtonBadge(paneHeaderBadges, btnSort);


		// button filter
		btnFilter.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			FilterPopup popup = new FilterPopup(stage);
			Scene scene = new Scene(popup, 1000, 400);
			stage.setScene(scene);
			stage.show();
		});


		// button group-by
		btnGroup.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			GroupByPopup popup = new GroupByPopup(stage);
			Scene scene = new Scene(popup, 500, 300);
			stage.setScene(scene);
			stage.show();
		});


		// button sort
		btnSort.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(ViewManager.getPrimaryStage());
			SortPopup popup = new SortPopup(stage);
			Scene scene = new Scene(popup, 600, 300);
			stage.setScene(scene);
			stage.show();
		});


		// label number of tasks
		updateNTaskLabel();

		// hamburger menu
		ButtonUtils.makeIconButton(btnActions, SVGIcons.HAMBURGER, 0.7f, "white");
		btnActions.setOnAction(event -> {
			ContextMenu popup = new ContextMenu();
			/*
			for(int i=0; i<actionFunctions.size(); i++) {
				MenuFunction func = actionFunctions.get(i);
				func.addToContextMenu(popup);
			}
			*/
			popup.show(btnActions, Side.BOTTOM, 0, 0);
		});

	}




	private void createSidebar() {

		// sidebar show/hide
		if (sidebarHidden) {
			splitContent.setDividerPosition(0, 1);
			labelHideSidebar.setText("<");
		} else {
			splitContent.setDividerPosition(0, 0.75);
			labelHideSidebar.setText(">");
		}

		// listen for dragging
		splitContent.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.doubleValue() > 0.95) {
				splitContent.setDividerPosition(0, 1);
				labelHideSidebar.setText("<");
				sidebarHidden = true;
			} else {
				labelHideSidebar.setText(">");
				sidebarHidden = false;
			}
		});


		// transform label into button
		LabelUtils.makeAsButton(labelHideSidebar, "-fx-background-color: #c9c9c9;", "-fx-background-color: #bcbcbc;", new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() != MouseButton.PRIMARY) {
					return;
				}
				sidebarHidden = !sidebarHidden;
				if (sidebarHidden) {
					splitContent.setDividerPosition(0, 1);
					labelHideSidebar.setText("<");
				} else {
					splitContent.setDividerPosition(0, 0.75);
					labelHideSidebar.setText(">");
				}
				event.consume();
			}
		});

		// create content
		sidebar = new Sidebar();
		AnchorUtils.setAnchors(sidebar, 0, 0, 0, 0);
		paneSidebar.getChildren().add(sidebar);

	}




	private void setupListeners() {
		EventManager.registerListener(this, e -> {
			if (taskViewVisible) {
				refreshTaskView();
			} else {
				refreshOnShow = true;
			}
		}, RefreshTaskDisplayRecommendationEvent.class);
	}




	private void refreshTaskView() {

		clearTaskList();

		Logic.tasks.getTaskGroups(new Request<TaskGroupData>(true) {
			@Override
			public void onResponse(Response<TaskGroupData> response) {
				TaskGroupData taskGroupData = response.getValue();
				lastTaskGroupData = taskGroupData;

				// display all tasks
				if (taskGroupData.attributes.isEmpty()) {
					if (!taskGroupData.groups.isEmpty()) {
						createTaskList("All Tasks", taskGroupData.groups.get(0).tasks);
					} else {
					}


				} else { // display grouped-tasks

					// for each group
					for (TaskGroup group : taskGroupData.groups) {

						// build title
						StringBuilder title = new StringBuilder();

						SyncRequest<String> reqHeaderString = new SyncRequest<>();
						Logic.group.getCustomHeaderString(reqHeaderString);
						Response<String> resHeaderString = reqHeaderString.getResponse();

						boolean useCustomHeaderString = resHeaderString.getState() == Response.State.SUCCESS;

						if (useCustomHeaderString) {
							String strCustomHeader = resHeaderString.getValue();

							for (int i = 0; i < taskGroupData.attributes.size(); i++) {
								TaskAttribute attribute = taskGroupData.attributes.get(i);
								if (strCustomHeader.contains("{" + attribute.name + "}")) {
									TaskAttributeValue value = group.values.get(attribute);
									strCustomHeader = strCustomHeader.replaceAll("\\{" + attribute.name + "\\}", value.toString());
								}
							}

							title.append(strCustomHeader);

						} else {
							for (int i = 0; i < taskGroupData.attributes.size(); i++) {
								TaskAttribute attribute = taskGroupData.attributes.get(i);
								TaskAttributeValue value = group.values.get(attribute);
								title.append(value.toString());
								if (i != taskGroupData.attributes.size() - 1) {
									title.append(", ");
								}
							}
						}

						// create list
						createTaskList(title.toString(), group.tasks);

					}

				}

				updateNTaskLabel();
				onTaskSelected(sidebar.currentTask);
			}
		});

	}




	private void clearTaskList() {
		for (Node node : boxTasks.getChildren()) {
			if (node instanceof TaskList) {
				((TaskList) node).dispose();
			}
		}
		boxTasks.getChildren().clear();
	}




	private void createTaskList(String name, List<Task> tasks) {
		TaskList list = new TaskList(name, tasks, this);
		boxTasks.getChildren().add(list);
	}




	private Label createButtonBadge(AnchorPane pane, Button button) {

		// create badge-label
		Label badge = new Label("!");
		badge.setMinSize(14, 14);
		badge.setPrefSize(14, 14);
		badge.setMaxSize(14, 14);
		pane.getChildren().add(badge);
		button.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
			double x = newValue.getMinX();
			double y = newValue.getMinY();
			double w = newValue.getWidth();
			double h = newValue.getHeight();
			AnchorPane.setLeftAnchor(badge, x + w - 7 - 3);
			AnchorPane.setBottomAnchor(badge, y + h - 7 - 7);

		});


		// get number to display
		setBadgeText(button, badge);


		// get event to listen to
		Class eventClass = null;
		if (button == btnSort) {
			eventClass = SortElementsChangedEvent.class;
		}
		if (button == btnFilter) {
			eventClass = FilterCriteriaChangedEvent.class;
		}
		if (button == btnGroup) {
			eventClass = GroupOrderChangedEvent.class;
		}


		// listen for changes
		EventManager.registerListener(this, new EventListener() {
			@Override
			public void onEvent(Event e) {
				setBadgeText(button, badge);
			}
		}, eventClass);

		return badge;
	}




	private void setBadgeText(Button button, Label badge) {

		Logic.project.getCurrentProject(new Request<Project>(true) {
			@Override
			public void onResponse(Response<Project> response) {
				Project project = response.getValue();
				int n = 0;
				if (button == btnSort) {
					n = project.sortElements.size();
				}
				if (button == btnFilter) {
					n = project.filterCriteria.size();
				}
				if (button == btnGroup) {
					n = project.taskGroupOrder.size();
				}

				if (0 < n && n < 10) {
					badge.setText("" + n);
					badge.setVisible(true);
				} else if (n >= 10) {
					badge.setText("!");
					badge.setVisible(true);
				} else {
					badge.setText("");
					badge.setVisible(false);
				}
			}
		});

	}




	private void updateNTaskLabel() {
		SyncRequest<List<Task>> requestTotal = new SyncRequest<>();
		Logic.tasks.getTasks(requestTotal);
		int nTotal = requestTotal.getResponse().getValue().size();

		int nDisplay = -1;
		if (lastTaskGroupData != null) {
			nDisplay = lastTaskGroupData.tasks.size();
		}

		labelNTasks.setText(nDisplay + "/" + nTotal);
	}




	public void onTaskSelected(Task task) {
		if (sidebar.currentTask != null) {
			findCard(sidebar.currentTask).onDeselect();
		}
		sidebar.showTask(task);
		if (sidebar.currentTask != null) {
			findCard(sidebar.currentTask).onSelect();
		}
	}




	public TaskCard findCard(Task task) {
		for (Node node : boxTasks.getChildren()) {
			if (node instanceof TaskList) {
				TaskList list = (TaskList) node;
				TaskCard card = list.findCard(task);
				if (card != null) {
					return card;
				}
			}
		}
		return null;
	}




	@Override
	public void onOpen() {
	}




	@Override
	public void onClose() {
		EventManager.deregisterListeners(this);
	}




	@Override
	public void onShow() {
		taskViewVisible = true;
		if (refreshOnShow) {
			refreshOnShow = false;
			refreshTaskView();
		}
	}




	@Override
	public void onHide() {
		taskViewVisible = false;
	}

}








