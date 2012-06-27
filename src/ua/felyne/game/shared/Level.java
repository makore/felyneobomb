package ua.felyne.game.shared;

import java.util.LinkedList;
import ua.felyne.game.shared.Block.BlockType;
import ua.felyne.game.shared.agwtapi.event.IEvents.GameAction;

public class Level extends Sprite {
	private Item[][] _items;
	private Block[][] _map; // width/blockSize
	private boolean[][] _placedBombs;
	private LinkedList<Bomb> _firedBombs;
	private boolean[][] _flames;
	private static final int DISTANCE_FROM_THE_CORNER = 28;
	private boolean _redraw;

	public Level() {
		super(0, 0, 0, 0);
		_placedBombs = new boolean[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		_firedBombs = new LinkedList<Bomb>();
		_flames = new boolean[SpriteSheet.Y_TILES][SpriteSheet.X_TILES];
		_redraw = false;
	}
	
	public void setFiredBombs(LinkedList<Bomb> firedBombs, boolean[][] placedBombs) {
		_firedBombs = firedBombs;
		_placedBombs = placedBombs;
	}

	public Item[][] getItems() {
		return _items;
	}

	public Item getItem(int xCell, int yCell) {
		if (xCell < 0 || yCell < 0 || xCell >= SpriteSheet.X_TILES || yCell >= SpriteSheet.Y_TILES) {
			return null;
		}
		return _items[yCell][xCell];
	}
	
	public void setItems(Item[][] items) {
		this._items = items;
	}

	public Block[][] getBlocks() {
		return _map;
	}
	
	public boolean[][] getFlames() {
		return _flames;
	}

	public void setFlames(boolean[][] flames) {
		_flames = flames;
	}
	
	public void setBlocks(Block[][] blocks) {
		this._map = blocks;
	}
	
	public LinkedList<Bomb> getFiredBombs() {
		return _firedBombs;
	}	
	
	public boolean thereIsABomb(int xCell, int yCell) {
		return _placedBombs[yCell][xCell];
	}
	
	public void setBomb(Bomb bomb) {
		int[] coords = getMapCell(bomb);		
		_placedBombs[coords[1]][coords[0]] = true;
		_firedBombs.add(bomb);
	}
	
	public void unsetBomb(Bomb bomb) {
		int[] coords = getMapCell(bomb);
		_placedBombs[coords[1]][coords[0]] = false;
		_firedBombs.remove();
	}
	
	public int getUsedBombsBy(Bomber bomber) {
		int used = 0;
		for (Bomb bomb : _firedBombs) {
			if (bomb.thrownBy() == bomber.getPlayerId()) {
				used++;
			}
		}
		return used;
	}
	
	public boolean thereIsAFlame(int xCell, int yCell) {
		if (xCell < 0 || yCell < 0 || xCell >= SpriteSheet.X_TILES || yCell >= SpriteSheet.Y_TILES) {
			return false;
		}
		return _flames[yCell][xCell];
	}
	
	public void setFlame(int xCell, int yCell) {
		if (xCell < 0 || yCell < 0 || xCell >= SpriteSheet.X_TILES || yCell >= SpriteSheet.Y_TILES) {
			return;
		}
		_flames[yCell][xCell] = true;		
	}
	
	public void unsetFlame(int xCell, int yCell) {
		if (xCell < 0 || yCell < 0 || xCell >= SpriteSheet.X_TILES || yCell >= SpriteSheet.Y_TILES) {
			return;
		}
		_flames[yCell][xCell] = false;		
	}
	
	public Block getBlock(int xCell, int yCell) {
		if (xCell < 0 || yCell < 0 || xCell >= SpriteSheet.X_TILES || yCell >= SpriteSheet.Y_TILES) {
			return null;
		}
		return _map[yCell][xCell];
	}
	
	public boolean thereIsAItem(int xCell, int yCell) {
		if (xCell < 0 || yCell < 0 || xCell >= SpriteSheet.X_TILES || yCell >= SpriteSheet.Y_TILES) {
			return false;
		}
		return _items[yCell][xCell].isVisible();
	}	
	
	private Block getObstacle(int x, int y, boolean isVerticalMovement, boolean[] canBeRounded) {
		int xCellLeft = x / SpriteSheet.BLOCK_SIZE;
		int yCellUp = y / SpriteSheet.BLOCK_SIZE;
		int xCellRight = (x + SpriteSheet.BLOCK_SIZE - 1) / SpriteSheet.BLOCK_SIZE;
		int yCellDown = (y + SpriteSheet.BLOCK_SIZE - 1) / SpriteSheet.BLOCK_SIZE;
		
		canBeRounded[0] = false;		
		Block obstacle = _map[yCellUp][xCellLeft]; 
		boolean bomb = false;
		bomb = thereIsABomb(xCellLeft, yCellUp);
		if (obstacle.getType() == BlockType.TERRAIN) {
			if (isVerticalMovement) {
				obstacle = _map[yCellUp][xCellRight];
			} else {
				obstacle = _map[yCellDown][xCellLeft];
			}
			if (obstacle.getType() == BlockType.TERRAIN) {
				return null;
			} else {
				if (!bomb) canBeRounded[0] = true;
			}			
		} else {
			Block otherObstacle = null;
			if (isVerticalMovement) {
				otherObstacle = _map[yCellUp][xCellRight];
				bomb = thereIsABomb(xCellRight, yCellUp);
			} else {
				otherObstacle = _map[yCellDown][xCellLeft];
				bomb = thereIsABomb(xCellLeft, yCellDown);
			}
			if (otherObstacle.getType() == BlockType.TERRAIN) {
				if (!bomb) canBeRounded[0] = true;
			}
		}
		return obstacle;
	}

	/*
	 * Calcula la separacion entre el objeto y la pared. Lo hacemos por si el
	 * desplazamiento del objeto en un frame es mayor que la separacion hasta la
	 * pared. Si fuera así el desplazamiento seria el de la separacion a la
	 * pared y no el del desplazamiento previamente calculado.
	 */
	public int gap(Bomber bomber, int stepX, int stepY, boolean[] canBeRounded) {
		int x = bomber.getX();
		int y = bomber.getY();
		int newX = x + stepX;
		int newY = y + stepY;
		int w = bomber.getWidth();
		int h = bomber.getHeight();
		int realY = y + (h - SpriteSheet.BLOCK_SIZE);
		int realNewY = newY + (h - SpriteSheet.BLOCK_SIZE);
		int gap = Integer.MAX_VALUE;
		int[] cellCoords = getMapCell(bomber);
		if (x == newX) {
			if (newY > y) {
				// hacia abajo
				// distancia a la pared
				Block obstacle = getObstacle(newX, newY + h + SpriteSheet.BLOCK_SIZE / 2, stepY != 0, canBeRounded);
				int yFootBomber = y + h;
				if (obstacle != null) {
					if (overlapWithBlock(bomber, obstacle)) {
						gap = 0;
					} else {
						gap = Math.abs(obstacle.getY() - yFootBomber);
					}
				} else {
					// distancia a la bomba
					boolean thereIsABombNear = thereIsABomb(cellCoords[0], cellCoords[1] + 1);
					if (!overlapWithBomb(bomber) && thereIsABombNear) {
						obstacle = getBlock(cellCoords[0], cellCoords[1] + 1);
						gap = Math.abs(obstacle.getY() - yFootBomber);							
					} else {
						// comprobar item
						boolean thereIsAItemNear = thereIsAItem(cellCoords[0], cellCoords[1] + 1);
						if (thereIsAItemNear) {
							Item item = getItem(cellCoords[0], cellCoords[1] + 1);
							if (overlapWithItem(bomber, item)) {
								bomber.increaseWeapons(item);
								item.hide();
								setRedraw(true);
							}
						}
					}
				}
			} else {
				// hacia arriba
				Block obstacle = getObstacle(newX, realNewY - SpriteSheet.BLOCK_SIZE / 2, stepY != 0, canBeRounded);
				if (obstacle != null) {
					if (overlapWithBlock(bomber, obstacle)) {
						gap = 0;
					} else {
						int yBaseBlock = obstacle.getY() + obstacle.getHeight();
						gap = Math.abs(realY - yBaseBlock);
					}
				} else {
					boolean thereIsABombNear = thereIsABomb(cellCoords[0], cellCoords[1] - 1);
					if (!overlapWithBomb(bomber) && thereIsABombNear) {
						obstacle = getBlock(cellCoords[0], cellCoords[1] - 1);
						int yBaseBlock = obstacle.getY() + obstacle.getHeight();
						gap = Math.abs(realY - yBaseBlock);						
					} else {
						boolean thereIsAItemNear = thereIsAItem(cellCoords[0], cellCoords[1] - 1);
						if (thereIsAItemNear) {
							Item item = getItem(cellCoords[0], cellCoords[1] - 1);
							if (overlapWithItem(bomber, item)) {
								bomber.increaseWeapons(item);
								item.hide();
								setRedraw(true);
							}
						}
					}
				}
			}
		} else {
			if (newX > x) {
				// hacia la derecha
				Block obstacle = getObstacle(newX + w + SpriteSheet.BLOCK_SIZE / 2, realNewY, stepY != 0, canBeRounded);
				int xBomber = x + w;
				if (obstacle != null) {
					if (overlapWithBlock(bomber, obstacle)) {
						gap = 0;
					} else {
						gap = Math.abs(obstacle.getX() - xBomber);
					}
				} else {
					boolean thereIsABombNear = thereIsABomb(cellCoords[0] + 1, cellCoords[1]);
					if (!overlapWithBomb(bomber) && thereIsABombNear) {
						obstacle = getBlock(cellCoords[0] + 1, cellCoords[1]);
						gap = Math.abs(obstacle.getX() - xBomber);						
					} else {
						boolean thereIsAItemNear = thereIsAItem(cellCoords[0] + 1, cellCoords[1]);
						if (thereIsAItemNear) {
							Item item = getItem(cellCoords[0] + 1, cellCoords[1]);
							if (overlapWithItem(bomber, item)) {
								bomber.increaseWeapons(item);				
								item.hide();
								setRedraw(true);
							}
						}
					}						
				}
			} else {
				// hacia la izquierda
				Block obstacle = getObstacle(newX - SpriteSheet.BLOCK_SIZE / 2, realNewY, stepY != 0, canBeRounded);
				if (obstacle != null) {
					if (overlapWithBlock(bomber, obstacle)) {
						gap = 0;
					} else {
						int xBlock = obstacle.getX() + obstacle.getWidth();
						gap = Math.abs(x - xBlock);
					}
				} else {
					boolean thereIsABombNear = thereIsABomb(cellCoords[0] - 1, cellCoords[1]);
					if (!overlapWithBomb(bomber) && thereIsABombNear) {
						obstacle = getBlock(cellCoords[0] - 1, cellCoords[1]);
						int xBlock = obstacle.getX() + obstacle.getWidth();
						gap = Math.abs(x - xBlock);						
					} else {
						boolean thereIsAItemNear = thereIsAItem(cellCoords[0] - 1, cellCoords[1]);
						if (thereIsAItemNear) {
							Item item = getItem(cellCoords[0] - 1, cellCoords[1]);
							if (overlapWithItem(bomber, item)) {
								bomber.increaseWeapons(item);
								item.hide();
								setRedraw(true);
							}
						}
					} 
				}
			}
		}
		return gap;	
	}
	
	public int gap(Bomber bomber, GameAction action) {
		int x = 0, y = 0;
		boolean[] c = new boolean[1];
		if (action == GameAction.UP) {
			x = 0; y = -1;
		} else if (action == GameAction.DOWN) {
			x = 0; y = 1;
		} else if (action == GameAction.LEFT) {
			x = -1; y = 0;
		} else if (action == GameAction.RIGHT) {
			x = 1; y = 0;
		}
		int gap = gap(bomber, x, y, c);
		if (gap > 0) gap--; 
		return gap;
	}

	/**
	 * Calcula la nueva posición del bomber cuando pasa por la esquina de un
	 * bloque. El radio de acci�n de la esquina será de 7 píxeles.
	 * 
	 * @param Bomber
	 * @param Desplazamiento
	 *            del personaje en las X. Valor positivo si el desplazamiento es
	 *            en dirección a la derecha, y negativo si es a la izquierda.
	 * @param Desplazamiento
	 *            del personaje en las X. Valor positivo si el desplazamiento es
	 *            en dirección a la derecha, y negativo si es a la izquierda.
	 * @return Array de tamaño 2 con la nueva posición, el primer elemento
	 *         corresponde a la X, y el segundo a la Y.
	 */
	public int[] roundTheCorner(Bomber bomber, int stepX, int stepY) {
		int x = bomber.getX();
		int y = bomber.getY();
		int[] roundedPosition = { x, y };
		int distance = getDistanceFromTheCorner(bomber, stepX, stepY);
		if (Math.abs(distance) > 0
				&& Math.abs(distance) < DISTANCE_FROM_THE_CORNER) {
			int displacement = 0;
			if (Math.abs(distance) >= Math
					.max(Math.abs(stepX), Math.abs(stepY))) {
				if (distance > 0) {
					displacement = Math.max(Math.abs(stepX), Math.abs(stepY));
				} else {
					displacement = -(Math.max(Math.abs(stepX), Math.abs(stepY)));
				}
			} else {
				displacement = distance;
			}

			if (stepX > 0) {
				roundedPosition[0] += stepX;
				roundedPosition[1] += displacement;
			} else if (stepX < 0) {
				roundedPosition[0] += stepX;
				roundedPosition[1] += displacement;
			} else if (stepY > 0) {
				roundedPosition[0] += displacement;
				roundedPosition[1] += stepY;
			} else if (stepY < 0) {
				roundedPosition[0] += displacement;
				roundedPosition[1] += stepY;
			}
		}
		return roundedPosition;
	}

	private int getDistanceFromTheCorner(Bomber bomber, int stepX, int stepY) {
		int x = bomber.getX();
		int y = bomber.getY();
		int newX = x + stepX;
		int newY = y + stepY;
		int w = bomber.getWidth();
		int h = bomber.getHeight();
		int realY = y + (h - SpriteSheet.BLOCK_SIZE);
		int realNewY = newY + (h - SpriteSheet.BLOCK_SIZE);
		int distanceFromTheCorner = 0;
		boolean[] canBeRounded = new boolean[1];
		Block block = null;
		if (stepX == 0) {
			if (stepY > 0) {
				block = getObstacle(newX, newY + h, stepY != 0, canBeRounded);
			} else {
				block = getObstacle(newX, realNewY, stepY != 0, canBeRounded);
			}
			if (block == null || canBeRounded[0] == false) {
				distanceFromTheCorner = Integer.MAX_VALUE;
			} else if (block.getX() < x) {
				distanceFromTheCorner = block.getX() + block.getWidth() - x;
			} else {
				distanceFromTheCorner = -(x + w - block.getX());
			}
		} else {
			if (stepX > 0) {
				block = getObstacle(newX + w, realNewY, stepY != 0, canBeRounded);
			} else {
				block = getObstacle(newX, realNewY, stepY != 0, canBeRounded);
			}
			if (block == null || canBeRounded[0] == false) {
				distanceFromTheCorner = Integer.MAX_VALUE;
			} else if (block.getY() < realY) {
				distanceFromTheCorner = block.getY() + block.getHeight()
						- realY;
			} else {
				distanceFromTheCorner = -(y + h - block.getY());
			}
		}
		return distanceFromTheCorner;
	}

	private boolean overlapWithBlock(Bomber bomber, Block block) {
		int diff1 = Math.abs(block.getX() - bomber.getX());
		int diff2 = Math .abs(block.getY() 
				- (bomber.getY() + (bomber.getHeight() - SpriteSheet.BLOCK_SIZE)));
		if (diff1 < block.getWidth() && diff2 < block.getHeight()) {
			return true;
		}
		return false;
	}

	private boolean overlapWithBomb(Bomber bomber) {
		int[] cellCoords = getMapCell(bomber);
		return thereIsABomb(cellCoords[0], cellCoords[1]);
	}

	private boolean overlapWithItem(Bomber bomber, Item item) {
		int diff1 = Math.abs(item.getX() - bomber.getX());
		int diff2 = Math .abs(item.getY() 
				- (bomber.getY() + (bomber.getHeight() - SpriteSheet.BLOCK_SIZE)));
		if (diff1 < item.getWidth() && diff2 < item.getHeight()) {
			return true;
		}
		return false;
	}

	public boolean overlapWithExplodedBomb(Bomber bomber) {
		int x = bomber.getXBoundingBox();
		int y = bomber.getYBoundingBox();
		int w = bomber.getWBoundingBox();
		int h = bomber.getHBoundingBox();
		
		int[] c = getMapCell(bomber);
		boolean overlap = false;
		
		if (thereIsAFlame(c[0], c[1])) {
			Block b = getBlock(c[0], c[1]);
			int d1 = Math.abs(b.getX() - x);
			int d2 = Math.abs(b.getY() - y);
			if (d1 < w && d2 < h) {
				overlap = true;
			}			
		} else if (thereIsAFlame(c[0], c[1] + 1)) {
			Block b = getBlock(c[0], c[1] + 1);
			int d = Math.abs(b.getY() - y);
			if (d < h) {
				overlap = true;
			}			
		} else if (thereIsAFlame(c[0], c[1] - 1)) {
			Block b = getBlock(c[0], c[1] - 1);
			int d = Math.abs(b.getY() - y);
			if (d < h) {
				overlap = true;
			}						
		} else if (thereIsAFlame(c[0] + 1, c[1])) {
			Block b = getBlock(c[0] + 1, c[1]);
			int d = b.getX() - (x + w);
			if (d < 0) {
				overlap = true;
			}			
		} else if (thereIsAFlame(c[0] - 1, c[1])) {
			Block b = getBlock(c[0] - 1, c[1]);
			int d = x - (b.getX() + w);
			if (d < 0) {
				overlap = true;
			}			
		}
		if (overlap) {
			bomber.die();
		}
		return overlap;
	}
	
	public int[] getMapCell(Bomber bomber) {
		int[] coords = new int[2];
		
		int x = 0;
		int y = (bomber.getY() + SpriteSheet.BLOCK_SIZE) / SpriteSheet.BLOCK_SIZE;

		if (bomber.getLastMoveAction() == GameAction.LEFT) {
			x = (bomber.getX() + (SpriteSheet.BLOCK_SIZE / 2) - (SpriteSheet.BLOCK_SIZE / 4)) / SpriteSheet.BLOCK_SIZE;
		} else if (bomber.getLastMoveAction() == GameAction.RIGHT) {
			x = (bomber.getX() + (SpriteSheet.BLOCK_SIZE / 2) + (SpriteSheet.BLOCK_SIZE / 4)) / SpriteSheet.BLOCK_SIZE;
		} else {			
			x = (bomber.getX() + (SpriteSheet.BLOCK_SIZE / 2)) / SpriteSheet.BLOCK_SIZE;
		}
		
		coords[0] = x;
		coords[1] = y;
		
		return coords;
	}
	
	public int[] getMapCell(Bomb bomb) {
		int[] coords = new int[2];
		int x = bomb.getX() / SpriteSheet.BLOCK_SIZE;
		int y = bomb.getY() / SpriteSheet.BLOCK_SIZE;
		coords[0] = x;
		coords[1] = y;		
		return coords;
	}
	
	public boolean canDrawExplosion(int xCell, int yCell) {
		return _map[yCell][xCell].getType() == BlockType.SOFT_BLOCK || 
				_map[yCell][xCell].getType() == BlockType.TERRAIN;
	}	
	
	public boolean needRedraw() {
		return _redraw;
	}
	
	public void setRedraw(boolean redraw) {
		_redraw = redraw;
	}
}