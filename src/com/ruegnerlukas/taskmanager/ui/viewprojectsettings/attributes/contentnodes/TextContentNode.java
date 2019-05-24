package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.projectdata.TaskAttribute;
import com.ruegnerlukas.taskmanager.data.projectdata.attributevalues.*;
import com.ruegnerlukas.taskmanager.data.projectdata.taskvalues.TextValue;
import com.ruegnerlukas.taskmanager.logic.attributes.AttributeLogic;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.ComboboxUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.SpinnerUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.MultiTextField;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class TextContentNode extends AttributeContentNode {


	private Spinner<Integer> spinnerCharLimit;
	private CheckBox cbMultiline;
	private CheckBox cbUseDefault;
	private MultiTextField fieldDefaultValue;
	private ComboBox<Boolean> choiceShowOnCard;
	private Button btnDiscard;
	private Button btnSave;

	private Map<AttributeValueType, AttributeValue<?>> values = new HashMap<>();




	public TextContentNode(TaskAttribute attribute) {
		super(attribute);

		// set value
		values.put(AttributeValueType.TEXT_CHAR_LIMIT, new TextCharLimitValue(AttributeLogic.TEXT_LOGIC.getCharLimit(attribute)));
		values.put(AttributeValueType.TEXT_MULTILINE, new TextMultilineValue(AttributeLogic.TEXT_LOGIC.getMultiline(attribute)));
		values.put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(AttributeLogic.TEXT_LOGIC.getUseDefault(attribute)));
		values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(AttributeLogic.TEXT_LOGIC.getDefaultValue(attribute)));

		// root box
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setSpacing(5);
		vbox.setMinSize(0, 0);
		AnchorUtils.setAnchors(vbox, 0, 0, 0, 0);
		this.getChildren().add(vbox);

		buildCharLimit(vbox);
		buildMultiline(vbox);
		buildUseDefault(vbox);
		buildCardDisplayType(vbox);
		buildDefaultValue(vbox);
		buildButtons(vbox);

		checkChanges();
	}




	private void buildCharLimit(VBox root) {

		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Character Limit:");

		spinnerCharLimit = new Spinner<>();
		spinnerCharLimit.setEditable(true);
		SpinnerUtils.initSpinner(
				spinnerCharLimit, getLocalCharLimit(), 1, 999, 1, 0, false, null);

		spinnerCharLimit.setMinSize(60, 32);
		spinnerCharLimit.setPrefSize(150, 32);
		spinnerCharLimit.setMaxSize(150, 32);
		boxAlign.getChildren().add(spinnerCharLimit);

		spinnerCharLimit.valueProperty().addListener((observable, oldValue, newValue) -> {
			onCharLimit(newValue);
		});

	}




	private void buildMultiline(VBox root) {

		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Multiline:");

		cbMultiline = new CheckBox();
		cbMultiline.setText("");
		cbMultiline.setMinSize(60, 32);
		cbMultiline.setPrefSize(150, 32);
		cbMultiline.setMaxSize(150, 32);
		boxAlign.getChildren().add(cbMultiline);

		cbMultiline.setOnAction(event -> {
			onMultiline(cbMultiline.isSelected());
		});

	}




	private void buildUseDefault(VBox root) {
		cbUseDefault = ContentNodeUtils.buildEntryUseDefault(root, getLocalUseDefault());
		cbUseDefault.setOnAction(event -> onUseDefault(cbUseDefault.isSelected()));
	}




	private void buildDefaultValue(VBox root) {

		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Default Value:");

		fieldDefaultValue = new MultiTextField();
		fieldDefaultValue.setMultiline(getLocalMultiline());
		fieldDefaultValue.setDisable(!getLocalUseDefault());
		boxAlign.getChildren().add(fieldDefaultValue);
		setDefaultValueHeight();


		fieldDefaultValue.textProperty().addListener(((observable, oldValue, newValue) -> {
			onDefaultValue(newValue);
		}));

	}




	private void setDefaultValueHeight() {
		final double height = getLocalMultiline() ? MultiTextField.calculateOptimalHeight(5) : 32;
		fieldDefaultValue.setMinSize(60, height);
		fieldDefaultValue.setPrefSize(10000, height);
		fieldDefaultValue.setMaxSize(10000, height);
		((HBox) fieldDefaultValue.getParent()).setMinSize(0, height);
		((HBox) fieldDefaultValue.getParent()).setPrefSize(100000, height);
		((HBox) fieldDefaultValue.getParent()).setMaxSize(100000, height);
		((HBox) fieldDefaultValue.getParent().getParent()).setMinSize(0, height);
		((HBox) fieldDefaultValue.getParent().getParent()).setPrefSize(100000, height);
		((HBox) fieldDefaultValue.getParent().getParent()).setMaxSize(100000, height);
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




	private void onCharLimit(int newValue) {
		values.put(AttributeValueType.TEXT_CHAR_LIMIT, new TextCharLimitValue(newValue));
		if (fieldDefaultValue.getText().length() > getLocalCharLimit()) {
			fieldDefaultValue.setText(fieldDefaultValue.getText().substring(0, getLocalCharLimit()));
		}
		checkChanges();
	}




	private void onMultiline(boolean newValue) {
		values.put(AttributeValueType.TEXT_MULTILINE, new TextMultilineValue(newValue));
		setDefaultValueHeight();
		fieldDefaultValue.setMultiline(getLocalMultiline());
		checkChanges();
	}




	private void onUseDefault(boolean newValue) {
		values.put(AttributeValueType.USE_DEFAULT, new UseDefaultValue(newValue));
		fieldDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private void onDefaultValue(String newValue) {
		String text = getLocalMultiline() ? newValue : newValue.replaceAll(System.lineSeparator(), "");
		text = text.substring(0, Math.min(text.length(), getLocalCharLimit()));
		values.put(AttributeValueType.DEFAULT_VALUE, new DefaultValue(new TextValue(text)));
		fieldDefaultValue.setTextSilent(text);
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

		SpinnerUtils.initSpinner(
				spinnerCharLimit, getLocalCharLimit(), 1, 999, 1, 0, false, null);

		cbMultiline.setSelected(getLocalMultiline());

		cbUseDefault.setSelected(getLocalUseDefault());

		setDefaultValueHeight();
		fieldDefaultValue.setMultiline(getLocalMultiline());
		fieldDefaultValue.setText(getLocalDefaultValue());
		fieldDefaultValue.setDisable(!getLocalUseDefault());

		checkChanges();
	}




	private int getLocalCharLimit() {
		TextCharLimitValue value = (TextCharLimitValue)values.get(AttributeValueType.TEXT_CHAR_LIMIT);
		if(value != null) {
			return value.getValue();
		} else {
			return 128;
		}
	}




	private boolean getLocalMultiline() {
		TextMultilineValue value = (TextMultilineValue)values.get(AttributeValueType.TEXT_MULTILINE);
		if(value != null) {
			return value.getValue();
		} else {
			return false;
		}
	}




	private boolean getLocalUseDefault() {
		UseDefaultValue value = (UseDefaultValue)values.get(AttributeValueType.USE_DEFAULT);
		if(value != null) {
			return value.getValue();
		} else {
			return false;
		}
	}




	private String getLocalDefaultValue() {
		DefaultValue value = (DefaultValue)values.get(AttributeValueType.DEFAULT_VALUE);
		if(value != null) {
			return ((TextValue)value.getValue()).getValue();
		} else {
			return "";
		}
	}




	@Override
	public double getContentHeight() {
		return 345;
	}

}
