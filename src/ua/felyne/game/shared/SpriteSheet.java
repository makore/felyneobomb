package ua.felyne.game.shared;

public class SpriteSheet {
	/*
	 * Screen: 720x528 Blocks: 48x48 Tiles: 15x11
	 */

	public static final int SCREEN_WIDTH = 720;
	public static final int SCREEN_HEIGHT = 528;
	public static final int BLOCK_SIZE = 48;
	public static final int X_TILES = 15;
	public static final int Y_TILES = 11;

	public static final int IMG_BOMBER1 = 0;
	public static final int IMG_BOMBS = 1;
	public static final int IMG_FLAMES = 2;
	public static final int IMG_ITEMS = 3;
	public static final int IMG_DEAD1 = 4;
	public static final int IMG_BG1 = 5;
	
	public class Bg1 {
		public static final int WALL_X = 0;
		public static final int WALL_Y = 0;
		public static final int SOLID_BLOCK_X = 48;
		public static final int SOLID_BLOCK_Y = 0;
		public static final int SOFT_BLOCK_X = 96;
		public static final int SOFT_BLOCK_Y = 0;
		public static final int TERRAIN_X = 144;
		public static final int TERRAIN_Y = 0;
	}

	public class Bomber1 {
		public static final int Y = 0;
		public static final int H = 115;
		public static final int W = 80;
		public static final int BACKWARDS_QUIET_X = 0;
		public static final int BACKWARDS_WALK1_X = 80;
		public static final int BACKWARDS_WALK2_X = 160;
		public static final int SIDED_RIGHT_QUIET_X = 240;
		public static final int SIDED_RIGHT_WALK1_X = 320;
		public static final int SIDED_RIGHT_WALK2_X = 400;
		public static final int FRONT_QUIET_X = 480;
		public static final int FRONT_WALK1_X = 560;
		public static final int FRONT_WALK2_X = 640;
		public static final int SIDED_LEFT_QUIET_X = 720;
		public static final int SIDED_LEFT_WALK1_X = 800;
		public static final int SIDED_LEFT_WALK2_X = 880;
	}
	
	public class Bombs {
		public static final int Y = 0;
		public static final int H = 48;
		public static final int W = 48;
		public static final int STATE_1_X = 0;
		public static final int STATE_2_X = 48;
		public static final int STATE_3_X = 96;
		public static final int KICK_STATE_1_X = 144;
		public static final int KICK_STATE_2_X = 192;
	}
	
	
	public class Items {
		public static final int H = 16;
		public static final int W = 16;
		public static final int BOMB_MARKED_X = 0;
		public static final int BOMB_MARKED_Y = 0;
		public static final int BOMB_X = 0;
		public static final int BOMB_Y = 16;
		public static final int FLAME_MARKED_X = 16;
		public static final int FLAME_MARKED_Y = 0;
		public static final int FLAME_X = 16;
		public static final int FLAME_Y = 16;
		public static final int SPEED_MARKED_X = 16;
		public static final int SPEED_MARKED_Y = 32;
		public static final int SPEED_X = 16;
		public static final int SPEED_Y = 48;
	}
	
	public class Flames {
		public static final int H = 72;
		public static final int W = 72;
		
		public static final int T1_LEFT_TOP_X = 0;
		public static final int T1_LEFT_TOP_Y = 0;
		public static final int T1_LEFT_MIDDLE_X = 72;
		public static final int T1_LEFT_MIDDLE_Y = 0;
		public static final int T1_CENTER_X = 144;
		public static final int T1_CENTER_Y = 0;
		public static final int T1_RIGHT_TOP_X = 0;
		public static final int T1_RIGHT_TOP_Y = 72;
		public static final int T1_RIGHT_MIDDLE_X = 72;
		public static final int T1_RIGHT_MIDDLE_Y = 72;
		public static final int T1_UP_TOP_X = 0;
		public static final int T1_UP_TOP_Y = 72 * 2;
		public static final int T1_UP_MIDDLE_X = 72;
		public static final int T1_UP_MIDDLE_Y = 72 * 2;
		public static final int T1_DOWN_TOP_X = 0;
		public static final int T1_DOWN_TOP_Y = 72 * 3;
		public static final int T1_DOWN_MIDDLE_X = 72;
		public static final int T1_DOWN_MIDDLE_Y = 72 * 3;
		
		public static final int T2_LEFT_TOP_X = 0;
		public static final int T2_LEFT_TOP_Y = 72 * 4;
		public static final int T2_LEFT_MIDDLE_X = 72;
		public static final int T2_LEFT_MIDDLE_Y = 72 * 4;
		public static final int T2_CENTER_X = 144;
		public static final int T2_CENTER_Y = 72 * 4;
		public static final int T2_RIGHT_TOP_X = 0;
		public static final int T2_RIGHT_TOP_Y = 72 * 5;
		public static final int T2_RIGHT_MIDDLE_X = 72;
		public static final int T2_RIGHT_MIDDLE_Y = 72 * 5;
		public static final int T2_UP_TOP_X = 0;
		public static final int T2_UP_TOP_Y = 72 * 6;
		public static final int T2_UP_MIDDLE_X = 72;
		public static final int T2_UP_MIDDLE_Y = 72 * 6;
		public static final int T2_DOWN_TOP_X = 0;
		public static final int T2_DOWN_TOP_Y = 72 * 7;
		public static final int T2_DOWN_MIDDLE_X = 72;
		public static final int T2_DOWN_MIDDLE_Y = 72 * 7;
		
		public static final int T3_LEFT_TOP_X = 0;
		public static final int T3_LEFT_TOP_Y = 580;
		public static final int T3_LEFT_MIDDLE_X = 72;
		public static final int T3_LEFT_MIDDLE_Y = 580;
		public static final int T3_CENTER_X = 144;
		public static final int T3_CENTER_Y = 580;
		public static final int T3_RIGHT_TOP_X = 0;
		public static final int T3_RIGHT_TOP_Y = 648;
		public static final int T3_RIGHT_MIDDLE_X = 72;
		public static final int T3_RIGHT_MIDDLE_Y = 648;
		public static final int T3_UP_TOP_X = 0;
		public static final int T3_UP_TOP_Y = 724;
		public static final int T3_UP_MIDDLE_X = 72;
		public static final int T3_UP_MIDDLE_Y = 724;
		public static final int T3_DOWN_TOP_X = 0;
		public static final int T3_DOWN_TOP_Y = 796;
		public static final int T3_DOWN_MIDDLE_X = 72;
		public static final int T3_DOWN_MIDDLE_Y = 796;
		
		public static final int T4_LEFT_TOP_X = 0;
		public static final int T4_LEFT_TOP_Y = 868;
		public static final int T4_LEFT_MIDDLE_X = 72;
		public static final int T4_LEFT_MIDDLE_Y = 868;
		public static final int T4_CENTER_X = 144;
		public static final int T4_CENTER_Y = 868;
		public static final int T4_RIGHT_TOP_X = 0;
		public static final int T4_RIGHT_TOP_Y = 940;
		public static final int T4_RIGHT_MIDDLE_X = 72;
		public static final int T4_RIGHT_MIDDLE_Y = 940;
		public static final int T4_UP_TOP_X = 0;
		public static final int T4_UP_TOP_Y = 1012;
		public static final int T4_UP_MIDDLE_X = 72;
		public static final int T4_UP_MIDDLE_Y = 1012;
		public static final int T4_DOWN_TOP_X = 0;
		public static final int T4_DOWN_TOP_Y = 1084;
		public static final int T4_DOWN_MIDDLE_X = 72;
		public static final int T4_DOWN_MIDDLE_Y = 1084;
		
		public static final int T5_LEFT_TOP_X = 0;
		public static final int T5_LEFT_TOP_Y = 1156;
		public static final int T5_LEFT_MIDDLE_X = 72;
		public static final int T5_LEFT_MIDDLE_Y = 1156;
		public static final int T5_CENTER_X = 144;
		public static final int T5_CENTER_Y = 1156;
		public static final int T5_RIGHT_TOP_X = 0;
		public static final int T5_RIGHT_TOP_Y = 1228;
		public static final int T5_RIGHT_MIDDLE_X = 72;
		public static final int T5_RIGHT_MIDDLE_Y = 1228;
		public static final int T5_UP_TOP_X = 0;
		public static final int T5_UP_TOP_Y = 1300;
		public static final int T5_UP_MIDDLE_X = 72;
		public static final int T5_UP_MIDDLE_Y = 1300;
		public static final int T5_DOWN_TOP_X = 0;
		public static final int T5_DOWN_TOP_Y = 1372;
		public static final int T5_DOWN_MIDDLE_X = 72;
		public static final int T5_DOWN_MIDDLE_Y = 1372;
	}
	
	public class Dead1 {
		public static final int H = 150;
		public static final int Y = 0;
		public static final int X1 = 0;
		public static final int W1 = 102;
		public static final int X2 = 114;
		public static final int W2 = 114;
		public static final int X3 = 240;
		public static final int W3 = 138;
		public static final int X4 = 390;
		public static final int W4 = 168;	
	}
}