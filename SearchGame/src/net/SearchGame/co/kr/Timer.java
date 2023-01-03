package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.Canvas;

public class Timer {
	protected TimerRes m_TimerRes = new TimerRes();
	protected int m_iX = 0, m_iY = 0;
	protected int m_iCurMilliSecond = 0;
	protected int m_iCurSecond = 0;
	protected int m_iStoredSecond = 0;
	
	boolean Setup(Context context, int x, int y, int second)
	{
		m_TimerRes.Setup(context);
		
		m_iX = x;
		m_iY = y;
		
		m_iCurSecond = second;
		m_iCurMilliSecond = 1000 * second;
		
		m_iStoredSecond = second;
		
		return true;
	}
	
	void Start()
	{
		m_iCurSecond = m_iStoredSecond;
		m_iCurMilliSecond = 1000 * m_iStoredSecond;
		
		m_iStoredSecond = m_iStoredSecond;
	}
	
	void SetX(int o)
	{
		m_iX = o;
	}
	
	void SetY(int o)
	{
		m_iY = o;
	}
	
	int GetCurSecond()
	{
		return m_iCurSecond;
	}
	
	boolean Update(int elapsedTime)
	{
		m_iCurMilliSecond -= elapsedTime;
		
		if (m_iCurMilliSecond < 0) m_iCurMilliSecond = 0;
		
		m_iCurSecond = m_iCurMilliSecond / 1000;
		
		if(m_iCurSecond < 0) m_iCurSecond = 0;
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		int count = 0;
		int iCurSecond = m_iCurSecond;
		
		while(iCurSecond > 0) {
			int iNum = iCurSecond % 10;
			iCurSecond /= 10;
			
			canvas.drawBitmap(m_TimerRes.GetNum(iNum), m_iX - (22 * count), m_iY, null);
			++count;
		}
		
		if (m_iCurSecond == 0) canvas.drawBitmap(m_TimerRes.GetNum(0), m_iX, m_iY, null);
		
		return true;
	}
}
