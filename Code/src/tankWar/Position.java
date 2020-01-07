package tankWar;

import java.io.Serializable;

public class Position implements Serializable {
	private static final long serialVersionUID = 4086229885164143490L;
	private double row, col;

	public double getRow() {
		return row;
	}
	public void setRow(double row) {
		this.row = row;
	}
	public double getCol() {
		return col;
	}
	public void setCol(double col) {
		this.col = col;
	}
	
	public Position(double row, double col) {
		setRow(row);
		setCol(col);
	}
	
}
