package com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.contentnodes;

import com.ruegnerlukas.taskmanager.data.attributes.DateAttributeAccess;
import com.ruegnerlukas.taskmanager.data.attributes.TaskAttribute;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.AttributeContentNode;
import com.ruegnerlukas.taskmanager.ui.viewprojectsettings.attributes.ContentNodeUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DateContentNode extends AttributeContentNode {


	private CheckBox cbUseDefault;
	private DatePicker pickerDefaultValue;
	private Button btnDiscard;
	private Button btnSave;

	private Map<String, Object> values = new HashMap<>();




	public DateContentNode(TaskAttribute attribute) {
		super(attribute);

		// set value
		values.put(DateAttributeAccess.DATE_USE_DEFAULT, DateAttributeAccess.getUseDefault(attribute));
		values.put(DateAttributeAccess.DATE_DEFAULT_VALUE, DateAttributeAccess.getDefaultValue(attribute));


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

		pickerDefaultValue = new DatePicker();
		pickerDefaultValue.setValue(getLocalDefaultValue());
		pickerDefaultValue.setMinSize(60, 32);
		pickerDefaultValue.setPrefSize(150, 32);
		pickerDefaultValue.setMaxSize(150, 32);
		boxAlignDefault.getChildren().add(pickerDefaultValue);
		pickerDefaultValue.setDisable(!getLocalUseDefault());
		pickerDefaultValue.setOnAction(event-> {
			onDefaultValue(pickerDefaultValue.getValue());
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
		values.put(DateAttributeAccess.DATE_USE_DEFAULT, useDefault);
		pickerDefaultValue.setDisable(!getLocalUseDefault());
		checkChanges();
	}




	private void onDefaultValue(LocalDate defaultValue) {
		values.put(DateAttributeAccess.DATE_DEFAULT_VALUE, defaultValue);
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
		return (boolean) values.get(DateAttributeAccess.DATE_USE_DEFAULT);
	}




	private LocalDate getLocalDefaultValue() {
		return (LocalDate) values.get(DateAttributeAccess.DATE_DEFAULT_VALUE);
	}




	@Override
	public double getContentHeight() {
		return 136;
	}

}
