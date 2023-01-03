package net.SearchGame.co.kr;

import android.graphics.Canvas;

public class ScreenQuake {
	protected int m_iCurMiliiSecond = 0;
	protected int m_iQuakeMilliSecond = 0;
	
	protected int m_iCurCount = 0;
	protected int m_iTargetCount = 0;
	
	protected int m_iTranslateX = 0, m_iTranslateY = 0;
	
	protected int m_iDir = -1;
	
	boolean Setup(int iQuakeMilliSecond, int iTranslateX, int iTranslateY, int iTargetCount)
	{
		m_iQuakeMilliSecond = iQuakeMilliSecond;
		m_iCurMiliiSecond = 0;
		
		m_iTranslateX = iTranslateX;
		m_iTranslateY = iTranslateY;
		
		m_iCurCount = iTargetCount;
		m_iTargetCount = iTargetCount;
		
		m_iDir = -1;
		
		return true;
	}
	
	boolean Start()
	{
		m_iCurCount = 0;
		
		return true;
	}
	
	boolean Stop()
	{
		m_iCurCount = m_iTargetCount;
		
		return true;
	}
	
	boolean Update(int elapsedTime)
	{
		if (m_iCurCount < m_iTargetCount) {
			if (m_iCurMiliiSecond >= m_iQuakeMilliSecond) m_iCurMiliiSecond = 0;
		
			m_iCurMiliiSecond += elapsedTime;
		
			if (m_iCurMiliiSecond > m_iQuakeMilliSecond) m_iCurMiliiSecond = m_iQuakeMilliSecond;
		
			m_iDir *= -1;
			++m_iCurCount;

		}
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		if (m_iCurCount < m_iTargetCount) {
			if (m_iCurMiliiSecond >= m_iQuakeMilliSecond) { 
				canvas.translate((m_iDir *m_iTranslateX), (m_iDir *m_iTranslateY));
			}
			
		}
		return true;
	}
}
