package com.ruegnerlukas.taskmanager.utils.uielements.customelements.terminal;


import com.ruegnerlukas.taskmanager.utils.uielements.AnchorUtils;
import com.ruegnerlukas.taskmanager.utils.uielements.customelements.ResizableCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class TerminalCanvas extends AnchorPane {


	private ResizableCanvas canvas;

	private Font font;
	private Text text;
	private double cellWidth;
	private double cellHeight;
	private double lineSpacing = 1.2;

	private List<String> lines = new ArrayList<>();

	private Selection selection = null;
	private Cursor cursor = new Cursor();

	private int nBlockedSymbols = 0;




	public TerminalCanvas() {

		this.font = Font.font("Consolas", 16);
		this.text = new Text(" ");
		text.setFont(font);
		this.cellWidth = text.getLayoutBounds().getWidth();
		this.cellHeight = text.getLayoutBounds().getHeight();

		canvas = new ResizableCanvas(this, 0, 0) {
			@Override
			public void onRepaint(GraphicsContext g) {
				TerminalCanvas.this.onRepaint(g);
			}
		};
		AnchorUtils.setAnchors(canvas, 0, 0, 0, 0);
		this.getChildren().add(canvas);

		moveCursorToEnd();
	}




	public void startSelection(double x, double y) {
		if (selection == null) {
			selection = new Selection();
		}
		selection.setStart(getColumnAt(x), getRowAt(y));
		selection.setEnd(getColumnAt(x), getRowAt(y));
		repaint();
	}




	public void endSelection(double x, double y) {
		if (selection == null) {
			selection = new Selection();
		}
		selection.setEnd(getColumnAt(x), getRowAt(y));
		repaint();
	}




	public Selection getSelection() {
		return selection;
	}




	public void moveCursor(int d) {
		int n = lines.isEmpty() ? 0 : lines.get(lines.size() - 1).length();
		cursor.column = Math.min(Math.max(cursor.column + d, 0), n);
		repaint();
	}




	private void moveCursorToEnd() {
		if (lines.isEmpty()) {
			cursor.column = 0;
		} else {
			cursor.column = lines.get(lines.size() - 1).length();
		}
	}




	public Cursor getTerminalCursor() {
		return cursor;
	}




	public int getColumnAt(double x) {
		return (int) (x / cellWidth);
	}




	public int getRowAt(double y) {
		double totalHeight = lines.size() * (cellHeight * lineSpacing);
		double yOffset = Math.min(0, canvas.getHeight() - totalHeight);
		return (int) ((y - yOffset) / (cellHeight * lineSpacing));
	}




	public char getCharAt(double x, double y) {
		int j = getRowAt(y);
		if (0 <= j && j < lines.size()) {
			String strRow = lines.get(j);
			int i = getColumnAt(x);
			if (0 <= i && i < strRow.length()) {
				return strRow.charAt(i);
			}
		}
		return ' ';
	}




	public void setFont(String family, double size) {
		this.font = Font.font(family, size);
		this.text = new Text(" ");
		text.setFont(font);
		this.cellWidth = text.getLayoutBounds().getWidth();
		this.cellHeight = text.getLayoutBounds().getHeight();
		repaint();
	}




	public void setTextData(List<String> lines) {
		this.lines = lines;
		moveCursorToEnd();
		repaint();
	}




	public void addLine(String line) {
		this.lines.add(line);
		moveCursorToEnd();
		repaint();
	}




	public void setLastLine(String text) {
		if (lines.isEmpty()) {
			lines.add(text);
		} else {
			lines.set(lines.size() - 1, text);
		}
		moveCursorToEnd();
		repaint();
	}




	public void deleteAtCursor(boolean isDelKey) {
		if (lines.isEmpty()) {
			return;
		}

		String line = lines.get(lines.size() - 1);
		if (line.length() == 0) {
			return;
		}

		String newLine = "";

		if (isDelKey) {
			if (line.length() <= cursor.column) {
				return;
			}
			List<Character> charsLine = new ArrayList<>();
			for (char c : line.toCharArray()) {
				charsLine.add(c);
			}
			charsLine.remove(cursor.column);
			for (char c : charsLine) {
				newLine += c;
			}

		} else {
			if (cursor.column == 0) {
				return;
			}
			if (line.length() <= cursor.column) {
				newLine = line.substring(0, line.length() - 1);
			} else {
				List<Character> charsLine = new ArrayList<>();
				for (char c : line.toCharArray()) {
					charsLine.add(c);
				}
				charsLine.remove(cursor.column - 1);
				for (char c : charsLine) {
					newLine += c;
				}
			}
		}

		lines.set(lines.size() - 1, newLine);

		if (!isDelKey) {
			moveCursor(-1);
		}

		repaint();
	}




	public void insertAtCursor(String text) {

		if (lines.isEmpty()) {
			addLine(text);

		} else {
			String line = lines.get(lines.size() - 1);
			String newLine = "";

			if (line.length() < cursor.column) {
				newLine = line + text;

			} else {
				List<Character> charsLine = new ArrayList<>();
				for (char c : line.toCharArray()) {
					charsLine.add(c);
				}

				List<Character> charsInsert = new ArrayList<>();
				for (char c : text.toCharArray()) {
					charsInsert.add(c);
				}

				charsLine.addAll(cursor.column, charsInsert);

				for (char c : charsLine) {
					newLine += c;
				}

			}
			lines.set(lines.size() - 1, newLine);
		}

		moveCursor(+1);
		repaint();
	}




	public List<String> getLines() {
		return lines;
	}




	public String getLastLine() {
		return lines.isEmpty() ? "" : lines.get(lines.size() - 1);
	}




	public void repaint() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		onRepaint(g);
	}




	private void onRepaint(GraphicsContext g) {
		drawBackground(g);
		if (lines != null && !lines.isEmpty()) {
			drawSelection(g);
			drawText(g);
		}
		drawCursor(g);
	}




	private void drawBackground(GraphicsContext g) {
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight());
	}




	private void drawSelection(GraphicsContext g) {

		final double totalHeight = lines.size() * (cellHeight * lineSpacing);
		final double yOffset = Math.min(0, g.getCanvas().getHeight() - totalHeight);

		// draw selection
		if (selection != null) {
			final double ey = selection.getRowEnd() * cellHeight * lineSpacing;

			g.setFill(Color.WHITE);

			if (selection.getRowStart() == selection.getRowEnd()) {
				final double y = selection.getRowStart() * cellHeight * lineSpacing + yOffset;
				final double sx = selection.getColStart() * cellWidth;
				final double ex = selection.getColEnd() * cellWidth;
				g.fillRect(sx, y, ex - sx + cellWidth, cellHeight);

			} else {
				for (int i = selection.getRowStart(); i <= selection.getRowEnd(); i++) {
					final double y = i * cellHeight * lineSpacing + yOffset;

					if (i == selection.getRowStart()) {
						final double sx = selection.getColStart() * cellWidth;
						g.fillRect(sx, y, canvas.getWidth() - sx, cellHeight * lineSpacing);

					} else if (i == selection.getRowEnd()) {
						final double ex = selection.getColEnd() * cellWidth;
						g.fillRect(0, y, ex, cellHeight * lineSpacing);

					} else {
						g.fillRect(0, y, canvas.getWidth(), cellHeight * lineSpacing);
					}

				}
			}

		}

	}




	private void drawCursor(GraphicsContext g) {

		g.setFill(Color.WHITE);
		g.setFont(font);

		final double totalHeight = lines.size() * (cellHeight * lineSpacing);
		final double yOffset = Math.min(0, g.getCanvas().getHeight() - totalHeight);

		final double py = (lines.isEmpty() ? 0 : lines.size() - 1) * (cellHeight * lineSpacing) + yOffset;
		final double px = cursor.column * cellWidth;

		g.fillText("_", px, py + text.getBaselineOffset());
	}




	private void drawText(GraphicsContext g) {
		g.setFill(Color.WHITE);
		g.setFont(font);

		final double totalHeight = lines.size() * (cellHeight * lineSpacing);
		final double yOffset = Math.min(0, g.getCanvas().getHeight() - totalHeight);

		for (int i = 0; i < lines.size(); i++) {
			final String line = lines.get(i);
			final double py = i * (cellHeight * lineSpacing) + yOffset;

			for (int j = 0; j < line.length(); j++) {
				final double px = j * cellWidth;

				if (selection != null && selection.insideSelection(j, i)) {
					g.setFill(Color.BLACK);
				} else {
					g.setFill(Color.WHITE);
				}
				g.fillText(Character.toString(line.charAt(j)), px, py + text.getBaselineOffset());

			}

		}

	}

}
