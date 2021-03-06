package com.ruegnerlukas.taskmanager.ui.viewtasks.sidebar.items;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.Task;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttributeData;
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

/**
 * A Item with the name of the attribute and a node to change the value.
 */
public abstract class SimpleSidebarItem extends SidebarItem {


	private Label label;
	private HBox boxValue;
	private Button button;
	private Label labelEmpty;

	private boolean isEmpty;

	private FXMapEntryChangeListener<TaskAttributeData, TaskValue<?>> listenerTaskValue;




	public SimpleSidebarItem(TasksSidebar sidebar, TaskAttribute attribute, Task task) {
		super(sidebar, attribute, task);
		this.getStyleClass().add("sidebar-item-simple");

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
		label.getStyleClass().add("sidebar-item-name");
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
		button.getStyleClass().add("sidebar-item-clear");
		ButtonUtils.makeIconButton(button, SVGIcons.CROSS, 0.4);
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


		listenerTaskValue = new FXMapEntryChangeListener<TaskAttributeData, TaskValue<?>>(getTask().getValues(), getAttribute()) {
			@Override
			public void onChanged(MapChangeListener.Change<? extends TaskAttributeData, ? extends TaskValue<?>> c) {
				refresh();
			}
		};


		create();
		refresh();
		refreshEmpty();
	}




	/**
	 * Creates and sets the node, name and logic of this item.
	 */
	protected abstract void create();


	/**
	 * Updates the displayed {@link TaskValue}.
	 */
	protected abstract void refresh();




	/**
	 * Set the name of this item to the given text.
	 */
	public void setText(String text) {
		label.setText(text);
	}




	/**
	 * Set the node to change the value to the given node.
	 */
	public void setValueNode(Node node) {
		boxValue.getChildren().setAll(node);
	}




	/**
	 * Whether to show the button to remove the value.
	 */
	public void setShowButton(boolean showButton) {
		button.setVisible(showButton);
		button.setDisable(!showButton);
	}




	/**
	 * When the button to remove the value was pressed.
	 */
	protected abstract void onSetEmpty(boolean empty);




	/**
	 * Checks the {@link TaskValue} of this task and displays it as the value or as an empty value.
	 */
	private void refreshEmpty() {
		final TaskValue<?> objValue = TaskLogic.getTaskValue(getTask(), getAttribute());
		if (objValue == null || objValue.getAttType() == null) {
			setEmpty(true);
		} else {
			setEmpty(false);
		}
	}




	/**
	 * Whether to set this item to an empty item.
	 */
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




	/**
	 * Set the text that is displayed then this item is set as empty
	 */
	private void setEmptyText() {
		TaskValue<?> value = TaskLogic.getTaskValue(getTask(), getAttribute());
		if (value.getAttType() == null) {
			TaskValue<?> valueOD = TaskLogic.getValueOrDefault(getTask(), getAttribute());
			if (valueOD.getAttType() == null) {
				labelEmpty.setText("no value");
			} else {
				labelEmpty.setText(TaskValue.valueToString(valueOD));
			}
		}
	}




	/**
	 * Update this item when a value of the {@link TaskAttribute} was changed.
	 */
	public void onAttChangedEvent() {
		setEmptyText();
		refresh();
		refreshEmpty();
	}




	@Override
	public void dispose() {
		listenerTaskValue.removeFromAll();
		super.dispose();
	}


}
