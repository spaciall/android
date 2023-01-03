package net.SearchGame.co.kr;

import android.graphics.Bitmap;

public class FxImg {
	protected Bitmap m_imgChar[] = new Bitmap[10];
	
	public FxImg()
	{
		for(int i = 0; i < 10; i++) m_imgChar[i] = null;
	}
	
	public void SetImg(int index, Bitmap bitmap)
	{
		if(bitmap == null) return;
	
		m_imgChar[index]= bitmap;
	}
	
	public Bitmap GetImg(int index)
	{
		return m_imgChar[index];
	}
	
}
