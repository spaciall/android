package net.SearchGame.co.kr;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharAni {
	enum Dir
	{
		UP,
		LEFT,
		RIGHT,
		DOWN
	}
	
	protected CharImg m_CharImg = null;
	protected Bitmap m_CurBitmap = null;

	protected Dir m_eCurDir = Dir.DOWN;
	protected int m_iCurFrame = 0;
	
	protected int m_iX = 0, m_iY = 0;
	
	boolean Setup(CharImgMgr charImgMgr, int chrIdx, Dir dir)
	{
		m_eCurDir = dir;
		m_CharImg = charImgMgr.GetImg(chrIdx);
		m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.DOWN1);
			
		return true;
	}
	
	void SetX(int o)
	{
		m_iX = o;
	}
	
	void SetY(int o)
	{
		m_iY = o;
	}
	
	void SetDir(Dir o)
	{
		m_eCurDir = o;
	}
	
	Dir GetDir()
	{
		return m_eCurDir;
	}
	
	CharImg GetCharImg()
	{
		return m_CharImg;
	}
	
	boolean Update()
	{
		switch (m_eCurDir)
		{
		case UP:
			if (m_iCurFrame == 0)
				m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.UP1);
			else 
				m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.UP2);
			break;

		case LEFT:
			if (m_iCurFrame == 0) 
				m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.LEFT1);
			else 
				m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.LEFT2);
			break;
		
		case RIGHT:
			if (m_iCurFrame == 0) 
				m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.RIGHT1);
			else 
				m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.RIGHT2);
			break;
		
		case DOWN:
			if (m_iCurFrame == 0) 
				m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.DOWN1);
			else 
				m_CurBitmap = m_CharImg.GetImg(CharImg.Dir.DOWN2);
			break;
		}
		
		if(++m_iCurFrame > 1) m_iCurFrame = 0;
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		if (m_CurBitmap != null) canvas.drawBitmap(m_CurBitmap, m_iX, m_iY, null);
		return true;
	}
}
