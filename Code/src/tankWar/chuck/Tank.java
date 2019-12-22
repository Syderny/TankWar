package tankWar.chuck;

import tankWar.Bullet;
import tankWar.Chuck;
import tankWar.Main;
import tankWar.Map;
import tankWar.Map.Color;
import tankWar.Map.Direction;
import tankWar.Position;

public class Tank extends Chuck {
	public static final double HEIGHT = 2;
	public static final double WIDTH = 2;
	public static final double SPEED = 10;
	public static final int MAX_HP = 2;
	public static final int INV_TIME = 1200;
	public static final int REL_TIME = 500;
	public static final int REB_TIME = 500;

	
	private Direction direction;
	private Color color;
	private boolean invulnerable;
	private boolean reloading;
	private double speed;
	private int HP;
	private boolean dead;
	
	
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
	public boolean isInvulnerable() {
		return invulnerable;
	}
	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int hP) {
		HP = hP;
	}
	
	public Tank(Map map, Color color) {
		super(map, null, HEIGHT, WIDTH);
		setColor(color);
		setInvulnerable(false);
		setHP(MAX_HP);
		setDead(false);
		reloading = false;
		speed = SPEED;
		if(color == Color.RED) {
			setPosition(Map.RED_POINT);
			setDirection(Direction.UP);
		}else {
			setPosition(Map.BLUE_POINT);
			setDirection(Direction.DOWN);
		}
	}
	
	public void fire() {
		if(reloading) {
//			System.out.println("Reloading");
			return;
		}
		
		Position position = null;
		if(direction == Direction.UP) {
			position = new Position(getPosition().getRow(), getPosition().getCol() + getWidth()/2);
		}else if(direction == Direction.DOWN) {
			position = new Position(getPosition().getRow() + getHeight(), getPosition().getCol() + getWidth()/2);
		}else if(direction == Direction.LEFT) {
			position = new Position(getPosition().getRow() + getHeight()/2, getPosition().getCol());
		}else if(direction == Direction.RIGHT) {
			position = new Position(getPosition().getRow() + getHeight()/2, getPosition().getCol() + getWidth());
		}
		getMap().getBullets().add(new Bullet(getMap(), position, direction, color));
		
		reloading = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(REL_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				reloading = false;
			}
		}).start();;
	}
	public void move() {
		double step = speed / Map.FRESH;
		Position position = new Position(getPosition().getRow(), getPosition().getCol());
		if(direction == Direction.UP) {
			setPosition(new Position(getPosition().getRow()-step, getPosition().getCol()));
		}else if(direction == Direction.DOWN) {
			setPosition(new Position(getPosition().getRow()+step, getPosition().getCol()));
		}else if(direction == Direction.LEFT) {
			setPosition(new Position(getPosition().getRow(), getPosition().getCol()-step));
		}else if(direction == Direction.RIGHT) {
			setPosition(new Position(getPosition().getRow(), getPosition().getCol()+step));
		}
		
		for(Wall wall: getMap().getWalls()) {
			if(this.collide(wall)) {				
				setPosition(position);
				return;
			}
		}
		if(getColor() == Color.RED && this.collide(getMap().getBlueTank())) {
			setPosition(position);
			return;
		}
		if(getColor() == Color.BLUE && this.collide(getMap().getRedTank())) {
			setPosition(position);
			return;
		}
		if(this.collide(getMap().getBlueFort()) || this.collide(getMap().getRedFort())) {
			setPosition(position);
			return;
		}
		
	}
	public void shot() {
//		System.out.println(getHP());
		if(!isInvulnerable()) {
			Main.playWav("fire.wav");
			setHP(getHP()-1);
		}
		if(getHP() == 0) {
			die();
		}
	}
	public void die() {
		Main.playWav("blast.wav");
		Position blast = new Position(getPosition().getRow(), getPosition().getCol());
		getMap().getBlasts().add(blast);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized(getMap().getBlasts()) {
					getMap().getBlasts().remove(blast);
				}
			}
			
		}).start();
		dead = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(REB_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				reborn();
			}
			
		}).start();;
	}
	public void reborn() {
		if(color == Color.RED) {
			setPosition(Map.RED_POINT);
			setDirection(Direction.UP);
		}else {
			setPosition(Map.BLUE_POINT);
			setDirection(Direction.DOWN);
		}
		setInvulnerable(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < INV_TIME; i+=100) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}						
					setDead(!isDead());
				}
				setDead(false);
				setInvulnerable(false);
			}
		}).start();
		setHP(MAX_HP);
		
		dead = false;
	}
	
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
}