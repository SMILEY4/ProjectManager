package com.ruegnerlukas.taskmanager_old.ui.projectsettingsview.taskattributes.valueitems;

import com.ruegnerlukas.simplemath.MathUtils;
import com.ruegnerlukas.taskmanager.ui.projectsettingsview.taskattributes.valueitems.AttributeValueItem;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.spinner.SpinnerUtils;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class FloatValueItem extends AttributeValueItem {


	private double value;
	private double min;
	private double max;
	private double step;
	private int decPlaces;

	private Spinner<Double> spinner;




	public FloatValueItem(String name, double value, double min, double max, double step, int decPlaces) {
		super();
		this.value = value;
		this.min = min;
		this.max = max;
		this.step = step;
		this.decPlaces = decPlaces;
		this.setId("item_float");

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

		SpinnerUtils.initSpinner(spinner, value, min, max, step, decPlaces, true, (ChangeListener<Double>) (observable, oldValue, newValue) -> {
			setValue(newValue);
		});

		this.setPrefSize(10000, 32);

	}




	public void setValue(double value) {
		if (this.value != value) {
			this.value = value;
			spinner.getValueFactory().setValue(value);
			setChanged(true);
		}
	}




	public void setRange(double min, double max) {
		this.min = min;
		this.max = max;
		this.value = MathUtils.clamp(value, min, max);
		SpinnerUtils.initSpinner(spinner, value, min, max, step, decPlaces, true, null);
	}




	public void setMin(double min) {
		this.min = min;
		if (value < min) {
			setValue(min);
		}
		SpinnerUtils.initSpinner(spinner, value, min, max, step, decPlaces, true, null);
	}




	public void setMax(double max) {
		this.max = max;
		if (value > max) {
			setValue(max);
		}
		SpinnerUtils.initSpinner(spinner, value, min, max, step, decPlaces, true, null);
	}




	public void setStep(double step) {
		this.step = step;
		SpinnerUtils.initSpinner(spinner, value, min, max, step, decPlaces, true, null);
	}




	public void setDecPlaces(int decPlaces) {
		this.decPlaces = decPlaces;
		setValue(MathUtils.setDecPlaces(value, decPlaces));
		SpinnerUtils.initSpinner(spinner, value, min, max, step, decPlaces, true, null);
	}




	public double getValue() {
		return value;
	}




	@Override
	public double getItemHeight() {
		return 32;
	}

}
