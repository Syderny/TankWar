package tankWar.chuck;

import tankWar.Chuck;
import tankWar.Main;
import tankWar.Map;
import tankWar.Position;
import tankWar.Target;

public class Wall extends Chuck implements Target {
	public static final double HEIGHT = 2;
	public static final double WIDTH = 2;

	
	private boolean breakable;
	

	public boolean isBreakable() {
		return breakable;
	}
	public void setBreakable(boolean breakable) {
		this.breakable = breakable;
	}
	
	public Wall(Map map, Position position, boolean breakable) {
		super(map, position, HEIGHT, WIDTH);
		setBreakable(breakable);
	}
	
	@Override
	public void shot() {
		Main.playWav("hit.wav");
		die();
	}
	@Override
	public void die() {
		if(isBreakable()) {
			getMap().getWalls().remove(this);
		}		
	}
}
