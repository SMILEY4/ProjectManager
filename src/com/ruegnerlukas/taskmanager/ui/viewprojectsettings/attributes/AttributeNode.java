package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes;

import com.ruegnerlukas.taskmanager.data.projectdata.AttributeType;
import com.ruegnerlukas.taskmanager.data.Data;
import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.logic.ProjectLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes.UnchangeableContentNode;
import com.ruegnerlukas.taskmanager.utils.SVGIcons;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ButtonUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.EditableLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings ("FieldCanBeLocal") // TODO
public class AttributeNode extends AnchorPane {


	private static final double HEADER_HEIGHT = 35;

	private AnchorPane headerPane;
	private HBox headerBox;
	private Button btnRemove;
	private ComboBox<AttributeType> choiceType;
	private EditableLabel labelName;
	private Button btnExpand;
	private Label labelUnsafed;
	private boolean isExpanded = false;

	private AnchorPane contentPane;

	private final TaskAttribute attribute;
	private AttributeContentNode content;




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

		if (attribute.type.get().fixed && AttributeContentNode.CONTENT_NODES.get(attribute.type.get()) == UnchangeableContentNode.class) {
			btnExpand.setDisable(true);
			btnExpand.setVisible(false);
		}


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

		Class<? extends AttributeContentNode> nodeClass = AttributeContentNode.CONTENT_NODES.get(attribute.type.get());
		if (nodeClass == null) {
			content = new UnchangeableContentNode(attribute);
		} else {
			try {
				content = nodeClass.getDeclaredConstructor(TaskAttribute.class).newInstance(attribute);
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
				content = new UnchangeableContentNode(attribute);
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
		ProjectLogic.removeAttributeFromProject(Data.projectProperty.get(), getAttribute());
	}




	protected void onTypeSelected(AttributeType oldValue, AttributeType newValue) {
		if (oldValue != newValue) {
			AttributeLogic.setTaskAttributeType(getAttribute(), newValue);
			setAttributeContent();
		}
	}




	protected void onRename(String oldValue, String newValue) {
		if (!oldValue.equals(newValue)) {
			AttributeLogic.renameTaskAttribute(Data.projectProperty.get(), getAttribute(), newValue);
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




	public void dispose() {
		if (content != null) {
			content.dispose();
		}
	}

}
