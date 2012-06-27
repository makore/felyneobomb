package ua.felyne.game.shared;

public class ObjectManager {
	private static Level _level;
	
	public static Bomber createBomber(int playerId) {
		int x = 0, y = 0, w = 0, h = 0;
		h = (int) (SpriteSheet.BLOCK_SIZE / (float) SpriteSheet.Bomber1.W
				* SpriteSheet.Bomber1.H);
		w = SpriteSheet.BLOCK_SIZE;
		if (playerId == 0) {
			x = SpriteSheet.BLOCK_SIZE;
			y = SpriteSheet.BLOCK_SIZE - (h - SpriteSheet.BLOCK_SIZE);
		}
		if (playerId == 1) {
			x = SpriteSheet.SCREEN_WIDTH - (SpriteSheet.BLOCK_SIZE * 2);
			y = SpriteSheet.BLOCK_SIZE - (h - SpriteSheet.BLOCK_SIZE);
		}
		if (playerId == 2) {
			x = SpriteSheet.BLOCK_SIZE;
			y = SpriteSheet.SCREEN_HEIGHT - SpriteSheet.BLOCK_SIZE - h;			
		}
		if (playerId == 3) {
			x = SpriteSheet.SCREEN_WIDTH - (SpriteSheet.BLOCK_SIZE * 2);
			y = SpriteSheet.SCREEN_HEIGHT - SpriteSheet.BLOCK_SIZE - h;			
		}
		
		return new Bomber(x, y, w, h, _level, playerId);
	}

	public static Level createLevel() {
		_level = LevelEditor.getLevel();
		return _level;
	}

	public static Bomb createBomb(int x, int y, int explosionLength, long bomberId) {
		return new Bomb(x, y, SpriteSheet.BLOCK_SIZE, explosionLength, bomberId);
	}
}