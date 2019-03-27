package com.ruegnerlukas.taskmanager.ui.projectsettingsview;

import com.ruegnerlukas.simpleutils.logging.logger.Logger;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.EventManager;
import com.ruegnerlukas.taskmanager.architecture.eventsystem.events.*;
import com.ruegnerlukas.taskmanager.data.Project;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.taskAttributes.TaskAttributeType;
import com.ruegnerlukas.taskmanager.logic.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.TabContent;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.AttributeNode;
import com.ruegnerlukas.taskmanager.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.button.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.combobox.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.editablelabel.EditableLabel;
import com.ruegnerlukas.taskmanager.utils.uielements.vbox.VBoxDragAndDrop;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProjectSettingsView extends AnchorPane implements TabContent {


	public static final String TITLE = "Project Settings";

	@FXML private AnchorPane rootProjectSettingsView;

	private EditableLabel labelName;

	@FXML private AnchorPane paneHeader;
	@FXML private VBox boxSettings;

	@FXML private VBox boxSettingsAttribs;
	@FXML private Button btnLockAttributes;
	@FXML private VBox boxTaskAttribs;
	@FXML private Button btnAddAttribute;

	@FXML private VBox boxSettingsAppearance;
	@FXML private Button btnLockCardAppearance;
	@FXML private ComboBox<TaskAttribute> choicePrevAtt1;
	@FXML private ComboBox<String> choicePrevType1;
	@FXML private ComboBox<TaskAttribute> choicePrevAtt2;
	@FXML private ComboBox<String> choicePrevType2;
	private final TaskAttribute emptyAttribute = new TaskAttribute("< Empty >", null);




	public ProjectSettingsView() {
		try {
			Parent root = UIDataHandler.loadFXML(UIModule.VIEW_PROJECTSETTINGS, this);
			AnchorUtils.setAnchors(root, 0, 0, 0, 0);
			this.getChildren().add(root);
		} catch (IOException e) {
			Logger.get().error("Error loading ProjectSettingsView-FXML: " + e);
		}

		create();
		createSettingsAttributes();
		createSettingsCardAppearance();
		setupListeners();
	}




	private void create() {

		// Project name
		labelName = new EditableLabel("");
		AnchorPane.setBottomAnchor(labelName, 0.0);
		AnchorPane.setTopAnchor(labelName, 0.0);
		AnchorPane.setLeftAnchor(labelName, 0.0);
		AnchorPane.setRightAnchor(labelName, 0.0);
		paneHeader.getChildren().add(labelName);
		labelName.addListener((observable, oldValue, newValue) -> {
			Logic.project.renameProject(newValue);
		});

		Project project = Logic.project.getCurrentProject().getValue();
		if (project != null) {
			labelName.setText(project.name);
		}


	}




	private void createSettingsAttributes() {

		// lock task values
		Boolean attributesLocked = Logic.attribute.getAttributeLock().getValue();
		if (attributesLocked != null) {
			if (attributesLocked) {
				ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_CLOSED, 1f, "black");
			} else {
				ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_OPEN, 1f, "black");
			}
			setAttributeLock(attributesLocked);
		}
		btnLockAttributes.setOnAction(event -> Logic.attribute.setAttributeLock(!Logic.attribute.getAttributeLock().getValue()));

		// listen for attribute-lock-event
		EventManager.registerListener(this, e -> {
			AttributeLockEvent event = (AttributeLockEvent) e;
			if (event.getLockNow()) {
				ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_CLOSED, 1f, "black");
			} else {
				ButtonUtils.makeIconButton(btnLockAttributes, SVGIcons.LOCK_OPEN, 1f, "black");
			}
			setAttributeLock(event.getLockNow());
		}, AttributeLockEvent.class);

		// task values
		boxTaskAttribs.getChildren().clear();
		boxTaskAttribs.setSpacing(3);
		VBoxDragAndDrop.enableDragAndDrop(boxTaskAttribs);


		// add attribute
		btnAddAttribute.setOnAction(event -> {
			Logic.attribute.createAttribute(
					"Attribute " + Integer.toHexString(new Integer(new Random().nextInt()).hashCode()), TaskAttributeType.TEXT);
		});

		// add initial attributes
		List<TaskAttribute> attributes = Logic.attribute.getAttributes().getValue();
		for (TaskAttribute attribute : attributes) {
			AttributeNode attrNode = new AttributeNode(attribute);
			boxTaskAttribs.getChildren().add(attrNode);
		}

		// listen for added attributes
		EventManager.registerListener(this, e -> {
			AttributeCreatedEvent event = (AttributeCreatedEvent) e;
			AttributeNode attrNode = new AttributeNode(event.getAttribute());
			boxTaskAttribs.getChildren().add(attrNode);
		}, AttributeCreatedEvent.class);

		// listen for removed attributes
		EventManager.registerListener(this, e -> {
			AttributeRemovedEvent event = (AttributeRemovedEvent) e;
			for (Node node : boxTaskAttribs.getChildren()) {
				AttributeNode attributeNode = (AttributeNode) node;
				if (attributeNode.getAttribute() == event.getAttribute()) {
					boxTaskAttribs.getChildren().remove(node);
					attributeNode.dispose();
					break;
				}
			}
		}, AttributeRemovedEvent.class);

	}




	private void createSettingsCardAppearance() {

		// lock appearance
		Boolean appearanceLocked = Logic.attribute.getCardAttributeLock().getValue();
		if (appearanceLocked != null) {
			if (appearanceLocked) {
				ButtonUtils.makeIconButton(btnLockCardAppearance, SVGIcons.LOCK_CLOSED, 1f, "black");
			} else {
				ButtonUtils.makeIconButton(btnLockCardAppearance, SVGIcons.LOCK_OPEN, 1f, "black");
			}
			setAppearanceLock(appearanceLocked);
		}
		btnLockCardAppearance.setOnAction(event -> Logic.attribute.setCardAttributeLock(!Logic.attribute.getCardAttributeLock().getValue()));

		// listen for appearance-lock-event
		EventManager.registerListener(this, e -> {
			AppearanceLockEvent event = (AppearanceLockEvent) e;
			if (event.getLockNow()) {
				ButtonUtils.makeIconButton(btnLockCardAppearance, SVGIcons.LOCK_CLOSED, 1f, "black");
			} else {
				ButtonUtils.makeIconButton(btnLockCardAppearance, SVGIcons.LOCK_OPEN, 1f, "black");
			}
			setAppearanceLock(event.getLockNow());
		}, AppearanceLockEvent.class);

		// setup comboboxes
		choicePrevAtt1.getItems().add(emptyAttribute);
		choicePrevAtt2.getItems().add(emptyAttribute);
		choicePrevAtt1.setButtonCell(ComboboxUtils.createListCellAttribute());
		choicePrevAtt1.setCellFactory(param -> ComboboxUtils.createListCellAttribute());
		choicePrevAtt2.setButtonCell(ComboboxUtils.createListCellAttribute());
		choicePrevAtt2.setCellFactory(param -> ComboboxUtils.createListCellAttribute());

		choicePrevAtt1.getSelectionModel().select(emptyAttribute);
		choicePrevAtt2.getSelectionModel().select(emptyAttribute);

		List<TaskAttribute> cardAttributes = Logic.attribute.getCardAttributes().getValue();
		if (cardAttributes.size() >= 1) {
			choicePrevAtt1.getSelectionModel().select(cardAttributes.get(0));
		}
		if (cardAttributes.size() >= 2) {
			choicePrevAtt2.getSelectionModel().select(cardAttributes.get(1));
		}

		List<TaskAttribute> attributes = Logic.attribute.getAttributes().getValue();
		choicePrevAtt1.getItems().addAll(attributes);
		choicePrevAtt2.getItems().addAll(attributes);

		choicePrevAtt1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != newValue && oldValue != null && newValue != null) {
				List<TaskAttribute> list = new ArrayList<>();
				if (choicePrevAtt1.getSelectionModel().getSelectedItem() != emptyAttribute) {
					list.add(choicePrevAtt1.getSelectionModel().getSelectedItem());
				}
				if (choicePrevAtt2.getSelectionModel().getSelectedItem() != emptyAttribute) {
					list.add(choicePrevAtt2.getSelectionModel().getSelectedItem());
				}
				choicePrevType1.setDisable(choicePrevAtt1.getValue() == emptyAttribute);
				Logic.attribute.setCardAttributes(list);
			}
		});

		choicePrevAtt2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != newValue && oldValue != null && newValue != null) {
				List<TaskAttribute> list = new ArrayList<>();
				if (choicePrevAtt1.getSelectionModel().getSelectedItem() != emptyAttribute) {
					list.add(choicePrevAtt1.getSelectionModel().getSelectedItem());
				}
				if (choicePrevAtt2.getSelectionModel().getSelectedItem() != emptyAttribute) {
					list.add(choicePrevAtt2.getSelectionModel().getSelectedItem());
				}
				choicePrevType2.setDisable(choicePrevAtt2.getValue() == emptyAttribute);
				Logic.attribute.setCardAttributes(list);
			}
		});

		choicePrevType1.getItems().addAll(AttributeLogic.CARD_ATTRIB_DISPLAY_TYPE_FULL, AttributeLogic.CARD_ATTRIB_DISPLAY_TYPE_ICON, AttributeLogic.CARD_ATTRIB_DISPLAY_TYPE_BOTH);
		choicePrevType2.getItems().addAll(AttributeLogic.CARD_ATTRIB_DISPLAY_TYPE_FULL, AttributeLogic.CARD_ATTRIB_DISPLAY_TYPE_ICON, AttributeLogic.CARD_ATTRIB_DISPLAY_TYPE_BOTH);
		choicePrevType1.getSelectionModel().select(0);
		choicePrevType2.getSelectionModel().select(0);
		choicePrevType1.setDisable(true);
		choicePrevType2.setDisable(true);

		choicePrevType1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(choicePrevAtt1.getValue() != null && choicePrevAtt1.getValue() != emptyAttribute) {
				Logic.attribute.setCardAttributeDisplayType(choicePrevAtt1.getValue(), newValue);
			}
		});

		choicePrevType2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(choicePrevAtt2.getValue() != null && choicePrevAtt2.getValue() != emptyAttribute) {
				Logic.attribute.setCardAttributeDisplayType(choicePrevAtt2.getValue(), newValue);
			}
		});


		// listen for added attributes
		EventManager.registerListener(this, e -> {
			AttributeCreatedEvent event = (AttributeCreatedEvent) e;
			choicePrevAtt1.getItems().add(event.getAttribute());
			choicePrevAtt2.getItems().add(event.getAttribute());
		}, AttributeCreatedEvent.class);

		// listen for removed attributes
		EventManager.registerListener(this, e -> {
			AttributeRemovedEvent event = (AttributeRemovedEvent) e;
			if (choicePrevAtt1.getSelectionModel().getSelectedItem() == event.getAttribute()) {
				choicePrevAtt1.getSelectionModel().select(emptyAttribute);
			}
			if (choicePrevAtt2.getSelectionModel().getSelectedItem() == event.getAttribute()) {
				choicePrevAtt2.getSelectionModel().select(emptyAttribute);
			}
			choicePrevAtt1.getItems().remove(event.getAttribute());
			choicePrevAtt2.getItems().remove(event.getAttribute());
		}, AttributeRemovedEvent.class);

	}




	private void setupListeners() {

		// listen for project renamed
		EventManager.registerListener(this, e -> {
			ProjectRenamedEvent event = (ProjectRenamedEvent) e;
			labelName.setText(event.getNewName());
		}, ProjectRenamedEvent.class);

	}




	private void setAttributeLock(boolean locked) {
		btnAddAttribute.setDisable(locked);
		for (Node node : boxTaskAttribs.getChildren()) {
			if (node instanceof AttributeNode) {
				((AttributeNode) node).setLocked(locked);
			}
		}
	}




	private void setAppearanceLock(boolean locked) {
		for (int i = 1; i < boxSettingsAppearance.getChildren().size(); i++) {
			boxSettingsAppearance.getChildren().get(i).setDisable(locked);
		}
	}




	@Override
	public void onOpen() {

	}




	@Override
	public void onClose() {
		EventManager.deregisterListeners(this);
	}




	@Override
	public void onShow() {

	}




	@Override
	public void onHide() {

	}

}
