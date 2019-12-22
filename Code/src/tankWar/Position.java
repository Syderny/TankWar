package tankWar;

public class Position {
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
