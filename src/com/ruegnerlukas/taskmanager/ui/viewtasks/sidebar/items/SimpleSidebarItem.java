package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.TasksSidebar;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.listeners.FXMapEntryChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import javafx.collections.MapChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public abstract class SimpleSidebarItem extends SidebarItem {


	private Label label;
	private HBox boxValue;
	private Button button;
	private Label labelEmpty;

	private boolean isEmpty;

	private FXMapEntryChangeListener<TaskAttribute,TaskValue<?>> listenerTaskValue;



	public SimpleSidebarItem(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);

		// root box
		HBox root = new HBox();
		root.setMinSize(0, 34);
		root.setPrefSize(100000, 34);
		root.setMaxHeight(34);
		root.setSpacing(20);
		root.setAlignment(Pos.CENTER_LEFT);
		AnchorUtils.setAnchors(root, 0, 0, 0, 0);
		this.getChildren().add(root);


		// left - label
		label = new Label("Attribute");
		label.setAlignment(Pos.CENTER_RIGHT);
		label.setMinWidth(0);
		label.setPrefSize(100000, 34);
		root.getChildren().add(label);


		// right - pane
		AnchorPane paneRight = new AnchorPane();
		paneRight.setMinWidth(0);
		paneRight.setPrefSize(100000, 34);
		root.getChildren().add(paneRight);


		// button - add / clear
		button = new Button();
		ButtonUtils.makeIconButton(button, SVGIcons.CROSS, 0.4, "black");
		button.setMinSize(34, 34);
		button.setMaxSize(34, 34);
		button.setOnAction(event -> onSetEmpty(!isEmpty));
		AnchorUtils.setAnchors(button, 0, 0, 0, null);
		paneRight.getChildren().add(button);


		// label empty
		labelEmpty = new Label();
		labelEmpty.setDisable(true);
		labelEmpty.setAlignment(Pos.CENTER);
		AnchorUtils.setAnchors(labelEmpty, 0, 34, 0, 0);
		paneRight.getChildren().add(labelEmpty);
		setEmptyText();


		// box value
		boxValue = new HBox();
		boxValue.setMinWidth(0);
		boxValue.setPrefHeight(34);
		boxValue.setAlignment(Pos.CENTER_LEFT);
		AnchorUtils.setAnchors(boxValue, 0, 34, 0, 0);
		paneRight.getChildren().add(boxValue);


		this.setText(attribute.name.get() + ":");
		this.setOnAttribNameChanged(event -> {
			this.setText(attribute.name.get() + ":");
		});


		listenerTaskValue = new FXMapEntryChangeListener<TaskAttribute, TaskValue<?>>(getTask().values, getAttribute()) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends TaskAttribute, ? extends TaskValue<?>> c) {
				refresh();
			}
		};


		create();
		refresh();
		checkEmpty();
	}




	protected abstract void create();

	protected abstract void refresh();




	public void setText(String text) {
		label.setText(text);
	}




	public void setValueNode(Node node) {
		boxValue.getChildren().setAll(node);
	}




	public void setShowButton(boolean showButton) {
		button.setVisible(showButton);
		button.setDisable(!showButton);
	}




	protected abstract void onSetEmpty(boolean empty);




	private void checkEmpty() {
		final TaskValue<?> objValue = TaskLogic.getTaskValue(getTask(), getAttribute());
		if (objValue == null || objValue.getAttType() == null) {
			setEmpty(true);
		} else {
			setEmpty(false);
		}
	}




	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
		if (this.isEmpty) {
			labelEmpty.setVisible(true);
			boxValue.setDisable(true);
			boxValue.setVisible(false);
		} else {
			labelEmpty.setVisible(false);
			boxValue.setDisable(false);
			boxValue.setVisible(true);
		}
		setEmptyText();
	}




	private void setEmptyText() {
		TaskValue<?> value = TaskLogic.getTaskValue(getTask(), getAttribute());
		if (value.getAttType() == null) {
			TaskValue<?> valueOD = TaskLogic.getValueOrDefault(getTask(), getAttribute());
			if (valueOD.getAttType() == null) {
				labelEmpty.setText("no value");
			} else {
				labelEmpty.setText(valueOD.getValue().toString());
			}
		}
	}




	public void onAttChangedEvent() {
		setEmptyText();
		refresh();
		checkEmpty();
	}




	@Override
	public void dispose() {
		listenerTaskValue.removeFromAll();
		super.dispose();
	}


}
