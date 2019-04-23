package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupmasterpreset;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.logic.PresetLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.ui.viewtasks.header.TasksPopup;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

@SuppressWarnings ("Duplicates")
public class PopupMasterPreset extends TasksPopup {


	// header
	@FXML private Button btnReset;

	// presets
	@FXML private VBox boxPresets;
	@FXML private ComboBox<String> choicePreset;
	@FXML private Button btnDeletePreset;
	@FXML private TextField fieldPresetName;
	@FXML private Button btnSavePreset;

	// filter
	@FXML private HBox boxPresetFilter;
	@FXML private ComboBox<String> selectFilter;
	@FXML private Button btnClearFilter;

	// taskgroups
	@FXML private HBox boxPresetGroup;
	@FXML private ComboBox<String> selectTaskGroup;
	@FXML private Button btnClearTaskGroup;

	// sort
	@FXML private HBox boxPresetSort;
	@FXML private ComboBox<String> selectSort;
	@FXML private Button btnClearSort;

	// bottom
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;




	public PopupMasterPreset() {
		super(800, 600);
	}




	@Override
	public void create() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_PRESETS, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading PresetsPopup-FXML: " + e);
		}


		// button reset
		btnReset.setOnAction(event -> {
			onReset();
		});


		// load preset
		choicePreset.setPromptText("Select Preset");
		choicePreset.getItems().addAll(Data.projectProperty.get().data.masterPresets.keySet());
		choicePreset.getSelectionModel().clearSelection();
		choicePreset.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {
				onLoadMasterPreset(newValue);
			}
		}));


		// delete preset
		btnDeletePreset.setOnAction(event -> {
			onDeleteMasterPreset(choicePreset.getValue());
		});


		// save preset
		btnSavePreset.setOnAction(event -> {
			onSaveMasterPreset(fieldPresetName.getText());
		});


		// load filter preset
		selectFilter.setPromptText("Select Filter-Preset");
		selectFilter.getItems().addAll(Data.projectProperty.get().data.filterPresets.keySet());
		selectFilter.getSelectionModel().clearSelection();
		selectFilter.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {
				onLoadFilterPreset(newValue);
			}
		}));

		// clear filter preset
		btnClearFilter.setOnAction(event -> {
			onClearFilterData();
		});


		// load group preset
		selectTaskGroup.setPromptText("Select Group-Preset");
		selectTaskGroup.getItems().addAll(Data.projectProperty.get().data.groupPresets.keySet());
		selectTaskGroup.getSelectionModel().clearSelection();
		selectTaskGroup.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {
				onLoadGroupPreset(newValue);
			}
		}));

		// clear filter preset
		btnClearTaskGroup.setOnAction(event -> {
			onClearGroupData();
		});


		// load sort preset
		selectSort.setPromptText("Select Sort-Preset");
		selectSort.getItems().addAll(Data.projectProperty.get().data.sortPresets.keySet());
		selectSort.getSelectionModel().clearSelection();
		selectSort.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {
				onLoadSortPreset(newValue);
			}
		}));

		// clear filter preset
		btnClearSort.setOnAction(event -> {
			onClearSortData();
		});


		// button cancel
		btnCancel.setOnAction(event -> {
			onCancel();
		});


		// button accept
		btnAccept.setOnAction(event -> {
			onAccept();
		});

	}




	private void onReset() {
		onClearAllData();
		choicePreset.getSelectionModel().clearSelection();
		onMasterPresetDeselected();
	}




	private void onLoadMasterPreset(String name) {
		MasterPreset preset = PresetLogic.getMasterPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearAllData();
		} else {
			onSetAllData(preset);
		}
		onMasterPresetSelected();
	}




	private void onDeleteMasterPreset(String name) {
		boolean deleted = PresetLogic.deleteMasterPreset(Data.projectProperty.get(), name);
		if (deleted) {
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.masterPresets.keySet());
			choicePreset.getSelectionModel().clearSelection();
			onMasterPresetDeselected();
		}
	}




	private void onSaveMasterPreset(String name) {
		String strName = name.trim();
		boolean saved = PresetLogic.saveMasterPreset(Data.projectProperty.get(), strName, buildMasterPreset());
		if (saved) {
			fieldPresetName.setText("");
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.masterPresets.keySet());
			choicePreset.getSelectionModel().select(strName);
			onMasterPresetSelected();
		}
	}




	private void onMasterPresetSelected() {
		btnDeletePreset.setDisable(false);
		fieldPresetName.setDisable(true);
		fieldPresetName.setText("");
		btnSavePreset.setDisable(true);
	}




	private void onMasterPresetDeselected() {
		btnDeletePreset.setDisable(true);
		fieldPresetName.setDisable(false);
		btnSavePreset.setDisable(false);
	}




	private void onLoadFilterPreset(String name) {
		FilterCriteria preset = PresetLogic.getFilterPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearFilterData();
		} else {
			onSetFilterData(name);
		}
	}




	private void onLoadGroupPreset(String name) {
		TaskGroupData preset = PresetLogic.getTaskGroupPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearGroupData();
		} else {
			onSetGroupData(name);
		}
	}




	private void onLoadSortPreset(String name) {
		SortData preset = PresetLogic.getSortPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearSortData();
		} else {
			onSetSortData(name);
		}
	}




	private void onClearAllData() {
		onClearFilterData();
		onClearGroupData();
		onClearSortData();
	}




	private void onClearFilterData() {
		selectFilter.getSelectionModel().clearSelection();
		onModified();
	}




	private void onClearGroupData() {
		selectTaskGroup.getSelectionModel().clearSelection();
		onModified();
	}




	private void onClearSortData() {
		selectSort.getSelectionModel().clearSelection();
		onModified();
	}




	private void onSetAllData(MasterPreset preset) {
		if (preset.filterPreset.get() == null) {
			onClearFilterData();
		} else {
			onSetFilterData(preset.filterPreset.get());
		}
		if (preset.groupPreset.get() == null) {
			onClearGroupData();
		} else {
			onSetGroupData(preset.groupPreset.get());
		}
		if (preset.sortPreset.get() == null) {
			onClearSortData();
		} else {
			onSetSortData(preset.sortPreset.get());
		}
	}




	private void onSetFilterData(String filterData) {
		selectFilter.getSelectionModel().select(filterData);
		onModified();
	}




	private void onSetGroupData(String groupData) {
		selectTaskGroup.getSelectionModel().select(groupData);
		onModified();
	}




	private void onSetSortData(String sortData) {
		selectSort.getSelectionModel().select(sortData);
		onModified();
	}




	private MasterPreset buildMasterPreset() {
		MasterPreset preset = new MasterPreset();
		preset.filterPreset.set(selectFilter.getValue());
		preset.groupPreset.set(selectTaskGroup.getValue());
		preset.sortPreset.set(selectSort.getValue());
		return preset;
	}




	private FilterCriteria getFilterData() {
		if (selectFilter.getValue() == null) {
			return null;
		} else {
			return Data.projectProperty.get().data.filterPresets.get(selectFilter.getValue());
		}
	}




	private TaskGroupData getTaskGroupData() {
		if (selectTaskGroup.getValue() == null) {
			return null;
		} else {
			return Data.projectProperty.get().data.groupPresets.get(selectTaskGroup.getValue());
		}
	}




	private SortData getSortData() {
		if (selectSort.getValue() == null) {
			return null;
		} else {
			return Data.projectProperty.get().data.sortPresets.get(selectSort.getValue());
		}
	}




	private void onModified() {
		Platform.runLater(() -> choicePreset.getSelectionModel().clearSelection());
		onMasterPresetDeselected();
	}




	private void onCancel() {
		this.getStage().close();
	}




	private void onAccept() {
		TaskLogic.setFilter(Data.projectProperty.get(), getFilterData());
		TaskLogic.setGroupData(Data.projectProperty.get(), getTaskGroupData());
		TaskLogic.setSortData(Data.projectProperty.get(), getSortData());
		this.getStage().close();
	}


}


