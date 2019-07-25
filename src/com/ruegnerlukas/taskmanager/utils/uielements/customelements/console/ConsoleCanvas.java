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


	public final Color COLOR_TEXT = Color.WHITE;
	public final Color COLOR_BACKGROUND = Color.BLACK;

	protected ResizableCanvas canvas;
	protected ScrollBar scrollBar;

	private char[][] cells;
	private Color[] rowColors;
	private double cellWidth;
	private double cellHeight;
	private double lineSpacing = 1.2;

	private Font font;
	private double baselineOffset;

	private Selection selection;

	private int cursorColumn = 0;
	private int cursorRow = 0;




	/**
	 * @param maxLines the max amount of lines
	 * @param maxChars the max amount of characters per line
	 */
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
		rowColors = new Color[maxLines];

		// misc
		setFont("Consolas", 16);
		repaintAll();
	}




	/**
	 * Selects an area specified by the given {@link Selection}
	 */
	public void setSelection(Selection selection) {
		this.selection = selection;
	}




	/**
	 * @return the selected area as a {@link Selection} or null
	 */
	public Selection getSelection() {
		return selection;
	}




	/**
	 * moves the cursor to the end of its current line.
	 */
	public void moveCursorToEndOfLine() {
		String line = getLine(getCursorRow());
		setCursorPos(line.length(), getCursorRow());
	}




	/**
	 * Sets the position of the cursor
	 */
	public void setCursorPos(int col, int row) {
		int lastCol = cursorColumn;
		int lastRow = cursorRow;

		// row
		cursorRow = Math.max(0, Math.min(row, cells.length));

		if (cursorRow >= cells.length) {
			clearConsole();
		}

		if (cursorRow > getBottomRow()) {
			int scrollToRow = getTopRow() + (cursorRow - getBottomRow());
			setTopLine(scrollToRow);
		}

		// col
		final int n = Math.min(cells[cursorRow].length - 1, getLine(cursorRow).length());
		cursorColumn = Math.max(0, Math.min(col, n));

		// repaint
		repaintCell(lastCol, lastRow);
		repaintCell(cursorColumn, cursorRow);
	}




	private void clearConsole() {
		cells = new char[cells.length][cells[0].length];
		rowColors = new Color[cells.length];
		cursorRow = 0;
		repaintAll();
	}




	private int getTopRow() {
		return (int) scrollBar.getValue();
	}




	private int getBottomRow() {
		return (int) Math.max(0, (getTopRow() + canvas.getHeight() / (cellHeight * lineSpacing)) - 1);
	}




	public int getCursorColumn() {
		return cursorColumn;
	}




	public int getCursorRow() {
		return cursorRow;
	}




	/**
	 * @return the specified row as a string
	 */
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




	/**
	 * @return a list of all lines selected by the given {@link Selection}.
	 */
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




	/**
	 * @return all lines selected by the given {@link Selection} as a single string with linebreaks.
	 */
	public String getSelectedText(Selection selection) {
		List<String> list = getSelectedTextAsList(selection);
		StringBuilder builder = new StringBuilder();
		for (String str : list) {
			builder.append(str).append(System.lineSeparator());
		}
		return builder.toString();
	}




	/**
	 * Sets the text of the console to the given lines
	 */
	public void setText(List<String> lines) {
		for (int i = 0; i < Math.min(lines.size(), cells.length); i++) {
			final String line = lines.get(i);
			for (int j = 0; j < Math.min(line.length(), cells[i].length); j++) {
				cells[i][j] = line.charAt(j);
			}
		}
	}




	/**
	 * Sets the line specified by the row parameter to the given string and color.
	 * If the repaint parameter is set to false, the console will not imminently repaint the line.
	 */
	public void setLine(int row, String str, Color color, boolean repaint) {
		if (0 <= row && row < cells.length) {
			rowColors[row] = color;
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




	/**
	 * Sets the line specified by the row parameter to the given string.
	 * If the repaint parameter is set to false, the console will not imminently repaint the line.
	 */
	public void setLine(int row, String str, boolean repaint) {
		setLine(row, str, COLOR_TEXT, repaint);
	}




	/**
	 * Sets the character at the given row and column.
	 * If the repaint parameter is set to false, the console will not imminently repaint the cell.
	 */
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




	/**
	 * Set the font of this console
	 */
	public void setFont(String family, double size) {
		this.font = Font.font(family, size);
		Text text = new Text(" ");
		text.setFont(font);
		this.cellWidth = text.getLayoutBounds().getWidth();
		this.cellHeight = text.getLayoutBounds().getHeight();
		this.baselineOffset = text.getBaselineOffset();
	}




	public void setTopLine(int line) {
		scrollBar.setValue(Math.max(0, Math.min(line, cells.length - 1)));
	}




	private void onScroll(int lineOffset) {
		repaintAll();
	}




	private void onResize() {
		repaintAll();
	}




	/**
	 * @return the column-index at the given x-coordinate
	 */
	public int getColumn(double x) {
		return (int) (x / cellWidth);
	}




	/**
	 * @return the row-index at the given x-coordinate
	 */
	public int getRow(double y) {
		return (int) (y / (cellHeight * lineSpacing) + scrollBar.getValue());
	}




	/**
	 * Repaint the whole console.
	 */
	public void repaintAll() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		repaintAll(g);
	}




	/**
	 * Repaint the whole console.
	 */
	private void repaintAll(GraphicsContext g) {
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		final int firstLine = (int) scrollBar.getValue();
		final int nLines = (int) (canvas.getHeight() / (cellHeight * lineSpacing)) + 1;

		for (int i = firstLine; i <= Math.min(cells.length - 1, firstLine + nLines); i++) {
			repaintLine(i, g);
		}

	}




	/**
	 * Repaint the given line.
	 */
	public void repaintLine(int row) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		repaintLine(row, g);
	}




	/**
	 * Repaint the given line
	 */
	public void repaintLine(int row, GraphicsContext g) {
		final int nCells = (int) (canvas.getWidth() / cellWidth) + 1;
		for (int i = 0; i <= Math.min(cells[row].length - 1, nCells); i++) {
			repaintCell(i, row, g);
		}
	}




	/**
	 * Repaint the currently selected area.
	 */
	public void repaintSelection() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		repaintSelection(g);
	}




	/**
	 * Repaint the currently selected area.
	 */
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




	/**
	 * Repaint the given cell
	 */
	public void repaintCell(int col, int row) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		repaintCell(col, row, g);
	}




	/**
	 * Repaint the given cell
	 */
	public void repaintCell(int col, int row, GraphicsContext g) {

		final double cellX = col * cellWidth;
		final double cellY = (row - (int) scrollBar.getValue()) * cellHeight * lineSpacing;
		final double cellW = cellWidth;
		final double cellH = cellHeight * lineSpacing;

		g.setFill(getBackgroundColor(col, row));
		g.fillRect(cellX, cellY, cellW + 1, cellH + 1);

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
			return COLOR_BACKGROUND.invert();
		} else {
			return COLOR_BACKGROUND;
		}
	}




	private Color getTextColor(int col, int row) {
		Color color = rowColors[row] == null ? COLOR_TEXT : rowColors[row];
		if (selection != null && selection.insideSelection(col, row)) {
			return color.invert();
		} else {
			return color;
		}
	}

}
