package net.SearchGame.co.kr;

import android.graphics.Bitmap;

public class CharImg {
	enum Dir{
		UP1, UP2,
		LEFT1, LEFT2,
		DOWN1, DOWN2,
		RIGHT1, RIGHT2
	}
	
	protected Bitmap m_imgChar[] = new Bitmap[8];
	
	public CharImg()
	{
	}
	
	public void SetImg(Dir o, Bitmap bitmap)
	{
		if(bitmap == null) return;
		
		switch (o)
		{
		case UP1:
			m_imgChar[0] = bitmap;
			break;
	
		case UP2:
			m_imgChar[1] = bitmap;
			break;

		case RIGHT1:
			m_imgChar[2] = bitmap;
			break;

		case RIGHT2:
			m_imgChar[3] = bitmap;
			break;

		case DOWN1:
			m_imgChar[4] = bitmap;
			break;

		case DOWN2:
			m_imgChar[5] = bitmap;
			break;

		case LEFT1:
			m_imgChar[6] = bitmap;
			break;

		case LEFT2:
			m_imgChar[7] = bitmap;
			break;
		}
	}
	
	public Bitmap GetImg(Dir o)
	{
		switch (o)
		{
		case UP1:
			return m_imgChar[0];
	
		case UP2:
			return m_imgChar[1];

		case RIGHT1:
			return m_imgChar[2];

		case RIGHT2:
			return m_imgChar[3];

		case DOWN1:
			return m_imgChar[4];

		case DOWN2:
			return m_imgChar[5];

		case LEFT1:
			return m_imgChar[6];

		case LEFT2:
			return m_imgChar[7];
		}
		
		return null;
	}
	
}
