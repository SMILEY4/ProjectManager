package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.attributes.ChoiceAttributeAccess;
import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class ChoiceContentNode extends AttributeContentNode {


	private TextField fieldValues;
	private CheckBox cbUseDefault;
	private ComboBox<String> choiceDefaultValue;
	private Button btnDiscard;
	private Button btnSave;

	private Map<String, Object> values = new HashMap<>();




	public ChoiceContentNode(TaskAttribute attribute) {
		super(attribute);

		// set value
		values.put(ChoiceAttributeAccess.CHOICE_VALUE_LIST, ChoiceAttributeAccess.getValueList(attribute));
		values.put(ChoiceAttributeAccess.CHOICE_USE_DEFAULT, ChoiceAttributeAccess.getUseDefault(attribute));
		values.put(ChoiceAttributeAccess.CHOICE_DEFAULT_VALUE, ChoiceAttributeAccess.getDefaultValue(attribute));


		// root box
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setSpacing(5);
		vbox.setMinSize(0, 0);
		AnchorUtils.setAnchors(vbox, 0, 0, 0, 0);
		this.getChildren().add(vbox);

		buildValuesListField(vbox);
		buildUseDefault(vbox);
		buildDefaultValue(vbox);
		buildButtons(vbox);

		checkChanges();
	}




	private void buildValuesListField(VBox root) {
		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Values (CSV):");

		fieldValues = new TextField();
		fieldValues.setPromptText("Comma (,) Separated Values");
		fieldValues.setMinSize(60, 32);
		fieldValues.setPrefSize(10000, 32);
		fieldValues.setMaxSize(10000, 32);
		boxAlign.getChildren().add(fieldValues);

		fieldValues.textProperty().addListener(((observable, oldValue, newValue) -> {
			onValueList(newValue);
		}));

	}




	private void buildUseDefault(VBox root) {
		cbUseDefault = ContentNodeUtils.buildEntryUseDefault(root, getLocalUseDefault());
		cbUseDefault.setOnAction(event -> onUseDefault(cbUseDefault.isSelected()));
	}




	private void buildDefaultValue(VBox root) {

		// default value
		HBox boxAlignDefault = ContentNodeUtils.buildEntryWithAlignment(root, "Default Value:");

		choiceDefaultValue = new ComboBox<>();
		choiceDefaultValue.getItems().addAll(getLocalValueList());
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




	private void onValueList(String strValues) {
		String[] values = strValues.split(",");
		for (int i = 0; i < values.length; i++) {
			values[i] = values[i].trim();
		}
		onValueList(values);
	}




	private void onValueList(String... valueList) {
		values.put(ChoiceAttributeAccess.CHOICE_VALUE_LIST, valueList);

		choiceDefaultValue.getItems().clear();
		choiceDefaultValue.getItems().addAll(valueList);

		if (valueList.length == 0) {
			onDefaultValue(null);
		} else {
			String defaultValue = getLocalDefaultValue();
			boolean contains = false;
			for (String str : valueList) {
				if (str.equals(defaultValue)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				choiceDefaultValue.getSelectionModel().select(valueList[0]);
			}
		}

		checkChanges();
	}




	private void onUseDefault(boolean useDefault) {
		values.put(ChoiceAttributeAccess.CHOICE_USE_DEFAULT, useDefault);
		choiceDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private void onDefaultValue(String defaultValue) {
		values.put(ChoiceAttributeAccess.CHOICE_DEFAULT_VALUE, defaultValue);
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

		String[] valueList = getLocalValueList();
		String defaultValue = getLocalDefaultValue();

		fieldValues.setText(String.join(", ", valueList));

		choiceDefaultValue.getItems().setAll(valueList);
		choiceDefaultValue.getSelectionModel().select(defaultValue);
		choiceDefaultValue.setDisable(!getLocalUseDefault());

		cbUseDefault.setSelected(getLocalUseDefault());

		checkChanges();
	}




	private String[] getLocalValueList() {
		return (String[]) values.get(ChoiceAttributeAccess.CHOICE_VALUE_LIST);
	}




	private boolean getLocalUseDefault() {
		return (boolean) values.get(ChoiceAttributeAccess.CHOICE_USE_DEFAULT);
	}




	private String getLocalDefaultValue() {
		return (String) values.get(ChoiceAttributeAccess.CHOICE_DEFAULT_VALUE);
	}




	@Override
	public double getContentHeight() {
		return 175;
	}

}
