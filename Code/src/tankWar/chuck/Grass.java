package tankWar.chuck;

import tankWar.Chuck;
import tankWar.Map;
import tankWar.Position;

public class Grass extends Chuck {
	
	public static final double HEIGHT = 2;
	public static final double WIDTH = 2;

	public Grass(Map map, Position position) {
		super(map, position, HEIGHT, WIDTH);
	}
}
