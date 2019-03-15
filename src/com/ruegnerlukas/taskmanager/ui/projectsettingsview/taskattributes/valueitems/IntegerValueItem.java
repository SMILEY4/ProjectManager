package com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class IntegerValueItem extends AttributeValueItem {


	private int value;
	private int min;
	private int max;
	private int step;

	private Spinner<Integer> spinner;


	private String name;


	public IntegerValueItem(String name, int value, int min, int max, int step) {
		super();
		this.name = name;
		this.value = value;
		this.min = min;
		this.max = max;
		this.step = step;
		this.setId("item_integer");

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

		spinner = new Spinner<>();
		spinner.setMinSize(50, 32);
		spinner.setPrefSize(200, 32);
		spinner.setMaxSize(200, 32);
		spinner.setEditable(true);
		boxValue.getChildren().add(spinner);

		SpinnerUtils.initSpinner(spinner, value, min, max, step, 0, (ChangeListener<Integer>) (observable, oldValue, newValue) -> {
			setValue(newValue);
		});

		this.setPrefSize(10000, 32);

	}




	public void setValue(int value) {
		if (this.value != value) {
			this.value = value;
			spinner.getValueFactory().setValue(value);
			setChanged(true);
		}
	}




	public void setRange(int min, int max) {
		this.min = min;
		this.max = max;
		SpinnerUtils.initSpinner(spinner, value, min, max, step, 0, null);
	}




	public void setMin(int min) {
		this.min = min;
		if (value < min) {
			setValue(min);
		}
		SpinnerUtils.initSpinner(spinner, value, min, max, step, 0, null);
	}




	public void setMax(int max) {
		this.max = max;
		if (value > max) {
			setValue(max);
		}
		SpinnerUtils.initSpinner(spinner, value, min, max, step, 0, null);
	}




	public void setStep(int step) {
		this.step = step;
		SpinnerUtils.initSpinner(spinner, value, min, max, step, 0, null);
	}




	public int getValue() {
		return value;
	}




	@Override
	public double getItemHeight() {
		return 32;
	}

}
