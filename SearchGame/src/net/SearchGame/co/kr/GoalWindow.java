package net.SearchGame.co.kr;

import net.SearchGame.co.kr.CharImg.Dir;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GoalWindow {
	protected Bitmap m_bmpBoard = null;
	protected CharMgr m_CharMgr = null;

	protected int m_iX, m_iY;
	protected int m_iBoard;
	protected int m_iMax;
	
	GoalWindow(CharMgr charMgr, int x, int y, int board, int max)
	{
		m_CharMgr = charMgr;

		m_iX = x;
		m_iY = y;
		
		m_iBoard = board;
		m_iMax = max;
	
	}
	
	boolean Render(Canvas canvas)
	{
		if(m_bmpBoard != null)
			canvas.drawBitmap(m_bmpBoard, (m_iX - m_iBoard), (m_iY - m_iBoard), null);
		
		int count = 0;
		
		for(int i = 0 ; i < m_CharMgr.GetSize(); i++) {
			canvas.drawBitmap(m_CharMgr.GetChar(i).GetCharAni().GetCharImg().GetImg(Dir.DOWN1), m_iX  , m_iY + (count * (32 + m_iBoard)), null);
			
			if(i >= (m_iMax - 1)) break;
			
			++count;
		}
		
		return true;
	}
	
	
	
}
