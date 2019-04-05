package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.data.attributes.NumberAttributeAccess;
import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.SpinnerUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class NumberContentNode extends AttributeContentNode {


	private Spinner<Integer> spinnerDecPlaces;
	private Spinner<Double> spinnerMinValue;
	private Spinner<Double> spinnerMaxValue;
	private CheckBox cbUseDefault;
	private Spinner<Double> spinnerDefaultValue;
	private Button btnDiscard;
	private Button btnSave;

	private Map<String, Object> values = new HashMap<>();




	public NumberContentNode(TaskAttribute attribute) {
		super(attribute);

		// set value
		values.put(NumberAttributeAccess.NUMBER_DEC_PLACES, NumberAttributeAccess.getDecPlaces(attribute));
		values.put(NumberAttributeAccess.NUMBER_MIN_VALUE, NumberAttributeAccess.getMinValue(attribute));
		values.put(NumberAttributeAccess.NUMBER_MAX_VALUE, NumberAttributeAccess.getMaxValue(attribute));
		values.put(NumberAttributeAccess.NUMBER_USE_DEFAULT, NumberAttributeAccess.getUseDefault(attribute));
		values.put(NumberAttributeAccess.NUMBER_DEFAULT_VALUE, NumberAttributeAccess.getDefaultValue(attribute));


		// root box
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setSpacing(5);
		vbox.setMinSize(0, 0);
		AnchorUtils.setAnchors(vbox, 0, 0, 0, 0);
		this.getChildren().add(vbox);

		buildDecPlaces(vbox);
		buildMinValue(vbox);
		buildMaxValue(vbox);
		buildUseDefault(vbox);
		buildDefaultValue(vbox);
		buildButtons(vbox);

		checkChanges();
	}




	private void buildDecPlaces(VBox root) {

		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Decimal Places:");

		spinnerDecPlaces = new Spinner<>();
		spinnerDecPlaces.setEditable(true);
		SpinnerUtils.initSpinner(
				spinnerDecPlaces, getLocalDecPlaces(), 0, 9, 1, 0, false, null);

		spinnerDecPlaces.setMinSize(60, 32);
		spinnerDecPlaces.setPrefSize(150, 32);
		spinnerDecPlaces.setMaxSize(150, 32);
		boxAlign.getChildren().add(spinnerDecPlaces);

		spinnerDecPlaces.valueProperty().addListener((observable, oldValue, newValue) -> {
			onDecPlaces(newValue);
		});

	}




	private void buildMinValue(VBox root) {

		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Min Value:");

		spinnerMinValue = new Spinner<>();
		spinnerMinValue.setEditable(true);
		SpinnerUtils.initSpinner(
				spinnerMinValue,
				MathUtils.setDecPlaces(getLocalMinValue().doubleValue(), getLocalDecPlaces()),
				Integer.MIN_VALUE,
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);


		spinnerMinValue.setMinSize(60, 32);
		spinnerMinValue.setPrefSize(150, 32);
		spinnerMinValue.setMaxSize(150, 32);
		boxAlign.getChildren().add(spinnerMinValue);

		spinnerMinValue.valueProperty().addListener((observable, oldValue, newValue) -> {
			onMinValue(newValue);
		});

	}




	private void buildMaxValue(VBox root) {

		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Max Value:");

		spinnerMaxValue = new Spinner<>();
		spinnerMaxValue.setEditable(true);
		SpinnerUtils.initSpinner(
				spinnerMaxValue,
				MathUtils.setDecPlaces(getLocalMaxValue().doubleValue(), getLocalDecPlaces()),
				getLocalMinValue().doubleValue(),
				Integer.MAX_VALUE,
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);


		spinnerMaxValue.setMinSize(60, 32);
		spinnerMaxValue.setPrefSize(150, 32);
		spinnerMaxValue.setMaxSize(150, 32);
		boxAlign.getChildren().add(spinnerMaxValue);

		spinnerMaxValue.valueProperty().addListener((observable, oldValue, newValue) -> {
			onMaxValue(newValue);
		});

	}




	private void buildUseDefault(VBox root) {
		cbUseDefault = ContentNodeUtils.buildEntryUseDefault(root, getLocalUseDefault());
		cbUseDefault.setOnAction(event -> onUseDefault(cbUseDefault.isSelected()));
	}




	private void buildDefaultValue(VBox root) {

		HBox boxAlign = ContentNodeUtils.buildEntryWithAlignment(root, "Default Value:");

		spinnerDefaultValue = new Spinner<>();
		spinnerDefaultValue.setEditable(true);
		SpinnerUtils.initSpinner(
				spinnerDefaultValue,
				MathUtils.setDecPlaces(getLocalDefaultValue().doubleValue(), getLocalDecPlaces()),
				getLocalMinValue().doubleValue(),
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		spinnerDefaultValue.setMinSize(60, 32);
		spinnerDefaultValue.setPrefSize(150, 32);
		spinnerDefaultValue.setMaxSize(150, 32);
		boxAlign.getChildren().add(spinnerDefaultValue);

		spinnerDefaultValue.setDisable(!getLocalUseDefault());

		spinnerDefaultValue.valueProperty().addListener((observable, oldValue, newValue) -> {
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




	private void onMinValue(double newValue) {
		values.put(NumberAttributeAccess.NUMBER_MIN_VALUE, newValue);

		SpinnerUtils.initSpinner(
				spinnerMaxValue,
				getLocalMaxValue().doubleValue(),
				getLocalMinValue().doubleValue(),
				Integer.MAX_VALUE,
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		SpinnerUtils.initSpinner(
				spinnerDefaultValue,
				getLocalDefaultValue().doubleValue(),
				getLocalMinValue().doubleValue(),
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		checkChanges();
	}




	private void onMaxValue(double newValue) {
		values.put(NumberAttributeAccess.NUMBER_MAX_VALUE, newValue);

		SpinnerUtils.initSpinner(
				spinnerMinValue,
				getLocalMinValue().doubleValue(),
				Integer.MIN_VALUE,
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		SpinnerUtils.initSpinner(
				spinnerDefaultValue,
				getLocalDefaultValue().doubleValue(),
				getLocalMinValue().doubleValue(),
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		checkChanges();
	}




	private void onDecPlaces(int newValue) {

		values.put(NumberAttributeAccess.NUMBER_DEC_PLACES, newValue);

		SpinnerUtils.initSpinner(
				spinnerMinValue,
				MathUtils.setDecPlaces(getLocalMinValue().doubleValue(), getLocalDecPlaces()),
				Integer.MIN_VALUE,
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		SpinnerUtils.initSpinner(
				spinnerMaxValue,
				MathUtils.setDecPlaces(getLocalMaxValue().doubleValue(), getLocalDecPlaces()),
				getLocalMinValue().doubleValue(),
				Integer.MAX_VALUE,
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		SpinnerUtils.initSpinner(
				spinnerDefaultValue,
				MathUtils.setDecPlaces(getLocalDefaultValue().doubleValue(), getLocalDecPlaces()),
				getLocalMinValue().doubleValue(),
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		checkChanges();
	}




	private void onUseDefault(boolean newValue) {
		values.put(NumberAttributeAccess.NUMBER_USE_DEFAULT, newValue);
		spinnerDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private void onDefaultValue(Number newValue) {
		values.put(NumberAttributeAccess.NUMBER_DEFAULT_VALUE, newValue);
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
				spinnerDecPlaces, getLocalDecPlaces(), 0, 9, 1, 0, false, null);

		SpinnerUtils.initSpinner(
				spinnerMinValue,
				getLocalMinValue().doubleValue(),
				Integer.MIN_VALUE,
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		SpinnerUtils.initSpinner(
				spinnerMaxValue,
				getLocalMaxValue().doubleValue(),
				getLocalMinValue().doubleValue(),
				Integer.MAX_VALUE,
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);

		cbUseDefault.setSelected(getLocalUseDefault());

		SpinnerUtils.initSpinner(
				spinnerDefaultValue,
				getLocalDefaultValue().doubleValue(),
				getLocalMinValue().doubleValue(),
				getLocalMaxValue().doubleValue(),
				1.0 / Math.pow(10, getLocalDecPlaces()),
				getLocalDecPlaces(),
				true, null);
		spinnerDefaultValue.setDisable(!getLocalUseDefault());

		checkChanges();
	}




	private int getLocalDecPlaces() {
		return (int) values.get(NumberAttributeAccess.NUMBER_DEC_PLACES);
	}




	private Number getLocalMinValue() {
		return (Number) values.get(NumberAttributeAccess.NUMBER_MIN_VALUE);
	}




	private Number getLocalMaxValue() {
		return (Number) values.get(NumberAttributeAccess.NUMBER_MAX_VALUE);
	}




	private boolean getLocalUseDefault() {
		return (boolean) values.get(NumberAttributeAccess.NUMBER_USE_DEFAULT);
	}




	private Number getLocalDefaultValue() {
		return (Number) values.get(NumberAttributeAccess.NUMBER_DEFAULT_VALUE);
	}




	@Override
	public double getContentHeight() {
		return 247;
	}

}
