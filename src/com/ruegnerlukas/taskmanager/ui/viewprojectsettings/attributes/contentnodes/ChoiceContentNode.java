package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.ChoiceValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.logic.attributes.ChoiceAttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.TagBar;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class ChoiceContentNode extends AttributeContentNode {


	private TagBar fieldValues;
	private CheckBox cbUseDefault;
	private ComboBox<String> choiceDefaultValue;
	private Button btnDiscard;
	private Button btnSave;

	private Map<String, Object> values = new HashMap<>();




	public ChoiceContentNode(TaskAttribute attribute) {
		super(attribute);

		// set value
		values.put(ChoiceAttributeLogic.CHOICE_VALUE_LIST, ChoiceAttributeLogic.getValueList(attribute));
		values.put(AttributeLogic.ATTRIB_USE_DEFAULT, ChoiceAttributeLogic.getUseDefault(attribute));
		values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, ChoiceAttributeLogic.getDefaultValue(attribute));


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
		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Values:");

		fieldValues = new TagBar();
		fieldValues.setMinSize(60, 32);
		fieldValues.setPrefSize(10000, 32);
		fieldValues.setMaxSize(10000, 32);
		fieldValues.allowDuplicates.set(false);
		boxAlign.getChildren().add(fieldValues);

		fieldValues.setOnTagAdded(((observable, oldValue, newValue) -> onValueList(fieldValues.getTagArray())));
		fieldValues.setOnTagRemoved(((observable, oldValue, newValue) -> onValueList(fieldValues.getTagArray())));
		fieldValues.setOnTagChanged(((observable, oldValue, newValue) -> onValueList(fieldValues.getTagArray())));

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




	private void onValueList(String... valueList) {
		values.put(ChoiceAttributeLogic.CHOICE_VALUE_LIST, valueList);

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
		values.put(AttributeLogic.ATTRIB_USE_DEFAULT, useDefault);
		choiceDefaultValue.setDisable(!getLocalUseDefault());
		fieldValues.removeCssStyleClass(null, "tag-default");
		if(getLocalUseDefault()) {
			fieldValues.addCssStyleClass(getLocalDefaultValue(), "tag-default");
		}
		checkChanges();
	}




	private void onDefaultValue(String defaultValue) {
		if(defaultValue == null) {
			return;
		}
		values.put(AttributeLogic.ATTRIB_DEFAULT_VALUE, new ChoiceValue(defaultValue));
		fieldValues.removeCssStyleClass(null, "tag-default");
		if(getLocalUseDefault()) {
			fieldValues.addCssStyleClass(getLocalDefaultValue(), "tag-default");
		}
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

		fieldValues.removeAll();
		fieldValues.addTags(valueList);

		choiceDefaultValue.getItems().setAll(valueList);
		choiceDefaultValue.getSelectionModel().select(defaultValue);
		choiceDefaultValue.setDisable(!getLocalUseDefault());

		cbUseDefault.setSelected(getLocalUseDefault());

		fieldValues.removeCssStyleClass(null, "tag-default");
		if(getLocalUseDefault()) {
			fieldValues.addCssStyleClass(defaultValue, "tag-default");
		}

		checkChanges();
	}




	private String[] getLocalValueList() {
		return (String[]) values.get(ChoiceAttributeLogic.CHOICE_VALUE_LIST);
	}




	private boolean getLocalUseDefault() {
		return (boolean) values.get(AttributeLogic.ATTRIB_USE_DEFAULT);
	}




	private String getLocalDefaultValue() {
		return ((ChoiceValue) values.get(AttributeLogic.ATTRIB_DEFAULT_VALUE)).getValue();
	}




	@Override
	public double getContentHeight() {
		return 175;
	}

}
