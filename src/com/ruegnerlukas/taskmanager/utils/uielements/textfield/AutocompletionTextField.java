package com.ruegnerlukas.taskmanager.utils.uielements.textfield;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AutocompletionTextField extends TextField {


	private Set<String> entries;
	private ContextMenu popup;

	private String delimiter;
	private boolean ignoreCase;
	private int maxEntries = 10;

	private Color colorText = Color.web("#29323c");
	private Color colorMatch = Color.web("#3e4d59");

	private Font fontText = new Label().getFont();
	private Font fontMatch = Font.font(fontText.getFamily(), FontWeight.BOLD, fontText.getSize());




	public AutocompletionTextField(Set<String> entries, boolean ignoreCase, String delimiter) {
		super();
		this.entries = entries;
		this.popup = new ContextMenu();
		this.ignoreCase = ignoreCase;
		this.delimiter = delimiter;
		addListener();
	}


	public AutocompletionTextField(Set<String> entries, boolean ignoreCase, String delimiter, String text) {
		super();
		this.entries = entries;
		this.popup = new ContextMenu();
		this.ignoreCase = ignoreCase;
		this.delimiter = delimiter;
		this.setText(text);
		addListener();
	}




	public void setMaxEntries(int n) {
		this.maxEntries = n;
	}




	public int getMaxEntries() {
		return maxEntries;
	}




	public void styleText(Color color, Font font) {
		this.colorText = color;
		this.fontText = font;
	}




	public Color getColorText() {
		return colorText;
	}




	public Font getFontText() {
		return fontText;
	}




	public void styleMatch(Color color, Font font) {
		this.colorMatch = color;
		this.fontMatch = font;
	}




	public Color getColorMatch() {
		return colorText;
	}




	public Font getFontMatch() {
		return fontText;
	}




	private void addListener() {
		this.textProperty().addListener((observable, oldValue, newValue) -> {
			String enteredText = AutocompletionTextField.this.getText();

			if (enteredText == null || enteredText.isEmpty() || enteredText.matches(".*(" + delimiter + ")")) {
				popup.hide();

			} else {
				String[] enteredWords = enteredText.split(delimiter);
				String lastWord = enteredWords[enteredWords.length - 1];

				List<String> matches = new ArrayList<>();
				for (String entry : entries) {
					if (ignoreCase) {
						if (entry.toLowerCase().contains(lastWord.toLowerCase())) {
							matches.add(entry);
						}
					} else {
						if (entry.contains(lastWord)) {
							matches.add(entry);
						}
					}
				}

				if (matches.isEmpty()) {
					popup.hide();
				} else {
					updatePopup(matches, lastWord);
					if (!popup.isShowing()) {
						popup.show(AutocompletionTextField.this, Side.BOTTOM, 0, 0);
					}
				}

			}


		});
	}




	private void updatePopup(List<String> entries, String filter) {

		List<CustomMenuItem> items = new ArrayList<>();

		for (int i = 0; i < Math.min(entries.size(), maxEntries); i++) {
			String entry = entries.get(i);

			Label label = new Label();
			label.setGraphic(buildTextFlow(entry, filter));
			label.setPrefHeight(16);
			CustomMenuItem item = new CustomMenuItem(label, true);
			items.add(item);

			item.setOnAction(actionEvent -> {
				String[] enteredWords = this.getText().split(delimiter);
				String lastWord = enteredWords[enteredWords.length - 1];
				this.setText(this.getText().replaceAll(lastWord+"$", entry));
				this.positionCaret(this.getText().length());
				popup.hide();
			});

			popup.getItems().clear();
			popup.getItems().addAll(items);

		}

	}




	private TextFlow buildTextFlow(String text, String filter) {

		int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
		Text textBefore = new Text(text.substring(0, filterIndex));
		Text textAfter = new Text(text.substring(filterIndex + filter.length()));
		Text textFilter = new Text(text.substring(filterIndex, filterIndex + filter.length()));

		textBefore.setFont(fontText);
		textAfter.setFont(fontText);
		textBefore.setFill(colorText);
		textAfter.setFill(colorText);

		textFilter.setFont(fontMatch);
		textFilter.setFill(colorMatch);

		return new TextFlow(textBefore, textFilter, textAfter);
	}


}
