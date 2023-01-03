package net.SearchGame.co.kr;

import java.util.ArrayList;

import net.SearchGame.co.kr.CharImg.Dir;
import android.content.Context;
import android.graphics.Bitmap;

public class CharImgMgr {
	protected ArrayList<CharImg> m_List = new ArrayList<CharImg>();
	protected CharRes m_Char0 = new CharRes();
	
	boolean Setup(Context context, int bmpId)
	{
		m_Char0.Setup(context, bmpId);
		Parse();
		
		return true;
	}
	
	CharImg GetImg(int index)
	{
		if (index >= 0 && index < m_List.size())
				return m_List.get(index);
		
		return null;
	}
	
	Bitmap GetImg(int x, int y)
	{
		return m_Char0.GetChar(x, y);
	}
	
	boolean Parse()
	{
		for (int j = 0; j < 12; j++) {
			Bitmap bitmap0 = m_Char0.GetChar(0, j);
			Bitmap bitmap1 = m_Char0.GetChar(1, j);
			Bitmap bitmap2 = m_Char0.GetChar(2, j);
			Bitmap bitmap3 = m_Char0.GetChar(3, j);
			Bitmap bitmap4 = m_Char0.GetChar(4, j);
			Bitmap bitmap5 = m_Char0.GetChar(5, j);
			Bitmap bitmap6 = m_Char0.GetChar(6, j);
			Bitmap bitmap7 = m_Char0.GetChar(7, j);	
			
			CharImg charImg = new CharImg();
			charImg.SetImg(Dir.UP1	 , bitmap0);
			charImg.SetImg(Dir.UP2	 , bitmap1);
			charImg.SetImg(Dir.RIGHT1, bitmap2);
			charImg.SetImg(Dir.RIGHT2, bitmap3);
			charImg.SetImg(Dir.DOWN1 , bitmap4);
			charImg.SetImg(Dir.DOWN2 , bitmap5);
			charImg.SetImg(Dir.LEFT1 , bitmap6);
			charImg.SetImg(Dir.LEFT2 , bitmap7);
		
			m_List.add(charImg);
		}
		
		for (int j = 0; j < 12; j++) {
			Bitmap bitmap0 = m_Char0.GetChar(8, j);
			Bitmap bitmap1 = m_Char0.GetChar(9, j);
			Bitmap bitmap2 = m_Char0.GetChar(10, j);
			Bitmap bitmap3 = m_Char0.GetChar(11, j);
			Bitmap bitmap4 = m_Char0.GetChar(12, j);
			Bitmap bitmap5 = m_Char0.GetChar(13, j);
			Bitmap bitmap6 = m_Char0.GetChar(14, j);
			Bitmap bitmap7 = m_Char0.GetChar(15, j);	
			
			CharImg charImg = new CharImg();
			charImg.SetImg(Dir.UP1	 , bitmap0);
			charImg.SetImg(Dir.UP2	 , bitmap1);
			charImg.SetImg(Dir.RIGHT1, bitmap2);
			charImg.SetImg(Dir.RIGHT2, bitmap3);
			charImg.SetImg(Dir.DOWN1 , bitmap4);
			charImg.SetImg(Dir.DOWN2 , bitmap5);
			charImg.SetImg(Dir.LEFT1 , bitmap6);
			charImg.SetImg(Dir.LEFT2 , bitmap7);
		
			m_List.add(charImg);
		}	
		
		return true;
	}
	
}
