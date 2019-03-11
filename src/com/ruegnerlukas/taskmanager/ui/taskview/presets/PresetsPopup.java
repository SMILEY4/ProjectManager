package com.ruegnerlukas.taskmanager.ui.taskview.presets;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Request;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.PresetDeletedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.PresetSavedEvent;
import com.ruegnerlukas.taskmanager.data.Preset;
import com.ruegnerlukas.taskmanager.data.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.groups.AttributeGroupData;
import com.ruegnerlukas.taskmanager.data.sorting.SortElement;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PresetsPopup extends AnchorPane {


	private Stage stage;

	@FXML private ComboBox<String> choiceSaved;
	@FXML private Button btnDeleteSaved;
	@FXML private TextField fieldSave;
	@FXML private Button btnSave;

	@FXML CheckBox useIndPresets;
	@FXML VBox boxIndPresets;
	@FXML ComboBox<String> choiceFilter;
	@FXML ComboBox<String> choiceTaskGroup;
	@FXML ComboBox<String> choiceSort;

	@FXML private Button btnCancel;
	@FXML private Button btnAccept;




	public PresetsPopup(Stage stage) {
		this.stage = stage;

		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_PRESETS, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading PresetsPopup-FXML: " + e);
		}

		create();

	}




	private void create() {

		loadSaved();

		choiceSaved.setOnAction(event -> {
			btnDeleteSaved.setDisable(choiceSaved.getValue() == null);
		});


		// delete presets
		btnDeleteSaved.setDisable(choiceSaved.getValue() == null);
		btnDeleteSaved.setOnAction(event -> {
			Logic.presets.deletePreset(choiceSaved.getValue());
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
			Logic.presets.saveAsPreset(name);
		});

		// events presets
		EventManager.registerListener(e -> {
			PresetSavedEvent event = (PresetSavedEvent) e;
			loadSaved();
			choiceSaved.getSelectionModel().select(event.getPreset().name);
		}, PresetSavedEvent.class);

		EventManager.registerListener(e -> {
			PresetDeletedEvent event = (PresetDeletedEvent) e;
			loadSaved();
			choiceSaved.getSelectionModel().select(null);
		}, PresetDeletedEvent.class);


		// use ind presets
		useIndPresets.setSelected(false);
		useIndPresets.setOnAction(event -> {
			boxIndPresets.setDisable(!useIndPresets.isSelected());
		});
		boxIndPresets.setDisable(!useIndPresets.isSelected());


		// accept
		btnAccept.setOnAction(event -> {
			if (useIndPresets.isSelected()) {
				Logic.presets.loadPreset(
						choiceFilter.getValue().isEmpty() ? null : choiceFilter.getValue(),
						choiceTaskGroup.getValue().isEmpty() ? null : choiceTaskGroup.getValue(),
						choiceSort.getValue().isEmpty() ? null : choiceSort.getValue()
				);
			} else {
				if (choiceSaved.getValue() != null) {
					Logic.presets.loadPreset(choiceSaved.getValue());
				}
			}
			EventManager.deregisterListeners(this);
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			EventManager.deregisterListeners(this);
			this.stage.close();
		});

	}




	private void loadSaved() {

		// presets
		Logic.presets.getPresets(new Request<List<Preset>>() {
			@Override
			public void onResponse(Response<List<Preset>> response) {
				choiceSaved.getItems().clear();
				for (Preset preset : response.getValue()) {
					choiceSaved.getItems().add(preset.name);
				}
				btnDeleteSaved.setDisable(choiceSaved.getValue() == null);
			}
		});


		// filter
		Logic.filter.getSavedFilterCriterias(new Request<Map<String, List<FilterCriteria>>>() {
			@Override
			public void onResponse(Response<Map<String, List<FilterCriteria>>> response) {
				choiceFilter.getItems().clear();
				for (String name : response.getValue().keySet()) {
					choiceFilter.getItems().add(name);
				}
				choiceFilter.getItems().add("");
				choiceFilter.getSelectionModel().select("");
			}
		});

		// group
		Logic.group.getSavedGroupOrders(new Request<Map<String, AttributeGroupData>>(true) {
			@Override
			public void onResponse(Response<Map<String, AttributeGroupData>> response) {
				choiceTaskGroup.getItems().clear();
				for (String name : response.getValue().keySet()) {
					choiceTaskGroup.getItems().add(name);
				}
				choiceTaskGroup.getItems().add("");
				choiceTaskGroup.getSelectionModel().select("");
			}
		});

		// sort
		Logic.sort.getSavedSortElements(new Request<Map<String, List<SortElement>>>() {
			@Override
			public void onResponse(Response<Map<String, List<SortElement>>> response) {
				choiceSort.getItems().clear();
				for (String name : response.getValue().keySet()) {
					choiceSort.getItems().add(name);
				}
				choiceSort.getItems().add("");
				choiceSort.getSelectionModel().select("");
			}
		});
	}


}
