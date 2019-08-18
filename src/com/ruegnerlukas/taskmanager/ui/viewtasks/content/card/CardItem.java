package com.ruegnerlukas.taskmanager.ui.viewtasks.content.card;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskvalues.TaskValue;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.utils.TaskValueChangeEvent;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CardItem extends HBox {


	private final TaskCard card;
	private final TaskAttribute attribute;

	private Label labelName;
	private Label labelValue;

	private FXChangeListener<String> listenerAttributeName;
	private EventHandler<TaskValueChangeEvent> listenerTask;




	public CardItem(TaskCard card, TaskAttribute attribute) {
		this.card = card;
		this.attribute = attribute;
		this.getStyleClass().add("task-card-item");

		this.setMinSize(0, 22);
		this.setPrefSize(100000, 22);
		this.setMaxSize(100000, 22);
		this.setSpacing(15);

		labelName = new Label(attribute.name.get() + ":");
		labelName.getStyleClass().add("task-card-item-key");
		labelName.setMinSize(0, 22);
		labelName.setPrefSize(10000, 22);
		labelName.setMaxSize(10000, 22);
		labelName.setAlignment(Pos.CENTER_RIGHT);
		this.getChildren().add(labelName);

		labelValue = new Label();
		labelValue.getStyleClass().add("task-card-item-value");
		labelValue.setMinSize(0, 22);
		labelValue.setPrefSize(10000, 22);
		labelValue.setMaxSize(10000, 22);
		labelValue.setAlignment(Pos.CENTER_LEFT);
		this.getChildren().add(labelValue);

		updateValue();

		// listen for name changes
		listenerAttributeName = new FXChangeListener<String>(attribute.name) {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelName.setText(newValue);
			}
		};

		// listen for changes of task value
		listenerTask = e -> {
			if (e.getTask() == card.getTask()) {
				updateValue();
			}
		};
		TaskLogic.addOnTaskValueChanged(listenerTask);

	}




	/**
	 * Updates the value of this item.
	 */
	private void updateValue() {
		final TaskValue<?> value = TaskLogic.getValueOrDefault(card.getTask(), attribute);
		if (value.getAttType() == null) {
			labelValue.setText("");
		} else {
			labelValue.setText(TaskValue.valueToString(value));
		}
	}




	public void dispose() {
		listenerAttributeName.removeFromAll();
		TaskLogic.removeOnTaskValueChanged(listenerTask);
	}


}
