package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup;

import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values.*;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class FilterCriteriaNode extends HBox {


	private Button btnRemove;
	private ChoiceBox<String> choiceAttrib;
	private ChoiceBox<String> choiceCompOp;
	private List<Node> valueNodes = new ArrayList<>();

	public FilterValue filterValue;
	public TaskAttribute attribute;
	public FilterCriteria.ComparisonOp comparisonOp;
	public TaskAttributeValue compValue;




	public FilterCriteriaNode(TaskAttribute attribute) {
		this(attribute, FilterCriteria.getPossibleComparisionOps(attribute)[0], null);
	}




	public FilterCriteriaNode(TaskAttribute attribute, FilterCriteria.ComparisonOp comparisonOp, TaskAttributeValue compValue) {
		this.attribute = attribute;
		this.comparisonOp = comparisonOp;
		this.compValue = compValue;


		// root
		this.setMinSize(100, 34);
		this.setPrefSize(10000, 34);
		this.setMaxSize(10000, 34);
		this.setSpacing(10);
		this.setPadding(new Insets(0, 3, 0, 0));
		this.setAlignment(Pos.CENTER_LEFT);


		// remove
		btnRemove = new Button();
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f, "black");
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		btnRemove.setOnAction(event -> {
			VBox parent = (VBox) this.getParent();
			parent.getChildren().remove(this);
		});
		this.getChildren().add(btnRemove);


		// choice attribute
		choiceAttrib = new ChoiceBox<>();
		choiceAttrib.setMinSize(250, 32);
		choiceAttrib.setPrefSize(250, 32);
		choiceAttrib.setMaxSize(250, 32);
		for (TaskAttribute attrib : Logic.project.getProject().attributes) {
			choiceAttrib.getItems().add(attrib.name);
		}
		choiceAttrib.getSelectionModel().select(attribute.name);
		choiceAttrib.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			for (TaskAttribute attrib : Logic.project.getProject().attributes) {
				if (attrib.name.equals(choiceAttrib.getValue())) {
					this.attribute = attrib;
					this.compValue = null;
					update();
					break;
				}
			}
		});
		this.getChildren().add(choiceAttrib);


		// choice comparision
		choiceCompOp = new ChoiceBox<>();
		choiceCompOp.setMinSize(200, 32);
		choiceCompOp.setPrefSize(200, 32);
		choiceCompOp.setMaxSize(200, 32);
		this.getChildren().add(choiceCompOp);

		update();
	}




	private void update() {

		choiceCompOp.getItems().clear();
		for (FilterCriteria.ComparisonOp comp : FilterCriteria.getPossibleComparisionOps(attribute)) {
			choiceCompOp.getItems().add(comp.display);
		}

		boolean foundCompOp = false;
		for (FilterCriteria.ComparisonOp c : FilterCriteria.getPossibleComparisionOps(attribute)) {
			if (c == comparisonOp) {
				foundCompOp = true;
				break;
			}
		}

		if (foundCompOp) {
			choiceCompOp.getSelectionModel().select(comparisonOp.display);
		} else {
			this.comparisonOp = FilterCriteria.getPossibleComparisionOps(attribute)[0];
			choiceCompOp.getSelectionModel().select(comparisonOp.display);
		}

		choiceCompOp.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			for (FilterCriteria.ComparisonOp comp : FilterCriteria.getPossibleComparisionOps(attribute)) {
				if (comp.display.equals(choiceCompOp.getValue())) {
					this.comparisonOp = comp;
					this.compValue = null;
					updateCompValues();
					break;
				}
			}
		});


		updateCompValues();
	}




	private void updateCompValues() {

		this.getChildren().removeAll(valueNodes);
		valueNodes.clear();

		// discard old filter value
		if (filterValue != null) {
			filterValue.setOnAction(null);
		}

		// create new filter value
		if (TaskAttributeType.BOOLEAN == attribute.data.getType()) {
			filterValue = new BoolFilterValue();

		} else if (TaskAttributeType.CHOICE == attribute.data.getType()) {
			filterValue = new ChoiceFilterValue();

		} else if (TaskAttributeType.DESCRIPTION == attribute.data.getType()) {
			filterValue = new DescriptionFilterValue();

		} else if (TaskAttributeType.FLAG == attribute.data.getType()) {
			filterValue = new FlagFilterValue();

		} else if (TaskAttributeType.ID == attribute.data.getType()) {
			filterValue = new IDFilterValue();

		} else if (TaskAttributeType.NUMBER == attribute.data.getType()) {
			filterValue = new NumberFilterValue();

		} else if (TaskAttributeType.TEXT == attribute.data.getType()) {
			filterValue = new TextFilterValue();
		}

		// update new filter value
		if (filterValue != null) {
			filterValue.setOnAction(event -> {
				this.compValue = filterValue.getValue();
			});
			filterValue.update(valueNodes, attribute.data, comparisonOp, compValue);
		}


		if (valueNodes.isEmpty()) {
			Label label = new Label("Undefined");
			label.setMinSize(10, 32);
			label.setPrefSize(100000, 32);
			label.setMaxSize(100000, 32);
			label.setDisable(true);
			valueNodes.add(label);
		}

		this.getChildren().addAll(valueNodes);
	}


}
