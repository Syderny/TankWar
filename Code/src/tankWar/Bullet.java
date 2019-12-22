package tankWar;

import tankWar.Map.Color;
import tankWar.Map.Direction;
import tankWar.chuck.Wall;

public class Bullet {
	private Position position;
	private Direction direction;
	private Color color;
	private double speed;
	
	public static final double SPEED = 40;
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Bullet(Map map, Position position, Direction direction, Color color) {
		setPosition(position);
		setDirection(direction);
		setColor(color);
		speed = SPEED;
		
		new Thread(new BulletThread(this, map)).start();;
	}
	public void move() {
		double step = speed / Map.FRESH;
		if(direction == Direction.UP) {
			setPosition(new Position(getPosition().getRow()-step, getPosition().getCol()));
		}else if(direction == Direction.DOWN) {
			setPosition(new Position(getPosition().getRow()+step, getPosition().getCol()));
		}else if(direction == Direction.LEFT) {
			setPosition(new Position(getPosition().getRow(), getPosition().getCol()-step));
		}else if(direction == Direction.RIGHT) {
			setPosition(new Position(getPosition().getRow(), getPosition().getCol()+step));
		}
	}
	public boolean shot(Chuck chuck) {
		if(position.getCol() >= chuck.getPosition().getCol() && 
		   position.getCol() <= chuck.getPosition().getCol() + chuck.getWidth() &&
		   position.getRow() >= chuck.getPosition().getRow() &&
		   position.getRow() <= chuck.getPosition().getRow() + chuck.getHeight()) {
			return true;
		}
		return false;
	}
}

class BulletThread implements Runnable {
	private Bullet bullet;
	private Map map;
	
	public BulletThread(Bullet bullet, Map map) {
		this.bullet = bullet;
		this.map = map;
	}
	
	@Override
	public void run() {
		while(true) {
			boolean shot = false;
			synchronized(map.getWalls()) {
				for(Wall wall : map.getWalls()) {
					if(bullet.shot(wall)) {
						wall.shot();
						shot = true;
						break;
					}
				}
			}
			if(bullet.shot(map.getRedFort())) {
				map.getRedFort().shot();
				shot = true;
//				System.out.println("int");
			}
			if(bullet.shot(map.getBlueFort())) {
				map.getBlueFort().shot();
				shot = true;
			}
			if(bullet.shot(map.getRedTank()) && bullet.getColor() == Color.BLUE) {
				map.getRedTank().shot();
				shot = true;
			}
//			System.out.println("out");
			if(bullet.shot(map.getBlueTank()) && bullet.getColor() == Color.RED) {
				map.getBlueTank().shot();
				shot = true;
			}
			
			if(shot) {
				Position position = new Position(bullet.getPosition().getRow(), bullet.getPosition().getCol());
				map.getBullets().remove(bullet);
				map.getBooms().add(position);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						synchronized(map.getBooms()) {
							map.getBooms().remove(position);
						}
					}
					
				}).start();;
				break;
			}
			
			bullet.move();
			
			try {
				Thread.sleep(Map.FRESH);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}