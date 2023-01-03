package net.SearchGame.co.kr;

import android.graphics.Canvas;

public class Fx {
	protected FxAni m_FxAni = null;
	protected FxMgr m_FxMgr = new FxMgr();  
	
	protected int m_iX = 0, m_iY = 0;
	protected int m_iWidth = 0, m_iHeight = 0;
	protected int m_iId = -1;
	protected int m_iCurLifeSpan = 0;
	protected int m_iLastLifeSpan = 0;
	protected int m_iColliedBoard = 0;
	
	protected int m_iSX = 0;
	protected int m_iSY = 0;
	
	protected int m_iDamage = 0;
	
	Fx(FxMgr fxMgr, FxAni fxAni, int id, int sx, int sy, int x, int y, int width, int height,  int damage, int colliedBoard, int lifespan)
	{
		m_iX = x;
		m_iY = y;
		
		m_iWidth = width;
		m_iHeight = height;
		
		m_iSX = sx;
		m_iSY = sy;
		
		m_iDamage = damage;
		
		m_iColliedBoard = colliedBoard;
		m_iLastLifeSpan = lifespan;

		m_iId = id;
		
		m_FxAni = fxAni;
		m_FxMgr = fxMgr;
	
		if (m_FxMgr != null) {
			if (m_FxMgr.Find(this) == false) { 
				m_FxMgr.Add(this);
			}
		}	
	}
	
	void SetX(int o)
	{
		m_iX = o;
	}
	
	void SetY(int o)
	{
		m_iY = o;
	}
	
	int GetID()
	{
		return m_iId;
	}
	
	int GetDamage()
	{
		return m_iDamage;
	}
	
	boolean IsCrashed(Char chr)
	{
		int chr_left = chr.GetDispX();
		int chr_top = chr.GetDispY();
		int chr_right = chr_left + 32;
		int chr_bottom = chr_top + 32;
		
		int left =   m_iX + m_iSX + m_iColliedBoard;
		int top =  	 m_iY + m_iSY + m_iColliedBoard;
		int right =  m_iX + m_iSX + m_iWidth - m_iColliedBoard;
		int bottom = m_iY + m_iSY + m_iHeight - m_iColliedBoard;
		
		if ( left >= chr_right || right <= chr_left || bottom <= chr_top || top >= chr_bottom)
			return false;
	
		return true;
	}
	
	boolean IsLifeEnd()
	{
		if (m_iCurLifeSpan >= m_iLastLifeSpan)
			return true;
			
		return false;
	}
	
	boolean Update()
	{
		if (m_iCurLifeSpan < m_iLastLifeSpan) {
			m_FxAni.Update();
			
			++m_iCurLifeSpan;
		}
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		if (m_iCurLifeSpan < m_iLastLifeSpan) { 
			m_FxAni.SetX(m_iSX + m_iX);
			m_FxAni.SetY(m_iSY + m_iY);
			m_FxAni.SetWidth(m_iWidth);
			m_FxAni.SetHeight(m_iHeight);
			m_FxAni.Render(canvas);
		}
		
		return true;
	}
	
	
}
