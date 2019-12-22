package tankWar.chuck;

import tankWar.Chuck;
import tankWar.Main;
import tankWar.Map;
import tankWar.Position;
import tankWar.Map.Color;

public class Fort extends Chuck {
	public static final double WIDTH = 2;
	public static final double HEIGHT = 2;
	public static final int MAX_HP = 5;

	
	private Color color;
	private int HP;
	
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int hP) {
		HP = hP;
	}
	
	public void shot() {
//		System.out.println("fuck");
		Main.playWav("fire.wav");
		setHP(getHP()-1);
		if(getHP() == 0) {
			die();
		}
	}
	private void die() {
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
		
		String msg;
		Main.playWav("blast.wav");
		if(color == Color.RED)
			msg = "Player2 win!";
		else
			msg = "Player1 win!";
		getMap().gameOver(msg);
	}
	
	public Fort(Map map, Color color) {
		super(map, null, HEIGHT, WIDTH);
		setColor(color);
		setHP(MAX_HP);
		if(color == Color.RED) {
			setPosition(Map.RED_FORT_POSITION);
		}else {
			setPosition(Map.BLUE_FORT_POSITION);
		}
	}
}
