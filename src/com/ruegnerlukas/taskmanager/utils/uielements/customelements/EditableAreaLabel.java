package com.ruegnerlukas.taskmanager.utils.uielements.customelements;

import com.ruegnerlukas.taskmanager.ui.uidata.UIDataHandler;
import com.ruegnerlukas.taskmanager.ui.uidata.UIModule;
import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

/**
 * A text area that can be in edit or display mode. Double-click to enter edit-mode and click outside to exit the mode again.
 */
public class EditableAreaLabel extends AnchorPane {


	private Label label;
	private TextArea area;
	private boolean editMode = false;

	private boolean editable = true;
	private boolean selectOnEdit = true;

	private List<ChangeListener<String>> listener = new ArrayList<>();




	public EditableAreaLabel() {
		this("");
	}




	/**
	 * @param text the initial content text of this area
	 */
	public EditableAreaLabel(String text) {
		super();

		label = new Label(text);
		label.setWrapText(true);
		label.setAlignment(Pos.TOP_LEFT);
		AnchorUtils.setAnchors(label, 0, 0, 0, 0);
		label.setVisible(true);
		label.setMouseTransparent(false);
		label.setPadding(new Insets(5, 10, 5, 10));
		this.getChildren().add(label);

		area = new TextArea(text);
		area.setWrapText(true);
		AnchorUtils.setAnchors(area, 0, 0, 0, 0);
		area.setMouseTransparent(true);
		area.setVisible(false);
		this.getChildren().add(area);

		label.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				if (event.getClickCount() == 2) {
					enterEditMode();
				}
			}
		});

		area.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				exitEditMode(false);
			}
		});

		area.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				exitEditMode(true);
			}
		});

		UIDataHandler.addRoot(this, UIModule.CONTROL_EDITABLE_AREA);
		UIDataHandler.setStyle(this, UIModule.CONTROL_EDITABLE_AREA);
	}




	/**
	 * whether the content of this area should be editable.
	 */
	public EditableAreaLabel setEditable(boolean editable) {
		this.editable = editable;
		return this;
	}




	/**
	 * whether the content should be selected when entering edit-mode
	 */
	public EditableAreaLabel setSelectOnEdit(boolean selectOnEdit) {
		this.selectOnEdit = selectOnEdit;
		return this;
	}




	private void enterEditMode() {
		if (!editable) {
			return;
		}
		if (editMode) {
			return;
		}
		label.setVisible(false);
		label.setMouseTransparent(true);
		area.setText(label.getText());
		area.setMouseTransparent(false);
		area.setVisible(true);
		area.requestFocus();
		if (selectOnEdit) {
			area.selectAll();
		}
		editMode = true;
	}




	private void exitEditMode(boolean cancel) {
		if (!editMode) {
			return;
		}
		if (cancel) {
			label.setVisible(true);
			label.setMouseTransparent(false);
			area.setMouseTransparent(true);
			area.setVisible(false);
			editMode = false;

		} else {
			String before = label.getText();
			String after = area.getText();
			label.setText(after);
			label.setVisible(true);
			label.setMouseTransparent(false);
			area.setMouseTransparent(true);
			area.setVisible(false);
			editMode = false;
			for (ChangeListener<String> listener : this.listener) {
				listener.changed(null, before, after);
			}
		}
	}




	/**
	 * @return the used {@link Label}
	 */
	public Label getLabel() {
		return this.label;
	}




	/**
	 * @return the used {@link TextArea}
	 */
	public TextArea getTextArea() {
		return this.area;
	}




	/**
	 * @return true, if this area is currently is edit-mode
	 */
	public boolean inEditMode() {
		return this.editMode;
	}




	/**
	 * Sets the content text of this area
	 */
	public void setText(String text) {
		if (inEditMode()) {
			area.setText(text);
		} else {
			label.setText(text);
		}
	}




	/**
	 * @return the content text of this area
	 */
	public String getText() {
		if (inEditMode()) {
			return area.getText();
		} else {
			return label.getText();
		}
	}




	public void addListener(ChangeListener<String> listener) {
		this.listener.add(listener);
	}




	public void removeListener(ChangeListener<String> listener) {
		this.listener.add(listener);
	}


}
