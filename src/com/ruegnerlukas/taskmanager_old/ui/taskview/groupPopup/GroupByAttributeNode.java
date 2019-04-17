package com.ruegnerlukas.taskmanager_old.ui.taskview.groupPopup;

import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.taskview.groupPopup.GroupByPopup;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxOrder;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GroupByAttributeNode extends HBox {


	public TaskAttribute attribute;

	private com.ruegnerlukas.taskmanager.ui.taskview.groupPopup.GroupByPopup parent;

	private Button btnRemove;
	private ComboBox<TaskAttribute> choiceAttrib;
	private Button btnUp;
	private Button btnDown;




	public GroupByAttributeNode(TaskAttribute attribute, GroupByPopup parent) {
		this.attribute = attribute;
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
			parent.onAttributesChanged(GroupByAttributeNode.this);
		});
		this.getChildren().add(btnRemove);


		// choice attribute
		choiceAttrib = new ComboBox<>();
		choiceAttrib.setButtonCell(ComboboxUtils.createListCellAttribute());
		choiceAttrib.setCellFactory(param -> ComboboxUtils.createListCellAttribute());
		choiceAttrib.setMinSize(250, 32);
		choiceAttrib.setPrefSize(250, 32);
		choiceAttrib.setMaxSize(500, 32);

		choiceAttrib.getItems().addAll(Logic.attribute.getAttributes().getValue());

		choiceAttrib.getSelectionModel().select(attribute);
		choiceAttrib.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			GroupByAttributeNode.this.attribute = choiceAttrib.getValue();
			parent.onAttributesChanged(GroupByAttributeNode.this);
		});
		this.getChildren().add(choiceAttrib);


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
			int index = boxAttributes.getChildren().indexOf(GroupByAttributeNode.this);
			if (index > 0) {
				VBoxOrder.moveItem(boxAttributes, GroupByAttributeNode.this, -1);
				parent.onAttributesChanged(GroupByAttributeNode.this);
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
			int index = boxAttributes.getChildren().indexOf(GroupByAttributeNode.this);
			if (index < boxAttributes.getChildren().size() - 1) {
				VBoxOrder.moveItem(boxAttributes, GroupByAttributeNode.this, +1);
				parent.onAttributesChanged(GroupByAttributeNode.this);
			}
		});
		this.getChildren().add(btnDown);

	}


}