package com.ruegnerlukas.taskmanager.ui.taskview.sortPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.SortDeleteSavedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.SortSavedEvent;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.utils.FXMLUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SortPopup extends AnchorPane {


	private Stage stage;

	@FXML private Button btnReset;

	@FXML private ChoiceBox<String> choiceSaved;
	@FXML private Button btnDeleteSaved;
	@FXML private TextField fieldSave;
	@FXML private Button btnSave;

	@FXML private VBox boxAttributes;
	@FXML private Button btnAdd;

	@FXML private Button btnCancel;
	@FXML private Button btnAccept;




	public SortPopup(Stage stage) {
		this.stage = stage;

		try {
			Parent root = FXMLUtils.loadFXML(getClass().getResource("layout_sort.fxml"), this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading SortPopup-FXML: " + e);
		}

		create();

	}




	private void create() {

		// reset
		btnReset.setOnAction(event -> {
			choiceSaved.getSelectionModel().select(null);
			fieldSave.setText("");
			setSortElements(new ArrayList<SortElement>());
		});

		// select saved
		loadSaved();
		choiceSaved.setOnAction(event -> {
			Logic.sort.getSavedSortElements(choiceSaved.getValue(), new Request<List<SortElement>>(true) {
				@Override
				public void onResponse(Response<List<SortElement>> response) {
					setSortElements(response.getValue());
				}
			});
		});

		// delete saved
		btnDeleteSaved.setDisable(choiceSaved.getValue() == null);
		btnDeleteSaved.setOnAction(event -> {
			Logic.sort.deleteSavedSortElements(choiceSaved.getValue());
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
			Logic.sort.saveSortElements(name, getSortElements());
		});

		// events saved
		EventManager.registerListener(e -> {
			SortSavedEvent event = (SortSavedEvent) e;
			loadSaved();
			choiceSaved.getSelectionModel().select(event.getName());
		}, SortSavedEvent.class);

		EventManager.registerListener(e -> {
			SortDeleteSavedEvent event = (SortDeleteSavedEvent) e;
			loadSaved();
			choiceSaved.getSelectionModel().select(null);
		}, SortDeleteSavedEvent.class);


		// elements
		VBoxDragAndDrop.enableDragAndDrop(boxAttributes);
		Logic.sort.getSortElements(new Request<List<SortElement>>(true) {
			@Override
			public void onResponse(Response<List<SortElement>> response) {
				setSortElements(response.getValue());
			}
		});


		// add element
		btnAdd.setOnAction(event -> {
			Logic.attribute.getAttributes(new Request<List<TaskAttribute>>(true) {
				@Override
				public void onResponse(Response<List<TaskAttribute>> response) {
					SortElementNode node = new SortElementNode(response.getValue().get(0), SortPopup.this);
					boxAttributes.getChildren().add(node);
					onSortChanged(node);
				}
			});
		});


		// accept
		btnAccept.setOnAction(event -> {
			List<SortElement> elements = getSortElements();
			Logic.sort.setSort(elements);
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			this.stage.close();
		});

	}




	private void loadSaved() {
		Logic.sort.getSavedSortElements(new Request<Map<String, List<SortElement>>>() {
			@Override
			public void onResponse(Response<Map<String, List<SortElement>>> response) {
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




	private List<SortElement> getSortElements() {
		List<SortElement> elements = new ArrayList<>();
		for (Node node : boxAttributes.getChildren()) {
			SortElementNode sortNode = (SortElementNode) node;
			elements.add(new SortElement(sortNode.sortDir, sortNode.attribute));
		}
		return elements;
	}




	private void setSortElements(List<SortElement> sortElements) {
		boxAttributes.getChildren().clear();
		for (SortElement element : sortElements) {
			boxAttributes.getChildren().add(new SortElementNode(element.attribute, element.sortDir, this));
		}
	}




	protected void onSortChanged(SortElementNode node) {
		choiceSaved.getSelectionModel().select(null);
	}


}
