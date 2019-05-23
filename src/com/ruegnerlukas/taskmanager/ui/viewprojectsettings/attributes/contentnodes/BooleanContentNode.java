package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.AttributeValueType;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.DefaultValue;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.UseDefaultValue;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.BoolValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.BooleanAttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class BooleanContentNode extends AttributeContentNode {


	private CheckBox cbUseDefault;
	private ComboBox<Boolean> choiceDefaultValue;
	private ComboBox<Boolean> choiceShowOnCard;
	private Button btnDiscard;
	private Button btnSave;

	private Map<AttributeValueType, AttributeValue<?>> values = new HashMap<>();




	public BooleanContentNode(TaskAttribute attribute) {
		super(attribute);

		// set value
		values.put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(BooleanAttributeLogic.getUseDefault(attribute)));
		values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(BooleanAttributeLogic.getDefaultValue(attribute)));


		// root box
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setSpacing(5);
		vbox.setMinSize(0, 0);
		AnchorUtils.setAnchors(vbox, 0, 0, 0, 0);
		this.getChildren().add(vbox);

		buildUseDefault(vbox);
		buildDefaultValue(vbox);
		buildCardDisplayType(vbox);
		buildButtons(vbox);

		checkChanges();
	}




	private void buildUseDefault(VBox root) {
		cbUseDefault = ContentNodeUtils.buildEntryUseDefault(root, getLocalUseDefault());
		cbUseDefault.setOnAction(event -> onUseDefault(cbUseDefault.isSelected()));
	}




	private void buildDefaultValue(VBox root) {

		HBox boxAlignDefault = ContentNodeUtils.buildEntryWithAlignment(root, "Default Value:");

		choiceDefaultValue = new ComboBox<>();
		choiceDefaultValue.setButtonCell(ComboboxUtils.createListCellBoolean());
		choiceDefaultValue.setCellFactory(param -> ComboboxUtils.createListCellBoolean());
		choiceDefaultValue.getItems().addAll(true, false);
		choiceDefaultValue.setMinSize(60, 32);
		choiceDefaultValue.setPrefSize(150, 32);
		choiceDefaultValue.setMaxSize(150, 32);
		boxAlignDefault.getChildren().add(choiceDefaultValue);
		choiceDefaultValue.setDisable(!getLocalUseDefault());
		choiceDefaultValue.getSelectionModel().select(getLocalDefaultValue());
		choiceDefaultValue.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			onDefaultValue(newValue);
		});
	}




	private void buildCardDisplayType(VBox root) {

		HBox boxAlignDefault = ContentNodeUtils.buildEntryWithAlignment(root, "Display on Task-Card:");

		choiceShowOnCard = new ComboBox<>();
		choiceShowOnCard.setButtonCell(ComboboxUtils.createListCellBoolean());
		choiceShowOnCard.setCellFactory(param -> ComboboxUtils.createListCellBoolean());
		choiceShowOnCard.getItems().addAll(true, false);
		choiceShowOnCard.setMinSize(60, 32);
		choiceShowOnCard.setPrefSize(150, 32);
		choiceShowOnCard.setMaxSize(150, 32);
		boxAlignDefault.getChildren().add(choiceShowOnCard);
		choiceShowOnCard.getSelectionModel().select(getShowOnCard());
		choiceShowOnCard.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			setShowOnCard(newValue);
		});
	}




	private boolean getShowOnCard() {
		return AttributeLogic.getShowOnTaskCard(attribute);
	}




	private void setShowOnCard(boolean show) {
		AttributeLogic.setShowOnTaskCard(attribute, show);
	}




	private void buildButtons(VBox root) {
		Button[] buttons = ContentNodeUtils.buildButtons(root);
		btnDiscard = buttons[0];
		btnDiscard.setOnAction(event -> onDiscard());
		btnSave = buttons[1];
		btnSave.setOnAction(event -> onSave());
	}




	private void onUseDefault(boolean useDefault) {
		values.put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(useDefault));
		choiceDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private void onDefaultValue(boolean defaultValue) {
		values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(new BoolValue(defaultValue)));
		checkChanges();
	}




	private void checkChanges() {
		boolean hasChanged = compareValues(values);
		changedProperty.set(!hasChanged);
		btnDiscard.setDisable(hasChanged);
		btnSave.setDisable(hasChanged);
	}




	private void onSave() {
		saveValues(values);
		checkChanges();
	}




	private void onDiscard() {
		discardValues(values);
		cbUseDefault.setSelected(getLocalUseDefault());
		choiceDefaultValue.getSelectionModel().select(getLocalDefaultValue());
		choiceDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private boolean getLocalUseDefault() {
		UseDefaultValue value = (UseDefaultValue) values.get(AttributeValueType.USE_DEFAULT);
		if(value != null) {
			return value.getValue();
		} else {
			return false;
		}
	}




	private boolean getLocalDefaultValue() {
		DefaultValue value = (DefaultValue) values.get(AttributeValueType.DEFAULT_VALUE);
		if(value != null) {
			return ((BoolValue)value.getValue()).getValue();
		} else {
			return false;
		}
	}




	@Override
	public double getContentHeight() {
		return 171;
	}

}
