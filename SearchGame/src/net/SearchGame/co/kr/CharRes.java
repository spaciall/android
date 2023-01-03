package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CharRes {
	static final int iCHAR_WIDTH = 32;
	static final int iCHAR_HEIGHT = 32;
	
	protected static Bitmap[][] m_imgChar = new Bitmap[12][16];
	
	public CharRes() 
	{
	}

	public Bitmap GetChar(int x, int y)
	{
		if (m_imgChar[y][x] != null)
			return m_imgChar[y][x];
		
		return null;
	}
	
	public boolean Setup(Context context, int bmpId)
	{
	    Bitmap imgSrc = BitmapFactory.decodeResource(context.getResources(), bmpId);
	   
	    for (int j = 0; j < 12; j++) {
	    	for(int i = 0; i < 16; i++) {
	    		m_imgChar[j][i] = Bitmap.createBitmap(imgSrc, i * 32, j * 32, 32, 32);
	    	}
	    }
		
		return true;
	}
}
