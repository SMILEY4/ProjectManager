package com.ruegnerlukas.taskmanager.utils.uielements.customelements.console;


import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.ResizableCanvas;
import javafx.geometry.Orientation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class ConsoleCanvas extends AnchorPane {


	protected ResizableCanvas canvas;
	protected ScrollBar scrollBar;

	private char[][] cells;
	private double cellWidth;
	private double cellHeight;
	private double lineSpacing = 1.2;

	private Font font;
	private double baselineOffset;

	private Selection selection;

	private int cursorColumn = 0;
	private int cursorRow = 0;




	public ConsoleCanvas(int maxLines, int maxChars) {

		// canvas
		canvas = new ResizableCanvas(this, 0, 0) {
			@Override
			public void onResize() {
				ConsoleCanvas.this.onResize();
			}
		};
		AnchorUtils.setAnchors(canvas, 0, 15, 0, 0);
		this.getChildren().add(canvas);

		// scrollbar
		scrollBar = new ScrollBar();
		scrollBar.setOrientation(Orientation.VERTICAL);
		scrollBar.setMinWidth(15);
		scrollBar.setMaxWidth(15);
		scrollBar.setMin(0);
		scrollBar.setMax(maxLines);
		AnchorUtils.setAnchors(scrollBar, 0, 0, 0, null);
		this.getChildren().add(scrollBar);
		scrollBar.valueProperty().addListener(((observable, oldValue, newValue) -> {
			onScroll(newValue.intValue());
		}));

		// cells
		cells = new char[maxLines][maxChars];

		// misc
		setFont("Consolas", 16);
		repaintAll();
	}




	public void setSelection(Selection selection) {
		this.selection = selection;
	}




	public Selection getSelection() {
		return selection;
	}




	public void setCursorPos(int col, int row) {
		int lastCol = cursorColumn;
		int lastRow = cursorRow;
		cursorRow = Math.max(0, Math.min(row, cells.length));
		final int n = Math.min(cells[cursorRow].length - 1, getLine(cursorRow).length());
		cursorColumn = Math.max(0, Math.min(col, n));
		if(cursorRow > getBottomRow()) {
			int scrollToRow = getTopRow() + (cursorRow - getBottomRow());
			scrollBar.setValue(Math.max(0, Math.min(scrollToRow, cells.length-1)));
		}
		repaintCell(lastCol, lastRow);
		repaintCell(cursorColumn, cursorRow);
	}




	private int getTopRow() {
		return (int) scrollBar.getValue();
	}




	private int getBottomRow() {
		return (int) (getTopRow() + canvas.getHeight() / (cellHeight * lineSpacing)) - 1;
	}




	public int getCursorColumn() {
		return cursorColumn;
	}




	public int getCursorRow() {
		return cursorRow;
	}




	public String getLine(int row) {
		if (0 <= row && row < cells.length) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0, n = cells[row].length; i < n; i++) {
				char c = cells[row][i];
				if (c == 0) {
					break;
				} else {
					builder.append(c);
				}
			}
			return builder.toString();
		} else {
			return "";
		}
	}




	public List<String> getSelectedTextAsList(Selection selection) {

		List<String> lines = new ArrayList<>();

		if (selection != null) {

			StringBuilder builder = new StringBuilder();

			if (selection.getRowStart() == selection.getRowEnd()) {
				final int row = selection.getRowStart();
				if (row < cells.length) {
					for (int i = selection.getColStart(); i <= Math.min(selection.getColEnd(), cells[row].length - 1); i++) {
						builder.append(cells[row][i]);
					}
				}
				lines.add(builder.toString());

			} else {
				final int rowStart = Math.min(cells.length - 1, Math.max(0, selection.getRowStart()));
				final int rowEnd = Math.min(cells.length - 1, Math.max(0, selection.getRowEnd()));
				for (int i = rowStart; i <= rowEnd; i++) {
					builder.setLength(0);
					for (int j = 0; j <= cells[i].length; j++) {
						if (j == cells[i].length || cells[i][j] == 0) {
							lines.add(builder.toString());
							break;
						} else {
							builder.append(cells[i][j]);
						}
					}
				}
			}
		}

		return lines;
	}




	public String getSelectedText(Selection selection) {
		List<String> list = getSelectedTextAsList(selection);
		StringBuilder builder = new StringBuilder();
		for (String str : list) {
			builder.append(str).append(System.lineSeparator());
		}
		return builder.toString();
	}




	public void setText(List<String> lines) {
		for (int i = 0; i < Math.min(lines.size(), cells.length); i++) {
			final String line = lines.get(i);
			for (int j = 0; j < Math.min(line.length(), cells[i].length); j++) {
				cells[i][j] = line.charAt(j);
			}
		}
	}




	public void setLine(int row, String str, boolean repaint) {
		if (0 <= row && row < cells.length) {
			for (int i = 0; i < Math.min(cells[row].length, str.length()); i++) {
				char c = str.charAt(i);
				cells[row][i] = c;
			}
			for (int i = str.length(); i < cells[row].length; i++) {
				cells[row][i] = 0;
			}
			if (repaint) {
				repaintLine(row);
			}
		}
	}




	public void setCell(int col, int row, char c, boolean repaint) {
		if (0 <= row && row < cells.length) {
			if (0 <= col && col < cells[row].length) {
				cells[row][col] = c;
				if (repaint) {
					repaintCell(col, row);
				}
			}
		}
	}




	public void setFont(String family, double size) {
		this.font = Font.font(family, size);
		Text text = new Text(" ");
		text.setFont(font);
		this.cellWidth = text.getLayoutBounds().getWidth();
		this.cellHeight = text.getLayoutBounds().getHeight();
		this.baselineOffset = text.getBaselineOffset();
	}




	public void setTopLine(int line) {
		scrollBar.setValue(Math.max(0, Math.min(line, cells.length)));
	}




	private void onScroll(int lineOffset) {
		repaintAll();
	}




	private void onResize() {
		repaintAll();
	}




	public int getColumn(double x) {
		return (int) (x / cellWidth);
	}




	public int getRow(double y) {
		return (int) (y / (cellHeight * lineSpacing) + scrollBar.getValue());
	}




	public void repaintAll() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		repaintAll(g);
	}




	private void repaintAll(GraphicsContext g) {
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		final int firstLine = (int) scrollBar.getValue();
		final int nLines = (int) (canvas.getHeight() / (cellHeight * lineSpacing)) + 1;

		for (int i = firstLine; i <= Math.min(cells.length - 1, firstLine + nLines); i++) {
			repaintLine(i, g);
		}

	}




	public void repaintLine(int row) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		repaintLine(row, g);
	}




	public void repaintLine(int row, GraphicsContext g) {
		final int nCells = (int) (canvas.getWidth() / cellWidth) + 1;
		for (int i = 0; i <= Math.min(cells[row].length - 1, nCells); i++) {
			repaintCell(i, row, g);
		}
	}




	public void repaintSelection() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		repaintSelection(g);
	}




	public void repaintSelection(GraphicsContext g) {
		if (selection != null) {
			if (selection.getRowStart() == selection.getRowEnd()) {
				final int row = selection.getRowStart();
				if (row < cells.length) {
					for (int i = selection.getColStart(); i <= Math.min(selection.getColEnd(), cells[row].length - 1); i++) {
						repaintCell(i, row, g);
					}
				}
			} else {
				final int rowStart = Math.min(cells.length - 1, Math.max(0, selection.getRowStart()));
				final int rowEnd = Math.min(cells.length - 1, Math.max(0, selection.getRowEnd()));
				for (int i = rowStart; i <= rowEnd; i++) {
					repaintLine(i, g);
				}
			}
		}
	}




	public void repaintCell(int col, int row) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		repaintCell(col, row, g);
	}




	public void repaintCell(int col, int row, GraphicsContext g) {

		final double cellX = col * cellWidth;
		final double cellY = (row - (int) scrollBar.getValue()) * cellHeight * lineSpacing;
		final double cellW = cellWidth;
		final double cellH = cellHeight * lineSpacing;

		g.setFill(getBackgroundColor(col, row));
		g.fillRect(cellX, cellY, cellW, cellH);

		char c = cells[row][col];

		if (c != 0 && c != ' ') {
			g.setFill(getTextColor(col, row));
			g.setFont(font);
			g.fillText(String.valueOf(c), cellX, cellY + baselineOffset);
		}

		if (col == cursorColumn && row == cursorRow) {
			g.setFill(getTextColor(col, row));
			g.setFont(font);
			g.fillText("_", cellX, cellY + baselineOffset);
		}

	}




	private Color getBackgroundColor(int col, int row) {
		if (selection != null && selection.insideSelection(col, row)) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}




	private Color getTextColor(int col, int row) {
		if (selection != null && selection.insideSelection(col, row)) {
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}

}
