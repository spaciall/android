package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MapRes {
	protected static Bitmap[][] m_imgTile = new Bitmap[8][16];
	
	public MapRes() 
	{
	}

	public Bitmap GetTile(int x, int y)
	{
		if (m_imgTile[y][x] != null)
			return m_imgTile[y][x];
		
		return null;
	}
	
	public boolean Setup(Context context)
	{
	    Bitmap imgSrc = BitmapFactory.decodeResource(context.getResources(), R.drawable.map);
	   
	    for (int j = 0; j < 8; j++) {
	    	for(int i = 0; i < 16; i++) {
	    		m_imgTile[j][i] = Bitmap.createBitmap(imgSrc, i * 32, j * 32, 32, 32);
	    	}
	    }
		
		return true;
	}
}
