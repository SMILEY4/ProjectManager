package com.ruegnerlukas.taskmanager.utils.uielements.customelements;

import com.ruegnerlukas.taskmanager.utils.FXNodeSilencer;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


/**
 * A text field that can be behave like a {@link TextArea} or a {@link TextField}
 */
public class MultiTextField extends AnchorPane {


	public static double calculateOptimalHeight(int nlines) {
		return 18.5 * (double) nlines + 28.5;
	}




	private boolean multiline = false;
	private TextArea area = new TextArea();
	private TextField field = new TextField();
	private final SimpleStringProperty text = new SimpleStringProperty("");




	public MultiTextField() {

		area.textProperty().addListener((observable, oldValue, newValue) -> text.set(newValue));
		AnchorUtils.setAnchors(area, 0, 0, 0, 0);
		this.getChildren().add(area);

		field.textProperty().addListener((observable, oldValue, newValue) -> text.set(newValue));
		AnchorUtils.setAnchors(field, 0, 0, 0, 0);
		this.getChildren().add(field);

		setMultiline(false);
	}




	public SimpleStringProperty textProperty() {
		return this.text;
	}




	public void setText(String value) {
		area.setText(value);
		field.setText(value);
	}




	/**
	 * Set the text of this field withour triggering actions.
	 */
	public void setTextSilent(String value) {
		FXNodeSilencer.muteNode(area);
		FXNodeSilencer.muteNode(field);
		area.setText(value);
		field.setText(value);
		FXNodeSilencer.unmuteNode(area);
		FXNodeSilencer.unmuteNode(field);
	}




	public void setPromptText(String value) {
		area.setPromptText(value);
		field.setPromptText(value);
	}




	/**
	 * whether this field should behave like a {@link TextField} (multiline = false) or a {@link TextArea} (multiline = true)
	 */
	public void setMultiline(boolean multiline) {
		String text = getText();
		this.multiline = multiline;
		this.area.setVisible(multiline);
		this.area.setDisable(!multiline);
		this.area.setMouseTransparent(!multiline);
		this.area.setText(text);
		this.field.setVisible(!multiline);
		this.field.setDisable(multiline);
		this.field.setMouseTransparent(multiline);
		this.field.setText(text);
	}




	public TextArea getTextArea() {
		return this.area;
	}




	public TextField getTextField() {
		return this.field;
	}




	/**
	 * @return true if this field behaves like a {@link TextArea} (false for {@link TextField}
	 */
	public boolean isMultiline() {
		return this.multiline;
	}




	public String getText() {
		return this.textProperty().get();
	}

}
