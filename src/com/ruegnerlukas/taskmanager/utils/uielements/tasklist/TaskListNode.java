package com.ruegnerlukas.taskmanager.utils.uielements.tasklist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.logic.data.TaskCard;
import com.ruegnerlukas.taskmanager.logic.data.TaskFlag.FlagColor;
import com.ruegnerlukas.taskmanager.logic.data.TaskList;
import com.ruegnerlukas.taskmanager.logic.eventsystem.Event;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventListener;
import com.ruegnerlukas.taskmanager.logic.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.logic.eventsystem.RequestEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.RequestEventListener;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListChangedColorEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListChangedNameEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListDeleteRequest;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.ListsChangedOrderEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.TaskCreatedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.TaskDeletedEvent;
import com.ruegnerlukas.taskmanager.logic.eventsystem.events.TaskMovedEvent;
import com.ruegnerlukas.taskmanager.logic.services.DataService;
import com.ruegnerlukas.taskmanager.logic.services.ListService.ListDeleteBehavior;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import com.ruegnerlukas.taskmanager.utils.uielements.menu.ColorMenuFunction;
import com.ruegnerlukas.taskmanager.utils.uielements.menu.MenuFunction;
import com.ruegnerlukas.taskmanager.utils.uielements.taskcard.TaskCardNode;
import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TaskListNode extends AnchorPane {

	
	public final TaskList list;
	
	private EditableLabel labelTitle;
	
	
	@FXML private Button btnActions;
	@FXML private AnchorPane paneHeader;
	@FXML private ScrollPane scrollPane;
	@FXML private VBox boxContent;
	
	private List<TaskCardNode> listNodes = new ArrayList<TaskCardNode>();
	
	private List<MenuFunction> actionFunctions = new ArrayList<MenuFunction>();
	
	
	
	
	
	
	public TaskListNode(TaskList list) {
		this.list = list;
		try {
			loadFromFXML();
			createCustomElements();
			setupListeners();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	private void loadFromFXML() throws IOException {
		final String PATH = "tasklist.fxml";
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
		loader.setController(this);
		AnchorPane  root = (AnchorPane) loader.load();
		root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
		root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
		root.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.R) {
					root.getStylesheets().clear();
					root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
					root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
				}
			}
		});

		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);
	}
	
	
	
	
	private void createCustomElements() {
		
		// label title
		labelTitle = new EditableLabel(list.title);
		labelTitle.getLabel().setAlignment(Pos.CENTER);
		labelTitle.getTextField().setAlignment(Pos.CENTER);
		labelTitle.addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				DataService.lists.renameList(list, newValue);
			}
		});
		AnchorUtils.setAnchors(labelTitle, 0, 40, 0, 0);
		paneHeader.getChildren().add(labelTitle);
		Color colorLight = Color.web("#29323c").brighter().desaturate();
		labelTitle.getTextField().setStyle("-fx-background-color: rgba(" + (int)(255*colorLight.getRed()) +","+ (int)(255*colorLight.getGreen()) +","+ (int)(255*colorLight.getBlue()) + ",255);");

		// button actions
		updateActions();
		ButtonUtils.makeIconButton(btnActions, SVGIcons.getHamburger(), 40, 40, "white");
		btnActions.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				ContextMenu popup = new ContextMenu();
				for(int i=0; i<actionFunctions.size(); i++) {
					MenuFunction func = actionFunctions.get(i);
					func.addToContextMenu(popup);
				}
				popup.show(btnActions, Side.BOTTOM, 0, 0);
			}			
		});
		
		// tasks
		for(TaskCard task : list.cards) {
			TaskCardNode cardNode = new TaskCardNode(task);
			listNodes.add(cardNode);
			boxContent.getChildren().add(cardNode);
		}
		
	}
	
	
	
	
	private void setupListeners() {
		
		EventManager.registerListener(new RequestEventListener<ListDeleteBehavior>() {
			@Override public ListDeleteBehavior onEvent(RequestEvent<ListDeleteBehavior> e) {
				ListDeleteRequest event = (ListDeleteRequest)e;
				if(event.getList() == list) {
					event.consume();

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Delete TaskList");
					alert.setHeaderText(null);
					alert.setContentText("The List \"" + list.title + "\" contains some tasks. Do you want to delete these Tasks ?");
					
					ButtonType deleteTasks = new ButtonType("Delete Tasks");
					ButtonType archiveTasks = new ButtonType("Archive Tasks");
					ButtonType cancel = new ButtonType("Cancel");
					alert.getButtonTypes().clear();
					alert.getButtonTypes().addAll(deleteTasks, archiveTasks, cancel);
					
					Optional<ButtonType> option = alert.showAndWait();
					if(option.get() == null || option.get() == cancel) {
						return ListDeleteBehavior.CANCEL;
					} else if(option.get() == deleteTasks) {
						return ListDeleteBehavior.DELETE_TASKS;
					} else if(option.get() == archiveTasks) {
						return ListDeleteBehavior.ARCHIVE_TASKS;
					}
					
					return ListDeleteBehavior.CANCEL;
					
				} else {
					return null;
				}
			}
		}, ListDeleteRequest.class);
		
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event event) {
				if(((ListChangedNameEvent)event).getList().id == list.id) {
					labelTitle.setText( ((ListChangedNameEvent)event).getNewName() );
				}
				updateActions();
			}
		}, ListChangedNameEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event event) {
				updateActions();
			}
		}, ListCreatedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				updateActions();
			}
		}, ListsChangedOrderEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				ListChangedColorEvent event = (ListChangedColorEvent)e;
				if(event.getList().id != list.id) {
					return;
				}
				Color color = event.getNewColor();
				paneHeader.setStyle("-fx-background-color: rgba(" + (int)(255*color.getRed()) +","+ (int)(255*color.getGreen()) +","+ (int)(255*color.getBlue()) + ",255);");
				Color colorLight = color.brighter().desaturate();
				labelTitle.getTextField().setStyle("-fx-background-color: rgba(" + (int)(255*colorLight.getRed()) +","+ (int)(255*colorLight.getGreen()) +","+ (int)(255*colorLight.getBlue()) + ",255);");
			}
		}, ListChangedColorEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				TaskCreatedEvent event = (TaskCreatedEvent)e;
				if(event.getList().id == list.id) {
					TaskCardNode cardNode = new TaskCardNode(event.getCard());
					listNodes.add(cardNode);
					boxContent.getChildren().add(0, cardNode);
					scrollToTask(cardNode);
				}
			}
		}, TaskCreatedEvent.class);
		
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				TaskMovedEvent event = (TaskMovedEvent)e;
				
				if(event.getOldList() == list) {
					TaskCardNode node = getNode(event.getTask());
					if(node != null) {
						listNodes.remove(node);
						boxContent.getChildren().remove(node);
					}
				}
				
				if(event.getNewList() == list) {
					TaskCardNode cardNode = new TaskCardNode(event.getTask());
					listNodes.add(cardNode);
					boxContent.getChildren().add(0, cardNode);
					scrollToTask(cardNode);
				}
				
			}
		}, TaskMovedEvent.class);
		
		EventManager.registerListener(new EventListener() {
			@Override public void onEvent(Event e) {
				TaskDeletedEvent event = (TaskDeletedEvent)e;
				TaskCardNode node = getNode(event.getTask());
				if(node != null) {
					listNodes.remove(node);
					boxContent.getChildren().remove(node);
				}
			}
		}, TaskDeletedEvent.class);
		
	}
	
	
	
	
	private void updateActions() {
		actionFunctions.clear();
		
		// create task
		actionFunctions.add(new MenuFunction("New Task") { @Override public void onAction() {
			DataService.tasks.createTask(list);
		}});
		
		// move
		if(DataService.lists.getLists().size() > 1) {
			
			actionFunctions.add(new MenuFunction("Move", "First") { @Override public void onAction() {
				DataService.lists.moveListFirst(list);
			}});
			actionFunctions.add(new MenuFunction("Move", "Last") { @Override public void onAction() {
				DataService.lists.moveListLast(list);
			}});
			
			
			for(TaskList list : DataService.lists.getLists()) {
				actionFunctions.add(new MenuFunction("Move", "Before", list.title) { @Override public void onAction() {
					DataService.lists.moveListBefore(list, (TaskList)getValue("list"));
					
				}}.setValue("list", list));
				
				actionFunctions.add(new MenuFunction("Move", "After", list.title) { @Override public void onAction() {
					DataService.lists.moveListAfter(list, (TaskList)getValue("list"));
				}}.setValue("list", list));
			}
			
		} else {
			actionFunctions.add(new MenuFunction("Move") { @Override public void onAction() {}}.setDisable(true));
		}
		
		// hide list
		actionFunctions.add(new MenuFunction("Hide") { @Override public void onAction() {
			DataService.lists.hideList(list);
		}});
		
		// delete list
		actionFunctions.add(new MenuFunction("Delete") { @Override public void onAction() {
			DataService.lists.deleteList(list);
		}});
		
		// change color
		Color defaultColor = Color.web("#29323c");
		actionFunctions.add(new ColorMenuFunction(defaultColor, "Change Color", "default") { @Override public void onAction() {
			DataService.lists.recolorList(list, Color.web("#29323c"));
		}});
		for(FlagColor color : FlagColor.values()) {
			actionFunctions.add(new ColorMenuFunction(color.color.darker(), "Change Color", color.toString()) { @Override public void onAction() {
				FlagColor flagColor = FlagColor.valueOf(FlagColor.class, this.text);
				Color color = flagColor.color.darker();
				DataService.lists.recolorList(list, color);
			}});
		}

		// sort tasks
		actionFunctions.add(new MenuFunction("Sort Tasks") { @Override public void onAction() { System.out.println("ToDo: tasklist.actions.sort"); }});
		
		// move tasks
		if(DataService.lists.getNumLists() > 1) {
			for(TaskList list : DataService.lists.getLists()) {
				if(list != this.list) {
					actionFunctions.add(new MenuFunction("Move all Tasks to List", list.title) { @Override public void onAction() {
						DataService.tasks.moveAllTasks(TaskListNode.this.list, (TaskList)getValue("list"));
					}}.setValue("list", list));
				}
			}
		} else {
			actionFunctions.add(new MenuFunction("Move all Tasks to List") { @Override public void onAction() {}}.setDisable(true));
		}
		
	}
	
	
	
	
	private TaskCardNode getNode(TaskCard task) {
		for(int i=0; i<listNodes.size(); i++) {
			TaskCardNode node = listNodes.get(i);
			if(node.card == task) {
				return node;
			}
		}
		return null;
	}
	
	
	
	
	private void scrollToTask(TaskCardNode node) {
		int index = boxContent.getChildren().indexOf(node);
		double taskHeight = node.getHeight();
		double totalHeight = boxContent.getHeight();
		double x = index * taskHeight;
		double value = MathUtils.clamp(x/totalHeight, 0, 1);
		if(index == 0) {
			value = 0;
		} else if(index == boxContent.getChildren().size()-1) {
			value = 1;
		}
		scrollPane.setVvalue(value);
	}
}
