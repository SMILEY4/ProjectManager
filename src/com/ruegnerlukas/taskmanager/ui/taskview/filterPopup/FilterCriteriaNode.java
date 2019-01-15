package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup;

import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;
import com.ruegnerlukas.taskmanager.logic.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FilterCriteriaNode extends HBox {


	public FilterCriteria criteria;
	private Button btnRemove;
	private ChoiceBox<String> choiceAttrib;
	private ChoiceBox<String> choiceCompOp;




	public FilterCriteriaNode(TaskAttribute attribute) {
		this(new FilterCriteria(attribute, FilterCriteria.ComparisonOp.EQUALITY));
	}


	public FilterCriteriaNode(FilterCriteria criteria) {
		this.criteria = criteria;

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
			VBox parent = (VBox)this.getParent();
			parent.getChildren().remove(this);
		});
		this.getChildren().add(btnRemove);


		// choice attribute
		choiceAttrib = new ChoiceBox<>();
		choiceAttrib.setMinSize(250, 32);
		choiceAttrib.setPrefSize(250, 32);
		choiceAttrib.setMaxSize(500, 32);
		for(TaskAttribute attrib : Logic.project.getProject().attributes ) {
			choiceAttrib.getItems().add(attrib.name);
		}
		choiceAttrib.getSelectionModel().select(criteria.attribute.name);
		choiceAttrib.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			for(TaskAttribute attrib : Logic.project.getProject().attributes) {
				if(attrib.name.equals(choiceAttrib.getValue())) {
					this.criteria.attribute = attrib;
					updateComparisonOps();
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
		updateComparisonOps();
		this.getChildren().add(choiceCompOp);

	}




	private void updateComparisonOps() {

		choiceCompOp.getItems().clear();
		for(FilterCriteria.ComparisonOp comp : criteria.getPossibleComparisionOps() ) {
			choiceCompOp.getItems().add(comp.display);
		}

		choiceCompOp.getSelectionModel().select(criteria.comparisonOp.display);
		choiceCompOp.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			for(FilterCriteria.ComparisonOp comp : criteria.getPossibleComparisionOps() ) {
				if(comp.display.equals(choiceCompOp.getValue())) {
					this.criteria.comparisonOp = comp;
					break;
				}
			}
		});

	}




}
