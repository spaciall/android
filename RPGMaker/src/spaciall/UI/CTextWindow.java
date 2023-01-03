package spaciall.UI;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class CTextWindow extends CWindow{
	
	protected String m_strText = "";
	protected Paint	 m_pntText = new Paint(Globals.DEFAULT_PAINT);

	protected PointF m_ptText = new PointF(0, 0);
	
	public CTextWindow()
	{
	}
	
	@Override
	public int OnLButtonDown(PointF ptPos, boolean focusFlag) {
		// TODO Auto-generated method stub
		int res = super.OnLButtonDown(ptPos, focusFlag);
		if(res > 0)	return res;
		
		return 0;
	}
	
	
	@Override
	public void OnSelfPaint(GL10 gl) {
		// TODO Auto-generated method stub
		super.OnSelfPaint(gl);

		if(gl != null)
			if(m_pntText != null)
				if(m_ptPos != null)
					if(m_strText.length() > 0)
					{
						String[] aryString = m_strText.split("\n");
						
						RectF rect = GetRect();
						
						float sx = Globals.DEVICE_WIDTH * (rect.left + m_ptText.x);
						float sy = Globals.DEVICE_HEIGHT * (rect.top + m_ptText.y);
						float dx = 0;
						float dy = m_pntText.descent() - m_pntText.ascent();
						
						for(int j = 0; j < aryString.length; j++) {
							for (int i = 0; i < aryString[j].length(); ++i) {
								/*
								canvas.drawText(
										aryString[j].substring(i, i + 1), sx + dx, 
										sy + dy, 
										m_pntText);
								*/
								
								float curCharWidth = m_pntText.measureText(aryString[j].substring(i, i + 1));
								dx += curCharWidth;
								
								if ((sx + dx + curCharWidth) >= Globals.DEVICE_WIDTH * (rect.left + m_nWidth)) {
									dx = 0;
									dy +=  m_pntText.getFontSpacing();
								}
							}
						
							dx = 0;
							dy +=  m_pntText.getFontSpacing();
						}
					}
	}

	// 폰트
	public void SetTextSize(float o) { m_pntText.setTextSize(o); };
	
	//프로퍼티
	public void SetText(String o) { m_strText = o; }
	public void SetTextPos(PointF o) {m_ptText = o; }
	public void SetTextPos(float x, float y) { m_ptText.x = x; m_ptText.y = y; }
}
