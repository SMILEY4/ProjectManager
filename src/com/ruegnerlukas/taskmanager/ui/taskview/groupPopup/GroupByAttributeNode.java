package com.ruegnerlukas.taskmanager.ui.taskview.groupPopup;

import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxOrder;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GroupByAttributeNode extends HBox {


	public TaskAttribute attribute;

	private Button btnRemove;
	private ChoiceBox<String> choiceAttrib;
	private Button btnUp;
	private Button btnDown;




	public GroupByAttributeNode(TaskAttribute attribute) {
		this.attribute = attribute;


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
		});
		this.getChildren().add(btnRemove);


		// choice attribute
		choiceAttrib = new ChoiceBox<>();
		choiceAttrib.setMinSize(250, 32);
		choiceAttrib.setPrefSize(250, 32);
		choiceAttrib.setMaxSize(500, 32);
		for (TaskAttribute attrib : Logic.project.getProject().attributes) {
			choiceAttrib.getItems().add(attrib.name);
		}
		choiceAttrib.getSelectionModel().select(attribute.name);
		choiceAttrib.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			for (TaskAttribute attrib : Logic.project.getProject().attributes) {
				if (attrib.name.equals(choiceAttrib.getValue())) {
					this.attribute = attrib;
					break;
				}
			}
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
			}
		});
		this.getChildren().add(btnDown);

	}


}
