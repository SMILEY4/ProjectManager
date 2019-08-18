package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupmasterpreset;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.MasterPreset;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.filter.FilterCriteria;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.sort.SortData;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.logic.PresetLogic;
import com.ruegnerlukas.taskmanager.logic.TaskLogic;
import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.PopupBase;
import com.ruegnerlukas.taskmanager.utils.CustomProperty;
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

public class PopupMasterPreset extends PopupBase {


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


	private final CustomProperty<MasterPreset> masterData = new CustomProperty<>();




	public PopupMasterPreset() {
		super(800, 400);
	}




	@Override
	public void create() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_PRESETS, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading PresetsPopup-FXML.", e);
		}


		// button reset
		btnReset.setOnAction(event -> {
			onReset();
		});


		// load preset
		choicePreset.setPromptText("Select Preset");
		choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsMaster.keySet());
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
		selectFilter.getItems().addAll(Data.projectProperty.get().data.presetsFilter.keySet());
		selectFilter.getSelectionModel().clearSelection();
		selectFilter.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {
				onLoadFilterPreset(newValue);
			}
			btnClearFilter.setDisable(selectFilter.getValue() == null);
		}));

		// clear filter preset
		btnClearFilter.setDisable(true);
		btnClearFilter.setOnAction(event -> {
			onClearFilterData();
			onModified();
		});


		// load group preset
		selectTaskGroup.setPromptText("Select Group-Preset");
		selectTaskGroup.getItems().addAll(Data.projectProperty.get().data.presetsGroup.keySet());
		selectTaskGroup.getSelectionModel().clearSelection();
		selectTaskGroup.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {
				onLoadGroupPreset(newValue);
			}
			btnClearTaskGroup.setDisable(selectTaskGroup.getValue() == null);

		}));

		// clear group preset
		btnClearTaskGroup.setDisable(true);
		btnClearTaskGroup.setOnAction(event -> {
			onClearGroupData();
			onModified();
		});


		// load sort preset
		selectSort.setPromptText("Select Sort-Preset");
		selectSort.getItems().addAll(Data.projectProperty.get().data.presetsSort.keySet());
		selectSort.getSelectionModel().clearSelection();
		selectSort.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
			if (newValue != null) {
				onLoadSortPreset(newValue);
			}
			btnClearSort.setDisable(selectSort.getValue() == null);
		}));

		// clear sort preset
		btnClearSort.setDisable(true);
		btnClearSort.setOnAction(event -> {
			onClearSortData();
			onModified();
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
		String initialPreset = Data.projectProperty.get().data.presetSelectedMaster.get();
		if (initialPreset != null) {
			choicePreset.getSelectionModel().select(initialPreset);
			masterData.set(Data.projectProperty.get().data.presetsMaster.get(initialPreset));
			onMasterPresetSelected();

		} else {
			String initialFilterPreset = Data.projectProperty.get().data.presetSelectedFilter.get();
			if (initialFilterPreset != null) {
				selectFilter.getSelectionModel().select(initialFilterPreset);
			}
			String initialGroupPreset = Data.projectProperty.get().data.presetSelectedGroup.get();
			if (initialGroupPreset != null) {
				selectTaskGroup.getSelectionModel().select(initialGroupPreset);
			}
			String initialSortPreset = Data.projectProperty.get().data.presetSelectedSort.get();
			if (initialSortPreset != null) {
				selectSort.getSelectionModel().select(initialSortPreset);
			}
		}
	}




	/**
	 * Resets the presets.
	 */
	private void onReset() {
		onClearAllData();
		choicePreset.getSelectionModel().clearSelection();
		onMasterPresetDeselected();
	}




	/**
	 * Loads the {@link MasterPreset} with the given name and selects it.
	 */
	private void onLoadMasterPreset(String name) {
		MasterPreset preset = PresetLogic.getMasterPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearAllData();
		} else {
			onSetAllData(preset);
		}
		onMasterPresetSelected();
	}




	/**
	 * Deletes the {@link MasterPreset} with the given name and deselects it.
	 */
	private void onDeleteMasterPreset(String name) {
		boolean deleted = PresetLogic.deleteMasterPreset(Data.projectProperty.get(), name);
		if (deleted) {
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsMaster.keySet());
			choicePreset.getSelectionModel().clearSelection();
			onMasterPresetDeselected();
		}
	}




	/**
	 * Saves the current data as a {@link MasterPreset} with the given name and deselects it.
	 */
	private void onSaveMasterPreset(String name) {
		String strName = name.trim();
		boolean saved = PresetLogic.saveMasterPreset(Data.projectProperty.get(), strName, buildMasterPreset());
		if (saved) {
			fieldPresetName.setText("");
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsMaster.keySet());
			choicePreset.getSelectionModel().select(strName);
			onMasterPresetSelected();
		}
	}




	/**
	 * Called when a preset was selected and enables/disables the buttons.
	 */
	private void onMasterPresetSelected() {
		btnDeletePreset.setDisable(false);
		fieldPresetName.setDisable(true);
		fieldPresetName.setText("");
		btnSavePreset.setDisable(true);
	}




	/**
	 * Called when a preset was deselected and enables/disables the buttons.
	 */
	private void onMasterPresetDeselected() {
		btnDeletePreset.setDisable(true);
		fieldPresetName.setDisable(false);
		btnSavePreset.setDisable(false);
	}




	/**
	 * Loads a {@link FilterCriteria} with the given name and selects it.
	 */
	private void onLoadFilterPreset(String name) {
		FilterCriteria preset = PresetLogic.getFilterPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearFilterData();
		} else {
			onSetFilterData(name);
		}
		onModified();
	}




	/**
	 * Loads a {@link TaskGroupData} with the given name and selects it.
	 */
	private void onLoadGroupPreset(String name) {
		TaskGroupData preset = PresetLogic.getTaskGroupPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearGroupData();
		} else {
			onSetGroupData(name);
		}
		onModified();
	}




	/**
	 * Loads a {@link SortData} with the given name and selects it.
	 */
	private void onLoadSortPreset(String name) {
		SortData preset = PresetLogic.getSortPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearSortData();
		} else {
			onSetSortData(name);
		}
		onModified();
	}




	/**
	 * Clears all presets.
	 */
	private void onClearAllData() {
		onClearFilterData();
		onClearGroupData();
		onClearSortData();
	}




	private void onClearFilterData() {
		selectFilter.getSelectionModel().clearSelection();
	}




	private void onClearGroupData() {
		selectTaskGroup.getSelectionModel().clearSelection();
	}




	private void onClearSortData() {
		selectSort.getSelectionModel().clearSelection();
	}




	/**
	 * Sets all presets to the presets of the given {@link MasterPreset}.
	 */
	private void onSetAllData(MasterPreset preset) {
		if (preset.filterPreset == null) {
			onClearFilterData();
		} else {
			onSetFilterData(preset.filterPreset);
		}
		if (preset.groupPreset == null) {
			onClearGroupData();
		} else {
			onSetGroupData(preset.groupPreset);
		}
		if (preset.sortPreset == null) {
			onClearSortData();
		} else {
			onSetSortData(preset.sortPreset);
		}
	}




	private void onSetFilterData(String filterData) {
		selectFilter.getSelectionModel().select(filterData);
	}




	private void onSetGroupData(String groupData) {
		selectTaskGroup.getSelectionModel().select(groupData);
	}




	private void onSetSortData(String sortData) {
		selectSort.getSelectionModel().select(sortData);
	}




	/**
	 * @return a new {@link MasterPreset} with the currently selected presets.
	 */
	private MasterPreset buildMasterPreset() {
		return new MasterPreset(
				selectFilter.getValue(),
				selectTaskGroup.getValue(),
				selectSort.getValue()
		);
	}




	/**
	 * @return the currently selected {@link FilterCriteria} or null.
	 */
	private FilterCriteria getFilterData() {
		if (selectFilter.getValue() == null) {
			return null;
		} else {
			return Data.projectProperty.get().data.presetsFilter.get(selectFilter.getValue());
		}
	}




	/**
	 * @return the currently selected {@link TaskGroupData} or null.
	 */
	private TaskGroupData getTaskGroupData() {
		if (selectTaskGroup.getValue() == null) {
			return null;
		} else {
			return Data.projectProperty.get().data.presetsGroup.get(selectTaskGroup.getValue());
		}
	}




	/**
	 * @return the currently selected {@link SortData} or null.
	 */
	private SortData getSortData() {
		if (selectSort.getValue() == null) {
			return null;
		} else {
			return Data.projectProperty.get().data.presetsSort.get(selectSort.getValue());
		}
	}




	private void onModified() {
		Platform.runLater(() -> choicePreset.getSelectionModel().clearSelection());
		onMasterPresetDeselected();
	}




	/**
	 * Closes this popup without saving.
	 */
	private void onCancel() {
		this.getStage().close();
	}




	/**
	 * Saves this data and closes this popup.
	 */
	private void onAccept() {
		Data.projectProperty.get().data.presetSelectedMaster.set(choicePreset.getValue());
		TaskLogic.setFilter(Data.projectProperty.get(), getFilterData(), selectFilter.getValue());
		TaskLogic.setGroupData(Data.projectProperty.get(), getTaskGroupData(), selectTaskGroup.getValue());
		TaskLogic.setSortData(Data.projectProperty.get(), getSortData(), selectSort.getValue());
		this.getStage().close();
	}


}


