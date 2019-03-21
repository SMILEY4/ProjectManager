package com.ruegnerlukas.taskmanager.ui.taskview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.TaskManager;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.Event;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
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
import com.ruegnerlukas.taskmanager.ui.taskview.presets.PresetsPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.sidebar.Sidebar;
import com.ruegnerlukas.taskmanager.ui.taskview.sortPopup.SortPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.taskcard.TaskCard;
import com.ruegnerlukas.taskmanager.ui.taskview.tasklist.TaskList;
import com.ruegnerlukas.taskmanager.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.label.LabelUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.menu.MenuFunction;
import com.ruegnerlukas.taskmanager.utils.uielements.scrollpane.ScrollPaneUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
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
	@FXML private Button btnPresets;
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
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_TASKS, this);
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

		List<Task> tasks = Logic.tasks.getTasks().getValue();
		for (Task task : tasks) {
			TaskAttribute flagAttribute = Logic.attribute.getAttribute(TaskAttributeType.FLAG).getValue();
			FlagValue value = new FlagValue(((FlagAttributeData) flagAttribute.data).flags[new Random().nextInt(((FlagAttributeData) flagAttribute.data).flags.length)]);
			Logic.tasks.setAttributeValue(task, flagAttribute, value);
		}


		Logic.attribute.createAttribute("Dependency", TaskAttributeType.DEPENDENCY);
//		Logic.tasks.getTasks(new Request<List<Task>>(true) {
//			@Override
//			public void onResponse(Response<List<Task>> response) {
//				List<Task> tasks = response.getValue();
//				Logic.attribute.getAttribute(TaskAttributeType.DEPENDENCY, new Request<TaskAttribute>() {
//					@Override
//					public void onResponse(Response<TaskAttribute> response) {
//						Random random = new Random();
//						for(int i=0; i<30; i++) {
//							int i0 = random.nextInt(tasks.size());
//							int i1 = random.nextInt(tasks.size());
//							if(i0 == i1) {
//								continue;
//							}
//							Logic.dependencies.createDependency(tasks.get(i0), tasks.get(i1), response.getValue());
//						}
//
//					}
//				});
//			}
//		});
//


		Platform.runLater(() -> {

			getScene().addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
				if (ke.getCode() == KeyCode.J) {
					List<Task> taskList = Logic.tasks.getTasks().getValue();
					int index = new Random().nextInt(taskList.size());
					Task task = taskList.get(index);
					System.out.println("JUMP TO TASK: " + index);
					onTaskSelected(task, true, true);
					ke.consume();
				}
			});

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
			stage.initOwner(TaskManager.getPrimaryStage());
			FilterPopup popup = new FilterPopup(stage);
			Scene scene = new Scene(popup, 1000, 400);
			scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
				if (ke.getCode() == KeyCode.R) {
					UIDataHandler.reloadAll();
					ke.consume();
				}
			});
			stage.setScene(scene);
			stage.show();
		});


		// button group-by
		btnGroup.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(TaskManager.getPrimaryStage());
			GroupByPopup popup = new GroupByPopup(stage);
			Scene scene = new Scene(popup, 750, 430);
			scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
				if (ke.getCode() == KeyCode.R) {
					UIDataHandler.reloadAll();
					ke.consume();
				}
			});
			stage.setScene(scene);
			stage.show();
		});


		// button sort
		btnSort.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(TaskManager.getPrimaryStage());
			SortPopup popup = new SortPopup(stage);
			Scene scene = new Scene(popup, 600, 300);
			scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
				if (ke.getCode() == KeyCode.R) {
					UIDataHandler.reloadAll();
					ke.consume();
				}
			});
			stage.setScene(scene);
			stage.show();
		});

		// button presets
		btnPresets.setOnAction(event -> {
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(TaskManager.getPrimaryStage());
			PresetsPopup popup = new PresetsPopup(stage);
			Scene scene = new Scene(popup, 760, 425);
			scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
				if (ke.getCode() == KeyCode.R) {
					UIDataHandler.reloadAll();
					ke.consume();
				}
			});
			stage.setScene(scene);
			stage.show();
		});


		// label number of tasks
		updateNTaskLabel();

		// hamburger menu
		ButtonUtils.makeIconButton(btnActions, SVGIcons.HAMBURGER, 0.7f, "white");
		btnActions.setOnAction(event -> {
			ContextMenu popup = new ContextMenu();

			MenuFunction funcCreateTask = new MenuFunction("Create Task") {
				@Override
				public void onAction() {
					onCreateTask();
				}
			};
			funcCreateTask.addToContextMenu(popup);

			popup.show(btnActions, Side.BOTTOM, 0, 0);
		});

	}




	private void createSidebar() {

		// sidebar show/hide
		if (sidebarHidden) {
			hideSidebar();
		} else {
			showSidebar(true);
		}

		// listen for dragging
		splitContent.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.doubleValue() > 0.95) {
				hideSidebar();
			} else {
				showSidebar(false);
			}
		});


		// transform label into button
		LabelUtils.makeAsButton(labelHideSidebar, "", "", event -> {
			if (event.getButton() != MouseButton.PRIMARY) {
				return;
			}
			sidebarHidden = !sidebarHidden;
			if (sidebarHidden) {
				hideSidebar();
			} else {
				showSidebar(true);
			}
			event.consume();
		});

		// createItem content
		sidebar = new Sidebar(this);
		AnchorUtils.setAnchors(sidebar, 0, 0, 0, 0);
		paneSidebar.getChildren().add(sidebar);

	}




	private void showSidebar(boolean setDivider) {
		paneSidebar.setMaxWidth(10000);
		if (setDivider) {
			splitContent.setDividerPosition(0, 0.75);
		}

		SVGPath svg = new SVGPath();
		svg.setContent(SVGIcons.ARROW_RIGHT.data);
		labelHideSidebar.setGraphic(svg);
		labelHideSidebar.setText("");
		svg.scaleXProperty().bind(labelHideSidebar.widthProperty().divide(SVGIcons.ARROW_RIGHT.width / 0.8));
		svg.scaleYProperty().bind(labelHideSidebar.widthProperty().divide(SVGIcons.ARROW_RIGHT.height / 0.8));

		sidebarHidden = false;
	}




	private void hideSidebar() {
		paneSidebar.setMaxWidth(0);
		splitContent.setDividerPosition(0, 1);

		SVGPath svg = new SVGPath();
		svg.setContent(SVGIcons.ARROW_LEFT.data);
		labelHideSidebar.setGraphic(svg);
		labelHideSidebar.setText("");
		svg.scaleXProperty().bind(labelHideSidebar.widthProperty().divide(SVGIcons.ARROW_LEFT.width / 0.8));
		svg.scaleYProperty().bind(labelHideSidebar.widthProperty().divide(SVGIcons.ARROW_LEFT.height / 0.8));

		sidebarHidden = true;
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

		TaskGroupData taskGroupData = Logic.tasks.getTaskGroups().getValue();
		lastTaskGroupData = taskGroupData;

		// display all tasks
		if (taskGroupData.attributes.isEmpty()) {
			if (!taskGroupData.groups.isEmpty()) {
				createTaskList("All Tasks", taskGroupData.groups.get(0).tasks);
			} else {
			}


		} else { // display grouped-tasks

			Response<String> resHeaderString = Logic.group.getCustomHeaderString();
			boolean useCustomHeaderString = resHeaderString.getState() == Response.State.SUCCESS;

			// for each group
			for (TaskGroup group : taskGroupData.groups) {

				// build title
				StringBuilder title = new StringBuilder();

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

				// createItem list
				createTaskList(title.toString(), group.tasks);

			}

		}

		updateNTaskLabel();

		// select last selected task (if still exists)
		List<Task> tasksProject = Logic.tasks.getTasks().getValue();
		if (tasksProject.contains(sidebar.currentTask)) {
			onTaskSelected(sidebar.currentTask, false, true);
		} else {
			onTaskSelected(null, false, false);
		}

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

		// createItem badge-label
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
		}, eventClass, PresetLoadEvent.class);

		return badge;
	}




	private void setBadgeText(Button button, Label badge) {
		Project project = Logic.project.getCurrentProject().getValue();
		int n = 0;
		if (button == btnSort) {
			n = project.sortElements.size();
		}
		if (button == btnFilter) {
			n = project.filterCriteria.size();
		}
		if (button == btnGroup) {
			n = project.attribGroupData.attributes.size(); // TODO
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




	private void updateNTaskLabel() {

		int nTotal = Logic.tasks.getTasks().getValue().size();

		int nDisplay = -1;
		if (lastTaskGroupData != null) {
			nDisplay = lastTaskGroupData.tasks.size();
		}

		labelNTasks.setText(nDisplay + "/" + nTotal);
	}




	public void onCreateTask() {
		Platform.runLater(() -> {
			sidebar.getBreadcrumb().clearTasks();
			Response<Task> response = Logic.tasks.createNewTask();
			onTaskSelected(response.getValue(), true, true);
		});
	}




	public void onTaskSelected(Task task, boolean jumpToTask, boolean addToBreadcrumb) {

		if (sidebar.currentTask != null) {
			TaskCard card = findCard(sidebar.currentTask);
			if (card != null) {
				card.onDeselect();
			}
		}

		sidebar.showTask(task);

		if (jumpToTask) {
			jumpToTask(task);
			if (addToBreadcrumb) {
				sidebar.getBreadcrumb().pushTask(task);
			}

		} else {
			if (addToBreadcrumb) {
				sidebar.getBreadcrumb().clearTasks();
				sidebar.getBreadcrumb().pushTask(task);
			}
		}

		if (sidebar.currentTask != null) {
			TaskCard card = findCard(sidebar.currentTask);
			if (card != null) {
				card.onSelect();
			}
			if (sidebarHidden) {
				showSidebar(true);
			}
		}

	}




	public void jumpToTask(Task task) {
		TaskList list = findList(task);
		if (list != null) {
			ScrollPaneUtils.centerContent(scollTasks, list);
			list.jumpToTask(task);
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




	public TaskList findList(Task task) {
		for (Node node : boxTasks.getChildren()) {
			if (node instanceof TaskList) {
				TaskList list = (TaskList) node;
				if (list.findCard(task) != null) {
					return list;
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








