package net.SearchGame.co.kr;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FxAni {
	protected FxImg m_FxImg = null;
	protected Bitmap m_CurBitmap = null;

	protected Paint m_Paint = null;

	protected int m_iCurFrame = 0;
	protected int m_iMaxFrame = 0;
	
	protected int m_iX = 0, m_iY = 0;
	protected int m_iWidth = 0, m_iHeight = 0;
	
	protected Rect m_rtSrcRect = new Rect();
	protected Rect m_rtDstRect = new Rect();
	
	boolean Setup(FxImgMgr fxImgMgr, int fxIdx, int maxFrame, Paint paint)
	{
		m_FxImg = fxImgMgr.GetImg(fxIdx);
		m_iMaxFrame = maxFrame;
		m_Paint = paint;
		
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
	
	void SetWidth(int o)
	{
		m_iWidth = o;
	}

	void SetHeight(int o)
	{
		m_iHeight = o;
	}
	
	FxImg GetFxImg()
	{
		return m_FxImg;
	}
	
	boolean Update()
	{
		m_CurBitmap = m_FxImg.GetImg(m_iCurFrame);
		
		if(++m_iCurFrame > (m_iMaxFrame - 1)) m_iCurFrame = 0;
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		if(m_CurBitmap != null) {
			m_rtSrcRect.left = 0;
			m_rtSrcRect.top = 0;
			m_rtSrcRect.right = m_CurBitmap.getWidth();
			m_rtSrcRect.bottom = m_CurBitmap.getHeight();
			
			m_rtDstRect.left = m_iX;
			m_rtDstRect.top = m_iY;
			m_rtDstRect.right = m_iX + m_iWidth;
			m_rtDstRect.bottom = m_iY + m_iHeight;
			
			canvas.drawBitmap(m_CurBitmap, m_rtSrcRect, m_rtDstRect, null);
		}
		
		return true;
	}
}
