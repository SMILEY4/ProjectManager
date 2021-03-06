package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.localdata.Data;
import com.ruegnerlukas.taskmanager.data.localdata.Project;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.localdata.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.ProjectSettingsView;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.listeners.FXChangeListener;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.EditableLabel;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * An entry in the list of Attributes in the {@link ProjectSettingsView}. <br>
 * Consists of a header area with the {@link AttributeType}, the name and a few general functions;
 * and a content-area containing a {@link AttributeContentNode}.
 */
public class AttributeNode extends AnchorPane {


	private static final double HEADER_HEIGHT = 36;

	private Button btnExpand;
	private Label labelUnsafed;
	private boolean isExpanded = false;

	private AnchorPane contentPane;

	private final TaskAttribute attribute;
	private AttributeContentNode content;

	private FXChangeListener<AttributeType> typeListener;
	private FXChangeListener<String> nameListener;




	public AttributeNode(TaskAttribute attribute) {
		this.attribute = attribute;
		this.getStyleClass().add("attribute-node");

		// this / root
		this.setMinSize(100, HEADER_HEIGHT);
		this.setPrefSize(10000, HEADER_HEIGHT);
		this.setMaxSize(10000, HEADER_HEIGHT);


		// header pane
		AnchorPane headerPane = new AnchorPane();
		headerPane.setMinSize(100, HEADER_HEIGHT);
		headerPane.setPrefSize(10000, HEADER_HEIGHT);
		headerPane.setMaxSize(10000, HEADER_HEIGHT);
		AnchorUtils.setAnchors(headerPane, 0, 0, null, 0);
		this.getChildren().add(headerPane);

		// box header
		HBox headerBox = new HBox();
		headerBox.setAlignment(Pos.CENTER_LEFT);
		headerBox.getStyleClass().add("box-header");
		headerBox.setMinSize(100, HEADER_HEIGHT);
		headerBox.setPrefSize(10000, HEADER_HEIGHT);
		headerBox.setMaxSize(10000, HEADER_HEIGHT);
		headerBox.setSpacing(5);
		AnchorUtils.setAnchors(headerBox, 0, 0, 0, 0);
		headerPane.getChildren().add(headerBox);


		// badge unsafed-changes
		labelUnsafed = new Label("!");
		labelUnsafed.setVisible(false);
		labelUnsafed.setAlignment(Pos.TOP_CENTER);
		labelUnsafed.getStyleClass().add("warning-unsafed-changes");
		labelUnsafed.setMinSize(18, 18);
		labelUnsafed.setPrefSize(18, 18);
		labelUnsafed.setMaxSize(18, 18);
		AnchorUtils.setAnchors(labelUnsafed, -9, -9, null, null);
		headerPane.getChildren().add(labelUnsafed);
		labelUnsafed.setTooltip(new Tooltip("Unsafed Changes!"));


		// remove-button
		Button btnRemove = new Button();
		btnRemove.getStyleClass().add("button-remove");
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f);
		btnRemove.setDisable(attribute.type.get().fixed);
		headerBox.getChildren().add(btnRemove);
		btnRemove.setOnAction(event -> onRemoveAttribute());


		// choice attribute-type
		ComboBox<AttributeType> choiceType = new ComboBox<>();
		choiceType.getStyleClass().add("choice-type");
		choiceType.setButtonCell(ComboboxUtils.createListCellAttributeType());
		choiceType.setCellFactory(param -> ComboboxUtils.createListCellAttributeType());
		if (attribute.type.getValue().fixed) {
			choiceType.getItems().add(attribute.type.getValue());
		} else {
			choiceType.getItems().addAll(AttributeType.getFreeTypes());
		}
		choiceType.getSelectionModel().select(this.attribute.type.getValue());
		choiceType.setMinSize(150, 32);
		choiceType.setPrefSize(150, 32);
		choiceType.setMaxSize(150, 32);
		choiceType.setDisable(attribute.type.get().fixed);
		headerBox.getChildren().add(choiceType);
		choiceType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (!oldValue.equals(newValue)) {
				onTypeSelected(oldValue, newValue);
			}
		});
		typeListener = new FXChangeListener<AttributeType>(attribute.type) {
			@Override
			public void changed(ObservableValue<? extends AttributeType> observable, AttributeType oldValue, AttributeType newValue) {
				choiceType.setValue(newValue);
			}
		};


		// attribute name
		EditableLabel labelName = new EditableLabel(attribute.name.getValue());
		labelName.getStyleClass().add("label-name");
		labelName.setMinSize(32, 32);
		labelName.setPrefSize(10000, 32);
		labelName.setMaxSize(10000, 32);
		labelName.setDisable(attribute.type.get().fixed);
		headerBox.getChildren().add(labelName);
		labelName.addListener((observable, oldValue, newValue) -> onRename(oldValue, newValue));
		nameListener = new FXChangeListener<String>(attribute.name) {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				labelName.setText(newValue);
			}
		};


		// expand-button
		btnExpand = new Button();
		btnExpand.getStyleClass().add("button-expand");
		btnExpand.setMinSize(32, 32);
		btnExpand.setPrefSize(32, 32);
		btnExpand.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_DOWN, 0.75f);
		headerBox.getChildren().add(btnExpand);
		btnExpand.setOnAction(event -> {
			if (isExpanded) {
				onCollapse();
			} else {
				onExpand();
			}
		});

		if (attribute.type.get().fixed && ContentNodeFactory.isUnchangable(attribute)) {
			btnExpand.setDisable(true);
			btnExpand.setVisible(false);
		}


		// content pane
		contentPane = new AnchorPane();
		contentPane.setPadding(new Insets(0, 10, 0, 10));
		contentPane.getStyleClass().add("pane-content");
		contentPane.setVisible(isExpanded);
		contentPane.setMinSize(0, 0);
		contentPane.setPrefSize(10000, 10000);
		contentPane.setMaxSize(10000, 10000);
		AnchorUtils.setAnchors(contentPane, 34, 0, 0, 0);
		this.getChildren().add(contentPane);


		// attribute content
		setAttributeContent();
	}




	/**
	 * (Re-)Sets the content of this {@link AttributeNode} and resizes it to fit the new content.
	 */
	private void setAttributeContent() {

		content = ContentNodeFactory.create(attribute);
		content.setParentNode(this);
		AnchorUtils.setAnchors(content, 0, 0, 0, 0);
		contentPane.getChildren().setAll(content);
		resize();

		content.changedProperty.addListener((observable, oldValue, newValue) -> {
			labelUnsafed.setVisible(newValue);
		});

	}




	/**
	 * Removes the {@link TaskAttribute} handled by this node from the {@link Project}.
	 */
	private void onRemoveAttribute() {
		ProjectLogic.removeAttributeFromProject(Data.projectProperty.get(), getAttribute());
	}




	/**
	 * Changes the {@link AttributeType} of the {@link TaskAttribute} handled by this node to the new given type and resets the content of this node.
	 */
	private void onTypeSelected(AttributeType oldValue, AttributeType newValue) {
		if (oldValue != newValue) {
			if (content != null) {
				content.dispose();
			}
			AttributeLogic.setTaskAttributeType(Data.projectProperty.get(), getAttribute(), newValue);
			setAttributeContent();
			this.getStyleClass().remove("attribute-node-"+oldValue.toString().toLowerCase());
			this.getStyleClass().add("attribute-node-"+newValue.toString().toLowerCase());
		}
	}




	/**
	 * Renames the {@link TaskAttribute} handled by this node.
	 */
	private void onRename(String prevName, String newName) {
		if (!prevName.equals(newName)) {
			AttributeLogic.renameTaskAttribute(getAttribute(), newName);
		}
	}




	/**
	 * Expand this node to show its content/details.
	 */
	private void onExpand() {
		isExpanded = true;
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_UP, 0.75f);
		resize();
	}




	/**
	 * Collapse this node to hide its content/details.
	 */
	private void onCollapse() {
		isExpanded = false;
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_DOWN, 0.75f);
		resize();
	}




	/**
	 * Changes the height (of content area) of this node to fit its content.
	 */
	public void resize() {

		if (isExpanded) {
			contentPane.setVisible(true);
			contentPane.setMinSize(100, content.getContentHeight());
			contentPane.setPrefSize(10000, content.getContentHeight());
			contentPane.setMaxSize(10000, content.getContentHeight());
			this.setMinSize(100, HEADER_HEIGHT + content.getContentHeight());
			this.setPrefSize(10000, HEADER_HEIGHT + content.getContentHeight());
			this.setMaxSize(10000, HEADER_HEIGHT + content.getContentHeight());

		} else {
			contentPane.setVisible(false);
			contentPane.setMinSize(100, 0);
			contentPane.setPrefSize(10000, 0);
			contentPane.setMaxSize(10000, 0);
			this.setMinSize(100, HEADER_HEIGHT);
			this.setPrefSize(10000, HEADER_HEIGHT);
			this.setMaxSize(10000, HEADER_HEIGHT);
		}

	}




	/**
	 * @return the {@link TaskAttribute} handled by this node.
	 */
	public TaskAttribute getAttribute() {
		return attribute;
	}




	public void dispose() {
		if (content != null) {
			content.dispose();
		}
		nameListener.removeFromAll();
		typeListener.removeFromAll();
	}

}
