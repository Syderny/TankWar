package tankWar;

public abstract class Chuck {
	private Position position;
	private double height, width;
	private Map map;
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	
	public Chuck(Map map, Position position, double height, double width) {
		setPosition(position);
		setHeight(height);
		setWidth(width);
		setMap(map);
	}
	
	public boolean collide(Chuck chuck) {
		double lub = 0.2;
		if(chuck.getPosition().getCol()+lub >= this.getPosition().getCol()+this.getWidth() ||
		   chuck.getPosition().getCol()+chuck.getWidth()-lub <= this.getPosition().getCol() ||
		   chuck.getPosition().getRow()+lub >= this.getPosition().getRow()+this.getHeight() ||
		   chuck.getPosition().getRow()+chuck.getHeight()-lub <= this.getPosition().getRow())
			return false;
		return true;
	}
}
