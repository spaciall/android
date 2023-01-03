package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;


public class Map {
	protected final int MAP_WIDTH = 14;
	protected final int MAP_HEIGHT = 10;

	protected MapRes m_MapRes = new MapRes();
	
	protected Point m_iData[][] = new Point[MAP_HEIGHT][MAP_WIDTH];	
	
	Map()
	{
		Clear();
	}
	
	void Clear()
	{
		for (int j = 0; j < MAP_HEIGHT; j++) {
			for (int i = 0; i < MAP_WIDTH; i++) {
				m_iData[j][i] = new Point(13, 6);
			}
		}		
	}
	
	int GetWidthByPixel()
	{
		return (MAP_WIDTH * 32);
	}
	
	int GetHeightByPixel()
	{
		return (MAP_HEIGHT * 32);
	}
	
	boolean Setup(Context context)
	{
		m_MapRes.Setup(context);
		
		return true;
	}
	
	void SetXY(int x, int y, Point o)
	{
		m_iData[y][x] = o;
	}
	
	Point GetXY(int x, int y)
	{
		return m_iData[y][x];
	}
	
	boolean Render(Canvas canvas)
	{
		for (int j = 0; j < MAP_HEIGHT; j++) {
			for (int i = 0; i < MAP_WIDTH; i++) {
				Point pt = m_iData[j][i];
				canvas.drawBitmap(m_MapRes.GetTile(pt.x, pt.y), (i * 32), (j * 32), null);
			}
		}
		
		return true;
	}
}
