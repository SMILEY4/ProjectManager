package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.DateValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.DateAttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DateContentNode extends AttributeContentNode {


	private CheckBox cbUseDefault;
	private DatePicker pickerDefaultValue;
	private ComboBox<Boolean> choiceShowOnCard;
	private Button btnDiscard;
	private Button btnSave;

	private Map<String, Object> values = new HashMap<>();




	public DateContentNode(TaskAttribute attribute) {
		super(attribute);

		// set value
		values.put(TaskAttribute.ATTRIB_USE_DEFAULT, DateAttributeLogic.getUseDefault(attribute));
		values.put(TaskAttribute.ATTRIB_DEFAULT_VALUE, DateAttributeLogic.getDefaultValue(attribute));


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

		// default value
		HBox boxAlignDefault = ContentNodeUtils.buildEntryWithAlignment(root, "Default Value:");

		pickerDefaultValue = new DatePicker();
		pickerDefaultValue.setValue(getLocalDefaultValue());
		pickerDefaultValue.setMinSize(60, 32);
		pickerDefaultValue.setPrefSize(150, 32);
		pickerDefaultValue.setMaxSize(150, 32);
		boxAlignDefault.getChildren().add(pickerDefaultValue);
		pickerDefaultValue.setDisable(!getLocalUseDefault());
		pickerDefaultValue.setOnAction(event -> {
			onDefaultValue(pickerDefaultValue.getValue());
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
		values.put(TaskAttribute.ATTRIB_USE_DEFAULT, useDefault);
		pickerDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private void onDefaultValue(LocalDate defaultValue) {
		values.put(TaskAttribute.ATTRIB_DEFAULT_VALUE, new DateValue(defaultValue));
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
		pickerDefaultValue.setValue(getLocalDefaultValue());
		pickerDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private boolean getLocalUseDefault() {
		return (boolean) values.get(TaskAttribute.ATTRIB_USE_DEFAULT);
	}




	private LocalDate getLocalDefaultValue() {
		return ((DateValue) values.get(TaskAttribute.ATTRIB_DEFAULT_VALUE)).getValue();
	}




	@Override
	public double getContentHeight() {
		return 171;
	}

}
