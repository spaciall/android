package net.SearchGame.co.kr;

import net.SearchGame.co.kr.CharAni.Dir;
import android.graphics.Canvas;

public class Char {
	protected CharAni m_CharAni = null;
	protected CharMgr m_CharMgr = null;
	
	protected HpWindow m_HpWnd = new HpWindow();
	
	protected int m_iId = -1;
	protected int m_iHp = 100;
	
	Char(CharMgr charMgr, CharAni charAni, int id, int x, int y, int hp, int speed)
	{
		m_iX = x; 
		m_iY = y;
	
		m_iTileX = (int)(x / 32); 
		m_iTileY = (int)(y / 32);
		
		m_iDispX = x;
		m_iDispY = y;
		
		m_iToX = x;
		m_iToY = y;
		
		m_iSpeed = speed;
		
		m_iId = id;

		m_CharMgr = charMgr;
		m_CharAni = charAni;
		
		if (m_CharMgr != null) {
			if (m_CharMgr.Find(this) == false) { 
				m_CharMgr.Add(this);
			}
		}
		
		m_iHp = hp;
		
		m_HpWnd.Setup(hp, hp);
	}
	
	protected int m_iX = 0, m_iY = 0;
	protected int m_iDispX = 0, m_iDispY = 0;
	protected int m_iToX = 0, m_iToY = 0;
	protected int m_iTileX = 0, m_iTileY = 0;
	protected int m_iSpeed = 4;
	
	CharAni GetCharAni()
	{
		return m_CharAni;
	}
	
	int GetX()
	{
		return m_iX;
	}

	int GetY()
	{
		return m_iY;
	}
	
	int GetToX()
	{
		return m_iToX;
	}

	int GetToY()
	{
		return m_iToY;
	}
	
	int GetDispX()
	{
		return m_iDispX;
	}

	int GetDispY()
	{
		return m_iDispY;
	}		
	
	int GetID()
	{
		return m_iId;
	}
	
	int GetTileX()
	{
		return m_iTileX;
	}

	int GetTileY()
	{
		return m_iTileY;
	}
	
	void SetHP(int o)
	{
		m_iHp = o;
		
		if(m_iHp < 0) m_iHp = 0;
		
		m_HpWnd.SetHP(m_iHp);
	}
	
	int GetHP()
	{
		return m_iHp;
	}
	
	boolean CalcTileXY()
	{
		m_iTileX = m_iX / 32;
		m_iTileY = m_iY / 32;
		
		return true;
	}
	
	void RotateDir()
	{
		Dir dir = m_CharAni.GetDir();
		
		switch (dir)
		{
		case UP:
			m_CharAni.SetDir(Dir.LEFT);
			break;
		
		case LEFT:
			m_CharAni.SetDir(Dir.DOWN);
			break;
		
		case DOWN:
			m_CharAni.SetDir(Dir.RIGHT);
			break;
		
		case RIGHT:
			m_CharAni.SetDir(Dir.UP);
			break;
		}
	}
	
	boolean Update()
	{
		if ((m_iX == m_iToX) && (m_iY == m_iToY) && (m_iX == m_iDispX) && (m_iY == m_iDispY)) {
			int value = Rand.NextInt();
			if (value < 0) value = -(value);
			
			value %= 4;
			
			switch(value) {
				case 0:	//UP
					m_iToY = Math.max(0, m_iY - 32);
					m_CharAni.SetDir(Dir.UP);
					break;
					
				case 1:	//LEFT
					m_iToX = Math.max(0, m_iX - 32);
					m_CharAni.SetDir(Dir.LEFT);
					break;
				
				case 2:	//DOWN
					m_iToY = Math.min(320 - 32, m_iY + 32);
					m_CharAni.SetDir(Dir.DOWN);
					break;
				
				case 3:	//RIGHT
					m_iToX = Math.min(448 - 32, m_iX + 32);
					m_CharAni.SetDir(Dir.RIGHT);
					break;
			}
		}
		
		//현재 좌표와 목표 좌표가 다르다. 이동한다.
		if ((m_iDispX != m_iToX) || (m_iDispY != m_iToY)) {
		
			int delX = m_iToX - m_iX;
			int delY = m_iToY - m_iY;
		
			if(delX != 0) delX /= Math.abs(delX);	
			if(delY != 0) delY /= Math.abs(delY);

			int tmpX = m_iDispX;
			int tmpY = m_iDispY;
			
			m_iDispX += (delX * m_iSpeed);
			m_iDispY += (delY * m_iSpeed);
		
			if (m_CharMgr.IsInOther(this) == true) {
				m_iDispX = tmpX;
				m_iDispY = tmpY;
				
				//제 자리로 Back 한다.
				int tmpToX = m_iToX;
				int tmpToY = m_iToY;
				
				m_iToX = m_iX;
				m_iToY = m_iY;
				
				m_iX = tmpToX;
				m_iY = tmpToY;
			}
		}
		else {
			m_iX = m_iToX;
			m_iY = m_iToY;
			m_iDispX = m_iToX;
			m_iDispY = m_iToY;
		}
		
		CalcTileXY();
		m_CharAni.Update();
		m_HpWnd.Update();
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		m_CharAni.SetX(m_iDispX);
		m_CharAni.SetY(m_iDispY);
		m_CharAni.Render(canvas);
		
		m_HpWnd.SetX(m_iDispX);
		m_HpWnd.SetY(m_iDispY - 8);
		m_HpWnd.Render(canvas);
		
		return true;
	}
	
}
