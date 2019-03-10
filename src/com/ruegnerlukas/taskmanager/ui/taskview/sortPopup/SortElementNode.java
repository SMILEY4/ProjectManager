package com.ruegnerlukas.taskmanager.ui.taskview.sortPopup;

import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxOrder;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class SortElementNode extends HBox {


	private SortPopup parent;

	public SortElement.Sort sortDir;
	public TaskAttribute attribute;

	private Button btnRemove;
	private ComboBox<TaskAttribute> choiceAttrib;
	private ComboBox<SortElement.Sort> choiceSortDir;
	private Button btnUp;
	private Button btnDown;




	public SortElementNode(TaskAttribute attribute, SortPopup parent) {
		this(attribute, SortElement.Sort.ASC, parent);
	}




	public SortElementNode(TaskAttribute attribute, SortElement.Sort sortDir, SortPopup parent) {
		this.attribute = attribute;
		this.sortDir = sortDir;
		this.parent = parent;

		// root
		this.setMinSize(100, 34);
		this.setPrefSize(10000, 34);
		this.setMaxSize(10000, 34);
		this.setSpacing(10);


		// remove
		btnRemove = new Button();
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f, "black");
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		btnRemove.setOnAction(event -> {
			VBox boxAttributes = (VBox) this.getParent();
			boxAttributes.getChildren().remove(this);
			parent.onSortChanged(this);
		});
		this.getChildren().add(btnRemove);


		// choice attribute
		choiceAttrib = new ComboBox<>();
		choiceAttrib.setButtonCell(ComboboxUtils.createListCellAttribute());
		choiceAttrib.setCellFactory(param -> ComboboxUtils.createListCellAttribute());
		choiceAttrib.setMinSize(250, 32);
		choiceAttrib.setPrefSize(250, 32);
		choiceAttrib.setMaxSize(500, 32);
		Logic.attribute.getAttributes(new Request<List<TaskAttribute>>(true) {
			@Override
			public void onResponse(Response<List<TaskAttribute>> response) {
				choiceAttrib.getItems().addAll(response.getValue());
			}
		});
		choiceAttrib.getSelectionModel().select(attribute);
		choiceAttrib.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			SortElementNode.this.attribute = choiceAttrib.getValue();
			parent.onSortChanged(SortElementNode.this);
		});
		this.getChildren().add(choiceAttrib);


		// choice sort dir
		choiceSortDir = new ComboBox<>();
		choiceSortDir.setButtonCell(ComboboxUtils.createListCellSortDir());
		choiceSortDir.setCellFactory(param -> ComboboxUtils.createListCellSortDir());
		choiceSortDir.setMinSize(150, 32);
		choiceSortDir.setPrefSize(150, 32);
		choiceSortDir.setMaxSize(150, 32);
		choiceSortDir.getItems().addAll(SortElement.Sort.values());
		choiceSortDir.getSelectionModel().select(sortDir);
		choiceSortDir.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			this.sortDir = choiceSortDir.getValue();
			parent.onSortChanged(SortElementNode.this);
		});
		this.getChildren().add(choiceSortDir);


		// separator
		Region region = new Region();
		region.setMinSize(0, 1);
		region.setPrefSize(10000, 1);
		region.setMaxSize(10000, 1);
		this.getChildren().add(region);


		// button up
		btnUp = new Button();
		ButtonUtils.makeIconButton(btnUp, SVGIcons.LONG_ARROW_UP, 0.7f, "black");
		btnUp.setMinSize(32, 32);
		btnUp.setPrefSize(32, 32);
		btnUp.setMaxSize(32, 32);
		btnUp.setOnAction(event -> {
			VBox boxAttributes = (VBox) this.getParent();
			int index = boxAttributes.getChildren().indexOf(SortElementNode.this);
			if (index > 0) {
				VBoxOrder.moveItem(boxAttributes, SortElementNode.this, -1);
				parent.onSortChanged(SortElementNode.this);
			}

		});
		this.getChildren().add(btnUp);


		// button down
		btnDown = new Button();
		ButtonUtils.makeIconButton(btnDown, SVGIcons.LONG_ARROW_DOWN, 0.7f, "black");
		btnDown.setMinSize(32, 32);
		btnDown.setPrefSize(32, 32);
		btnDown.setMaxSize(32, 32);
		btnDown.setOnAction(event -> {
			VBox boxAttributes = (VBox) this.getParent();
			int index = boxAttributes.getChildren().indexOf(SortElementNode.this);
			if (index < boxAttributes.getChildren().size() - 1) {
				VBoxOrder.moveItem(boxAttributes, SortElementNode.this, +1);
				parent.onSortChanged(SortElementNode.this);
			}
		});
		this.getChildren().add(btnDown);

	}

}
