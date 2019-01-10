//package com.ruegnerlukas.taskmanager.ui.taskview.taskcard;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.ruegnerlukas.taskmanager.logic_v1.data.TaskCard;
//import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;
//import com.ruegnerlukas.taskmanager.logic_v1.data.TaskList;
//import com.ruegnerlukas.taskmanager.eventsystem.Event;
//import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
//import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagAddedEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagChangedColorEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagChangedNameEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.FlagRemovedEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.ListCreatedEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.ListDeletedEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.TaskChangedFlagEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.TaskChangedTextEvent;
//import com.ruegnerlukas.taskmanager.eventsystem.events.TaskSelectedEvent;
//import com.ruegnerlukas.taskmanager.logic_v1.services.DataService;
//import com.ruegnerlukas.taskmanager.utils.SVGIcons;
//import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
//import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
//import com.ruegnerlukas.taskmanager.utils.uielements.editablelabelarea.EditableAreaLabel;
//import com.ruegnerlukas.taskmanager.utils.uielements.menu.ColorMenuFunction;
//import com.ruegnerlukas.taskmanager.utils.uielements.menu.MenuFunction;
//import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
//
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Side;
//import javafx.scene.control.Button;
//import javafx.scene.control.ContextMenu;
//import javafx.scene.control.Label;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//
//public class TaskCardNode extends AnchorPane {
//
//	public final TaskCard card;
//
//	@FXML private Pane paneFlag;
//	@FXML private Pane paneBackground;
//	@FXML private Label labelID;
//	@FXML private Button btnActions;
//	@FXML private AnchorPane paneText;
//	@FXML private VBox boxContent;
//	@FXML private HBox boxTags;
//	private EditableAreaLabel labelText;
//
//	private List<MenuFunction> actionFunctions = new ArrayList<MenuFunction>();
//
//
//
//
//
//	public TaskCardNode(TaskCard card) {
//		this.card = card;
//		try {
//			loadFromFXML();
//			createCustomElements();
//			setupListeners();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//
//
//
//
//	private void loadFromFXML() throws IOException {
//		final String PATH = "taskcard_cards.fxml";
//
//		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
//		loader.setController(this);
//		AnchorPane  root = (AnchorPane) loader.load();
//		root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
//		root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
//		root.setOnKeyReleased(new EventHandler<KeyEvent>() {
//			@Override public void handle(KeyEvent event) {
//				if(event.getCode() == KeyCode.R) {
//					root.getStylesheets().clear();
//					root.getStylesheets().add(ViewManager.class.getResource("bootstrap4_2.css").toExternalForm());
//					root.getStylesheets().add(ViewManager.class.getResource("style.css").toExternalForm());
//				}
//			}
//		});
//
//		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
//		this.getChildren().add(root);
//	}
//
//
//
//
//	private void createCustomElements() {
//
//		// flag
//		Color flagColor = card.flag.color.color;
//		paneFlag.setStyle("-fx-background-color: rgba(" + (int)(255*flagColor.getRed()) +","+ (int)(255*flagColor.getGreen()) +","+ (int)(255*flagColor.getBlue()) + ",255);");
//
//
//		// label id
//		labelID.setText("T-" + card.id);
//
//
//		// button action
//		updateActions();
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
//		// text area
//		labelText = new EditableAreaLabel();
//		labelText.setSelectOnEdit(true);
//		AnchorUtils.setAnchors(labelText, 0, 0, 0, 0);
//		paneText.getChildren().add(labelText);
//		labelText.addListener(new ChangeListener<String>() {
//			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				labelText.setText(oldValue);
//				DataService.tasks.setText(card, newValue);
//			}
//		});
//		labelText.setText(card.text);
//
//		// tags
//		for(String tag : card.tags) {
//			boxTags.getChildren().add(new Label(tag));
//		}
//
//
//		// selectable
//		EventHandler<MouseEvent> selectHandler = new EventHandler<MouseEvent>() {
//			@Override public void handle(MouseEvent event) {
//				EventManager.fireEvent(new TaskSelectedEvent(card, this));
//			}
//		};
//		paneFlag.setOnMouseClicked(selectHandler);
//		paneBackground.setOnMouseClicked(selectHandler);
//		labelID.setOnMouseClicked(selectHandler);
//		boxTags.setOnMouseClicked(selectHandler);
//		boxContent.setOnMouseClicked(selectHandler);
//	}
//
//
//
//
//	private void setupListeners() {
//
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event event) {
//				updateActions();
//			}
//		}, FlagAddedEvent.class, FlagRemovedEvent.class, FlagChangedColorEvent.class, FlagChangedNameEvent.class);
//
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event event) {
//				if(((TaskChangedFlagEvent)event).getTask() == card) {
//					Color flagColor = ((TaskChangedFlagEvent)event).getNewFlag().color.color;
//					paneFlag.setStyle("-fx-background-color: rgba(" + (int)(255*flagColor.getRed()) +","+ (int)(255*flagColor.getGreen()) +","+ (int)(255*flagColor.getBlue()) + ",255);");
//				}
//			}
//		}, TaskChangedFlagEvent.class);
//
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				FlagChangedColorEvent event = (FlagChangedColorEvent)e;
//				if(TaskCardNode.this.card.flag == event.getFlag()) {
//					Color flagColor = TaskCardNode.this.card.flag.color.color;
//					paneFlag.setStyle("-fx-background-color: rgba(" + (int)(255*flagColor.getRed()) +","+ (int)(255*flagColor.getGreen()) +","+ (int)(255*flagColor.getBlue()) + ",255);");
//				}
//			}
//		}, FlagChangedColorEvent.class);
//
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				updateActions();
//			}
//		}, FlagChangedNameEvent.class);
//
//
//
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				labelText.setText( ((TaskChangedTextEvent)e).getNewText() );
//			}
//		}, TaskChangedTextEvent.class);
//
//
//
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				updateActions();
//			}
//		}, ListCreatedEvent.class, ListDeletedEvent.class);
//
//	}
//
//
//
//
//	private void updateActions() {
//		actionFunctions.clear();
//
//		// set flag
//		for(TaskFlag flag : DataService.flags.getFlags()) {
//			actionFunctions.add(new ColorMenuFunction(flag.color.color, "Set Flag", flag.name) { @Override public void onAction() {
//				TaskFlag flag = (TaskFlag)getValue("flag");
//				DataService.tasks.setFlag(card, flag);
//			}}.displayText(true).setValue("flag", flag));
//		}
//
//		// move
//		for(TaskList list : DataService.lists.getLists()) {
//			if(list == card.parentList) {
//				continue;
//			}
//			actionFunctions.add(new MenuFunction("Move to", list.title) {
//				@Override public void onAction() {
//					DataService.tasks.moveTask(card, list);
//				}
//			});
//		}
//
//		// add tag
//		actionFunctions.add(new MenuFunction("Add Tag") {
//			@Override public void onAction() {
//				System.out.println("TODO: add tag");
//			}
//		});
//
//		// delete
//		actionFunctions.add(new MenuFunction("Delete") {
//			@Override public void onAction() {
//				DataService.tasks.deleteTask(card);
//			}
//		});
//
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
