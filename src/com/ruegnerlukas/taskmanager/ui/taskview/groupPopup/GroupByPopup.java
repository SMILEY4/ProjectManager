package com.ruegnerlukas.taskmanager.ui.taskview.groupPopup;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.Response;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.GroupOrderDeletedSavedEvent;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.GroupOrderSavedEvent;
import com.ruegnerlukas.taskmanager.data.groups.AttributeGroupData;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupByPopup extends AnchorPane {


	private Stage stage;

	@FXML private Button btnReset;

	@FXML private ComboBox<String> choiceSaved;
	@FXML private Button btnDeleteSaved;
	@FXML private TextField fieldSave;
	@FXML private Button btnSave;

	@FXML private VBox boxAttributes;
	@FXML private Button btnAdd;

	@FXML private CheckBox cbUseHeaderString;
	@FXML private TextField fieldHeaderText;

	@FXML private Button btnCancel;
	@FXML private Button btnAccept;




	public GroupByPopup(Stage stage) {
		this.stage = stage;

		try {
			Parent root = UIDataHandler.loadFXML(UIModule.POPUP_GROUPBY, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading GroupByPopup-FXML: " + e);
		}

		create();
	}




	private void create() {

		// reset
		btnReset.setOnAction(event -> {
			choiceSaved.getSelectionModel().select(null);
			fieldSave.setText("");
			setAttributes(new ArrayList<TaskAttribute>());
			cbUseHeaderString.setSelected(false);
			fieldHeaderText.setText("");
		});

		// select presets
		loadSaved();
		choiceSaved.setOnAction(event -> {

			AttributeGroupData groupData = Logic.group.getSavedGroupOrder(choiceSaved.getValue()).getValue();
			if (groupData != null) {
				setAttributes(groupData.attributes);
				cbUseHeaderString.setSelected(groupData.useCustomHeader);
				fieldHeaderText.setText(groupData.customHeader);
			}
		});

		// delete presets
		btnDeleteSaved.setDisable(choiceSaved.getValue() == null);
		btnDeleteSaved.setOnAction(event -> {
			Logic.group.deleteSavedGroupOrder(choiceSaved.getValue());
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
			Logic.group.saveGroupOrder(name, getAttributes(), cbUseHeaderString.isSelected(), cbUseHeaderString.isSelected() ? fieldHeaderText.getText() : "");
		});

		// events presets
		EventManager.registerListener(e -> {
			GroupOrderSavedEvent event = (GroupOrderSavedEvent) e;
			loadSaved();
			choiceSaved.getSelectionModel().select(event.getName());
		}, GroupOrderSavedEvent.class);

		EventManager.registerListener(e -> {
			GroupOrderDeletedSavedEvent event = (GroupOrderDeletedSavedEvent) e;
			loadSaved();
			choiceSaved.getSelectionModel().select(null);
		}, GroupOrderDeletedSavedEvent.class);


		// values
		VBoxDragAndDrop.enableDragAndDrop(boxAttributes);
		setAttributes(Logic.group.getTaskGroupOrder().getValue());


		// add attribute
		btnAdd.setOnAction(event -> {
			List<TaskAttribute> attributes = Logic.attribute.getAttributes().getValue();
			GroupByAttributeNode node = new GroupByAttributeNode(attributes.get(0), GroupByPopup.this);
			boxAttributes.getChildren().add(node);
			onAttributesChanged(node);
		});


		// custom header string
		Response<String> response = Logic.group.getCustomHeaderString();
		if (response.getState() == Response.State.SUCCESS) {
			cbUseHeaderString.setSelected(true);
			fieldHeaderText.setText(response.getValue());
		} else {
			cbUseHeaderString.setSelected(false);
			fieldHeaderText.setText("");
		}

		fieldHeaderText.textProperty().addListener((observable, oldValue, newValue) -> {
			choiceSaved.getSelectionModel().select(null);
		});
		fieldHeaderText.setDisable(!cbUseHeaderString.isSelected());

		cbUseHeaderString.setOnAction(event -> {
			choiceSaved.getSelectionModel().select(null);
			fieldHeaderText.setDisable(!cbUseHeaderString.isSelected());
		});


		// accept
		btnAccept.setOnAction(event -> {
			EventManager.deregisterListeners(this);
			List<TaskAttribute> attributes = getAttributes();
			Logic.group.setUseCustomHeaderString(cbUseHeaderString.isSelected());
			Logic.group.setGroupHeaderString(fieldHeaderText.getText());
			Logic.group.setGroupOrder(attributes);
			this.stage.close();
		});


		// cancel
		btnCancel.setOnAction(event -> {
			EventManager.deregisterListeners(this);
			this.stage.close();
		});

	}




	private void loadSaved() {
		Response<Map<String, AttributeGroupData>> response = Logic.group.getSavedGroupOrders();
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




	private List<TaskAttribute> getAttributes() {
		List<TaskAttribute> attributes = new ArrayList<>();
		for (Node node : boxAttributes.getChildren()) {
			GroupByAttributeNode groupByNode = (GroupByAttributeNode) node;
			attributes.add(groupByNode.attribute);
		}
		return attributes;
	}




	private void setAttributes(List<TaskAttribute> attributes) {
		boxAttributes.getChildren().clear();
		for (TaskAttribute attribute : attributes) {
			boxAttributes.getChildren().add(new GroupByAttributeNode(attribute, this));
		}
	}




	protected void onAttributesChanged(GroupByAttributeNode node) {
		choiceSaved.getSelectionModel().select(null);
	}


}
