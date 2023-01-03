package com.android.othello;

public class CellBoard {
	private final int m_nWidth = Globals.BOARD_SIZE_X;
	private final int m_nHeight = Globals.BOARD_SIZE_Y;
	
	private eCell m_aryCell[][];
	
	CellBoard()
	{
		m_aryCell = new eCell[m_nWidth][m_nHeight];
		Initialize();
	}
	
	void Initialize()
	{
		for(int j = 0; j < m_nHeight; j++)
			for(int i = 0; i < m_nWidth; i++)
			{
				m_aryCell[j][i] = eCell.NORMAL_CELL;
			}
	}
	
	void SetCellXY(int x, int y, eCell c)
	{
		if(x < 0 || x >= m_nWidth)
			return;
		if(y < 0 || y >= m_nHeight)
			return;
		
		m_aryCell[y][x] = c;
	}
	
	eCell GetCellXY(int x, int y)
	{
		if(x < 0 || x >= m_nWidth)
			return eCell.ERROR_CELL;
		if(y < 0 || y >= m_nHeight)
			return eCell.ERROR_CELL;
		
		return m_aryCell[y][x];
	}
}
