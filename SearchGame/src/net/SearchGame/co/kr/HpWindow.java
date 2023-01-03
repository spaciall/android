package net.SearchGame.co.kr;

import android.graphics.Canvas;
import android.graphics.Paint;

public class HpWindow {
	protected final int m_iBoard = 1;

	protected int m_iX = 0, m_iY = 0;
	protected int m_iWidth = 32, m_iHeight = 8;

	protected int m_iDispMaxHP = 32 - (2 * m_iBoard);
	protected int m_iDispHP = 32 - (2 * m_iBoard);
	
	protected int m_iMaxHP = 0;
	protected int m_iHP = 0;
	
	protected Paint m_ptBoard = new Paint();
	protected Paint m_ptDamage = new Paint();
	protected Paint m_ptHP = new Paint();

	void SetX(int o)
	{
		m_iX = o;
	}

	void SetY(int o)
	{
		m_iY = o;
	}
	
	void SetHP(int o)
	{
		m_iHP = o;
		
		if(m_iHP < 0) m_iHP = 0;
		if(m_iHP > m_iMaxHP) m_iHP = m_iMaxHP;
	}
	
	int GetHP()
	{
		if (m_iHP >= 0)
			return m_iHP;
		else
			return 0;
	}
	
	boolean Setup(int maxhp, int hp)
	{
		m_iMaxHP = maxhp;
		m_iHP = hp;
		
		m_ptBoard.setARGB(255, 0, 0, 0);
		m_ptDamage.setARGB(255, 255, 255, 255);
		m_ptHP.setARGB(255, 255, 0, 0);
	
		return true;
	}
	
	boolean Update()
	{
		float tmpHP = m_iHP;
		float tmpMaxHP = m_iMaxHP;
		float tmpRatioHP = (tmpHP / tmpMaxHP);
		
		tmpRatioHP *= m_iDispMaxHP;
		m_iDispHP =  (int)tmpRatioHP;
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		canvas.drawRect(m_iX, m_iY, m_iX + m_iWidth, m_iY + m_iHeight, m_ptBoard);
		canvas.drawRect(m_iX + m_iBoard, m_iY + m_iBoard, m_iX + m_iWidth - m_iBoard, m_iY + m_iHeight - m_iBoard, m_ptDamage);
		canvas.drawRect(m_iX + m_iBoard, m_iY + m_iBoard, m_iX + m_iBoard + m_iDispHP , m_iY + m_iHeight - m_iBoard, m_ptHP);
		
		return true;
	}
	
}
