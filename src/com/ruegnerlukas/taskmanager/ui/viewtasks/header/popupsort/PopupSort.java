package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupsort;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.logic.MiscLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.VBoxOrder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings ("Duplicates")
public class PopupSort extends PopupBase {


	// header
	@FXML private Button btnReset;

	// presets
	@FXML private VBox boxPresets;
	@FXML private ComboBox<String> choicePreset;
	@FXML private Button btnDeletePreset;
	@FXML private TextField fieldPresetName;
	@FXML private Button btnSavePreset;

	// body
	@FXML private VBox boxElements;
	@FXML private Button btnAdd;

	// bottom
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;


	private final CustomProperty<SortData> sortData = new CustomProperty<>();




	public PopupSort() {
		super(800, 600);
	}




	@Override
	public void create() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_SORT, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading SortPopup-FXML.", e);
		}


		// button reset
		btnReset.setOnAction(event -> {
			onReset();
		});


		// load preset
		choicePreset.setPromptText("Select Preset");
		choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsGroup.keySet());
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


		// add attribute
		btnAdd.setOnAction(event -> {
			onAddSortElement();
		});


		// button cancel
		btnCancel.setOnAction(event -> {
			onCancel();
		});


		// button accept
		btnAccept.setOnAction(event -> {
			onAccept();
		});


		// load initial data
		String initialPreset = Data.projectProperty.get().data.presetSelectedSort.get();
		if (initialPreset != null) {
			choicePreset.getSelectionModel().select(initialPreset);
			sortData.set(Data.projectProperty.get().data.presetsSort.get(initialPreset));
			onPresetSelected();
		} else {
			SortData initialData = Data.projectProperty.get().data.sortData.get();
			sortData.set(initialData);
			if (initialData == null) {
				onClearSortData();
			} else {
				onSetSortData(initialData);
			}
			onPresetDeselected();
		}

	}




	/**
	 * Resets the list to the original state.
	 */
	private void onReset() {
		if (sortData.get() == null) {
			onClearSortData();
		} else {
			onSetSortData(sortData.get());
		}
		choicePreset.getSelectionModel().clearSelection();
		onPresetDeselected();
	}




	/**
	 * Loads the preset with the given name and selects it. This replaces the previous elements.
	 */
	private void onLoadPreset(String name) {
		SortData preset = MiscLogic.getSortPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearSortData();
		} else {
			onSetSortData(preset);
		}
		onPresetSelected();
	}




	/**
	 * Deletes the preset with the given name and deselects it.
	 */
	private void onDeletePreset(String name) {
		boolean deleted = MiscLogic.deleteSortPreset(Data.projectProperty.get(), name);
		if (deleted) {
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsSort.keySet());
			choicePreset.getSelectionModel().clearSelection();
			onPresetDeselected();
		}
	}




	/**
	 * Save the current list of {@link SortElementNode} as a new preset with the given name and selects it.
	 */
	private void onSavePreset(String name) {
		String strName = name.trim();
		boolean saved = MiscLogic.saveSortPreset(Data.projectProperty.get(), strName, buildSortData());
		if (saved) {
			fieldPresetName.setText("");
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsSort.keySet());
			choicePreset.getSelectionModel().select(strName);
			onPresetSelected();
		}
	}




	/**
	 * Called when a preset was deselected. Enables/Disables the buttons and clears the name field.
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
	 * Adds all {@link SortElement}s from the given {@link SortData} to the list (Removes the previous elements).
	 */
	private void onSetSortData(SortData data) {
		boxElements.getChildren().clear();
		for (SortElement element : data.sortElements) {
			onAddSortElement(element);
		}
	}




	/**
	 * Removes all {@link SortElementNode} from the list.
	 */
	private void onClearSortData() {
		boxElements.getChildren().clear();
		onModified();
	}




	/**
	 * Adds a new {@link SortElement} as a {@link SortElementNode} to the bottom of the list.
	 */
	private void onAddSortElement() {
		TaskAttribute attribute = AttributeLogic.findAttributeByType(Data.projectProperty.get(), AttributeType.ID);
		SortElement element = new SortElement(attribute, SortElement.SortDir.ASC);
		onAddSortElement(element);
		onModified();
	}




	/**
	 * Adds the given {@link SortElement} as a {@link SortElementNode} to the bottom of the list.
	 */
	private void onAddSortElement(SortElement element) {
		SortElementNode node = new SortElementNode(element);
		node.setOnModified(event -> onModified());
		node.setOnRemove(event -> onRemoveSortElement(node));
		node.setOnMoveUp(event -> onMoveUpSortNode(node));
		node.setOnMoveDown(event -> onMoveDownSortNode(node));
		boxElements.getChildren().add(node);
	}




	/**
	 * Moves the given {@link SortElementNode} one up in the list (if possible).
	 */
	private void onMoveUpSortNode(SortElementNode sortNode) {
		int index = boxElements.getChildren().indexOf(sortNode);
		if (index > 0) {
			VBoxOrder.moveItem(boxElements, sortNode, -1);
			onModified();
		}
	}




	/**
	 * Moves the given {@link SortElementNode} one down in the list (if possible).
	 */
	private void onMoveDownSortNode(SortElementNode sortNode) {
		final int index = boxElements.getChildren().indexOf(sortNode);
		if (index < boxElements.getChildren().size() - 1) {
			VBoxOrder.moveItem(boxElements, sortNode, +1);
			onModified();
		}
	}




	/**
	 * Removes the given {@link SortElementNode} from the list.
	 */
	private void onRemoveSortElement(SortElementNode node) {
		boxElements.getChildren().remove(node);
		onModified();
	}




	/**
	 * @return a created {@link SortData} from the list of {@link SortElementNode}.
	 */
	private SortData buildSortData() {
		List<SortElement> sortElements = new ArrayList<>();
		for (Node node : boxElements.getChildren()) {
			sortElements.add(((SortElementNode) node).buildSortElement());
		}
		return new SortData(sortElements);
	}




	/**
	 * Called when the list or an item changed. Clears the currently selected preset.
	 */
	private void onModified() {
		Platform.runLater(() -> choicePreset.getSelectionModel().clearSelection());
		onPresetDeselected();
	}




	/**
	 * Closes this preset without saving.
	 */
	private void onCancel() {
		this.getStage().close();
	}




	/**
	 * Set the sort data of the {@link Project} and closes this popup.
	 */
	private void onAccept() {
		TaskLogic.setSortData(Data.projectProperty.get(), buildSortData(), choicePreset.getValue());
		this.getStage().close();
	}


}


