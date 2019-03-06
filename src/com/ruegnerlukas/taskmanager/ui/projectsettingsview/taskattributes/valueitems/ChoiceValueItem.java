package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class ChoiceValueItem extends AttributeValueItem {


	private String value;
	private String[] values;

	private ChoiceBox<String> choiceBox;




	public ChoiceValueItem(String name, String selectedValue, String... values) {
		super();
		this.value = selectedValue;
		this.values = values;

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

		choiceBox = new ChoiceBox<>();
		choiceBox.getItems().addAll(values);
		choiceBox.getSelectionModel().select(selectedValue);
		choiceBox.setMinSize(50, 32);
		choiceBox.setPrefSize(200, 32);
		choiceBox.setMaxSize(200, 32);
		boxValue.getChildren().add(choiceBox);

		choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
			choiceBox.getSelectionModel().select(value);
			setChanged(true);
		}
	}




	public void setChoices(String... values) {
		choiceBox.getItems().setAll(values);
		choiceBox.getSelectionModel().select(value);
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
