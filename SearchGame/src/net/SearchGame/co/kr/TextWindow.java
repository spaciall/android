package net.SearchGame.co.kr;

import android.graphics.Canvas;
import android.graphics.Paint;


public class TextWindow {
	protected int m_iCurTime = 0;
	protected int m_iEndTime = 0;

	protected int m_iX = 0, m_iY = 0;
	protected Paint m_Paint = null;
	
	protected String m_Text = "";
	
	void Start(int x, int y, String text, int iEndTime, Paint paint)
	{
		m_iX = x;
		m_iY = y;
		
		m_Text = text;
		
		m_iCurTime = 0;
		m_iEndTime = iEndTime;
		
		m_Paint = paint;
	}
	
	void Stop()
	{
		m_iCurTime = m_iEndTime;
	}
	
	boolean Update(int elapsedTime) 
	{
		if (m_iCurTime < m_iEndTime) m_iCurTime += elapsedTime;
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		if (m_iCurTime < m_iEndTime) canvas.drawText(m_Text, m_iX, m_iY, m_Paint);
		
		return true;
	}
	
	

}
