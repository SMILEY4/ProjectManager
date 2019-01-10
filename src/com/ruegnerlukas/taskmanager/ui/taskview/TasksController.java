//package com.ruegnerlukas.taskmanager.ui.taskview;
//
//import com.ruegnerlukas.simplemath.MathUtils;
//import com.ruegnerlukas.taskmanager.logic_v1.data.TaskList;
//import com.ruegnerlukas.taskmanager.eventsystem.Event;
//import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
//import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
//import com.ruegnerlukas.taskmanager.eventsystem.events.*;
//import com.ruegnerlukas.taskmanager.logic_v1.services.DataService;
//import com.ruegnerlukas.taskmanager.ui.taskview.taskdetails.TaskDetailsNode;
//import com.ruegnerlukas.taskmanager.ui.taskview.tasklist.TaskListNode;
//import com.ruegnerlukas.taskmanager.utils.LoremIpsum;
//import com.ruegnerlukas.taskmanager.utils.SVGIcons;
//import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
//import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
//import com.ruegnerlukas.taskmanager.utils.uielements.label.LabelUtils;
//import com.ruegnerlukas.taskmanager.utils.uielements.menu.MenuFunction;
//import com.ruegnerlukas.taskmanager.utils.uielements.textfield.AutocompletionTextField;
//import com.ruegnerlukas.taskmanager.utils.viewsystem.IViewController;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.geometry.Side;
//import javafx.scene.control.*;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class TasksController implements IViewController {
//
//
//	@FXML private AnchorPane rootTaskView;
//
//	@FXML private AnchorPane paneHeader;
//
//	@FXML private AnchorPane paneOperationBar;
//	@FXML private ChoiceBox<String> choiceOperation;
//	@FXML private TextField operationDisplay;
//	@FXML private Button btnClearOps;
//
//	@FXML private Button btnActions;
//
//	@FXML private SplitPane splitContent;
//
//	@FXML private Label labelHideSidebar;
//	@FXML private AnchorPane paneContent;
//	@FXML private ScrollPane scollTasks;
//	@FXML private AnchorPane paneTasks;
//	@FXML private HBox boxTasks;
//
//	private boolean sidebarHidden = true;
//	@FXML private AnchorPane paneSidebar;
//	private TaskDetailsNode detailsNode;
//
//	private List<MenuFunction> actionFunctions = new ArrayList<MenuFunction>();
//
//	private List<TaskListNode> listNodes = new ArrayList<TaskListNode>();
//
//
//
//
//
//
//	@Override
//	public void create() {
//		updateActions();
//		setupListeners();
//		createCustomElements();
//	}
//
//
//
//
//	private void createCustomElements() {
//
//		ButtonUtils.makeIconButton(btnActions, SVGIcons.getHamburger(), 40, 40, "white");
//		btnActions.setOnAction(new EventHandler<ActionEvent>() {
//			@Override public void handle(ActionEvent event) {
//				ContextMenu popup = new ContextMenu();
//				for(int i=0; i<actionFunctions.size(); i++) {
//					MenuFunction func = actionFunctions.get(i);
//					func.addToContextMenu(popup);
//				}
//				popup.show(btnActions, Side.BOTTOM, 0, 0);
//			}
//		});
//
//
//		// header filter bar
//		Set<String> entries = new HashSet<String>();
//		for(int i=0; i<100; i++) {
//			entries.add(LoremIpsum.get(1, true));
//		}
//		AutocompletionTextField fieldFilter = new AutocompletionTextField(entries);
//		fieldFilter.setPromptText("Type filter here");
//		AnchorPane.setTopAnchor(fieldFilter, 0.0);
//		AnchorPane.setBottomAnchor(fieldFilter, 0.0);
//		AnchorPane.setLeftAnchor(fieldFilter, 0.0);
//		AnchorPane.setRightAnchor(fieldFilter, 0.0);
//		paneOperationBar.getChildren().clear();
//		paneOperationBar.getChildren().add(fieldFilter);
//
//		ButtonUtils.makeIconButton(btnClearOps, SVGIcons.getCross(), 40, 40, "white");
//		btnClearOps.setOnAction(new EventHandler<ActionEvent>() {
//			@Override public void handle(ActionEvent event) {
//				fieldFilter.setText("");
//			}
//		});
//
//
//		// sidebar show/hide
//		if(sidebarHidden) {
//			splitContent.setDividerPosition(0, 1);
//			labelHideSidebar.setText("<");
//		} else {
//			splitContent.setDividerPosition(0, 0.75);
//			labelHideSidebar.setText(">");
//		}
//
//
//		splitContent.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
//			@Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				if(newValue.doubleValue() > 0.95) {
//					splitContent.setDividerPosition(0, 1);
//					labelHideSidebar.setText("<");
//					sidebarHidden = true;
//				} else {
//					labelHideSidebar.setText(">");
//					sidebarHidden = false;
//				}
//			}
//		});
//
//		LabelUtils.makeAsButton(labelHideSidebar, "-fx-background-color: #c9c9c9;", "-fx-background-color: #bcbcbc;", new EventHandler<MouseEvent>() {
//			@Override public void handle(MouseEvent event) {
//				if(event.getButton() != MouseButton.PRIMARY) {
//					return;
//				}
//				sidebarHidden = !sidebarHidden;
//				if(sidebarHidden) {
//					splitContent.setDividerPosition(0, 1);
//					labelHideSidebar.setText("<");
//				} else {
//					splitContent.setDividerPosition(0, 0.75);
//					labelHideSidebar.setText(">");
//				}
//				event.consume();
//			}
//		});
//
//	}
//
//
//
//
//	private void updateActions() {
//		actionFunctions.clear();
//
//		// ADD LIST
//		actionFunctions.add(new MenuFunction("New List") { @Override public void onAction() {
//			DataService.lists.createList("New List", 0);
//		}});
//
//		// SORT ALL TASKS
//		actionFunctions.add(new MenuFunction("Sort Tasks", "by Flag") { @Override public void onAction() { System.out.println("todo: sort.flag"); }});
//		actionFunctions.add(new MenuFunction("Sort Tasks", "by Title") { @Override public void onAction() { System.out.println("todo: sort.title"); }});
//		actionFunctions.add(new MenuFunction("Sort Tasks", "by Date (created)") { @Override public void onAction() { System.out.println("todo: sort.date.created"); }});
//		actionFunctions.add(new MenuFunction("Sort Tasks", "by Date (last updated)") { @Override public void onAction() { System.out.println("todo: sort.date.last"); }});
//		actionFunctions.add(new MenuFunction("Sort Tasks", "by ID") { @Override public void onAction() { System.out.println("todo: sort.id"); }});
//
//		// CHANGE APPERANCE
//		actionFunctions.add(new MenuFunction("Apperance", "Simple") { @Override public void onAction() { System.out.println("todo: apperance.simple"); } });
//		actionFunctions.add(new MenuFunction("Apperance", "Cards") { @Override public void onAction() { System.out.println("todo: apperance.cards"); } });
//		actionFunctions.add(new MenuFunction("Apperance", "List") { @Override public void onAction() { System.out.println("todo: apperance.list"); } });
//
//		// SHOW HIDDEN LISTS
//		actionFunctions.add(new MenuFunction("Show Hidden Lists", "All") { @Override public void onAction() {
//			for(TaskList list : DataService.lists.getLists()) {
//				if(list.hidden) {
//					DataService.lists.unhideList(list);
//				}
//			}
//		}});
//		for(TaskList list : DataService.lists.getLists()) {
//			if(list.hidden) {
//				actionFunctions.add(new MenuFunction("Show Hidden Lists", list.title) { @Override public void onAction() {
//					DataService.lists.unhideList(list);
//				}});
//			}
//		}
//
//	}
//
//
//
//
//	private void setupListeners() {
//
//		// LIST CREATED
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event event) {
//				TaskList list = ((ListCreatedEvent)event).getCreatedList();
//				TaskListNode listNode = new TaskListNode(list);
//				listNodes.add(listNode);
//				boxTasks.getChildren().add(((ListCreatedEvent)event).getIndex(), listNode);
//				scrollToList(listNode);
//			}
//		}, ListCreatedEvent.class);
//
//		// LIST CHANGED ORDER
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				List<Integer> newOrder = ((ListsChangedOrderEvent)e).getNewOrder();
//
//				boxTasks.getChildren().clear();
//				for(int i=0; i<newOrder.size(); i++) {
//					int id = newOrder.get(i);
//					TaskListNode node = null;
//					for(int j=0; j<listNodes.size(); j++) {
//						TaskListNode n = listNodes.get(j);
//						if(n.list.id == id) {
//							node = n;
//							break;
//						}
//					}
//					if(node != null) {
//						boxTasks.getChildren().add(node);
//					}
//				}
//				if(e instanceof ListMovedEvent) {
//					TaskList movedList = ((ListMovedEvent)e).getList();
//					TaskListNode node = getNode(movedList);
//					if(node != null) {
//						scrollToList(node);
//					}
//				}
//			}
//		}, ListsChangedOrderEvent.class, ListMovedEvent.class);
//
//		// LIST (UN-)HIDE
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				ListChangedVisibilityEvent event = (ListChangedVisibilityEvent)e;
//				TaskList list = event.getList();
//				if(event.isListHidden()) {
//					TaskListNode node = getNode(list);
//					if(node != null) {
//						listNodes.remove(node);
//						boxTasks.getChildren().remove(node);
//					}
//				} else {
//					TaskListNode listNode = new TaskListNode(list);
//					int index = 0;
//					for(int i=0, j=0; i<DataService.lists.getListOrder().size(); i++) {
//						int id = DataService.lists.getListOrder().get(i);
//						if(!DataService.lists.getListByID(id).hidden) {
//							if(id == list.id) {
//								index = j;
//								break;
//							}
//							j++;
//						}
//					}
//					listNodes.add(listNode);
//					boxTasks.getChildren().add(index, listNode);
//					scrollToList(listNode);
//				}
//				updateActions();
//			}
//		}, ListChangedVisibilityEvent.class);
//
//		// LIST DELETED
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event event) {
//				TaskList list = ((ListDeletedEvent)event).getDeletedList();
//				TaskListNode node = getNode(list);
//				if(node != null) {
//					listNodes.remove(node);
//					boxTasks.getChildren().remove(node);
//				}
//			}
//		}, ListDeletedEvent.class);
//
//		// TASK SELECTED
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				TaskSelectedEvent event = (TaskSelectedEvent)e;
//				detailsNode = new TaskDetailsNode(event.getTask());
//				AnchorUtils.setAnchors(detailsNode, 0, 0, 0, 0);
//				paneSidebar.getChildren().clear();
//				paneSidebar.getChildren().add(detailsNode);
//				if(sidebarHidden) {
//					labelHideSidebar.setText(">");
//					sidebarHidden = false;
//					splitContent.setDividerPosition(0, 0.75);
//				}
//			}
//		}, TaskSelectedEvent.class);
//
//		// TASK DELETED
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				TaskDeletedEvent event = (TaskDeletedEvent)e;
//				if(detailsNode != null && detailsNode.task == event.getTask()) {
//					paneSidebar.getChildren().remove(detailsNode);
//					detailsNode = null;
//				}
//			}
//		}, TaskDeletedEvent.class);
//
//
//
//	}
//
//
//
//
//	private TaskListNode getNode(TaskList list) {
//		for(int i=0; i<listNodes.size(); i++) {
//			TaskListNode node = listNodes.get(i);
//			if(node.list == list) {
//				return node;
//			}
//		}
//		return null;
//	}
//
//
//
//
//	private void scrollToList(TaskListNode node) {
//		int index = boxTasks.getChildren().indexOf(node);
//		double listWidth = node.getWidth();
//		double totalWidth = boxTasks.getWidth();
//		double x = index * listWidth;
//		double value = MathUtils.clamp(x/totalWidth, 0, 1);
//		if(index == 0) {
//			value = 0;
//		} else if(index == boxTasks.getChildren().size()-1) {
//			value = 1;
//		}
//		scollTasks.setHvalue(value);
//	}
//
//
//
//}
//
//
//
//
//
//
//
//
