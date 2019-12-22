package tankWar;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import tankWar.chuck.Fort;
import tankWar.chuck.Grass;
import tankWar.chuck.Tank;
import tankWar.chuck.Wall;

//import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Map extends JPanel {
	public enum Color {
		RED, BLUE
	}
	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	public static final double WIDTH = 30;
	public static final double HEIGHT = 32;
	public static final Position RED_FORT_POSITION = new Position(HEIGHT-Fort.HEIGHT-Wall.HEIGHT, WIDTH/2-Fort.WIDTH/2);
	public static final Position BLUE_FORT_POSITION = new Position(Wall.HEIGHT, WIDTH/2-Fort.WIDTH/2);
	public static final Position RED_POINT = new Position(HEIGHT-Tank.HEIGHT-Wall.HEIGHT, WIDTH/2+Fort.WIDTH/2+Wall.WIDTH);
	public static final Position BLUE_POINT = new Position(Wall.HEIGHT, WIDTH/2-Fort.WIDTH/2-Wall.WIDTH-Tank.WIDTH);
	public static final int FRESH = 40;
	public static final int FRESH_MAP = 30;

	
	private Tank redTank, blueTank;
	private Fort redFort, blueFort;
	private List<Wall> walls;
	private List<Grass> grass;
	private List<Bullet> bullets;
	private List<Position> booms;
	private List<Position> blasts;
	private boolean gameover;
	
	public String msg = "";
	
	
	public Map() {
		gameover = false;
		
		setRedTank(new Tank(this, Color.RED));
		setBlueTank(new Tank(this, Color.BLUE));
		
		setRedFort(new Fort(this, Color.RED));
		setBlueFort(new Fort(this, Color.BLUE));
		
		setWalls(new LinkedList<Wall>());
		setGrass(new LinkedList<Grass>());
		setBullets(new LinkedList<Bullet>());
		setBooms(new LinkedList<Position>());
		setBlasts(new LinkedList<Position>());
		
		for(int i = 0; i < WIDTH; i+=Wall.WIDTH) {
			getWalls().add(new Wall(this, new Position(0, i), false));
			getWalls().add(new Wall(this, new Position(HEIGHT-Wall.HEIGHT, i), false));
		}
		for(int i = 0; i < HEIGHT; i+=Wall.HEIGHT) {
			getWalls().add(new Wall(this, new Position(i, 0), false));
			getWalls().add(new Wall(this, new Position(i, WIDTH-Wall.WIDTH), false));
		}
		
		for(int i = (int) (2*Wall.HEIGHT); i < HEIGHT-2*Wall.HEIGHT; i+=Wall.HEIGHT) {
			if(i == HEIGHT/2 || i == HEIGHT/2-Wall.HEIGHT)
				continue;
			getGrass().add(new Grass(this, new Position(i, 3*Wall.HEIGHT)));
			getGrass().add(new Grass(this, new Position(i, WIDTH-4*Wall.WIDTH)));
		}
		for(int i = (int) (2*Wall.HEIGHT); i < HEIGHT-2*Wall.HEIGHT; i+=Wall.HEIGHT) {
			if(i == HEIGHT/2 || i == HEIGHT/2-Wall.HEIGHT)
				continue;
			if(i == 4*Wall.HEIGHT || i == HEIGHT-5*Wall.HEIGHT) {
				getWalls().add(new Wall(this, new Position(i, 2*Wall.HEIGHT), false));
				getWalls().add(new Wall(this, new Position(i, 4*Wall.HEIGHT), false));
				getWalls().add(new Wall(this, new Position(i, WIDTH-3*Wall.WIDTH), false));
				getWalls().add(new Wall(this, new Position(i, WIDTH-5*Wall.WIDTH), false));
			}else {				
				getWalls().add(new Wall(this, new Position(i, 2*Wall.HEIGHT), true));
				getWalls().add(new Wall(this, new Position(i, 4*Wall.HEIGHT), true));
				getWalls().add(new Wall(this, new Position(i, WIDTH-3*Wall.WIDTH), true));
				getWalls().add(new Wall(this, new Position(i, WIDTH-5*Wall.WIDTH), true));
			}
		}
		
		getWalls().add(new Wall(this, new Position(1*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(2*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH), true));
//		getWalls().add(new Wall(this, new Position(4*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(5*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(HEIGHT-6*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH), true));
//		getWalls().add(new Wall(this, new Position(HEIGHT-5*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(HEIGHT-3*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(HEIGHT-2*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH), true));
		
		getWalls().add(new Wall(this, new Position(1*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(2*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH), true));
//		getWalls().add(new Wall(this, new Position(4*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(5*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(HEIGHT-6*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH), true));
//		getWalls().add(new Wall(this, new Position(HEIGHT-5*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(HEIGHT-3*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH), true));
		getWalls().add(new Wall(this, new Position(HEIGHT-2*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH), true));
		
		getWalls().add(new Wall(this, new Position(2*Wall.HEIGHT, RED_FORT_POSITION.getCol()), true));
//		getWalls().add(new Wall(this, new Position(4*Wall.HEIGHT, RED_FORT_POSITION.getCol()), false));
		getWalls().add(new Wall(this, new Position(5*Wall.HEIGHT, RED_FORT_POSITION.getCol()), false));
		getGrass().add(new Grass(this, new Position(HEIGHT/2-2*Wall.HEIGHT, RED_FORT_POSITION.getCol())));
		getWalls().add(new Wall(this, new Position(HEIGHT/2-Wall.HEIGHT, RED_FORT_POSITION.getCol()), false));
		getWalls().add(new Wall(this, new Position(HEIGHT/2, RED_FORT_POSITION.getCol()), false));
		getGrass().add(new Grass(this, new Position(HEIGHT/2+Wall.HEIGHT, RED_FORT_POSITION.getCol())));
		getWalls().add(new Wall(this, new Position(HEIGHT-6*Wall.HEIGHT, RED_FORT_POSITION.getCol()), false));
//		getWalls().add(new Wall(this, new Position(HEIGHT-5*Wall.HEIGHT, RED_FORT_POSITION.getCol()), false));
		getWalls().add(new Wall(this, new Position(HEIGHT-3*Wall.HEIGHT, RED_FORT_POSITION.getCol()), true));
		
		getGrass().add(new Grass(this, new Position(HEIGHT/2-2*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH)));
		getGrass().add(new Grass(this, new Position(HEIGHT/2-1*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH)));
		getGrass().add(new Grass(this, new Position(HEIGHT/2-0*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH)));
		getGrass().add(new Grass(this, new Position(HEIGHT/2+1*Wall.HEIGHT, RED_FORT_POSITION.getCol()-Wall.WIDTH)));
		
		getGrass().add(new Grass(this, new Position(HEIGHT/2-2*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH)));
		getGrass().add(new Grass(this, new Position(HEIGHT/2-1*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH)));
		getGrass().add(new Grass(this, new Position(HEIGHT/2-0*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH)));
		getGrass().add(new Grass(this, new Position(HEIGHT/2+1*Wall.HEIGHT, RED_FORT_POSITION.getCol()+Wall.WIDTH)));
		
	}
	
	public void gameOver(String msg) {
		this.msg = msg;
		gameover = true;
	}
	
	public void run() {
		while(!gameover) {
			try {
				Thread.sleep(FRESH_MAP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		
		this.setBackground(java.awt.Color.BLACK);
		
		Image img;
		synchronized(walls) {
			for(Wall wall: walls) {
				if(!wall.isBreakable())
					img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\steels.gif").getAbsolutePath());
				else
					img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\walls.gif").getAbsolutePath());
				g.drawImage(img, 
						   (int) (wall.getPosition().getCol() * Main.SQURE),
						   (int) (wall.getPosition().getRow() * Main.SQURE),
						   (int) (wall.getWidth() * Main.SQURE),
						   (int) (wall.getHeight() * Main.SQURE), this);
			}
		}
		
		img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\star.png").getAbsolutePath());
		g.drawImage(img,
				   (int) (redFort.getPosition().getCol() * Main.SQURE),
				   (int) (redFort.getPosition().getRow() * Main.SQURE), 
				   (int) (redFort.getWidth() * Main.SQURE),
				   (int) (redFort.getHeight() * Main.SQURE),
				   this);
		
		g.drawImage(img,
				   (int) (blueFort.getPosition().getCol() * Main.SQURE),
				   (int) (blueFort.getPosition().getRow() * Main.SQURE),
				   (int) (blueFort.getWidth() * Main.SQURE),
				   (int) (blueFort.getHeight() * Main.SQURE),
				   this);
		
		Direction direction = redTank.getDirection();
		if(direction == Direction.UP) {
			img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\p1tankU.gif").getAbsolutePath());
		}else if(direction == Direction.DOWN) {
			img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\p1tankD.gif").getAbsolutePath());
		}else if(direction == Direction.LEFT) {
			img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\p1tankL.gif").getAbsolutePath());
		}else if(direction == Direction.RIGHT) {
			img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\p1tankR.gif").getAbsolutePath());
		}
		if(!redTank.isDead())
			g.drawImage(img,
				   (int) (redTank.getPosition().getCol() * Main.SQURE),
				   (int) (redTank.getPosition().getRow() * Main.SQURE), 
				   (int) (redTank.getWidth() * Main.SQURE),
				   (int) (redTank.getHeight() * Main.SQURE),
				   this);
		
		
		direction = blueTank.getDirection();
		if(direction == Direction.UP) {
			img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\p2tankU.gif").getAbsolutePath());
		}else if(direction == Direction.DOWN) {
			img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\p2tankD.gif").getAbsolutePath());
		}else if(direction == Direction.LEFT) {
			img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\p2tankL.gif").getAbsolutePath());
		}else if(direction == Direction.RIGHT) {
			img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\p2tankR.gif").getAbsolutePath());
		}
		if(!blueTank.isDead())
			g.drawImage(img,
				   (int) (blueTank.getPosition().getCol() * Main.SQURE),
				   (int) (blueTank.getPosition().getRow() * Main.SQURE), 
				   (int) (blueTank.getWidth() * Main.SQURE),
				   (int) (blueTank.getHeight() * Main.SQURE),
				   this);

		g.setColor(java.awt.Color.WHITE);
		synchronized(bullets) {
			for(Bullet bullet: bullets) {
				g.fillOval((int) (bullet.getPosition().getCol() * Main.SQURE),
						(int) (bullet.getPosition().getRow() * Main.SQURE), 5, 5);
			}
		}
		
		img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\born1.gif").getAbsolutePath());
		synchronized(booms) {
			for(Position boom: booms) {
				g.drawImage(img, (int) (boom.getCol() * Main.SQURE) - Main.SQURE,
						(int) (boom.getRow() * Main.SQURE) - Main.SQURE, 2*Main.SQURE, 2*Main.SQURE, this);
			}
		}
		img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\blast8.gif").getAbsolutePath());
		synchronized(blasts) {
			for(Position blast: blasts) {
				g.drawImage(img, (int) (blast.getCol() * Main.SQURE),
						(int) (blast.getRow() * Main.SQURE), 2*Main.SQURE, 2*Main.SQURE, this);
			}
		}
		
		img = Toolkit.getDefaultToolkit().getImage(new File("src\\img\\grass.png").getAbsolutePath());
		synchronized(grass) {
			for(Grass gr: grass) {
				g.drawImage(img,
						   (int) (gr.getPosition().getCol() * Main.SQURE),
						   (int) (gr.getPosition().getRow() * Main.SQURE),
						   (int) (gr.getWidth() * Main.SQURE),
						   (int) (gr.getHeight() * Main.SQURE),
						   this);
			}
		}
	}
	

	
	public List<Wall> getWalls() {
		return walls;
	}
	public void setWalls(List<Wall> walls) {
		this.walls = walls;
	}
	public Fort getRedFort() {
		return redFort;
	}
	public void setRedFort(Fort redFort) {
		this.redFort = redFort;
	}
	public Fort getBlueFort() {
		return blueFort;
	}
	public void setBlueFort(Fort blueFort) {
		this.blueFort = blueFort;
	}

	public Tank getRedTank() {
		return redTank;
	}

	public void setRedTank(Tank redTank) {
		this.redTank = redTank;
	}

	public Tank getBlueTank() {
		return blueTank;
	}

	public void setBlueTank(Tank blueTank) {
		this.blueTank = blueTank;
	}

	public List<Grass> getGrass() {
		return grass;
	}

	public void setGrass(List<Grass> grass) {
		this.grass = grass;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}

	public List<Position> getBooms() {
		return booms;
	}

	public void setBooms(List<Position> booms) {
		this.booms = booms;
	}

	public List<Position> getBlasts() {
		return blasts;
	}

	public void setBlasts(List<Position> blasts) {
		this.blasts = blasts;
	}


}
