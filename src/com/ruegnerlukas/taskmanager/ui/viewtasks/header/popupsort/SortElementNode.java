package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupsort;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
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

public class SortElementNode extends HBox {


	private ComboBox<TaskAttribute> boxAttribute;
	private ComboBox<SortElement.SortDir> boxSortDir;

	private EventHandler<ActionEvent> handlerOnRemove;
	private EventHandler<ActionEvent> handlerModified;
	private EventHandler<ActionEvent> handlerMoveUp;
	private EventHandler<ActionEvent> handlerMoveDown;




	public SortElementNode(SortElement element) {

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
		boxAttribute.getSelectionModel().select(element.attribute.get());
		boxAttribute.setOnAction(event -> onSelectAttribute(boxAttribute.getValue()));
		this.getChildren().add(boxAttribute);

		// box sort dir
		boxSortDir = new ComboBox<>();
		boxSortDir.setButtonCell(ComboboxUtils.createListCellSortDir());
		boxSortDir.setCellFactory(param -> ComboboxUtils.createListCellSortDir());
		boxSortDir.setMinWidth(100);
		boxSortDir.setPrefWidth(150);
		boxSortDir.setMinHeight(32);
		boxSortDir.setMaxHeight(32);
		boxSortDir.getItems().addAll(SortElement.SortDir.values());
		boxSortDir.getSelectionModel().select(element.dir.get());
		boxSortDir.setOnAction(event -> onSelectSortDir(boxSortDir.getValue()));
		this.getChildren().add(boxSortDir);

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




	public TaskAttribute getAttribute() {
		return boxAttribute.getValue();
	}




	public SortElement.SortDir getSortDir() {
		return boxSortDir.getValue();
	}




	/**
	 * @return a new {@link SortElement} with the same {@link TaskAttribute} and {@link SortElement.SortDir}.
	 */
	public SortElement buildSortElement() {
		return new SortElement(getAttribute(), getSortDir());
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
	 * notifies the listener that a new {@link TaskAttribute} was selected
	 */
	private void onSelectAttribute(TaskAttribute attribute) {
		if (handlerModified != null) {
			handlerModified.handle(new ActionEvent());
		}
	}




	/**
	 * notifies the listener that a new {@link SortElement.SortDir} was selected
	 */
	private void onSelectSortDir(SortElement.SortDir dir) {
		if (handlerModified != null) {
			handlerModified.handle(new ActionEvent());
		}
	}




	/**
	 * notifies the listener that this node wants to move one space up in the list.
	 */
	private void onMoveUp() {
		if (handlerMoveUp != null) {
			handlerMoveUp.handle(new ActionEvent());
		}
	}




	/**
	 * notifies the listener that this node wants to move one space down in the list.
	 */
	private void onMoveDown() {
		if (handlerMoveDown != null) {
			handlerMoveDown.handle(new ActionEvent());
		}
	}




	/**
	 * notifies the listener that this node wants to be removed from the list.
	 */
	private void onRemoveThis() {
		if (handlerOnRemove != null) {
			handlerOnRemove.handle(new ActionEvent());
		}
	}


}
