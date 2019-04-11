package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.Logic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes.*;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.EditableLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

@SuppressWarnings ("FieldCanBeLocal") // TODO
public class AttributeNode extends AnchorPane {


	private static final double HEADER_HEIGHT = 35;

	private AnchorPane headerPane;
	private HBox headerBox;
	private Button btnRemove;
	private ComboBox<TaskAttribute.Type> choiceType;
	private EditableLabel labelName;
	private Button btnExpand;
	private Label labelUnsafed;
	private boolean isExpanded = false;

	private AnchorPane contentPane;

	private final TaskAttribute attribute;
	private AttributeContentNode content;


	/*
	THIS: AnchorPane
	  - HEADER_PANE: AnchorPane
	  .   - HEADER_BOX: HBox
	  .   .   - BTN_REMOVE: Button
	  .   .   - CHOICE_TYPE: ChoiceBox
	  .   .   - ATTRIB_NAME: EditableLabel
	  .   .   - BTN_EXPAND: Button
	  - CONTENT_PANE: AnchorPane
	  .   .   - CONTENT: AttributeContentNode
	 */




	public AttributeNode(TaskAttribute attribute) {
		this.attribute = attribute;


		// this / root
		this.setId("attribute");
		this.setMinSize(100, HEADER_HEIGHT);
		this.setPrefSize(10000, HEADER_HEIGHT);
		this.setMaxSize(10000, HEADER_HEIGHT);

		// header pane
		headerPane = new AnchorPane();
		headerPane.setId("pane_header");
		headerPane.setMinSize(100, HEADER_HEIGHT);
		headerPane.setPrefSize(10000, HEADER_HEIGHT);
		headerPane.setMaxSize(10000, HEADER_HEIGHT);
		AnchorUtils.setAnchors(headerPane, 0, 0, 0, 0);
		this.getChildren().add(headerPane);


		// badge unsafed-changes
		labelUnsafed = new Label("!");
		labelUnsafed.setVisible(false);
		labelUnsafed.setAlignment(Pos.TOP_CENTER);
		labelUnsafed.setId("unsafed_changes_header");
		labelUnsafed.setMinSize(18, 18);
		labelUnsafed.setPrefSize(18, 18);
		labelUnsafed.setMaxSize(18, 18);
		AnchorUtils.setAnchors(labelUnsafed, -9, -9, null, null);
		headerPane.getChildren().add(labelUnsafed);
		labelUnsafed.setTooltip(new Tooltip("Unsafed Changes!"));


		// box header
		headerBox = new HBox();
		headerBox.setMinSize(100, HEADER_HEIGHT);
		headerBox.setPrefSize(10000, HEADER_HEIGHT);
		headerBox.setMaxSize(10000, HEADER_HEIGHT);
		headerBox.setSpacing(5);
		AnchorUtils.setAnchors(headerBox, 0, 0, 0, 0);
		headerPane.getChildren().add(headerBox);


		// remove-button
		btnRemove = new Button();
		btnRemove.setMinSize(32, 32);
		btnRemove.setPrefSize(32, 32);
		btnRemove.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnRemove, SVGIcons.CROSS, 0.7f, "black");
		btnRemove.setDisable(attribute.type.get().fixed);
		headerBox.getChildren().add(btnRemove);
		btnRemove.setOnAction(event -> {
			onRemoveAttribute();
		});


		// choice attribute-type
		choiceType = new ComboBox<>();
		choiceType.setButtonCell(ComboboxUtils.createListCellAttributeType());
		choiceType.setCellFactory(param -> ComboboxUtils.createListCellAttributeType());
		if (attribute.type.getValue().fixed) {
			choiceType.getItems().add(attribute.type.getValue());
		} else {
			choiceType.getItems().addAll(TaskAttribute.Type.getFreeTypes());
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


		// attribute name
		labelName = new EditableLabel(attribute.name.getValue());
		labelName.setMinSize(32, 32);
		labelName.setPrefSize(10000, 32);
		labelName.setMaxSize(10000, 32);
		labelName.setDisable(attribute.type.get().fixed);
		headerBox.getChildren().add(labelName);
		labelName.addListener((observable, oldValue, newValue) -> {
			onRename(oldValue, newValue);
		});


		// expand-button
		btnExpand = new Button();
		btnExpand.setMinSize(32, 32);
		btnExpand.setPrefSize(32, 32);
		btnExpand.setMaxSize(32, 32);
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_DOWN, 0.75f, "black");
		headerBox.getChildren().add(btnExpand);
		btnExpand.setOnAction(event -> {
			if (isExpanded) {
				onCollapse();
			} else {
				onExpand();
			}
		});


		// content pane
		contentPane = new AnchorPane();
		contentPane.setPadding(new Insets(0, 10, 0, 10));
		contentPane.setId("pane_content");
		contentPane.setVisible(isExpanded);
		contentPane.setMinSize(0, 0);
		contentPane.setPrefSize(10000, 10000);
		contentPane.setMaxSize(10000, 10000);
		AnchorUtils.setAnchors(contentPane, 34, 0, 0, 0);
		this.getChildren().add(contentPane);

		// attribute content
		setAttributeContent();
	}




	private void setAttributeContent() {
		switch (attribute.type.get()) {
			case ID: {
				content = new UnchangeableContentNode(attribute);
				break;
			}
			case DESCRIPTION: {
				content = new UnchangeableContentNode(attribute);
				break;
			}
			case FLAG: {
				content = new FlagContentNode(attribute);
				break;
			}
			case BOOLEAN: {
				content = new BooleanContentNode(attribute);
				break;
			}
			case NUMBER: {
				content = new NumberContentNode(attribute);
				break;
			}
			case TEXT: {
				content = new TextContentNode(attribute);
				break;
			}
			case CHOICE: {
				content = new ChoiceContentNode(attribute);
				break;
			}
			case DATE: {
				content = new DateContentNode(attribute);
				break;
			}
			case DEPENDENCY: {
				content = new UnchangeableContentNode(attribute);
				break;
			}
			default: {
				content = new UnchangeableContentNode(attribute);
				break;
			}
		}

		AnchorUtils.setAnchors(content, 0, 0, 0, 0);
		contentPane.getChildren().setAll(content);
		onResize();

		content.changedProperty.addListener((observable, oldValue, newValue) -> {
			labelUnsafed.setVisible(newValue);
		});

	}




	protected void onRemoveAttribute() {
		Logic.get().deleteTaskAttribute(getAttribute());
	}




	protected void onTypeSelected(TaskAttribute.Type oldValue, TaskAttribute.Type newValue) {
		if (oldValue != newValue) {
			Logic.get().setTaskAttributeType(getAttribute(), newValue);
			setAttributeContent();
		}
	}




	protected void onRename(String oldValue, String newValue) {
		if (!oldValue.equals(newValue)) {
			Logic.get().renameTaskAttribute(getAttribute(), newValue);
		}
	}




	protected void onExpand() {
		isExpanded = true;
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_UP, 0.75f, "black");
		onResize();
	}




	protected void onCollapse() {
		isExpanded = false;
		ButtonUtils.makeIconButton(btnExpand, SVGIcons.ARROW_DOWN, 0.75f, "black");
		onResize();
	}




	protected void onResize() {

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




	public TaskAttribute getAttribute() {
		return attribute;
	}


}
