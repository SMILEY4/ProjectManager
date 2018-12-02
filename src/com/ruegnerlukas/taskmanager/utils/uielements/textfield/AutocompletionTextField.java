package com.ruegnerlukas.taskmanager.utils.uielements.textfield;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class AutocompletionTextField extends TextField {

	
	private Set<String> entries;
	private ContextMenu popup;
	
	public AutocompletionTextField(Set<String> entries) {
		super();
		this.entries = entries;
		this.popup = new ContextMenu();
		addListener();
	}
	
	
	
	
	
	private void addListener() {
		this.textProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String enteredText = AutocompletionTextField.this.getText();
				
				if(enteredText == null || enteredText.isEmpty() || enteredText.charAt(enteredText.length()-1) == ' ') {
					popup.hide();
				
				} else {
					String[] enteredWords = enteredText.split(" ");
					String word = enteredWords[enteredWords.length-1];
					
					List<String> matches = new ArrayList<String>();
					for(String entry : entries) {
						if(entry.contains(word)) {
							matches.add(entry);
						}
					}
					
					if(matches.isEmpty()) {
						popup.hide();
					} else {
						updatePopup(matches, word);
						if(!popup.isShowing()) {
							popup.show(AutocompletionTextField.this, Side.BOTTOM, 0, 0);
						}
					}
					
				}
				
				
			}
		});
	}
	
	
	
	private void updatePopup(List<String> entries, String filter) {
		int maxEntries = 10;
		
		List<CustomMenuItem> items = new ArrayList<CustomMenuItem>();
		
		for(int i=0; i<Math.min(entries.size(), maxEntries); i++) {
			String entry = entries.get(i);
			
			Label label = new Label();
			label.setGraphic(buildTextFlow(entry, filter));
			label.setPrefHeight(16);
			CustomMenuItem item = new CustomMenuItem(label, true);
			items.add(item);
			
			item.setOnAction(actionEvent -> {
				if(this.getText().contains(" ")) {
					this.setText(this.getText().substring(0, this.getText().lastIndexOf(' ')) + " " + entry);
				} else {
					this.setText(entry);
				}
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
		Text textAfter = new Text(text.substring(filterIndex+filter.length()));
		Text textFilter = new Text(text.substring(filterIndex, filterIndex+filter.length()));
		
		Label tmp = new Label();
		
		textBefore.setFont(tmp.getFont());
		textAfter.setFont(tmp.getFont());
		textBefore.setFill(Color.web("#29323c"));
		textAfter.setFill(Color.web("#29323c"));

		textFilter.setFont(Font.font(tmp.getFont().getFamily(), FontWeight.BOLD, tmp.getFont().getSize()));
		textFilter.setFill(Color.web("#3e4d59"));

//		textBefore.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));
//		textAfter.setFont(Font.font("Helvetica", FontWeight.LIGHT, 16));
//		textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
		return new TextFlow(textBefore, textFilter, textAfter);
	}
	
	
	
	
}
