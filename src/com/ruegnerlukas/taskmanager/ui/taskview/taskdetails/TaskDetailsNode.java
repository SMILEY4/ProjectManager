//package com.ruegnerlukas.taskmanager.ui.taskview.taskdetails;
//
//import java.io.IOException;
//
//import com.ruegnerlukas.taskmanager.logic_v1.data.TaskCard;
//import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskFlag;
//import com.ruegnerlukas.taskmanager.logic_v1.data.TaskList;
//import com.ruegnerlukas.taskmanager.eventsystem.Event;
//import com.ruegnerlukas.taskmanager.eventsystem.EventListener;
//import com.ruegnerlukas.taskmanager.eventsystem.EventManager;
//import com.ruegnerlukas.taskmanager.eventsystem.events.TaskChangedTextEvent;
//import com.ruegnerlukas.taskmanager.logic_v1.services.DataService;
//import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
//import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
//import com.ruegnerlukas.taskmanager.utils.uielements.editablelabelarea.EditableAreaLabel;
//import com.ruegnerlukas.taskmanager.utils.viewsystem.ViewManager;
//
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.event.EventHandler;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.Pos;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.input.KeyCode;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//public class TaskDetailsNode extends AnchorPane {
//
//
//	public final TaskCard task;
//
//	@FXML private VBox boxContent;
//
//	@FXML private AnchorPane paneText;
//	private EditableAreaLabel textArea;
//
//	@FXML private VBox boxAttribs;
//
//
//
//
//	public TaskDetailsNode(TaskCard task) {
//		this.task = task;
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
//	private void loadFromFXML() throws IOException {
//		final String PATH = "taskdetails.fxml";
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
//		// text area
//		textArea = new EditableAreaLabel(task.text);
//		textArea.addListener(new ChangeListener<String>() {
//			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				DataService.tasks.setText(task, newValue);
//			}
//		});
//		textArea.setPrefHeight(200);
//		AnchorUtils.setAnchors(textArea, 0, 0, 0, 0);
//		paneText.getChildren().add(textArea);
//		textArea.getLabel().setStyle("-fx-border-radius: 5; -fx-border-color: #dddddd;");
//
//
//		// attribute - id
//		HBox boxAttribID = new HBox();
//		boxAttribID.setSpacing(20);
//		boxAttribID.setMinSize(0, 32);
//		boxAttribID.setPrefSize(100000, 32);
//		boxAttribID.setMaxSize(100000, 32);
//		boxAttribs.getChildren().add(boxAttribID);
//
//		Label keyID = new Label("ID:");
//		keyID.setAlignment(Pos.CENTER_RIGHT);
//		keyID.setMinSize(0, 32);
//		keyID.setPrefSize(100000, 32);
//		keyID.setMaxSize(100000, 32);
//		boxAttribID.getChildren().add(keyID);
//
//		Label valueID = new Label("T-"+task.id);
//		valueID.setAlignment(Pos.CENTER_LEFT);
//		valueID.setMinSize(0, 32);
//		valueID.setPrefSize(100000, 32);
//		valueID.setMaxSize(100000, 32);
//		boxAttribID.getChildren().add(valueID);
//
//
//		// attribute - flag
//		HBox boxAttribFlag = new HBox();
//		boxAttribFlag.setSpacing(20);
//		boxAttribFlag.setMinSize(0, 32);
//		boxAttribFlag.setPrefSize(100000, 32);
//		boxAttribFlag.setMaxSize(100000, 32);
//		boxAttribs.getChildren().add(boxAttribFlag);
//
//		Label keyFlag = new Label("Flag:");
//		keyFlag.setAlignment(Pos.CENTER_RIGHT);
//		keyFlag.setMinSize(0, 32);
//		keyFlag.setPrefSize(100000, 32);
//		keyFlag.setMaxSize(100000, 32);
//		boxAttribFlag.getChildren().add(keyFlag);
//
//		ComboBox<TaskFlag> valueFlag = new ComboBox<TaskFlag>();
//		ComboboxUtils.initComboboxTaskFlag(valueFlag);
//		valueFlag.setMinSize(0, 32);
//		valueFlag.setPrefSize(100000, 32);
//		valueFlag.setMaxSize(100000, 32);
//		valueFlag.getItems().add(DataService.flags.getDefaultFlag());
//		valueFlag.getItems().addAll(DataService.flags.getFlags());
//		valueFlag.getSelectionModel().select(task.flag);
//		boxAttribFlag.getChildren().add(valueFlag);
//
//		// attribute - list
//		HBox boxAttribList = new HBox();
//		boxAttribList.setSpacing(20);
//		boxAttribList.setMinSize(0, 32);
//		boxAttribList.setPrefSize(100000, 32);
//		boxAttribList.setMaxSize(100000, 32);
//		boxAttribs.getChildren().add(boxAttribList);
//
//		Label keyList = new Label("Tasklist:");
//		keyList.setAlignment(Pos.CENTER_RIGHT);
//		keyList.setMinSize(0, 32);
//		keyList.setPrefSize(100000, 32);
//		keyList.setMaxSize(100000, 32);
//		boxAttribList.getChildren().add(keyList);
//
//		ComboBox<TaskList> valueList = new ComboBox<TaskList>();
//		ComboboxUtils.initComboboxTasklist(valueList);
//		valueList.setMinSize(0, 32);
//		valueList.setPrefSize(100000, 32);
//		valueList.setMaxSize(100000, 32);
//		valueList.getItems().addAll(DataService.lists.getLists());
//		valueList.getSelectionModel().select(task.parentList);
//		boxAttribList.getChildren().add(valueList);
//
//
//	}
//
//
//
//
//	private void setupListeners() {
//
//		EventManager.registerListener(new EventListener() {
//			@Override public void onEvent(Event e) {
//				textArea.setText( ((TaskChangedTextEvent)e).getNewText() );
//			}
//		}, TaskChangedTextEvent.class);
//
//
//	}
//
//
//
//
//
//}
