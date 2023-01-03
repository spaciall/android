package com.android.othello;

public final class Globals {
	// �α� �̸�
    public static final String TAG = "BinGo-Othello";
    
    //������ ���� ������ Ÿ��
    public static final int GAME_DELAY_TIME = 20;
    
    //���� ���� �ȼ� ������
    public static final int BOARD_PIXEL_SIZE_WIDTH = 480;
    public static final int BOARD_PIXEL_SIZE_HEIGHT = 800;

    //�������� ���� ������
    public static final int SCORE_BOARD_WIDTH = 120;
    
    // �� �ϳ��� ����
    public static final int GAB_OF_CELL = 60;
    
    // 4��е� ���ھ� ������ 1/4�� ũ��
    public static final int GAB_OF_SCORE = SCORE_BOARD_WIDTH / 2;
 
    //�Ͽ��� �̹��� ���� �� ���� ���� �̹��� ����
    public static final int NUM_TURNOVER_NUMBER_IMAGE = 10;
    public static final int NUM_BINGO_NUMBER_IMAGE = 10;
    
    // �÷��̾� ����
    public static final int SIZE_OF_PLAYER = 44;
    public static final int SIZE_OF_POINTER = 44;
    public static final int SIZE_OF_SCORE_NUM = 44;
    public static final int SIZE_OF_SPACE_SCORE_NUM = SIZE_OF_SCORE_NUM / 2;
    
    // �÷��̾ ȭ�鿡 ��ġ�� �� ������ �� ����
    public static final int GAB_BETWEEN_PLAYER_AND_CELL = (GAB_OF_CELL - SIZE_OF_PLAYER) / 2;
    public static final int GAB_BETWEEN_POINTER_AND_CELL = (GAB_OF_CELL - SIZE_OF_POINTER) / 2;
    
    
    // �� �̹��� ������
    public static final int IMAGE_START_X = 0;
    public static final int IMAGE_START_Y = 180;
    
    public static final int SCORE_START_X = 15;
    public static final int SCORE_START_Y = 15;
    
    // �� ��ü ũ��
    public static final int BOARD_SIZE_X = 8;
    public static final int BOARD_SIZE_Y = 8;
    
    // ������ �� ����� �ִϸ��̼� ��� ����
    public static final int NUM_TURNOVER_ANI = 6;
    
    //���� ������ ����
    public static final int NUM_MAX_CELL = 4;
    
    //�� �÷��̾� �� ���� ĭ�� ����
    public static final int NUM_BINGO_CELL = 2;
    
    //���ӿ� �¸��ϱ� ���� �ʿ��� ������ ��
    public static int NUM_BINGO_NEED_TO_WIN = 1;
}
