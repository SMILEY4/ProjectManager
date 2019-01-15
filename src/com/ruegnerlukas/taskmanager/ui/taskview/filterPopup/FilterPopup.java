package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.logic.LogicService;
import com.ruegnerlukas.taskmanager.logic.data.filter.criteria.FilterCriteria;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterPopup extends AnchorPane {


	private Stage stage;
	@FXML private VBox boxAttributes;
	@FXML private Button btnAdd;
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;




	public FilterPopup(Stage stage) {
		this.stage = stage;
		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_filter.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading FilterPopup-FXML: " + e);
		}

		setupListeners();
		create();
	}




	private void create() {

		// attributes
		VBoxDragAndDrop.enableDragAndDrop(boxAttributes);
		for(FilterCriteria criteria : LogicService.get().getProject().filterCriteria) {
			boxAttributes.getChildren().add(new FilterCriteriaNode(criteria));
		}


		// add attribute
		btnAdd.setOnAction(event -> {
			boxAttributes.getChildren().add(new FilterCriteriaNode(LogicService.get().getProject().attributes.get(0)));
		});


		// accept
		btnAccept.setOnAction(event -> {
			List<FilterCriteria> attributes = new ArrayList<>();
			for(Node node : boxAttributes.getChildren()) {
				FilterCriteriaNode groupByNode = (FilterCriteriaNode)node;
				attributes.add(groupByNode.criteria);
			}
			LogicService.get().setFilterCriteria(attributes);
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			this.stage.close();
		});

	}




	private void setupListeners() {
	}


}
