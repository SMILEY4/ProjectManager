package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class MLTextValueItem extends AttributeValueItem {


	private String value;
	private int nLines;
	private TextArea textArea;




	public MLTextValueItem(String name, String value, String prompt, int nLines) {
		super();
		this.value = value;
		this.nLines = nLines;
		this.setId("item_mltext");

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
		boxValue.setAlignment(Pos.TOP_LEFT);
		boxValue.setMinSize(0, 32);
		boxValue.setPrefSize(10000, 32);
		boxValue.setMaxSize(10000, 32);
		box.getChildren().add(boxValue);

		textArea = new TextArea();
		textArea.setText(value);
		textArea.setPromptText(prompt);
		textArea.setMinSize(50, calcAreaHeight(nLines));
		textArea.setPrefSize(200, calcAreaHeight(nLines));
		textArea.setMaxSize(10000, calcAreaHeight(nLines));
		boxValue.getChildren().add(textArea);

//		textField.setOnAction(event -> {
//			setValue(textField.getText());
//		});
//		textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
//			setValue(textField.getText());
//		});

		textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			setValue(newValue);
		});


		this.setPrefSize(10000, 32);
	}




	public void setValue(String value) {
		if (!this.value.equals(value)) {
			this.value = value;
			textArea.setText(value);
			setChanged(true);
		}
	}




	public void setNumLines(int nLines) {
		this.nLines = nLines;
		textArea.setMinSize(50, calcAreaHeight(nLines));
		textArea.setPrefSize(200, calcAreaHeight(nLines));
		textArea.setMaxSize(10000, calcAreaHeight(nLines));
	}




	private double calcAreaHeight(int nLines) {
		return Math.max(33, 21.3 * nLines + 5);
	}




	public String getValue() {
		return value;
	}




	@Override
	public double getItemHeight() {
		return calcAreaHeight(nLines);
	}

}
