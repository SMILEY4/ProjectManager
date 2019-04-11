package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.logic.attributes.BooleanAttributeLogic;
import com.ruegnerlukas.taskmanager.data.TaskAttribute;
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
	private Button btnDiscard;
	private Button btnSave;

	private Map<String, Object> values = new HashMap<>();




	public BooleanContentNode(TaskAttribute attribute) {
		super(attribute);

		// set value
		values.put(BooleanAttributeLogic.BOOLEAN_USE_DEFAULT, BooleanAttributeLogic.getUseDefault(attribute));
		values.put(BooleanAttributeLogic.BOOLEAN_DEFAULT_VALUE, BooleanAttributeLogic.getDefaultValue(attribute));


		// root box
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setSpacing(5);
		vbox.setMinSize(0, 0);
		AnchorUtils.setAnchors(vbox, 0, 0, 0, 0);
		this.getChildren().add(vbox);

		buildUseDefault(vbox);
		buildDefaultValue(vbox);
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

		choiceDefaultValue = new ComboBox<>();
		choiceDefaultValue.setButtonCell(ComboboxUtils.createListCellBoolean());
		choiceDefaultValue.setCellFactory(param -> ComboboxUtils.createListCellBoolean());
		choiceDefaultValue.getItems().addAll(true, false);
		choiceDefaultValue.getSelectionModel().select(getLocalDefaultValue());
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




	private void buildButtons(VBox root) {
		Button[] buttons = ContentNodeUtils.buildButtons(root);
		btnDiscard = buttons[0];
		btnDiscard.setOnAction(event -> onDiscard());
		btnSave = buttons[1];
		btnSave.setOnAction(event -> onSave());
	}




	private void onUseDefault(boolean useDefault) {
		values.put(BooleanAttributeLogic.BOOLEAN_USE_DEFAULT, useDefault);
		choiceDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private void onDefaultValue(boolean defaultValue) {
		values.put(BooleanAttributeLogic.BOOLEAN_DEFAULT_VALUE, defaultValue);
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
		return (boolean) values.get(BooleanAttributeLogic.BOOLEAN_USE_DEFAULT);
	}




	private boolean getLocalDefaultValue() {
		return (boolean) values.get(BooleanAttributeLogic.BOOLEAN_DEFAULT_VALUE);
	}




	@Override
	public double getContentHeight() {
		return 136;
	}

}
