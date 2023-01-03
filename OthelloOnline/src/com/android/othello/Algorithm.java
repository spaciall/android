/*
 1. Algorithm.java


boolean IsValidPosition(int 占쏙옙표X, int 占쏙옙표Y, int 占쏙옙占쏙옙占쏙옙)


- 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙 占쌍댐옙占쏙옙 占실븝옙占싹댐옙 占쌉쇽옙


int GetValidPositionCount(int 占쏙옙占쏙옙占쏙옙)


- 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙 占쌍댐옙 占쏙옙 占쏙옙占쏙옙


Point GetValidMovePosition(int 占쏙옙占쏙옙占쏙옙)


- 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙 占쌍댐옙 占쏙옙 占쏙옙치


2. Board.java


- static 占쏙옙占쏙옙 占쏙옙占쏙옙 Class


3. OthelloBingo.java


- Main 占쌉쇽옙 占쏙옙占쏙옙 占쏙옙占쏙옙璣占�占쏙옙占쏙옙.

 */


package com.android.othello;

import java.util.ArrayList;

public class Algorithm {

	private Board mBoard = null;

	public Algorithm()
	{
	}
	
	public Algorithm(Board board_)
	{
		mBoard = board_;
	}
	

	public void SetBoard(Board board_)
	{
	    mBoard = board_;
	}
	
    public Board GetBoard()
    {
        return mBoard;
    }
	
	public boolean IsValidPosition(eStone stone, int row, int col)
	{
	    if(mBoard == null)
	        return false;
	    
		if (mBoard.GetStoneXY(row, col) != eStone.EMPTY_STONE)
			return false;

		int dr, dc;
		for (dr = -1; dr <= 1; dr++)
			for (dc = -1; dc <= 1; dc++)
			{
			    if((row + dr) < 0 || (row + dr) >= Globals.BOARD_SIZE_X)
			        continue;
	             if((col + dc) < 0 || (col + dc) >= Globals.BOARD_SIZE_Y)
	                    continue;

			    
				if (!(dr == 0 && dc == 0) && this.IsOutOfBoard(stone, row, col, dr, dc))
					return true;
			}

		return false;
	}

	public int GetValidPositionCount(eStone stone)
	{
        if(mBoard == null)
            return -1;
	    
		int n = 0;

		int i, j;
		for (i = 0; i < Globals.BOARD_SIZE_X; i++)
			for (j = 0; j < Globals.BOARD_SIZE_Y; j++)
				if (this.IsValidPosition(stone, i, j))
					n++;
		return n;
	}

	public Point[] GetValidMovePosition(eStone stone)
	{
        if(mBoard == null)
            return null;
	    
		Point[] SquenceOfPoint = new Point[GetValidPositionCount(stone)];

		int n = 0;
		for (int j = 0; j < Globals.BOARD_SIZE_Y; j++)
		{
			for (int i = 0; i < Globals.BOARD_SIZE_X; i++)
			{
				if(IsValidPosition(stone, i, j) == true)
				{
					SquenceOfPoint[n] = new Point();
					SquenceOfPoint[n].x = i;
					SquenceOfPoint[n].y = j;
					
					n++;
				}
			}
		}

		return SquenceOfPoint;
	}

	private boolean IsOutOfBoard(eStone stone, int row, int col, int dr, int dc)
	{
        if(mBoard == null)
            return false;
	    
	    eStone oppositeStone = null;

	    if(stone == eStone.BLACK_STONE)
	        oppositeStone = eStone.WHITE_STONE;
	    if(stone == eStone.WHITE_STONE)
	        oppositeStone = eStone.BLACK_STONE;

		int r = row + dr;
		int c = col + dc;
		while (r >= 0 && r < 8 && c >= 0 && c < 8 && (mBoard.GetStoneXY(r, c) == oppositeStone) )
		{
			r += dr;
			c += dc;
		}

		if (r < 0 || r >= Globals.BOARD_SIZE_X || c < 0 || c >= Globals.BOARD_SIZE_Y || (r - dr == row && c - dc == col) || mBoard.GetStoneXY(r, c) != stone)
			return false;

		return true;
	}
	
	
	public ArrayList<Point> TurnOverBy(eStone stone, int row, int col)
	{
	    if(mBoard == null)
	        return null;
	    
		if (mBoard.GetStoneXY(row, col) != eStone.EMPTY_STONE)
			return null;

		ArrayList<Point> ret = new ArrayList<Point>();
		ArrayList<Point> tmp = null;
		
		int dr, dc;
		for (dr = -1; dr <= 1; dr++)
			for (dc = -1; dc <= 1; dc++)
			{
			    if((row + dr) < 0 || (row + dr) >= Globals.BOARD_SIZE_X)
			        continue;
	             if((col + dc) < 0 || (col + dc) >= Globals.BOARD_SIZE_Y)
	                    continue;

			    
				if (!(dr == 0 && dc == 0))
				{
					tmp = this.TurnOverAtOneDir(stone, row, col, dr, dc);
					
					if(tmp != null)
					{
						for(int i = 0; i < tmp.size(); i++)
							ret.add(tmp.get(i));
					}
				}
			}

		return ret;
	}	
	
	private ArrayList<Point> TurnOverAtOneDir(eStone stone, int row, int col, int dr, int dc)
	{
        if(mBoard == null)
            return null;
	    
        ArrayList<Point> ptTemps = new ArrayList<Point>();
        ptTemps.clear();
        
	    eStone oppositeStone = null;
	    if(stone == eStone.BLACK_STONE)
	        oppositeStone = eStone.WHITE_STONE;
	    if(stone == eStone.WHITE_STONE)
	        oppositeStone = eStone.BLACK_STONE;

		int r = row + dr;
		int c = col + dc;
		while (r >= 0 && r < 8 && c >= 0 && c < 8 && (mBoard.GetStoneXY(r, c) == oppositeStone) )
		{
			Point ptTemp = new Point();
			ptTemp.x = r;
			ptTemp.y = c;

			ptTemps.add(ptTemp);
			
			
			r += dr;
			c += dc;
		}

		if (r < 0 || r >= Globals.BOARD_SIZE_X || c < 0 || c >= Globals.BOARD_SIZE_Y || (r - dr == row && c - dc == col) || mBoard.GetStoneXY(r, c) != stone)
			return null;


		for(int i = 0; i < ptTemps.size(); i++)
		{
			Point temp = ptTemps.get(i);
			mBoard.SetStoneXY(temp.x, temp.y, stone);
		}
		
		return ptTemps;
	}	
}
