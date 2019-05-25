package com.ruegnerlukas.taskmanager.utils.uielements.customelements.terminal;

public class Selection {


	private int colStart, rowStart;
	private int colEnd, rowEnd;




	public void setStart(int col, int row) {
		colStart = col;
		rowStart = row;
	}




	public void setEnd(int col, int row) {
		colEnd = col;
		rowEnd = row;
	}




	public boolean insideSelection(int col, int row) {

		final int sCol = getColStart();
		final int sRow = getRowStart();
		final int eCol = getColEnd();
		final int eRow = getRowEnd();

		if (sRow == eRow) {
			return sRow == row && sCol <= col && col <= eCol;

		} else {
			if (sRow < row && row < eRow) {
				return true;
			}
			if (sRow == row) {
				if (sCol <= col) {
					return true;
				}
			}
			if (eRow == row) {
				if (eCol >= col) {
					return true;
				}
			}
		}

		return false;
	}




	public int getColStart() {
		if (rowStart < rowEnd) {
			return colStart;
		}
		if (rowStart > rowEnd) {
			return colEnd;
		}
		return Math.min(colStart, colEnd);
	}




	public int getRowStart() {
		return Math.min(rowStart, rowEnd);
	}




	public int getColEnd() {
		if (rowStart < rowEnd) {
			return colEnd;
		}
		if (rowStart > rowEnd) {
			return colStart;
		}
		return Math.max(colStart, colEnd);
	}




	public int getRowEnd() {
		return Math.max(rowStart, rowEnd);
	}

}
