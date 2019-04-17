package com.ruegnerlukas.taskmanager_old.ui.taskview.filterPopup;

import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.data.taskAttributes.values.TaskAttributeValue;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.attributes.filter.AttributeFilter;
import com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.FilterPopup;
import com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.values.*;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class FilterCriteriaNode extends HBox {


	private com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.FilterPopup parent;

	private Button btnRemove;
	private ComboBox<TaskAttribute> choiceAttrib;
	private ComboBox<FilterCriteria.ComparisonOp> choiceCompOp;
	private List<Node> valueNodes = new ArrayList<>();

	public FilterValue filterValue;
	public TaskAttribute attribute;
	public FilterCriteria.ComparisonOp comparisonOp;
	public TaskAttributeValue compValue;




	public FilterCriteriaNode(TaskAttribute attribute, com.ruegnerlukas.taskmanager.ui.taskview.filterPopup.FilterPopup parent) {
		this(attribute, AttributeFilter.getPossibleComparisionOps(attribute)[0], null, parent);
	}




	public FilterCriteriaNode(TaskAttribute attribute, FilterCriteria.ComparisonOp comparisonOp, TaskAttributeValue compValue, FilterPopup parent) {
		this.attribute = attribute;
		this.comparisonOp = comparisonOp;
		this.compValue = compValue;
		this.parent = parent;


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
			VBox boxParent = (VBox) this.getParent();
			boxParent.getChildren().remove(this);
			parent.onFiltersChanged(FilterCriteriaNode.this);
		});
		this.getChildren().add(btnRemove);


		// choice attribute
		choiceAttrib = new ComboBox<>();
		choiceAttrib.setButtonCell(ComboboxUtils.createListCellAttribute());
		choiceAttrib.setCellFactory(param -> ComboboxUtils.createListCellAttribute());
		choiceAttrib.setMinSize(250, 32);
		choiceAttrib.setPrefSize(250, 32);
		choiceAttrib.setMaxSize(250, 32);

		choiceAttrib.getItems().addAll(Logic.attribute.getAttributes().getValue());

		choiceAttrib.getSelectionModel().select(attribute);
		choiceAttrib.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			this.attribute = choiceAttrib.getValue();
			this.compValue = null;
			parent.onFiltersChanged(FilterCriteriaNode.this);
			update();
		});
		this.getChildren().add(choiceAttrib);


		// choice comparision
		choiceCompOp = new ComboBox<>();
		choiceCompOp.setButtonCell(ComboboxUtils.createListCellComparisonOp());
		choiceCompOp.setCellFactory(param -> ComboboxUtils.createListCellComparisonOp());
		choiceCompOp.setMinSize(200, 32);
		choiceCompOp.setPrefSize(200, 32);
		choiceCompOp.setMaxSize(200, 32);
		this.getChildren().add(choiceCompOp);

		update();
	}




	private void update() {

		choiceCompOp.getItems().setAll(AttributeFilter.getPossibleComparisionOps(attribute));

		boolean foundCompOp = false;
		for (FilterCriteria.ComparisonOp c : AttributeFilter.getPossibleComparisionOps(attribute)) {
			if (c == comparisonOp) {
				foundCompOp = true;
				break;
			}
		}

		if (foundCompOp) {
			choiceCompOp.getSelectionModel().select(comparisonOp);
		} else {
			this.comparisonOp = AttributeFilter.getPossibleComparisionOps(attribute)[0];
			choiceCompOp.getSelectionModel().select(comparisonOp);
		}

		choiceCompOp.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			this.comparisonOp = choiceCompOp.getValue();
			this.compValue = null;
			updateCompValues();
			parent.onFiltersChanged(FilterCriteriaNode.this);
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

		// createItem new filter value
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

		} else if (TaskAttributeType.DEPENDENCY == attribute.data.getType()) {
			filterValue = new DependencyFilterValue();
		}

		// update new filter value
		if (filterValue != null) {
			filterValue.setOnAction(event -> {
				this.compValue = filterValue.getValue();
				parent.onFiltersChanged(FilterCriteriaNode.this);
			});
			filterValue.update(valueNodes, attribute.data, comparisonOp, compValue);
			this.compValue = filterValue.getValue();
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