package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FxRes {
	protected Bitmap[][] m_imgChar = new Bitmap[8][10];
	
	public FxRes() 
	{
   		for (int j = 0; j < 8; j++) {
   			for(int i = 0; i < 10; i++) {
   				m_imgChar[j][i] = null;
   			}
   		}
	}

	public Bitmap GetFx(int x, int y)
	{
		if (m_imgChar[y][x] != null)
			return m_imgChar[y][x];
		
		return null;
	}
	
	public boolean Setup(Context context, int bmpId)
	{
	    // 디자이너 에게 받은 이미지 파일이 나뉘어 있어 임의로 하드코딩을 한다.
   		m_imgChar[0][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp1);
  		m_imgChar[0][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp2);
  		m_imgChar[0][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp3);
  		m_imgChar[0][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp4);
  		
   		m_imgChar[1][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp5);
  		m_imgChar[1][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp6);
  		m_imgChar[1][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp7);
  		m_imgChar[1][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp8);
   		m_imgChar[1][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp9);
  		m_imgChar[1][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp10);
  		m_imgChar[1][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp11);
  		m_imgChar[1][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp12);
	
   		m_imgChar[2][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp13);
  		m_imgChar[2][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp14);
  		m_imgChar[2][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp15);
  		m_imgChar[2][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp16);
   		m_imgChar[2][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp17);
  		m_imgChar[2][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp18);
  		m_imgChar[2][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp19);
  		m_imgChar[2][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp20);
  		m_imgChar[2][8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp21);
  		m_imgChar[2][9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp22);

   		m_imgChar[3][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp23);
  		m_imgChar[3][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp24);
  		m_imgChar[3][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp25);
  		m_imgChar[3][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp26);
   		m_imgChar[3][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp27);
  		m_imgChar[3][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp28);
  		m_imgChar[3][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp29);
  		m_imgChar[3][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp30);

   		m_imgChar[4][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp31);
  		m_imgChar[4][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp32);
  		m_imgChar[4][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp33);
  		m_imgChar[4][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp34);
   		m_imgChar[4][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp35);

   		m_imgChar[5][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp36);
  		m_imgChar[5][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp37);
  		m_imgChar[5][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp38);
  		m_imgChar[5][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp39);
   		m_imgChar[5][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp40);
  		m_imgChar[5][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp41);
  		m_imgChar[5][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp42);
   		m_imgChar[5][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp43);
		
   		m_imgChar[6][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp44);
  		m_imgChar[6][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp45);
  		m_imgChar[6][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp46);
  		m_imgChar[6][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp47);
   		m_imgChar[6][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp48);
  		m_imgChar[6][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp49);
  		m_imgChar[6][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp50);
   		m_imgChar[6][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp51);
 
  		m_imgChar[7][0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp52);
  		m_imgChar[7][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp53);
  		m_imgChar[7][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp54);
  		m_imgChar[7][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp55);
   		m_imgChar[7][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp56);
  		m_imgChar[7][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp57);
  		m_imgChar[7][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp58);
   		m_imgChar[7][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wp59);
 
   		   		
  		// 이미지를 확대/축소 한다.
   		/*
   		for (int j = 0; j < 8; j++) {
   			for(int i = 0; i < 10; i++) {
   				if( m_imgChar[j][i] != null) 
   					m_imgChar[j][i] = Bitmap.createScaledBitmap(m_imgChar[j][i], 32, 32, true);
   			}
   		}
   		*/
    			return true;
	}
}
