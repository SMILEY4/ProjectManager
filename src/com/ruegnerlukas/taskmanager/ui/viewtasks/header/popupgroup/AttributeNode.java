package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupgroup;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class AttributeNode extends HBox {


	private ComboBox<TaskAttribute> boxAttribute;

	private EventHandler<ActionEvent> handlerOnRemove;
	private EventHandler<ActionEvent> handlerModified;
	private EventHandler<ActionEvent> handlerMoveUp;
	private EventHandler<ActionEvent> handlerMoveDown;




	public AttributeNode(TaskAttribute attribute) {

		this.setAlignment(Pos.CENTER_LEFT);
		this.setSpacing(5);
		this.setPadding(new Insets(0, 5, 0, 5));

		// box attribute
		boxAttribute = new ComboBox<>();
		boxAttribute.setButtonCell(ComboboxUtils.createListCellAttribute());
		boxAttribute.setCellFactory(param -> ComboboxUtils.createListCellAttribute());
		boxAttribute.setMinWidth(200);
		boxAttribute.setPrefWidth(250);
		boxAttribute.setMinHeight(32);
		boxAttribute.setMaxHeight(32);
		boxAttribute.getItems().addAll(Data.projectProperty.get().data.attributes);
		boxAttribute.getSelectionModel().select(attribute);
		boxAttribute.setOnAction(event -> onSelectAttribute(boxAttribute.getValue()));
		this.getChildren().add(boxAttribute);

		// spacing
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		this.getChildren().add(region);

		// button up
		Button btnUp = new Button();
		ButtonUtils.makeIconButton(btnUp, SVGIcons.LONG_ARROW_UP, 0.7f);
		btnUp.setMinSize(32, 32);
		btnUp.setPrefSize(32, 32);
		btnUp.setMaxSize(32, 32);
		btnUp.setOnAction(event -> {
			onMoveUp();
		});
		this.getChildren().add(btnUp);


		// button down
		Button btnDown = new Button();
		ButtonUtils.makeIconButton(btnDown, SVGIcons.LONG_ARROW_DOWN, 0.7f);
		btnDown.setMinSize(32, 32);
		btnDown.setPrefSize(32, 32);
		btnDown.setMaxSize(32, 32);
		btnDown.setOnAction(event -> {
			onMoveDown();
		});
		this.getChildren().add(btnDown);

		// button remove
		Button btnRemove = new Button();
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.65f);
		btnRemove.setOnAction(event -> {
			onRemoveThis();
		});
		this.getChildren().add(btnRemove);
	}




	/**
	 * @return the {@link TaskAttribute} handled by this node.
	 */
	public TaskAttribute getAttribute() {
		return boxAttribute.getValue();
	}




	public void setOnRemove(EventHandler<ActionEvent> handler) {
		this.handlerOnRemove = handler;
	}




	public void setOnModified(EventHandler<ActionEvent> handler) {
		this.handlerModified = handler;
	}




	public void setOnMoveUp(EventHandler<ActionEvent> handler) {
		this.handlerMoveUp = handler;
	}




	public void setOnMoveDown(EventHandler<ActionEvent> handler) {
		this.handlerMoveDown = handler;
	}




	/**
	 * Notifies the listener when a {@link TaskAttribute} was selected.
	 */
	private void onSelectAttribute(TaskAttribute attribute) {
		if (handlerModified != null) {
			handlerModified.handle(new ActionEvent());
		}
	}




	/**
	 * Notifies the listener when this node wants to move one up.
	 */
	private void onMoveUp() {
		if (handlerMoveUp != null) {
			handlerMoveUp.handle(new ActionEvent());
		}
	}




	/**
	 * Notifies the listener when this node wants to move one down.
	 */
	private void onMoveDown() {
		if (handlerMoveDown != null) {
			handlerMoveDown.handle(new ActionEvent());
		}
	}




	/**
	 * Notifies the listener when this node wants to be removed.
	 */
	private void onRemoveThis() {
		if (handlerOnRemove != null) {
			handlerOnRemove.handle(new ActionEvent());
		}
	}

}
