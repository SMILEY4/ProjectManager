package com.ruegnerlukas.taskmanager.ui.viewtasks.header.popupgroup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskgroup.TaskGroupData;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PopupGroup extends TasksPopup {


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
			Logger.get().error("Error loading GroupPopup-FXML: " + e);
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
		String initialPreset = Data.projectProperty.get().data.selectedGroupPreset.get();
		if (initialPreset != null) {
			choicePreset.getSelectionModel().select(initialPreset);
			groupData.set(Data.projectProperty.get().data.groupPresets.get(initialPreset));
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




	private void onReset() {
		if (groupData.get() == null) {
			onClearGroupData();
		} else {
			onSetGroupData(groupData.get());
		}
		choicePreset.getSelectionModel().clearSelection();
		onPresetDeselected();
	}




	private void onLoadPreset(String name) {
		TaskGroupData preset = PresetLogic.getTaskGroupPreset(Data.projectProperty.get(), name.trim());
		if (preset == null) {
			onClearGroupData();
		} else {
			onSetGroupData(preset);
		}
		onPresetSelected();
	}




	private void onDeletePreset(String name) {
		boolean deleted = PresetLogic.deleteTaskGroupPreset(Data.projectProperty.get(), name);
		if (deleted) {
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.groupPresets.keySet());
			choicePreset.getSelectionModel().clearSelection();
			onPresetDeselected();
		}
	}




	private void onSavePreset(String name) {
		String strName = name.trim();
		boolean saved = PresetLogic.saveTaskGroupPreset(Data.projectProperty.get(), strName, buildGroupData());
		if (saved) {
			fieldPresetName.setText("");
			choicePreset.getItems().clear();
			choicePreset.getItems().addAll(Data.projectProperty.get().data.groupPresets.keySet());
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




	private void onSetGroupData(TaskGroupData data) {
		boxAttributes.getChildren().clear();
		for (TaskAttribute attribute : data.attributes) {
			onAddAttribute(attribute);
		}
		if (data.customHeaderString.get() == null) {
			cbUseHeaderString.setSelected(false);
			fieldHeaderText.setText("");
		} else {
			cbUseHeaderString.setSelected(true);
			fieldHeaderText.setText(data.customHeaderString.get());
		}
	}




	private void onClearGroupData() {
		boxAttributes.getChildren().clear();
		cbUseHeaderString.setSelected(false);
		fieldHeaderText.setText("");
		onModified();
	}




	private void onAddAttribute() {
		TaskAttribute attribute = AttributeLogic.findAttribute(Data.projectProperty.get(), AttributeType.ID);
		onAddAttribute(attribute);
		onModified();
	}




	private void onAddAttribute(TaskAttribute attribute) {
		AttributeNode node = new AttributeNode(attribute);
		node.setOnModified(event -> onModified());
		node.setOnRemove(event -> onRemoveAttribute(node));
		node.setOnMoveUp(event -> onMoveUpAttributeNode(node));
		node.setOnMoveDown(event -> onMoveDownAttributeNode(node));
		boxAttributes.getChildren().add(node);
	}




	private void onMoveUpAttributeNode(AttributeNode attribNode) {
		int index = boxAttributes.getChildren().indexOf(attribNode);
		if (index > 0) {
			VBoxOrder.moveItem(boxAttributes, attribNode, -1);
			onModified();
		}
	}




	private void onMoveDownAttributeNode(AttributeNode attribNode) {
		final int index = boxAttributes.getChildren().indexOf(attribNode);
		if (index < boxAttributes.getChildren().size() - 1) {
			VBoxOrder.moveItem(boxAttributes, attribNode, +1);
			onModified();
		}
	}




	private void onRemoveAttribute(AttributeNode node) {
		boxAttributes.getChildren().remove(node);
		onModified();
	}




	private void onUseHeaderString(boolean useHeaderString) {
		fieldHeaderText.setDisable(!useHeaderString);
		onModified();
	}




	private void onSetHeaderString(String headerString) {
		System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		onModified();
	}




	private TaskGroupData buildGroupData() {
		TaskGroupData data = new TaskGroupData();
		data.customHeaderString.set((cbUseHeaderString.isSelected()) ? fieldHeaderText.getText() : null);
		for (Node node : boxAttributes.getChildren()) {
			data.attributes.add(((AttributeNode) node).getAttribute());
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
		TaskLogic.setGroupData(Data.projectProperty.get(), buildGroupData(), choicePreset.getValue());
		this.getStage().close();
	}


}


