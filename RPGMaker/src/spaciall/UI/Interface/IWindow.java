package spaciall.UI.Interface;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;
import android.graphics.RectF;

public interface IWindow {
	public enum EWindow{
		WINDOW_IO_SUCCESS,
		WINDOW_IO_FAIL,
	};	
	
	public enum EZPos {
		NORMAL,
		TOP_SIBLING,
	};
	
	public RectF GetRect();
	
	//Graphic 관련
	public void OnPrePaint(GL10 gl);			//자신을 그리기 전 작업을 작업을 한다.
	public void OnSelfPaint(GL10 gl);			//자신의 윈도우를 그린다.
	public void	OnPaint(GL10 gl);				//전체 윈도우를 그린다.
	public void OnPostPaint(GL10 gl);			//자신을 그린 후 나머지를 그린다.

	
	//IO 장치 관련
	public int OnLButtonDown(PointF ptPos, boolean focusFlag);
	public EWindow OnRButtonDown(PointF ptPos);

	public EWindow OnLDoubleClicked(PointF ptPos);
	public EWindow OnRDoubleClicked(PointF ptPos);
	
	public EWindow OnLButtonUp(PointF ptPos);
	public EWindow OnRButtonUp(PointF ptPos);
	
	public EWindow OnTrackBallMove(PointF ptPos);
	
	// 스크롤 관련
	public void SetCurScroll(int o);
	public int GetCurScroll();
}
