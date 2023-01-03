package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TimerRes {
	protected static Bitmap[] m_imgNum = new Bitmap[10];

	public TimerRes() 
	{
	}

	public Bitmap GetNum(int idx)
	{
		if (m_imgNum[idx] != null)
			return m_imgNum[idx];
		
		return null;
	}
	
	public boolean Setup(Context context)
	{
	    m_imgNum[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n0);
	    m_imgNum[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n1);
	    m_imgNum[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n2);
	    m_imgNum[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n3);
	    m_imgNum[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n4);
	    m_imgNum[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n5);
	    m_imgNum[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n6);
	    m_imgNum[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n7);
	    m_imgNum[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n8);
	    m_imgNum[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.n9);

	    return true;
	}	
	
}
