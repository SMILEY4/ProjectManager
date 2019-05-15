package com.ruegnerlukas.taskmanager_old.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.AttributeValueItem;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class ChoiceValueItem extends AttributeValueItem {


	private String value;
	private String[] values;

	private ComboBox<String> choices;




	public ChoiceValueItem(String name, String selectedValue, String... values) {
		super();
		this.value = selectedValue;
		this.values = values;
		this.setId("item_choice");

		HBox box = new HBox();
		box.setSpacing(20);
		box.setMinSize(0, 32);
		box.setPrefSize(10000, 32);
		box.setMaxSize(10000, 32);
		AnchorUtils.setAnchors(box, 0, 0, 0, 0);
		this.getChildren().add(box);

		VBox boxLabel = new VBox();
		boxLabel.setAlignment(Pos.CENTER_RIGHT);
		boxLabel.setMinSize(0, 32);
		boxLabel.setPrefSize(10000, 32);
		boxLabel.setMaxSize(10000, 32);
		box.getChildren().add(boxLabel);

		Label label = new Label(name);
		boxLabel.getChildren().add(label);

		VBox boxValue = new VBox();
		boxValue.setAlignment(Pos.CENTER_LEFT);
		boxValue.setMinSize(0, 32);
		boxValue.setPrefSize(10000, 32);
		boxValue.setMaxSize(10000, 32);
		box.getChildren().add(boxValue);

		choices = new ComboBox<>();
		choices.getItems().addAll(values);
		choices.getSelectionModel().select(selectedValue);
		choices.setMinSize(50, 32);
		choices.setPrefSize(200, 32);
		choices.setMaxSize(200, 32);
		boxValue.getChildren().add(choices);

		choices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			setValue(newValue);
		});

		this.setPrefSize(10000, 32);
	}




	private boolean containsValue(String value) {
		for (String v : values) {
			if (v.equals(value)) {
				return true;
			}
		}
		return false;
	}




	public void setValue(String value) {
		if (!this.value.equals(value) && containsValue(value)) {
			this.value = value;
			choices.getSelectionModel().select(value);
			setChanged(true);
		}
	}




	public void setChoices(String... values) {
		choices.getItems().setAll(values);
		choices.getSelectionModel().select(value);
		this.values = values;
		setChanged(true);
	}




	public String getValue() {
		return value;
	}




	@Override
	public double getItemHeight() {
		return 32;
	}

}
