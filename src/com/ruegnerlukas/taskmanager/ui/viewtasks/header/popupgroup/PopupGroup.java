package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupgroup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.taskgroup.TaskGroupData;
import com.ruegnerlukas.taskmanager.logic.PresetLogic;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PopupGroup extends PopupBase {


	// header
	@FXML private Button btnReset;

	// presets
	@FXML private VBox boxPresets;
	@FXML private ComboBox<String> choicePreset;
	@FXML private Button btnDeletePreset;
	@FXML private TextField fieldPresetName;
	@FXML private Button btnSavePreset;

	// body
	@FXML private VBox boxAttributes;
	@FXML private Button btnAdd;

	// header string
	@FXML private CheckBox cbUseHeaderString;
	@FXML private TextField fieldHeaderText;

	// bottom
	@FXML private Button btnCancel;
	@FXML private Button btnAccept;


	private final CustomProperty<TaskGroupData> groupData = new CustomProperty<>();




	public PopupGroup() {
		super(800, 600);
	}




	@Override
	public void create() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_GROUPBY, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading GroupPopup-FXML.", e);
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
			onAddAttribute();
		});


		// use header string
		cbUseHeaderString.selectedProperty().addListener((observable, oldValue, newValue) -> {
			onUseHeaderString(cbUseHeaderString.isSelected());
		});


		// text header string
		fieldHeaderText.textProperty().addListener(((observable, oldValue, newValue) -> {
			onSetHeaderString(fieldHeaderText.getText());
		}));


		// button cancel
		btnCancel.setOnAction(event -> {
			onCancel();
		});


		// button accept
		btnAccept.setOnAction(event -> {
			onAccept();
		});

		// load initial data
		String initialPreset = Data.projectProperty.get().data.presetSelectedGroup.get();
		if (initialPreset != null) {
			choicePreset.getSelectionModel().select(initialPreset);
			groupData.set(Data.projectProperty.get().data.presetsGroup.get(initialPreset));
			onPresetSelected();
		} else {
			TaskGroupData initialData = Data.projectProperty.get().data.groupData.get();
			groupData.set(initialData);
			if (initialData == null) {
				onClearGroupData();
			} else {
				onSetGroupData(initialData);
			}
			onPresetDeselected();
		}
	}




	/**
	 * Resets the list and the fields to the original state.
	 */
	private void onReset() {
		if (groupData.get() == null) {
			onClearGroupData();
		} else {
			onSetGroupData(groupData.get());
		}
		choicePreset.getSelectionModel().clearSelection();
		onPresetDeselected();
	}




	/**
	 * Loads the preset with the given name and selects it. This replaces the previous elements.
	 */
	private void onLoadPreset(String name) {
		TaskGroupData preset = PresetLogic.getTaskGroupPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearGroupData();
		} else {
			onSetGroupData(preset);
		}
		onPresetSelected();
	}




	/**
	 * Deletes the preset with the given name and deselects it.
	 */
	private void onDeletePreset(String name) {
		boolean deleted = PresetLogic.deleteTaskGroupPreset(Data.projectProperty.get(), name);
		if (deleted) {
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsGroup.keySet());
			choicePreset.getSelectionModel().clearSelection();
			onPresetDeselected();
		}
	}




	/**
	 * Save the current list of {@link AttributeNode} as a new preset with the given name and selects it.
	 */
	private void onSavePreset(String name) {
		String strName = name.trim();
		boolean saved = PresetLogic.saveTaskGroupPreset(Data.projectProperty.get(), strName, buildGroupData());
		if (saved) {
			fieldPresetName.setText("");
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.presetsGroup.keySet());
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
	 * Adds all {@link TaskAttribute}s from the given {@link TaskGroupData} to the list (Removes the previous elements)
	 * and copies the data for the header string from the {@link TaskGroupData} to the input fields.
	 */
	private void onSetGroupData(TaskGroupData data) {
		boxAttributes.getChildren().clear();
		for (TaskAttribute attribute : data.attributes) {
			onAddAttribute(attribute);
		}
		if (data.customHeaderString == null) {
			cbUseHeaderString.setSelected(false);
			fieldHeaderText.setText("");
		} else {
			cbUseHeaderString.setSelected(true);
			fieldHeaderText.setText(data.customHeaderString);
		}
	}




	/**
	 * Removes all item from the list and resets the fields for the header string.
	 */
	private void onClearGroupData() {
		boxAttributes.getChildren().clear();
		cbUseHeaderString.setSelected(false);
		fieldHeaderText.setText("");
		onModified();
	}




	/**
	 * Creates a new {@link TaskAttribute} and adds it as a {@link AttributeNode} to the list.
	 */
	private void onAddAttribute() {
		TaskAttribute attribute = AttributeLogic.findAttributeByType(Data.projectProperty.get(), AttributeType.ID);
		onAddAttribute(attribute);
		onModified();
	}




	/**
	 * Adds a new {@link AttributeNode} with the given {@link TaskAttribute} to the list.
	 */
	private void onAddAttribute(TaskAttribute attribute) {
		AttributeNode node = new AttributeNode(attribute);
		node.setOnModified(event -> onModified());
		node.setOnRemove(event -> onRemoveAttribute(node));
		node.setOnMoveUp(event -> onMoveUpAttributeNode(node));
		node.setOnMoveDown(event -> onMoveDownAttributeNode(node));
		boxAttributes.getChildren().add(node);
	}




	/**
	 * Moves the given {@link AttributeNode} one up.
	 */
	private void onMoveUpAttributeNode(AttributeNode attribNode) {
		int index = boxAttributes.getChildren().indexOf(attribNode);
		if (index > 0) {
			VBoxOrder.moveItem(boxAttributes, attribNode, -1);
			onModified();
		}
	}




	/**
	 * Moves the given {@link AttributeNode} one down.
	 */
	private void onMoveDownAttributeNode(AttributeNode attribNode) {
		final int index = boxAttributes.getChildren().indexOf(attribNode);
		if (index < boxAttributes.getChildren().size() - 1) {
			VBoxOrder.moveItem(boxAttributes, attribNode, +1);
			onModified();
		}
	}




	/**
	 * Removes the given {@link AttributeNode} from the list of nodes
	 */
	private void onRemoveAttribute(AttributeNode node) {
		boxAttributes.getChildren().remove(node);
		onModified();
	}




	/**
	 * Called when the user enabled/disabled the custom header string.
	 * This also enables/disables the text field for the header string.
	 */
	private void onUseHeaderString(boolean useHeaderString) {
		fieldHeaderText.setDisable(!useHeaderString);
		onModified();
	}




	/**
	 * Called when a text of the input field for the header string was changed.
	 */
	private void onSetHeaderString(String headerString) {
		onModified();
	}




	/**
	 * @return a created {@link TaskGroupData} from the list of {@link AttributeNode}.
	 */
	private TaskGroupData buildGroupData() {
		List<TaskAttribute> attributes = new ArrayList<>();
		for (Node node : boxAttributes.getChildren()) {
			attributes.add(((AttributeNode) node).getAttribute());
		}
		return new TaskGroupData((cbUseHeaderString.isSelected()) ? fieldHeaderText.getText() : null, attributes);
	}




	/**
	 * Called when the list or an item changed. Clears the currently selected preset.
	 */
	private void onModified() {
		Platform.runLater(() -> choicePreset.getSelectionModel().clearSelection());
		onPresetDeselected();
	}




	/**
	 * Closes this popup without saving.
	 */
	private void onCancel() {
		this.getStage().close();
	}




	/**
	 * Set the group data of the {@link Project} and closes this popup.
	 */
	private void onAccept() {
		TaskLogic.setGroupData(Data.projectProperty.get(), buildGroupData(), choicePreset.getValue());
		this.getStage().close();
	}


}


