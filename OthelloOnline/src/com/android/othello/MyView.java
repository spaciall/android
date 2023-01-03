package com.android.othello;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MyView extends SurfaceView 
implements SurfaceHolder.Callback, Runnable{
	
	//더블 버퍼링을 위한 표면 홀더 및 쓰레드
	protected SurfaceHolder holder = null;
	protected Thread thread = null;
	
	//각종 이미지
	protected Bitmap	 m_imgBackGrnd = null;
	protected Bitmap	 m_imgScoreBoard = null;
	protected Bitmap[] m_imgCell = new Bitmap[Globals.NUM_MAX_CELL];	//0: NORMAL, 1: BLUE, 2: RED, 3: GREEN
	protected Bitmap	 m_imgPlayer1 = null;
	protected Bitmap	 m_imgPlayer2 = null;
	protected Bitmap	 m_imgBlueStar = null;
	protected Bitmap	 m_imgRedStar = null;
	protected Bitmap	 m_imgRedPointer = null;
	protected Bitmap	 m_imgBluePointer = null;
	
	//뒤집어진 수 스코어 번호 이미지
	protected Bitmap[] m_imgTurnOver = new Bitmap[Globals.NUM_TURNOVER_NUMBER_IMAGE];
	//빙고 수 스코어 번호 이미지
	protected Bitmap[] m_imgBingo = new Bitmap[Globals.NUM_BINGO_NUMBER_IMAGE];
	
	//애니메이션 효과
	protected Bitmap[] m_imgRTB = new Bitmap[Globals.NUM_TURNOVER_ANI];
	protected Bitmap[] m_imgBTR = new Bitmap[Globals.NUM_TURNOVER_ANI];
	
	//화면 클릭 위치
	protected int m_nClickX = -1;
	protected int m_nClickY = -1;
	
	//돌의 개수
	protected int m_nBlackCount = 0;
	protected int m_nWhiteCount = 0;
	
	//돌의 빙고의 개수
	protected int m_nBlackBingoCount = 0;
	protected int m_nWhiteBingoCount = 0;
	
	//빙고 보여줄 돌의 위치
	protected ArrayList<Point> m_aryBlackBingoPos = null;
	protected ArrayList<Point> m_aryWhiteBingoPos = null;
	
	//애니메이션 될 돌의 위치
	protected ArrayList<Point> m_aryAnimation = null;
	
	//애니메이션 플러그
	protected boolean m_bAni = false;
	protected int		m_nCurTurnOverAni = 0;
	
	
	//돌의 정보를 담은 보드
	protected Board mBoard = new Board();
	
	//판 정보를 담은 보드
	protected CellBoard mCellBoard = new CellBoard();
	
	//占싯�占쏙옙
	protected Algorithm mAlgorithm = new Algorithm(mBoard); 
	
	//占쏙옙占쏙옙占쏙옙 占쏙옙
	protected eStone m_eCurTurnStone;
	
	//부모 ACTIVITY
	public Activity m_Parent = null;
	
	//게임이 종료 되었는가?
	protected boolean m_bIsGameOver = false;
	
	//터치했을 때 눌린 정보를 처리해 준다.
	//데이터 처리부로 볼 수 있다. 복잡해 지면 따로 이 부분도 함수로 부분 부분을 묶어서 빼 준다.
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    // TODO Auto-generated method stub
		boolean bProcessed = false;
		
		if(m_bAni != true)
		{
			m_nClickX = -(Globals.IMAGE_START_X);
			m_nClickY = -(Globals.IMAGE_START_Y);
	
	        m_nClickX += (int) event.getX(); 
	        m_nClickY += (int) event.getY();
	        
	        m_nClickX /= Globals.GAB_OF_CELL;
	        m_nClickY /= Globals.GAB_OF_CELL;
	   
	        Log.i(Globals.TAG, "onTouchEvent() -- (" + event.getX()+","+event.getY()+")");
	        Log.i(Globals.TAG, "onTouchEvent() -- (" + m_nClickX+","+m_nClickY+")");
	        Log.i(Globals.TAG, "onTouchEvent() -- Action " + event.getAction());
	
	        switch(event.getAction()) {
	            case MotionEvent.ACTION_DOWN :
	                Log.i(Globals.TAG, "onTouchEvent() -- ACTION_DOWN");
	
	                if(mAlgorithm.IsValidPosition(m_eCurTurnStone, m_nClickX, m_nClickY) == true)
	                {
	                	bProcessed = true;
	                	
	                	m_aryAnimation = mAlgorithm.TurnOverBy(m_eCurTurnStone, m_nClickX, m_nClickY);
	                	if(m_aryAnimation != null)
	                		m_bAni = true;
	                		
	                    mBoard.SetStoneXY(m_nClickX, m_nClickY, m_eCurTurnStone);
	            
	                    UpdateWhosTurn();
		                UpdateStoneCount();
		                UpdateBingoStoneCount();
		                UpdateCellBoard();
	                }
	                
	                break;
	            case MotionEvent.ACTION_MOVE :
	                Log.i(Globals.TAG, "onTouchEvent() -- ACTION_MOVE");
	                                    
	                break;
	            case MotionEvent.ACTION_UP :
	                Log.i(Globals.TAG, "onTouchEvent() -- ACTION_UP");
	                
	                break;
	        }
		}
		
		//게임 종료 조건 판단
	    try
	    {
	    	eStone eRes = eStone.ERROR_STONE;
	    	if( (eRes = CheckGameFinish()) != eStone.ERROR_STONE){
	    		if(eRes == eStone.BLACK_STONE){
					Toast t = Toast.makeText(m_Parent.getApplicationContext(), "Red, Win!", Toast.LENGTH_SHORT);
					t.show();
	    		}
	    		else if(eRes == eStone.WHITE_STONE){
					Toast t = Toast.makeText(m_Parent.getApplicationContext(), "Blue, Win!", Toast.LENGTH_SHORT);
					t.show();
	    		}
	    		
	    		m_bIsGameOver = true;
	    	}
	    }
	    catch (Throwable e) {
		  // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	
	
	    super.onTouchEvent(event);
	    return bProcessed;
	}
	
	public eStone CheckGameFinish()
	{
		//옵션에 할당된 빙고의 조건을 만족했다.
		if((m_nBlackBingoCount >= Globals.NUM_BINGO_NEED_TO_WIN) && (m_nWhiteBingoCount >= Globals.NUM_BINGO_NEED_TO_WIN))
		{
			//이 경우엔 옵션에 할당된 빙고의 조건을 만족했으나, 플레이어 둘다 만족했으므로 다른 조건으로 판단한다.
			if(m_nBlackBingoCount > m_nWhiteBingoCount)
			{
				return eStone.BLACK_STONE;
			}
			else if(m_nBlackBingoCount < m_nWhiteBingoCount)
			{
				return eStone.WHITE_STONE;
			}
			else	//m_nBlackBingoCount == m_nWhiteBingoCount
			{
				//서로 빙고의 수가 같기 때문에 돌의 수를 비교해 승자를 결정한다.
				if(m_nBlackCount > m_nWhiteCount)
				{
					return eStone.BLACK_STONE;
				}
				else if(m_nBlackCount < m_nWhiteCount)
				{
					return eStone.WHITE_STONE;
				}
				else	//m_nBlackCount == m_nWhiteCount
				{
					//eStone.EMPTY_STONE을 DRAW의 값 으로 한다.
					return eStone.EMPTY_STONE;
				}
			}
	}
		else if(m_nBlackBingoCount >= Globals.NUM_BINGO_NEED_TO_WIN)
		{
			return eStone.BLACK_STONE;
		}
		else if(m_nWhiteBingoCount >= Globals.NUM_BINGO_NEED_TO_WIN)
		{
			return eStone.WHITE_STONE;
		}
		else
		{
	    	//서로 더 이상 놓을 자리가 없다.
	    	if(mAlgorithm.GetValidPositionCount(eStone.BLACK_STONE) <= 0)
	    	{
	    		if(mAlgorithm.GetValidPositionCount(eStone.WHITE_STONE) <= 0)
	    		{
	    			if(m_nBlackBingoCount > m_nWhiteBingoCount)
	    			{
	    				return eStone.BLACK_STONE;
	    			}
	    			else if(m_nBlackBingoCount < m_nWhiteBingoCount)
	    			{
	    				return eStone.WHITE_STONE;
	    			}
	    			else	//m_nBlackBingoCount == m_nWhiteBingoCount
	    			{
	    				//서로 빙고의 수가 같기 때문에 돌의 수를 비교해 승자를 결정한다.
	        			if(m_nBlackCount > m_nWhiteCount)
	        			{
	        				return eStone.BLACK_STONE;
	        			}
	        			else if(m_nBlackCount < m_nWhiteCount)
	        			{
	        				return eStone.WHITE_STONE;
	        			}
	        			else	//m_nBlackCount == m_nWhiteCount
	        			{
	        				//eStone.EMPTY_STONE을 DRAW의 값 으로 한다.
	        				return eStone.EMPTY_STONE;
	        			}
	    			}
	    		}
	    	}
		}
		
		return eStone.ERROR_STONE;
	}
	
	public void UpdateWhosTurn()
	{
	    if(m_eCurTurnStone == eStone.BLACK_STONE)
	    	m_eCurTurnStone = eStone.WHITE_STONE;
	    else if(m_eCurTurnStone == eStone.WHITE_STONE)
	    	m_eCurTurnStone = eStone.BLACK_STONE;
	    else
	    	;
	    
	     //아무 돌도 놓을 수가 없으면 상대에게 턴을 양보한다.
	     if(mAlgorithm.GetValidPositionCount(m_eCurTurnStone) <= 0)
	     {
	         if(m_eCurTurnStone == eStone.BLACK_STONE)
	         	m_eCurTurnStone = eStone.WHITE_STONE;
	         else if(m_eCurTurnStone == eStone.WHITE_STONE)
	         	m_eCurTurnStone = eStone.BLACK_STONE;
	         else
	         	;
	     }
	}
	
	public void UpdateStoneCount()
	{
	     //돌의 개수를 구한다
	     m_nBlackCount = mBoard.GetStoneCount(eStone.BLACK_STONE);
	     m_nWhiteCount = mBoard.GetStoneCount(eStone.WHITE_STONE);
	}
	
	public void UpdateBingoStoneCount()
	{
	     //빙고된 돌의 개수를 구한다
	     m_nBlackBingoCount = mBoard.GetStoneBingoCount(eStone.BLACK_STONE);
	     m_nWhiteBingoCount = mBoard.GetStoneBingoCount(eStone.WHITE_STONE);
	}
	
	public void UpdateCellBoard()
	{
	     //빙고된 돌의 위치를 구한 후 셀보드를 갱신해 준다.
	     //프레임 당 실시간으로 새롭게 계산되므로 셀보드를 일단 리셋해 준다.
	     mCellBoard.Initialize();
	   
	     
	     //흰색 돌에 대해 다음 작업을 한다.
	     if(m_nWhiteBingoCount > 0)
	     {
	  	   //빙고 위치를 가져온다.
	  	   m_aryWhiteBingoPos = mBoard.GetStoneBingoPosition(eStone.WHITE_STONE);
	  	   
	  	   //셀 보드의 정보를 갱신해 준다.
	  	   if(m_aryWhiteBingoPos != null)
	  	   {
	      	   for(int i = 0; i < m_aryWhiteBingoPos.size(); i++)
	      	   {
	      		   Point tmp = m_aryWhiteBingoPos.get(i);
	      		   if(tmp != null)
	      			   mCellBoard.SetCellXY(tmp.x, tmp.y, eCell.WHITE_CELL);
	      	   }
	  	   }
	     }
	     
	     //이제 검은 돌에 대해 다음 작업을 한다.
	     if(m_nBlackBingoCount > 0)
	     {
	  	   //빙고 위치를 가져온다.
	  	   m_aryBlackBingoPos = mBoard.GetStoneBingoPosition(eStone.BLACK_STONE);
	
	  	   //셀 보드의 정보를 갱신해 준다.
	  	   if(m_aryBlackBingoPos != null)
	  	   {
	      	   for(int i = 0; i < m_aryBlackBingoPos.size(); i++)
	      	   {
	      		   Point tmp = m_aryBlackBingoPos.get(i);
	      		   if(tmp != null)
	      			   mCellBoard.SetCellXY(tmp.x, tmp.y, eCell.BLACK_CELL);
	      	   }
	  	   }
	     }
	}
	
	
	public MyView(Context context) {
	    super(context);
	    
	    //표면 홀더  생성
	    holder = getHolder();
	    holder.addCallback(this);
	    holder.setFixedSize(getWidth(), getHeight());
	
	    //占쏙옙占썲를 占쏙옙占싹곤옙, 占쏙옙占�占쏙옙占썲를 占싯�占쏠에듸옙 占쏙옙占쏙옙占쏙옙占�占쌔댐옙.
	    mBoard.InitBoard();
	    
	    m_eCurTurnStone = eStone.BLACK_STONE;
	    
	    m_imgBackGrnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
	    m_imgScoreBoard = BitmapFactory.decodeResource(context.getResources(), R.drawable.scoreboard);
	    
	    m_imgCell[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_normal);
	    m_imgCell[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_blue);
	    m_imgCell[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_red);
	    m_imgCell[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_green);
	    
	    m_imgPlayer1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player1);
	    m_imgPlayer2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player2);
	    m_imgBlueStar = BitmapFactory.decodeResource(context.getResources(), R.drawable.star_blue);
	    m_imgRedStar = BitmapFactory.decodeResource(context.getResources(), R.drawable.star_red);
	   
	    m_imgRedPointer = BitmapFactory.decodeResource(context.getResources(), R.drawable.pointer_red);
	    m_imgBluePointer = BitmapFactory.decodeResource(context.getResources(), R.drawable.pointer_blue);
	    
	    m_imgRTB[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rtb1);
	    m_imgRTB[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rtb2);
	    m_imgRTB[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rtb3);
	    m_imgRTB[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rtb4);
	    m_imgRTB[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rtb5);
	    m_imgRTB[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rtb6);
	    
	    m_imgBTR[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.btr1);
	    m_imgBTR[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.btr2);
	    m_imgBTR[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.btr3);
	    m_imgBTR[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.btr4);
	    m_imgBTR[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.btr5);
	    m_imgBTR[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.btr6);
	    
	    m_imgTurnOver[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t0);
	    m_imgTurnOver[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t1);
	    m_imgTurnOver[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t2);
	    m_imgTurnOver[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t3);
	    m_imgTurnOver[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t4);
	    m_imgTurnOver[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t5);
	    m_imgTurnOver[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t6);
	    m_imgTurnOver[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t7);
	    m_imgTurnOver[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t8);
	    m_imgTurnOver[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.t9);
	    
	    m_imgBingo[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b0);
	    m_imgBingo[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b1);
	    m_imgBingo[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b2);
	    m_imgBingo[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b3);
	    m_imgBingo[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b4);
	    m_imgBingo[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b5);
	    m_imgBingo[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b6);
	    m_imgBingo[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b7);
	    m_imgBingo[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b8);
	    m_imgBingo[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.b9);            
	   
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    Log.i(Globals.TAG, "onDraw");
	}
	
	public void drawBackImage(Canvas canvas)
	{
		canvas.drawBitmap(m_imgBackGrnd, 0, 0, null);
	}
	
	public void drawScoreBoard(Canvas canvas)
	{
		int x, y;
	
		//1Player
		x = Globals.SCORE_START_X;
		y = Globals.SCORE_START_Y;
		canvas.drawBitmap(m_imgScoreBoard, x, y, null);
		
		x += Globals.GAB_BETWEEN_PLAYER_AND_CELL;
		y += Globals.GAB_BETWEEN_PLAYER_AND_CELL;
		canvas.drawBitmap(m_imgPlayer1, x, y, null);
		x += Globals.GAB_OF_SCORE;
		canvas.drawBitmap(m_imgRedStar, x, y, null);
		
		//1Player 돌의 개수
		x = Globals.SCORE_START_X;
		y = Globals.SCORE_START_Y;
		
		y += Globals.GAB_OF_SCORE;
		int nBlackNum = m_nBlackCount / 10;
		if(nBlackNum <= Globals.NUM_TURNOVER_NUMBER_IMAGE)
			canvas.drawBitmap(m_imgTurnOver[nBlackNum], x, y, null);
		else
			Log.i(Globals.TAG, "GameActivity.java - private void drawScoreBoard(Canvas canvas) - nBlackNum Error!!");
		
		x += Globals.SIZE_OF_SPACE_SCORE_NUM;
		nBlackNum = m_nBlackCount % 10;
		if(nBlackNum <= Globals.NUM_TURNOVER_NUMBER_IMAGE)
			canvas.drawBitmap(m_imgTurnOver[nBlackNum], x, y, null);
		else
			Log.i(Globals.TAG, "GameActivity.java - private void drawScoreBoard(Canvas canvas) - nBlackNum Error!!");
	
		//1Player의 빙고의 개수
		x = Globals.SCORE_START_X;
		y = Globals.SCORE_START_Y;
	
		x += Globals.GAB_OF_SCORE;
		y += Globals.GAB_OF_SCORE;
		
		int nBlackBingoNum = m_nBlackBingoCount / 10;
		if(nBlackBingoNum <= Globals.NUM_TURNOVER_NUMBER_IMAGE)
			canvas.drawBitmap(m_imgBingo[nBlackBingoNum], x, y, null);
		else
			Log.i(Globals.TAG, "GameActivity.java - private void drawScoreBoard(Canvas canvas) - nBlackBingoNum Error!!");
		
		x += Globals.SIZE_OF_SPACE_SCORE_NUM;
		nBlackBingoNum = m_nBlackBingoCount % 10;
		if(nBlackBingoNum <= Globals.NUM_TURNOVER_NUMBER_IMAGE)
			canvas.drawBitmap(m_imgBingo[nBlackBingoNum], x, y, null);
		else
			Log.i(Globals.TAG, "GameActivity.java - private void drawScoreBoard(Canvas canvas) - nBlackBingoNum Error!!");
		
	
		
		//2Player
		x = -(Globals.SCORE_START_X);
		y = Globals.SCORE_START_Y;
		x += Globals.BOARD_PIXEL_SIZE_WIDTH - Globals.SCORE_BOARD_WIDTH;
	
		canvas.drawBitmap(m_imgScoreBoard, x, y, null);
		
		x += Globals.GAB_BETWEEN_PLAYER_AND_CELL;
		y += Globals.GAB_BETWEEN_PLAYER_AND_CELL;
		canvas.drawBitmap(m_imgBlueStar, x, y, null);
		x += Globals.GAB_OF_SCORE;
		canvas.drawBitmap(m_imgPlayer2, x, y, null);
	
		//2Player 빙고의  개수
		x = -(Globals.SCORE_START_X);
		y = Globals.SCORE_START_Y;
		x += Globals.BOARD_PIXEL_SIZE_WIDTH - Globals.SCORE_BOARD_WIDTH;
		
		y += Globals.GAB_OF_SCORE;
		int nWhiteNum = m_nWhiteBingoCount / 10;
		if(nWhiteNum <= Globals.NUM_TURNOVER_NUMBER_IMAGE)
			canvas.drawBitmap(m_imgBingo[nWhiteNum], x, y, null);
		else
			Log.i(Globals.TAG, "GameActivity.java - private void drawScoreBoard(Canvas canvas) - nWhiteBingoNum Error!!");
		
		x += Globals.SIZE_OF_SPACE_SCORE_NUM;
		nWhiteNum = m_nWhiteBingoCount % 10;
		if(nWhiteNum <= Globals.NUM_TURNOVER_NUMBER_IMAGE)
			canvas.drawBitmap(m_imgBingo[nWhiteNum], x, y, null);
		else
			Log.i(Globals.TAG, "GameActivity.java - private void drawScoreBoard(Canvas canvas) - nWhiteBingoNum Error!!");
	
		//2Player의  돌의  개수
		x = -(Globals.SCORE_START_X);
		y = Globals.SCORE_START_Y;
		x += Globals.BOARD_PIXEL_SIZE_WIDTH - Globals.SCORE_BOARD_WIDTH;
	
		x += Globals.GAB_OF_SCORE;
		y += Globals.GAB_OF_SCORE;
		
		int nWhiteBingoNum = m_nWhiteCount / 10;
		if(nWhiteBingoNum <= Globals.NUM_TURNOVER_NUMBER_IMAGE)
			canvas.drawBitmap(m_imgTurnOver[nWhiteBingoNum], x, y, null);
		else
			Log.i(Globals.TAG, "GameActivity.java - private void drawScoreBoard(Canvas canvas) - nWhiteNum Error!!");
		
		x += Globals.SIZE_OF_SPACE_SCORE_NUM;
		nWhiteBingoNum = m_nWhiteCount % 10;
		if(nWhiteBingoNum <= Globals.NUM_TURNOVER_NUMBER_IMAGE)
			canvas.drawBitmap(m_imgTurnOver[nWhiteBingoNum], x, y, null);
		else
			Log.i(Globals.TAG, "GameActivity.java - private void drawScoreBoard(Canvas canvas) - nWhiteNum Error!!");
		        	
	}
	
	//占쏙옙占쏙옙 占싱울옙占쏙옙 占쏙옙占썲를 占쌓몌옙占쏙옙 占쌉쇽옙
	public void drawGridImage(Canvas canvas)
	{
		int x, y;
	    for(int j = 0; j < Globals.BOARD_SIZE_Y; j++)
	    {
	        for(int i = 0; i < Globals.BOARD_SIZE_X; i++)
	        {
	
	        	x = Globals.IMAGE_START_X;
	        	y = Globals.IMAGE_START_Y;
		
	        	x += i * Globals.GAB_OF_CELL;
	        	y += j * Globals.GAB_OF_CELL;
	        	
	        	if(mCellBoard.GetCellXY(i, j) == eCell.NORMAL_CELL)
	           		canvas.drawBitmap(m_imgCell[0], x,  y, null);
	        	else if(mCellBoard.GetCellXY(i, j) == eCell.WHITE_CELL)
	           		canvas.drawBitmap(m_imgCell[1], x,  y, null);
	        	else if(mCellBoard.GetCellXY(i, j) == eCell.BLACK_CELL)
	           		canvas.drawBitmap(m_imgCell[2], x,  y, null);
	        	else
	        		Log.i(Globals.TAG, "GameActivity.java - void drawGridImage(Canvas canvas) - mCellBoard.GetCellXY(i, j)'s Return Error!!");
	        }
	    }
	}
	
	
	//占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙 占쌍댐옙 占쏙옙치占쏙옙 占쌓몌옙占쏙옙 占쌉쇽옙
	public void drawPointerImage(Canvas canvas)
	{
		//여기서 메모리 관련 예외가 나는 것 같아서 try, catch로 막아 두었다. 나중에 수정하지 않으면 게임을 진행할 수록
		//게임이 느려지는 것 같다. Thread를 쓰니까 이런 에러가 발생하는 걸 보면 어딘가에 Lock처리를 해 줘야 하는 것 같다.
		try
		{
	        if(mAlgorithm != null)
	        {
	        	Point[] mAvailablePoint = mAlgorithm.GetValidMovePosition(m_eCurTurnStone);
	        
	        	int x, y;
	        	for(int i = 0; i < mAlgorithm.GetValidPositionCount(m_eCurTurnStone); i++)
	        		if(mAvailablePoint.length > 0)
	        			if(mAvailablePoint[i] != null)
	        			{
	    					x = Globals.IMAGE_START_X;
	    					y = Globals.IMAGE_START_Y;
	    					
	    					x += Globals.GAB_BETWEEN_POINTER_AND_CELL + (mAvailablePoint[i].x * Globals.GAB_OF_CELL);
	    					y += Globals.GAB_BETWEEN_POINTER_AND_CELL + (mAvailablePoint[i].y * Globals.GAB_OF_CELL);
	
	    					if(m_eCurTurnStone == eStone.BLACK_STONE)
	    						canvas.drawBitmap(m_imgRedPointer, x, y, null);
	    					else if(m_eCurTurnStone == eStone.WHITE_STONE)
	    						canvas.drawBitmap(m_imgBluePointer, x, y, null);
	    					else;
	        			}
	        }
		}
		catch(Exception e)
		{
			Log.i(Globals.TAG, "drawPointerImage(Canvas canvas) -- catch(Exception e)");
		}
	}
		
	
	public void drawTurnOverAnimation(Canvas canvas)
	{
		//그릴 조건이 충족하는가?
		if(canvas != null)
		{
			if(m_aryAnimation != null)
			{
				if(m_aryAnimation.size() > 0)
				{
					if(m_nCurTurnOverAni < Globals.NUM_TURNOVER_ANI)
					{
						int x, y;
						Point temp = null;
	    				for(int i = 0; i < m_aryAnimation.size(); i++)
	    				{
	    					temp = m_aryAnimation.get(i);
	
	    					//셀 부터 먼저 그린다
	    					x = Globals.IMAGE_START_X;
	    					y = Globals.IMAGE_START_Y;
	    					
	    					x += (temp.x*Globals.GAB_OF_CELL);
	    					y += (temp.y*Globals.GAB_OF_CELL);
	    					
	    					//셀의 정보에 따라 그에 알맞은 색을 가진 셀을 배경으로 그려준다.
	    					if(mCellBoard.GetCellXY(temp.x, temp.y) == eCell.NORMAL_CELL)
	    						canvas.drawBitmap(m_imgCell[0], x, y, null);
	    					else if(mCellBoard.GetCellXY(temp.x, temp.y) == eCell.WHITE_CELL)
	    						canvas.drawBitmap(m_imgCell[1], x, y, null);
	    					else if(mCellBoard.GetCellXY(temp.x, temp.y) == eCell.BLACK_CELL)
	    						canvas.drawBitmap(m_imgCell[2], x, y, null);
	    					else
	    						Log.i(Globals.TAG, "void drawTurnOverAnimation(Canvas canvas) -- mCellBoard.GetCellXY(x, y)'s Return Error!!");
	    					
	    					
	    					//셀 위에 애니메이션 돌을 그린다
							x = Globals.IMAGE_START_X;
	    					y = Globals.IMAGE_START_Y;
	    					
	    					x += Globals.GAB_BETWEEN_PLAYER_AND_CELL + (temp.x*Globals.GAB_OF_CELL);
	    					y += Globals.GAB_BETWEEN_PLAYER_AND_CELL + (temp.y*Globals.GAB_OF_CELL);
							
	    					if(m_eCurTurnStone == eStone.WHITE_STONE)
	    					{
	    						canvas.drawBitmap(m_imgBTR[m_nCurTurnOverAni], x, y, null);
	    					}
	    					else if(m_eCurTurnStone == eStone.BLACK_STONE)
	    					{
	       						canvas.drawBitmap(m_imgRTB[m_nCurTurnOverAni], x, y, null);
	    					}
	    					else
	    					{
	    						Log.i(Globals.TAG, "drawTurnOverAnimation(Canvas canvas) -- m_eCurTurnStone's Value Error!!");
	    					}
	    				}
	    				
	    				m_nCurTurnOverAni++;
	    				
					}
					else
					{
						m_nCurTurnOverAni = 0;
						m_bAni = false;
					}
				}
			}
		}
	}
	
	public void drawBlockImage(Canvas canvas)
	{
	    //占쏙옙占쏙옙 占쌓뤄옙占쌔댐옙
		int x, y;
	    for(int j = 0; j < Globals.BOARD_SIZE_Y; j++)
	    {
	        for(int i = 0; i < Globals.BOARD_SIZE_X; i++)
	        {
	        	
	        	x = Globals.IMAGE_START_X;
	        	y = Globals.IMAGE_START_Y;
	        	
	        	x += Globals.GAB_BETWEEN_PLAYER_AND_CELL + (i*Globals.GAB_OF_CELL);
	        	y += Globals.GAB_BETWEEN_PLAYER_AND_CELL + (j*Globals.GAB_OF_CELL);
	
	        	
	        	eStone eKindStone = mBoard.GetStoneXY(i, j);
	        	if(eKindStone == eStone.EMPTY_STONE)
	        		continue;
	        	else if(eKindStone == eStone.BLACK_STONE)
	        		canvas.drawBitmap(m_imgPlayer1, x, y, null);
	        	else if(eKindStone == eStone.WHITE_STONE)
	        		canvas.drawBitmap(m_imgPlayer2, x, y, null);
	        	else
	        		;//占쏙옙占쏙옙 처占쏙옙 - 占싱뤄옙 占쏙옙占쏙옙 占쌩삼옙占싹몌옙 占쏙옙 占싫댐옙
	        }
	    }
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		Canvas canvas;
		while(thread != null)
		{
			canvas = holder.lockCanvas();
			//
			
			if(m_bAni == true)
			{
				drawBackImage(canvas);
				drawScoreBoard(canvas);
	            drawGridImage(canvas);
	            drawBlockImage(canvas);					
				drawTurnOverAnimation(canvas);
			}
			else
			{
				if(m_bIsGameOver == false){
					//백 그라운드를 그려준다.
					drawBackImage(canvas);
					//스코어보드를 그려준다.
					drawScoreBoard(canvas);
		            //그리드를 그려준다.
		            drawGridImage(canvas);
		            //블럭 이미지를 그린다.
		            drawBlockImage(canvas);
		            //포인터를 그려준다.
		            drawPointerImage(canvas);
				}
				else{
					//게임을 종료한다.
					try
					{
						Thread.sleep(Globals.GAME_DELAY_TIME * 50);
					}
					catch(Exception e)
					{
					}
					m_Parent.finish();
				}
	            
			}
	        
			//
			holder.unlockCanvasAndPost(canvas);
			
			try
			{
				Thread.sleep(Globals.GAME_DELAY_TIME);
			}
			catch(Exception e)
			{
			}
		}
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread = new Thread(this);
		thread.start();
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread = null;
	}


}
