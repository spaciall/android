package com.android.othello;

import java.util.ArrayList;

class Board {

    private eStone mBoard[][];
    
    public Board()
    {
        mBoard = new eStone[Globals.BOARD_SIZE_Y][Globals.BOARD_SIZE_X];
        
        for(int j = 0; j < Globals.BOARD_SIZE_Y; j++)
            for(int i = 0; i < Globals.BOARD_SIZE_X; i++)
            {
                mBoard[j][i] = eStone.EMPTY_STONE;
            }
    }
    
    //������ ������ �� �ֵ���, ���带 �ʱ�ȭ ���ش�.
    public boolean  InitBoard()
    {
        int sx = (Globals.BOARD_SIZE_Y / 2) - 1;
        int sy = (Globals.BOARD_SIZE_X / 2) - 1;
        
        if((sx + 1) >= Globals.BOARD_SIZE_X|| (sy + 1) >= Globals.BOARD_SIZE_Y)
            return false;
        
        for(int j = 0; j < Globals.BOARD_SIZE_Y; j++)
        	for(int i = 0; i < Globals.BOARD_SIZE_X; i++)
        		mBoard[j][i] = eStone.EMPTY_STONE;
        
        mBoard[sy][sx] = eStone.BLACK_STONE;
        mBoard[sy + 1][sx + 1] = eStone.BLACK_STONE;
 
        mBoard[sy][sx + 1] = eStone.WHITE_STONE;
        mBoard[sy + 1][sx] = eStone.WHITE_STONE;
                
        return true;
    }
    
    public boolean SetStoneXY(int x_, int y_, eStone stoneId_)
    {
    	if(y_ < 0 || y_ >= Globals.BOARD_SIZE_Y)
    		return false;
    	
       	if(x_ < 0 || x_ >= Globals.BOARD_SIZE_X)
    		return false;
    	
        mBoard[y_][x_] = stoneId_; 
        return true;
    }
    
    public boolean SetStoneXY_Integer(int x_, int y_, int stoneId_)
    {
    	if(y_ < 0 || y_ >= Globals.BOARD_SIZE_Y)
    		return false;
    	
       	if(x_ < 0 || x_ >= Globals.BOARD_SIZE_X)
    		return false;
    	
       	if(stoneId_ == 0)
       		mBoard[y_][x_] = eStone.BLACK_STONE; 
       	else if(stoneId_ == 1)
       		mBoard[y_][x_] = eStone.WHITE_STONE; 
       	else if(stoneId_ == 2)
       		mBoard[y_][x_] = eStone.EMPTY_STONE; 
       	else
       		mBoard[y_][x_] = eStone.ERROR_STONE;
       		
       	return true;
    }    
    
    public eStone GetStoneXY(int x_, int y_) 
    {
    	if(y_ < 0 || y_ >= Globals.BOARD_SIZE_Y)
    		return eStone.ERROR_STONE;
    	
       	if(x_ < 0 || x_ >= Globals.BOARD_SIZE_X)
    		return eStone.ERROR_STONE;
	
        return mBoard[y_][x_];
    }
    
    public int GetStoneXY_Integer(int x_, int y_) 
    {
    	if(y_ < 0 || y_ >= Globals.BOARD_SIZE_Y)
    		return -1;
    	
       	if(x_ < 0 || x_ >= Globals.BOARD_SIZE_X)
    		return -1;

       	int iRes = -1;
       	if(mBoard[y_][x_] == eStone.BLACK_STONE)
       		iRes = 0;
       	else if(mBoard[y_][x_] == eStone.WHITE_STONE)
       		iRes = 1;
       	else if(mBoard[y_][x_] == eStone.EMPTY_STONE)
       		iRes = 2;
       	else
       		iRes = -1;
       	
       	return iRes;
    }
    
    
    //���ڷ� ���� ���� ������ ���Ѵ�
    public int	GetStoneCount(eStone o)
    {
    	int count = 0;
    	if(mBoard != null)
    	{
	    	for(int j = 0; j < Globals.BOARD_SIZE_Y; j++)
	    		for(int i = 0; i < Globals.BOARD_SIZE_Y; i++)
	    		{
	    			if( mBoard[j][i] == o )
	    				count++;
	    		}
    	}
    	return count;
    }
    
    //���ڷ� ���� ���� ���� ������ ���Ѵ�
    public int GetStoneBingoCount(eStone o)
    {
    	int count = 0;
    	boolean flag = false;
   
    	//������ ����� ���� NxN�� ���� ��� N + N + 2 �̴�. N�� ����, �� ���� N�� ����, ������ 2�� ���� ����Ѵ�.
    	if(mBoard != null)
    	{
    		//Y�� �������� N�� ���
    		for(int j = 0; j < Globals.BOARD_SIZE_Y; j++)
    		{
    			flag = false;
    			for(int i = 0; i < Globals.BOARD_SIZE_X; i++)
    			{
    				if( mBoard[j][i] != o )
    					flag = true;	
    			}
    			
    			if(flag != true)
    				count++;
    		}
    	
       		//X�� �������� N�� ���
    		for(int j = 0; j < Globals.BOARD_SIZE_Y; j++)
    		{
    			flag = false;
    			for(int i = 0; i < Globals.BOARD_SIZE_X; i++)
    			{
    				if( mBoard[i][j] != o )
    					flag = true;	
    			}
    			
    			if(flag != true)
    				count++;
    		}
 
    		//2���� �밢���� ��� �� �ϳ�
    		int i = 0;
    		int j = 0;
    		flag = false;
    		while( (i < (Globals.BOARD_SIZE_X)) && (j < (Globals.BOARD_SIZE_Y)) )
    		{
    			if(mBoard[j][i] != o)
    				flag = true;
    			i++;
    			j++;
    		}
    		
    		if(flag != true)
    			count++;

    		//2���� �밢���� ��� �� �ٸ� �ϳ�
    		i = 0; 
    		j = Globals.BOARD_SIZE_Y - 1;
    		flag = false;
    		while( (i < (Globals.BOARD_SIZE_X)) && (j >= 0) )
    		{
    			if(mBoard[j][i] != o)
    				flag = true;
    			i++;
    			j--;
    		}
    		
    		if(flag != true)
    			count++;
    		
    	}

    	return count;
    }
    
    //���ڷ� ���� ���� ���� ��ġ�� ���Ѵ�
    public ArrayList<Point> GetStoneBingoPosition(eStone o)
    {
    	int count = 0;
    	boolean flag = false;
    	ArrayList<Point> aryPos = new ArrayList<Point>();
  
    	aryPos.clear();
   
    	//������ ����� ���� NxN�� ���� ��� N + N + 2 �̴�. N�� ����, �� ���� N�� ����, ������ 2�� ���� ����Ѵ�.
    	if(mBoard != null)
    	{
    		//Y�� �������� N�� ���
    		for(int j = 0; j < Globals.BOARD_SIZE_Y; j++)
    		{
    			flag = false;
    			for(int i = 0; i < Globals.BOARD_SIZE_X; i++)
    			{
    				if( mBoard[j][i] != o )
    					flag = true;	
    			}
    			
    			if(flag != true)
    			{
    				for(int k = 0; k < Globals.BOARD_SIZE_X; k++)
    				{
    					if(j >= 0 && j < Globals.BOARD_SIZE_Y && k >= 0 && k < Globals.BOARD_SIZE_X)
    						aryPos.add(new Point(k, j));
    				}
    			
    				count++;
    			}
    		}
    	
       		//X�� �������� N�� ���
    		for(int j = 0; j < Globals.BOARD_SIZE_X; j++)
    		{
    			flag = false;
    			for(int i = 0; i < Globals.BOARD_SIZE_Y; i++)
    			{
    				if( mBoard[i][j] != o )
    					flag = true;	
    			}
    			
    			if(flag != true)
    			{
    				for(int k = 0; k < Globals.BOARD_SIZE_Y; k++)
    				{
    					if(j >= 0 && j < Globals.BOARD_SIZE_X && k >= 0 && k < Globals.BOARD_SIZE_Y)
    						aryPos.add(new Point(j, k));
    				}
		
    				count++;
    			}
    		}
 
    		//2���� �밢���� ��� �� �ϳ�
    		int i = 0;
    		int j = 0;
    		flag = false;
    		while( (i < (Globals.BOARD_SIZE_X)) && (j < (Globals.BOARD_SIZE_Y)) )
    		{
    			if(mBoard[j][i] != o)
    				flag = true;
    			i++;
    			j++;
    		}
    		
    		if(flag != true)
    		{
    			i = 0; 
    			j = 0;
    			
    			while( (i < (Globals.BOARD_SIZE_X)) && (j < (Globals.BOARD_SIZE_Y)) )
    			{
    				if(j >= 0 && j < Globals.BOARD_SIZE_Y && i >= 0 && i < Globals.BOARD_SIZE_X)
    				{
    					aryPos.add(new Point(i, j));
    				}

    				i++;
        			j++;
    			}
    			
    			count++;
    		}

    		//2���� �밢���� ��� �� �ٸ� �ϳ�
    		i = 0; 
    		j = Globals.BOARD_SIZE_Y - 1;
    		flag = false;
    		while( (i < (Globals.BOARD_SIZE_X)) && (j >= 0) )
    		{
    			if(mBoard[j][i] != o)
    				flag = true;
    			i++;
    			j--;
    		}
    		
    		if(flag != true)
    		{
    			i = 0; 
    			j = Globals.BOARD_SIZE_Y - 1;;
    			
    			while( (i < (Globals.BOARD_SIZE_X)) && (j >= 0) )
    			{
    				if(i >= 0 && i < Globals.BOARD_SIZE_X && j >= 0 && j < Globals.BOARD_SIZE_X)
    				{
    					aryPos.add(new Point(i, j));
    				}

    				i++;
        			j--;
    			}
    			
    			count++;
    		}
    		
    	}

    	return aryPos;
    }    
}
