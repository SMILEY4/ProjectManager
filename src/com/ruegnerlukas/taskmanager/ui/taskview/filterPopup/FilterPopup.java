package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
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

		create();
	}




	private void create() {

		// values
		VBoxDragAndDrop.enableDragAndDrop(boxAttributes);
		Logic.filter.getFilterCriteria(new Request<List<FilterCriteria>>(true) {
			@Override
			public void onResponse(Response<List<FilterCriteria>> response) {
				List<FilterCriteria> filterCriteria = response.getValue();
				for (FilterCriteria criteria : filterCriteria) {
					boxAttributes.getChildren().add(new FilterCriteriaNode(criteria.attribute, criteria.comparisonOp, criteria.comparisionValue));
				}
			}
		});


		// add attribute
		btnAdd.setOnAction(event -> {
			Logic.attribute.getAttributes(new Request<List<TaskAttribute>>(true) {
				@Override
				public void onResponse(Response<List<TaskAttribute>> response) {
					List<TaskAttribute> attributes = response.getValue();
					boxAttributes.getChildren().add(new FilterCriteriaNode(attributes.get(0)));
				}
			});
		});


		// accept
		btnAccept.setOnAction(event -> {
			List<FilterCriteria> criteriaList = new ArrayList<>();
			for (Node node : boxAttributes.getChildren()) {
				FilterCriteriaNode filterNode = (FilterCriteriaNode) node;
				criteriaList.add(new FilterCriteria(filterNode.attribute, filterNode.comparisonOp, filterNode.compValue));
			}
			Logic.filter.setFilterCriteria(criteriaList);
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			this.stage.close();
		});

	}


}
