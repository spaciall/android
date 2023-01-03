package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ButtonWindow {
	protected Bitmap m_imgBitmap = null;
	protected ButtonWindowMgr m_ButtonWindowMgr = null;
	protected Bitmap m_imgBackGrnd = null;
	
	protected Rect m_srcRect = new Rect();
	protected Rect m_dstRect = new Rect();
	
	protected int m_iId = -1;
	
	protected int m_iX = 0, m_iY = 0;
	protected int m_iWidth = 0, m_iHeight = 0;
	
	protected boolean m_bSelected = false;
	
	boolean Setup(Context context, int id, Bitmap bitmap, int x, int y, int w, int h)
	{
		m_imgBackGrnd = BitmapFactory.decodeResource(context.getResources(), R.drawable.window0);
		
		m_iId = id;
		m_imgBitmap = bitmap;
		
		m_iX = x;
		m_iY = y;
		
		m_iWidth = w;
		m_iHeight = h;
		
		return true;
	}
	
	boolean OnClickedXY(int x, int y)
	{
		if ((m_iX < x) && ((m_iX + m_iWidth) > x) && (m_iY < y) && ((m_iY + m_iHeight) > y)) {
			m_bSelected = true;
			return true;
		}
		
		return false;
	}
	
	void SetSelected(boolean o)
	{
		m_bSelected = o;
	}
	
	int GetID()
	{
		return m_iId;
	}
	
	boolean Update()
	{
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		if (m_bSelected == true) { 
			m_srcRect.left = 0;
			m_srcRect.top = 0;
			m_srcRect.right = m_imgBackGrnd.getWidth();
			m_srcRect.bottom = m_imgBackGrnd.getHeight();
			
			m_dstRect.left = m_iX;
			m_dstRect.top = m_iY;
			m_dstRect.right = m_iX + m_iWidth;
			m_dstRect.bottom = m_iY + m_iHeight;
	
			canvas.drawBitmap(m_imgBackGrnd, m_srcRect, m_dstRect, null);
		}
	
		if (m_imgBitmap != null) {
			m_srcRect.left = 0;
			m_srcRect.top = 0;
			m_srcRect.right = m_imgBitmap.getWidth();
			m_srcRect.bottom = m_imgBitmap.getHeight();
			
			m_dstRect.left = m_iX;
			m_dstRect.top = m_iY;
			m_dstRect.right = m_iX + m_iWidth;
			m_dstRect.bottom = m_iY + m_iHeight;
			
			 canvas.drawBitmap(m_imgBitmap, m_srcRect, m_dstRect, null);
		}
		
		return true;
	}
}
