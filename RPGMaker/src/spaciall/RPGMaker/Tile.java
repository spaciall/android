package spaciall.RPGMaker;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;


public class Tile extends TileBase {
	public int m_Id = 0;
	public int m_Walkable = 0;
	
	protected static final int WIDTH_NUM = 32;
	protected static final int HEIGHT_NUM = 16;

	protected static boolean[] m_walkableIdPool = new boolean[WIDTH_NUM * HEIGHT_NUM];

	
	public static boolean Setup(GL10 gl, Context context, int id) {

		m_Tex.LoadTexture( gl, context, id );
		
		for (int i = 0; i < WIDTH_NUM * HEIGHT_NUM; ++i) {
			if (i >= 0 && i <= 9) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 16 && i <= 21) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 98 && i <= 99) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 130 && i <= 131) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 103 && i <= 108) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 135 && i <= 140) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 168 && i <= 171) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 200 && i <= 203) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 218 && i <= 219) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 250 && i <= 251) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 286 && i <= 287) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 318 && i <= 319) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 344 && i <= 351) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 376 && i <= 383) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 408 && i <= 413) m_walkableIdPool[i] = true; // 통과 가능
			else if (i >= 440 && i <= 441) m_walkableIdPool[i] = true; // 통과 가능
			else m_walkableIdPool[i] = false; // 통과 불가능
		}
		
		return true;
	}
	
	public boolean IsWalkable() {
		return m_walkableIdPool[m_Id];
	}
	
	public boolean Render(GL10 gl, int x, int y) {
		
		int m_IdY = m_Id / Tile.WIDTH_NUM;
		int m_IdX = m_Id % Tile. WIDTH_NUM;

		m_Tex.DrawTexture(gl, x * WIDTH_MOVE_PIXEL, y * HEIGHT_MOVE_PIXEL,  
				m_IdX * WIDTH_MOVE_PIXEL, m_IdY * HEIGHT_MOVE_PIXEL, WIDTH_MOVE_PIXEL, HEIGHT_MOVE_PIXEL, 
				0, 0, 0, 
				1.f, 1.f);
		
		return true;
	}	
}
