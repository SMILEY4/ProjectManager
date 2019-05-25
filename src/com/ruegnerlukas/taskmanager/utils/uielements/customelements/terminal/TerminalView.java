package com.ruegnerlukas.taskmanager.utils.uielements.customelements.terminal;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class TerminalView extends TerminalCanvas {


	private List<String> enteredLines = new ArrayList<>();
	private int indexEnteredLines = 0;




	public TerminalView() {

		this.setOnMousePressed(e -> {
			this.requestFocus();
			startSelection(e.getX(), e.getY());
		});

		this.setOnMouseDragged(e -> {
			this.requestFocus();
			endSelection(e.getX(), e.getY());
		});

		this.setOnMouseReleased(e -> {
			this.requestFocus();
			endSelection(e.getX(), e.getY());
		});


		this.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				onEnter();
			}
			if (e.getCode() == KeyCode.BACK_SPACE) {
				onDelete(false);
			}
			if (e.getCode() == KeyCode.DELETE) {
				onDelete(true);
			}
			if (e.getCode() == KeyCode.LEFT) {
				onLeft();
			}
			if (e.getCode() == KeyCode.RIGHT) {
				onRight();
			}
			if (e.getCode() == KeyCode.UP) {
				onUp();
			}
			if (e.getCode() == KeyCode.DOWN) {
				onDown();
			}
			if (e.isShortcutDown() && e.getCode() == KeyCode.C) {
				onCopy();
			}
			if (e.isShortcutDown() && e.getCode() == KeyCode.V) {
				onPaste();
			}

		});

		this.setOnKeyTyped(e -> {
			if (e.getCharacter().length() == 1 && !Character.isISOControl(e.getCharacter().charAt(0))) {
				onKeyTyped(e.getCharacter().charAt(0));
			}
		});

	}




	private void onKeyTyped(char c) {
		insertAtCursor("" + c);
		indexEnteredLines = 0;
	}




	private void onDelete(boolean isDelKey) {
		deleteAtCursor(isDelKey);
		indexEnteredLines = 0;
	}




	private void onEnter() {
		String line = getLastLine();
		process(line);
		setLastLine(lineStart + line);
		addLine("");
		enteredLines.add(0, line);
		indexEnteredLines = 0;
	}




	private void onUp() {
		setLastLine(enteredLines.get(indexEnteredLines));
		indexEnteredLines = Math.min(Math.max(0, indexEnteredLines + 1), enteredLines.size() - 1);
	}




	private void onDown() {
		setLastLine(enteredLines.get(indexEnteredLines));
		indexEnteredLines = Math.min(Math.max(0, indexEnteredLines - 1), enteredLines.size() - 1);
	}




	private void onLeft() {
		moveCursor(-1);
	}




	private void onRight() {
		moveCursor(+1);
	}




	private void onCopy() {
		System.out.println("COPY: " + getSelectedText());
	}




	private void onPaste() {
		insertAtCursor("-=Clipboard=-" + System.lineSeparator() + "Test");
	}




	private void process(String line) {
		System.out.println("PROC: " + line);
	}

}


