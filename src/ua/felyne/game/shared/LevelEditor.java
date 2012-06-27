package ua.felyne.game.shared;

import ua.felyne.game.shared.Block.BlockType;
import ua.felyne.game.shared.Item.ItemType;

public final class LevelEditor {
	public static final int TERRAIN = 0;
	public static final int WALL = 1;
	public static final int SOLID_BLOCK = 2;
	public static final int SOFT_BLOCK = 3;
	public static final int NONE = 0;
	public static final int BOMB = 1;
	public static final int SPEED = 2;
	public static final int FLAME = 3;

	private static final int[][] COLUMNS = {
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

	public static void genRandomMap(int[][] map, int[][] items) {
		for (int i = 0; i < SpriteSheet.Y_TILES; i++) {
			for (int j = 0; j < SpriteSheet.X_TILES; j++) {
				if (i == 0 || i == SpriteSheet.Y_TILES - 1 || j == 0
						|| j == SpriteSheet.X_TILES - 1) {
					map[i][j] = WALL;
				} else if (canPutBlock(map, i, j)) {
					if (choosePutSolidBlock(map, i, j)) {
						map[i][j] = SOLID_BLOCK;					
					} else if (choosePutSoftBlock()) {
						map[i][j] = SOFT_BLOCK;
						items[i][j] = choosePutItem();
					}
				}
			}
		}
	}
	
	private static boolean choosePutBlock(int chance) {
		java.util.Random r = new java.util.Random();
		return r.nextInt(100) < chance;		
	}
	
	private static boolean choosePutSolidBlock(int[][] map, int x, int y) {
		boolean decision = false;
		if (COLUMNS[x][y] == SOLID_BLOCK) {
			decision = choosePutBlock(100);
		}/* else {
			decision = choosePutBlock(5);
		}*/
		if (decision && numberOfSolidBlocksAround(map, x, y) < 2) {
			return true;
		}
		return false;
	}
	
	private static int numberOfSolidBlocksAround(int[][] map, int x, int y) {
		int solidBlocks = 0;
		if (map[x + 1][y] == SOLID_BLOCK) {
			solidBlocks++;
		}
		if (map[x + 1][y + 1] == SOLID_BLOCK) {
			solidBlocks++;
		}
		if (map[x + 1][y - 1] == SOLID_BLOCK) {
			solidBlocks++;
		}
		if (map[x - 1][y] == SOLID_BLOCK) {
			solidBlocks++;
		}
		if (map[x - 1][y + 1] == SOLID_BLOCK) {
			solidBlocks++;
		}
		if (map[x - 1][y - 1] == SOLID_BLOCK) {
			solidBlocks++;
		}
		if (map[x][y + 1] == SOLID_BLOCK) {
			solidBlocks++;
		}
		if (map[x][y - 1] == SOLID_BLOCK) {
			solidBlocks++;
		}
		return solidBlocks;
	}
	
	private static boolean choosePutSoftBlock() {
		return choosePutBlock(80);
	}

	private static int choosePutItem() {
		int item = 0;
		java.util.Random r = new java.util.Random();
		if (r.nextInt(10) > 5) {
			item = r.nextInt(4);			
		}
		return item;
	}
	
	private static boolean canPutBlock(int[][] map, int x, int y) {
		if (x == 1 && y == 1) {
			return false;
		} else if (x == 1 && y == 2) {
			return false;
		} else if (x == 2 && y == 1) {
			return false;
		} else if (x == 1 && y == SpriteSheet.X_TILES - 2) {
			return false;
		} else if (x == 1 && y == SpriteSheet.X_TILES - 3) {
			return false;
		} else if (x == 2 && y == SpriteSheet.X_TILES - 2) {
			return false;
		} else if (x == SpriteSheet.Y_TILES - 2 && y == 1) {
			return false;
		} else if (x == SpriteSheet.Y_TILES - 3 && y == 1) {
			return false;
		} else if (x == SpriteSheet.Y_TILES - 2 && y == 2) {
			return false;
		} else if (x == SpriteSheet.Y_TILES - 2 && y == SpriteSheet.X_TILES - 2) {
			return false;
		} else if (x == SpriteSheet.Y_TILES - 3 && y == SpriteSheet.X_TILES - 2) {
			return false;
		} else if (x == SpriteSheet.Y_TILES - 2 && y == SpriteSheet.X_TILES - 3) {
			return false;
		} else {
			return true;
		}
	}

	public static Level getLevel() {
		int[][] blockMatrix = new int[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		int[][] itemMatrix = new int[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		Level level = new Level();

		genRandomMap(blockMatrix, itemMatrix);

		Block[][] map = new Block[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		Item[][] items = new Item[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		int size = SpriteSheet.BLOCK_SIZE;
		for (int y = 0; y < SpriteSheet.Y_TILES; y++) { 
			for (int x = 0; x < SpriteSheet.X_TILES; x++) { 
				BlockType blockType = null;
				ItemType itemType = null;
				if (blockMatrix[y][x] == TERRAIN) {
					blockType = BlockType.TERRAIN;
				} else if (blockMatrix[y][x] == WALL) {
					blockType = BlockType.WALL;
				} else if (blockMatrix[y][x] == SOFT_BLOCK) {
					blockType = BlockType.SOFT_BLOCK;
				} else if (blockMatrix[y][x] == SOLID_BLOCK) {
					blockType = BlockType.SOLID_BLOCK;
				}
				map[y][x] = new Block(x * size, y * size, blockType, size, size);

				if (itemMatrix[y][x] == NONE) {
					itemType = ItemType.NONE;
				} else if (itemMatrix[y][x] == BOMB) {
					itemType = ItemType.BOMB;
				} else if (itemMatrix[y][x] == SPEED) {
					itemType = ItemType.SPEED;
				} else if (itemMatrix[y][x] == FLAME) {
					itemType = ItemType.FLAME;
				}
				items[y][x] = new Item(x * size, y * size, itemType, size, size);
			}
		}
		level.setBlocks(map);
		level.setItems(items);
		return level;
	}
}