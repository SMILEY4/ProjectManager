package com.ruegnerlukas.taskmanager.utils.uielements.customelements.console;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleView extends ConsoleCanvas {


	private InputListener listener;
	private List<String> lastInputList = new ArrayList<>();
	private int cmdIndex = 0;

	private String startText = "$ ";




	public ConsoleView(int maxLines, int maxChars) {
		super(maxLines, maxChars);

		// selecting
		this.setOnMousePressed(e -> {
			requestFocus();
			if (e.getButton() == MouseButton.PRIMARY) {
				startSelection(e.getX(), e.getY());
				endSelection(e.getX(), e.getY());
				repaintAll();
			}
		});

		this.setOnMouseDragged(e -> {
			requestFocus();
			if (e.getButton() == MouseButton.PRIMARY) {
				endSelection(e.getX(), e.getY());
				repaintAll();
			}
		});

		this.setOnMouseReleased(e -> {
			requestFocus();
			if (e.getButton() == MouseButton.PRIMARY) {
				endSelection(e.getX(), e.getY());
				repaintAll();
			}
		});

		// key-handling
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

		setLine(0, startText, true);
		setCursorPos(startText.length(), 0);
	}




	public void setInputListener(InputListener listener) {
		this.listener = listener;
	}




	public void setStartText(String text) {
		int nPrev = startText.length();
		this.startText = text;
		String line = getLine(getCursorRow());
		if (!line.isEmpty()) {
			if (nPrev == 0) {
				line = text + line;
			} else {
				line = text + line.substring(nPrev);
			}
			setLine(getCursorRow(), line, true);
			int cCol = Math.max(0, Math.min(getCursorColumn() + (text.length() - nPrev), line.length()));
			setCursorPos(cCol, getCursorRow());
		}
	}




	public void printLine(Color color, String str) {
		String currLine = getLine(getCursorRow());

		setLine(getCursorRow(), str, color, true);
		setCursorPos(getCursorColumn(), getCursorRow() + 1);

		setLine(getCursorRow(), currLine, true);
		setCursorPos(currLine.length(), getCursorRow());
	}




	public void printLine(String str) {
		String currLine = getLine(getCursorRow());

		setLine(getCursorRow(), str, true);
		setCursorPos(getCursorColumn(), getCursorRow() + 1);

		setLine(getCursorRow(), currLine, true);
		setCursorPos(currLine.length(), getCursorRow());
	}




	private void onKeyTyped(char c) {
		insertChar(c);
	}




	private void onLeft() {
		setCursorPos(Math.max(getCursorColumn() - 1, startText.length()), getCursorRow());
	}




	private void onRight() {
		setCursorPos(getCursorColumn() + 1, getCursorRow());
	}




	private void onUp() {
		if (0 <= cmdIndex && cmdIndex < lastInputList.size()) {
			String next = lastInputList.get(cmdIndex);
			setLine(getCursorRow(), startText + next, true);
			setCursorPos(getLine(getCursorRow()).length(), getCursorRow());
			cmdIndex = Math.min(lastInputList.size() - 1, cmdIndex + 1);
		}
	}




	private void onDown() {
		if (0 <= cmdIndex && cmdIndex < lastInputList.size()) {
			String next = lastInputList.get(cmdIndex);
			setLine(getCursorRow(), startText + next, true);
			setCursorPos(getLine(getCursorRow()).length(), getCursorRow());
			cmdIndex = Math.max(0, cmdIndex - 1);
		}
	}




	private void onDelete(boolean isDelKey) {
		deleteChar(isDelKey);
	}




	private void onEnter() {
		cmdIndex = 0;
		String str = getLine(getCursorRow());
		setCursorPos(startText.length(), getCursorRow() + 1);
		String input = str.substring(startText.length());
		lastInputList.add(0, input);
		if (listener != null) {
			listener.onInput(startText, input);
		}
		// add empty line
		setLine(getCursorRow(), "", true);
		setCursorPos(getCursorColumn(), getCursorRow() + 1);
		// start new line
		setLine(getCursorRow(), startText, true);
		setCursorPos(getLine(getCursorRow()).length(), getCursorRow());
	}




	private void onPaste() {
		try {
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			insertText(data);
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
	}




	private void onCopy() {
		StringSelection stringSelection = new StringSelection(getSelectedText(getSelection()));
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}




	private void insertText(String text) {
		cmdIndex = 0;
		for (char c : text.toCharArray()) {
			insertChar(c);
		}
	}




	private void insertChar(char c) {
		cmdIndex = 0;
		int col = getCursorColumn();
		int row = getCursorRow();
		setLine(row, insertChar(getLine(row), col, c), true);
		onRight();
	}




	private String insertChar(String line, int index, char c) {
		cmdIndex = 0;
		if (index == 0) {
			return c + line;
		} else if (index == line.length()) {
			return line + c;
		} else {
			return line.substring(0, index) + c + line.substring(index);
		}
	}




	private void deleteChar(boolean isDelKey) {
		cmdIndex = 0;
		int col = getCursorColumn();
		int row = getCursorRow();
		String line = getLine(row);
		if (line.length() == 0) {
			return;
		}
		if (col == 0) {
			if (isDelKey) {
				setLine(row, line.substring(1), true);
			}
		} else if (col == line.length()) {
			if (!isDelKey) {
				if (col > startText.length()) {
					setLine(row, line.substring(0, line.length() - 1), true);
					onLeft();
				}
			}
		} else {
			if (isDelKey) {
				setLine(row, line.substring(0, col) + line.substring(col + 1), true);
			} else {
				setLine(row, line.substring(0, col - 1) + line.substring(col), true);
				onLeft();
			}
		}
	}




	private void startSelection(double x, double y) {
		Selection selection = getSelection() == null ? new Selection() : getSelection();
		selection.setStart(getColumn(x), getRow(y));
		setSelection(selection);
	}




	private void endSelection(double x, double y) {
		Selection selection = getSelection() == null ? new Selection() : getSelection();
		selection.setEnd(getColumn(x), getRow(y));
		setSelection(selection);
	}


}
