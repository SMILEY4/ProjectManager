package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupfilter;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.logic.PresetLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PopupFilter extends PopupBase {


	// header
	@FXML private Button btnReset;

	// presets
	@FXML private VBox boxPresets;
	@FXML private ComboBox<String> choicePreset;
	@FXML private Button btnDeletePreset;
	@FXML private TextField fieldPresetName;
	@FXML private Button btnSavePreset;

	// body
	@FXML private AnchorPane paneCriteria;
	@FXML private ComboBox<FilterCriteria.CriteriaType> choiceAdd;

	// button
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;


	private final CustomProperty<FilterCriteria> filterCriteria = new CustomProperty<>();
	private final CustomProperty<CriteriaNode> criteriaNode = new CustomProperty<>();




	public PopupFilter() {
		super(800, 600);
	}




	@Override
	public void create() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_FILTER, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading FilterPopup-FXML: " + e);
		}


		// button reset
		btnReset.setOnAction(event -> {
			onReset();
		});


		// load preset
		choicePreset.setPromptText("Select Preset");
		choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsFilter.keySet());
		choicePreset.getSelectionModel().clearSelection();
		choicePreset.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {
				onLoadPreset(newValue);
			}
		}));


		// delete preset
		btnDeletePreset.setOnAction(event -> {
			onDeletePreset(choicePreset.getValue());
		});


		// save preset
		btnSavePreset.setOnAction(event -> {
			onSavePreset(fieldPresetName.getText());
		});


		// subCriteria list
		FilterUIUtils.initComboxCriteriaType(choiceAdd);
		choiceAdd.getItems().addAll(FilterCriteria.CriteriaType.values());
		choiceAdd.setOnAction(event -> {
			if (choiceAdd.getValue() != null) {
				onSetRootCriteria(choiceAdd.getValue());
				onNodeTreeModified();
			}
		});


		// button cancel / accept
		btnCancel.setOnAction(event -> onCancel());
		btnAccept.setOnAction(event -> onAccept());


		// load initial data
		String initialPreset = Data.projectProperty.get().data.presetSelectedFilter.get();
		if (initialPreset != null) {
			choicePreset.getSelectionModel().select(initialPreset);
			filterCriteria.set(Data.projectProperty.get().data.presetsFilter.get(initialPreset));
			onPresetSelected();
		} else {
			FilterCriteria initialCriteria = Data.projectProperty.get().data.filterData.get();
			filterCriteria.set(initialCriteria);
			if (initialCriteria == null) {
				onClearRootCriteria();
			} else {
				onSetRootCriteria(initialCriteria);
			}
			onPresetDeselected();
		}
	}




	/**
	 * Resets the tree of {@link CriteriaNode}s to the original state.
	 */
	private void onReset() {
		if (filterCriteria.get() == null) {
			onClearRootCriteria();
		} else {
			onSetRootCriteria(filterCriteria.get());
		}
		choicePreset.getSelectionModel().clearSelection();
		onPresetDeselected();
	}




	/**
	 * Loads the preset with the given name and selects it. This replaces the previous elements.
	 */
	private void onLoadPreset(String name) {
		FilterCriteria preset = PresetLogic.getFilterPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearRootCriteria();
		} else {
			onSetRootCriteria(preset);
		}
		onPresetSelected();
	}




	/**
	 * Deletes the preset with the given name and deselects it.
	 */
	private void onDeletePreset(String name) {
		boolean deleted = PresetLogic.deleteFilterPreset(Data.projectProperty.get(), name);
		if (deleted) {
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsFilter.keySet());
			choicePreset.getSelectionModel().clearSelection();
			onPresetDeselected();
		}
	}




	/**
	 * Save the current tree of {@link CriteriaNode}s as a new preset with the given name and selects it.
	 */
	private void onSavePreset(String name) {
		String strName = name.trim();
		boolean saved = PresetLogic.saveFilterPreset(Data.projectProperty.get(), strName, criteriaNode.get() == null ? null : criteriaNode.get().buildCriteriaTree());
		if (saved) {
			fieldPresetName.setText("");
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsFilter.keySet());
			choicePreset.getSelectionModel().select(strName);
			onPresetSelected();
		}
	}




	/**
	 * Called when a preset was selected. Enables/Disables the buttons.
	 */
	private void onPresetSelected() {
		btnDeletePreset.setDisable(false);
		fieldPresetName.setDisable(true);
		fieldPresetName.setText("");
		btnSavePreset.setDisable(true);
	}




	/**
	 * Called when a preset was deselected. Enables/Disables the buttons.
	 */
	private void onPresetDeselected() {
		btnDeletePreset.setDisable(true);
		fieldPresetName.setDisable(false);
		btnSavePreset.setDisable(false);
	}




	/**
	 * Sets the root {@link CriteriaNode} to a new default node with the given {@link FilterCriteria.CriteriaType}.
	 */
	private void onSetRootCriteria(FilterCriteria.CriteriaType type) {
		onSetRootCriteria(FilterUIUtils.createFilterCriteria(type));
	}




	/**
	 * Sets the root {@link CriteriaNode} to a new node with the given {@link FilterCriteria}.
	 */
	private void onSetRootCriteria(FilterCriteria criteria) {
		CriteriaNode node = FilterUIUtils.createFilterNode(criteria, event -> onNodeTreeModified());
		node.setOnRemove(event -> onClearRootCriteria());
		node.expandTree();
		AnchorUtils.setAnchors(node, 0, 0, 0, 0);
		paneCriteria.getChildren().setAll(choiceAdd, node);
		criteriaNode.set(node);
		choiceAdd.setDisable(true);
		choiceAdd.setVisible(false);
	}




	/**
	 * Removes the root {@link CriteriaNode} without replacing it.
	 */
	private void onClearRootCriteria() {
		paneCriteria.getChildren().setAll(choiceAdd);
		criteriaNode.set(null);
		choiceAdd.setDisable(false);
		choiceAdd.setVisible(true);
		choiceAdd.getSelectionModel().clearSelection();
	}




	/**
	 * Called when the tree of {@link CriteriaNode}s changed. Clears the currently selected preset.
	 */
	private void onNodeTreeModified() {
		choicePreset.getSelectionModel().clearSelection();
		onPresetDeselected();
	}




	/**
	 * Closes this popup without saving.
	 */
	private void onCancel() {
		this.getStage().close();
	}




	/**
	 * Set the filter data of the {@link Project} and closes this popup.
	 */
	private void onAccept() {
		TaskLogic.setFilter(Data.projectProperty.get(), criteriaNode.get() == null ? null : criteriaNode.get().buildCriteriaTree(), choicePreset.getValue());
		this.getStage().close();
	}

}
