package net.SearchGame.co.kr;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;

public class FxImgMgr {
	protected ArrayList<FxImg> m_List = new ArrayList<FxImg>();
	protected FxRes m_FxRes = new FxRes();
	
	boolean Setup(Context context, int bmpId)
	{
		m_FxRes.Setup(context, bmpId);
		Parse();
		
		return true;
	}
	
	FxImg GetImg(int index)
	{
		if (index >= 0 && index < m_List.size())
				return m_List.get(index);
		
		return null;
	}
	
	Bitmap GetImg(int x, int y)
	{
		return m_FxRes.GetFx(x, y);
	}
	
	boolean Parse()
	{
		int count = 0;
		
		for (int j = 0; j < 8; j++) {
			FxImg fxImg = new FxImg();
			count = 0;
			
			for(int i = 0; i < 10; i++) {
				Bitmap bitmap = m_FxRes.GetFx(i, j);
			
				if(bitmap != null) fxImg.SetImg(count++, bitmap);
			}
			
			m_List.add(fxImg);
		}
	
		return true;
	}
	
}
