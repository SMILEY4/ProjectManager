package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupfilter;

import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.OrFilterCriteria;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class OrNode extends CriteriaNode {


	private ObservableList<CriteriaNode> children = FXCollections.observableArrayList();




	public OrNode(FilterCriteria criteria, EventHandler<ActionEvent> handlerModified) {
		super(criteria, handlerModified);
		this.setId("or-criteria");

		// label
		Label label = new Label("Or-Criteria");
		label.setPrefHeight(22);
		AnchorUtils.setAnchors(label, 0, null, null, 0);
		this.getChildren().add(label);

		// remove this
		Button btnRemove = new Button();
		btnRemove.setMinSize(22, 22);
		btnRemove.setPrefSize(22, 22);
		btnRemove.setMaxSize(22, 22);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7, "black");
		AnchorUtils.setAnchors(btnRemove, 0, 0, null, null);
		this.getChildren().add(btnRemove);

		btnRemove.setOnAction(event -> {
			removeThisCriteria();
			onModified();
		});

		// content
		VBox content = new VBox();
		content.setMinSize(0, 0);
		content.setSpacing(5);
		AnchorUtils.setAnchors(content, 27, 0, 0, 0);
		this.getChildren().add(content);

		// add content
		ComboBox<FilterCriteria.CriteriaType> boxAdd = new ComboBox<>();
		boxAdd.setPrefSize(100000, 32);
		content.getChildren().add(boxAdd);
		FilterUIUtils.initComboxCriteriaType(boxAdd);
		boxAdd.getItems().addAll(FilterCriteria.CriteriaType.values());
		boxAdd.setOnAction(event -> {
			if (boxAdd.getValue() != null) {
				onCreateNew(boxAdd.getValue());
				onModified();
			}
		});

		// on children-list changed
		children.addListener((ListChangeListener<CriteriaNode>) c -> {
			while (c.next()) {
				if (c.wasAdded()) {
					content.getChildren().addAll(content.getChildren().size() - 1, c.getAddedSubList());
				}
				if (c.wasRemoved()) {
					content.getChildren().removeAll(c.getRemoved());
				}
			}
			Platform.runLater(() -> boxAdd.getSelectionModel().clearSelection());
		});

	}




	@Override
	public void expandTree() {
		for (FilterCriteria child : ((OrFilterCriteria) getCriteria()).subCriteria) {
			CriteriaNode childNode = FilterUIUtils.createFilterNode(child, this.getOnModified());
			childNode.setOnRemove(e -> children.remove(childNode));
			childNode.expandTree();
			this.children.add(childNode);
		}
	}




	private void onCreateNew(FilterCriteria.CriteriaType type) {
		if (type == null) {
			return;
		}
		final CriteriaNode node = FilterUIUtils.createFilterNode(FilterUIUtils.createFilterCriteria(type), this.getOnModified());
		node.setOnRemove(e -> children.remove(node));
		children.add(node);
	}




	@Override
	public FilterCriteria buildCriteriaTree() {
		List<FilterCriteria> list = new ArrayList<>();
		for (CriteriaNode childNode : children) {
			list.add(childNode.buildCriteriaTree());
		}
		return new OrFilterCriteria(list);
	}

}
