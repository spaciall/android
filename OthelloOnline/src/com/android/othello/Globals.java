package com.android.othello;

public final class Globals {
	// ï¿½Î±ï¿½ ï¿½Ì¸ï¿½
    public static final String TAG = "BinGo-Othello";
    
    //ÇÁ·¹ÀÓ ¸¶´Ù µô·¹ÀÌ Å¸ÀÓ
    public static final int GAME_DELAY_TIME = 20;
    
    //º¸µå ÆÇÀÇ ÇÈ¼¿ »çÀÌÁî
    public static final int BOARD_PIXEL_SIZE_WIDTH = 480;
    public static final int BOARD_PIXEL_SIZE_HEIGHT = 800;

    //Á¡¼öÆÇÀÇ °¡·Î »çÀÌÁî
    public static final int SCORE_BOARD_WIDTH = 120;
    
    // ï¿½ï¿½ ï¿½Ï³ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½
    public static final int GAB_OF_CELL = 60;
    
    // 4µîºÐµÈ ½ºÄÚ¾î º¸µåÀÇ 1/4ÀÇ Å©±â
    public static final int GAB_OF_SCORE = SCORE_BOARD_WIDTH / 2;
 
    //ÅÏ¿À¹ö ÀÌ¹ÌÁö °³¼ö ¹× ºù°í ¼ýÀÚ ÀÌ¹ÌÁö °³¼ö
    public static final int NUM_TURNOVER_NUMBER_IMAGE = 10;
    public static final int NUM_BINGO_NUMBER_IMAGE = 10;
    
    // ï¿½Ã·ï¿½ï¿½Ì¾ï¿½ ï¿½ï¿½ï¿½ï¿½
    public static final int SIZE_OF_PLAYER = 44;
    public static final int SIZE_OF_POINTER = 44;
    public static final int SIZE_OF_SCORE_NUM = 44;
    public static final int SIZE_OF_SPACE_SCORE_NUM = SIZE_OF_SCORE_NUM / 2;
    
    // ï¿½Ã·ï¿½ï¿½Ì¾î°¡ È­ï¿½é¿¡ ï¿½ï¿½Ä¡ï¿½ï¿½ ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½
    public static final int GAB_BETWEEN_PLAYER_AND_CELL = (GAB_OF_CELL - SIZE_OF_PLAYER) / 2;
    public static final int GAB_BETWEEN_POINTER_AND_CELL = (GAB_OF_CELL - SIZE_OF_POINTER) / 2;
    
    
    // ï¿½ï¿½ ï¿½Ì¹ï¿½ï¿½ï¿½ ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
    public static final int IMAGE_START_X = 0;
    public static final int IMAGE_START_Y = 180;
    
    public static final int SCORE_START_X = 15;
    public static final int SCORE_START_Y = 15;
    
    // ï¿½ï¿½ ï¿½ï¿½Ã¼ Å©ï¿½ï¿½
    public static final int BOARD_SIZE_X = 8;
    public static final int BOARD_SIZE_Y = 8;
    
    // µÚÁýÈú ¶§ »ç¿ëÇÒ ¾Ö´Ï¸ÞÀÌ¼Ç Àå¸é °³¼ö
    public static final int NUM_TURNOVER_ANI = 6;
    
    //¼¿ÀÇ Á¾·ùÀÇ °³¼ö
    public static final int NUM_MAX_CELL = 4;
    
    //ÇÑ ÇÃ·¹ÀÌ¾î ´ç ºù°í Ä­ÀÇ °³¼ö
    public static final int NUM_BINGO_CELL = 2;
    
    //°ÔÀÓ¿¡ ½Â¸®ÇÏ±â À§ÇØ ÇÊ¿äÇÑ ºù°íÀÇ ¼ö
    public static int NUM_BINGO_NEED_TO_WIN = 1;
}
