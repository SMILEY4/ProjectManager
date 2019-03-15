package com.ruegnerlukas.taskmanager.ui.taskview.filterPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.FilterCriteriaDeletedSavedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.FilterCriteriaSavedEvent;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterPopup extends AnchorPane {


	private Stage stage;

	@FXML private Button btnReset;

	@FXML private ComboBox<String> choiceSaved;
	@FXML private Button btnDeleteSaved;
	@FXML private TextField fieldSave;
	@FXML private Button btnSave;

	@FXML private VBox boxAttributes;

	@FXML private Button btnAdd;

	@FXML private Button btnCancel;
	@FXML private Button btnAccept;




	public FilterPopup(Stage stage) {
		this.stage = stage;

		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_FILTER, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading FilterPopup-FXML: " + e);
		}

		create();
	}




	private void create() {

		// reset
		btnReset.setOnAction(event -> {
			choiceSaved.getSelectionModel().select(null);
			fieldSave.setText("");
			setFilterCriterias(new ArrayList<>());
		});

		// select presets
		loadSaved();
		choiceSaved.setOnAction(event -> {
			Logic.filter.getSavedFilterCriteria(choiceSaved.getValue(), new Request<List<FilterCriteria>>(true) {
				@Override
				public void onResponse(Response<List<FilterCriteria>> response) {
					setFilterCriterias(response.getValue());
				}
			});
		});

		// delete presets
		btnDeleteSaved.setDisable(choiceSaved.getValue() == null);
		btnDeleteSaved.setOnAction(event -> {
			Logic.filter.deleteSavedFilterCriteria(choiceSaved.getValue());
		});

		// save name
		fieldSave.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.trim().isEmpty()) {
				btnSave.setDisable(true);
			} else {
				btnSave.setDisable(false);
			}
		});

		// save
		btnSave.setDisable(fieldSave.getText().trim().isEmpty());
		btnSave.setOnAction(event -> {
			String name = fieldSave.getText().trim();
			fieldSave.setText("");
			Logic.filter.saveFilterCriterias(name, getFilterCriteriaList());
		});

		// events presets
		EventManager.registerListener(e -> {
			FilterCriteriaSavedEvent event = (FilterCriteriaSavedEvent) e;
			loadSaved();
			choiceSaved.getSelectionModel().select(event.getName());
		}, FilterCriteriaSavedEvent.class);

		EventManager.registerListener(e -> {
			FilterCriteriaDeletedSavedEvent event = (FilterCriteriaDeletedSavedEvent) e;
			loadSaved();
			choiceSaved.getSelectionModel().select(null);
		}, FilterCriteriaDeletedSavedEvent.class);


		// values
		VBoxDragAndDrop.enableDragAndDrop(boxAttributes);
		Logic.filter.getFilterCriteria(new Request<List<FilterCriteria>>(true) {
			@Override
			public void onResponse(Response<List<FilterCriteria>> response) {
				setFilterCriterias(response.getValue());
			}
		});


		// add attribute
		btnAdd.setOnAction(event -> {
			Logic.attribute.getAttributes(new Request<List<TaskAttribute>>(true) {
				@Override
				public void onResponse(Response<List<TaskAttribute>> response) {
					List<TaskAttribute> attributes = response.getValue();
					FilterCriteriaNode node = new FilterCriteriaNode(attributes.get(0), FilterPopup.this);
					boxAttributes.getChildren().add(node);
					onFiltersChanged(node);
				}
			});
		});


		// accept
		btnAccept.setOnAction(event -> {
			List<FilterCriteria> criteriaList = getFilterCriteriaList();
			Logic.filter.setFilterCriteria(criteriaList);
			EventManager.deregisterListeners(this);
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			EventManager.deregisterListeners(this);
			this.stage.close();
		});

	}




	private void setFilterCriterias(List<FilterCriteria> criterias) {
		boxAttributes.getChildren().clear();
		for (FilterCriteria criteria : criterias) {
			boxAttributes.getChildren().add(new FilterCriteriaNode(criteria.attribute, criteria.comparisonOp, criteria.comparisionValue, this));
		}
	}




	private List<FilterCriteria> getFilterCriteriaList() {
		List<FilterCriteria> criteriaList = new ArrayList<>();
		for (Node node : boxAttributes.getChildren()) {
			FilterCriteriaNode filterNode = (FilterCriteriaNode) node;
			criteriaList.add(new FilterCriteria(filterNode.attribute, filterNode.comparisonOp, filterNode.compValue));
		}
		return criteriaList;
	}




	private void loadSaved() {

		Logic.filter.getSavedFilterCriterias(new Request<Map<String, List<FilterCriteria>>>() {
			@Override
			public void onResponse(Response<Map<String, List<FilterCriteria>>> response) {
				choiceSaved.getItems().clear();
				if (response.getValue().isEmpty()) {
					btnDeleteSaved.setDisable(true);
				} else {
					btnDeleteSaved.setDisable(false);
					for (String name : response.getValue().keySet()) {
						choiceSaved.getItems().add(name);
					}
				}
			}
		});
	}




	protected void onFiltersChanged(FilterCriteriaNode node) {
		choiceSaved.getSelectionModel().select(null);
	}

}
