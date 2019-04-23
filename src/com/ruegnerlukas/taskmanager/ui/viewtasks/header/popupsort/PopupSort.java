package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupsort;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortElement;
import com.ruegnerlukas.taskmanager.logic.PresetLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.TasksPopup;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
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

@SuppressWarnings ("Duplicates")
public class PopupSort extends TasksPopup {


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
			Logger.get().error("Error loading SortPopup-FXML: " + e);
		}


		// button reset
		btnReset.setOnAction(event -> {
			onReset();
		});


		// load preset
		choicePreset.setPromptText("Select Preset");
		choicePreset.getItems().addAll(Data.projectProperty.get().data.groupPresets.keySet());
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
		SortData initialData = Data.projectProperty.get().data.sortData.get();
		sortData.set(initialData);
		if (initialData == null) {
			onClearSortData();
		} else {
			onSetSortData(initialData);
		}
		onPresetDeselected();
	}




	private void onReset() {
		if (sortData.get() == null) {
			onClearSortData();
		} else {
			onSetSortData(sortData.get());
		}
		choicePreset.getSelectionModel().clearSelection();
		onPresetDeselected();
	}




	private void onLoadPreset(String name) {
		SortData preset = PresetLogic.getSortPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearSortData();
		} else {
			onSetSortData(preset);
		}
		onPresetSelected();
	}




	private void onDeletePreset(String name) {
		boolean deleted = PresetLogic.deleteSortPreset(Data.projectProperty.get(), name);
		if (deleted) {
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.sortPresets.keySet());
			choicePreset.getSelectionModel().clearSelection();
			onPresetDeselected();
		}
	}




	private void onSavePreset(String name) {
		String strName = name.trim();
		boolean saved = PresetLogic.saveSortPreset(Data.projectProperty.get(), strName, buildSortData());
		if (saved) {
			fieldPresetName.setText("");
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.sortPresets.keySet());
			choicePreset.getSelectionModel().select(strName);
			onPresetSelected();
		}
	}




	private void onPresetSelected() {
		btnDeletePreset.setDisable(false);
		fieldPresetName.setDisable(true);
		fieldPresetName.setText("");
		btnSavePreset.setDisable(true);
	}




	private void onPresetDeselected() {
		btnDeletePreset.setDisable(true);
		fieldPresetName.setDisable(false);
		btnSavePreset.setDisable(false);
	}




	private void onSetSortData(SortData data) {
		boxElements.getChildren().clear();
		for (SortElement element : data.sortElements) {
			onAddSortElement(element);
		}
	}




	private void onClearSortData() {
		boxElements.getChildren().clear();
		onModified();
	}




	private void onAddSortElement() {
		TaskAttribute attribute = AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.ID);
		SortElement element = new SortElement(attribute, SortElement.SortDir.ASC);
		onAddSortElement(element);
		onModified();
	}




	private void onAddSortElement(SortElement element) {
		SortElementNode node = new SortElementNode(element);
		node.setOnModified(event -> onModified());
		node.setOnRemove(event -> onRemoveSortElement(node));
		node.setOnMoveUp(event -> onMoveUpSortNode(node));
		node.setOnMoveDown(event -> onMoveDownSortNode(node));
		boxElements.getChildren().add(node);
	}




	private void onMoveUpSortNode(SortElementNode sortNode) {
		int index = boxElements.getChildren().indexOf(sortNode);
		if (index > 0) {
			VBoxOrder.moveItem(boxElements, sortNode, -1);
			onModified();
		}
	}




	private void onMoveDownSortNode(SortElementNode sortNode) {
		final int index = boxElements.getChildren().indexOf(sortNode);
		if (index < boxElements.getChildren().size() - 1) {
			VBoxOrder.moveItem(boxElements, sortNode, +1);
			onModified();
		}
	}




	private void onRemoveSortElement(SortElementNode node) {
		boxElements.getChildren().remove(node);
		onModified();
	}




	private SortData buildSortData() {
		SortData data = new SortData();
		for (Node node : boxElements.getChildren()) {
			data.sortElements.add(((SortElementNode) node).buildSortElement());
		}
		return data;
	}




	private void onModified() {
		Platform.runLater(() -> choicePreset.getSelectionModel().clearSelection());
		onPresetDeselected();
	}




	private void onCancel() {
		this.getStage().close();
	}




	private void onAccept() {
		TaskLogic.setSortData(Data.projectProperty.get(), buildSortData());
		this.getStage().close();
	}


}


