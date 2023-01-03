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
	
	//Graphic ����
	public void OnPrePaint(GL10 gl);			//�ڽ��� �׸��� �� �۾��� �۾��� �Ѵ�.
	public void OnSelfPaint(GL10 gl);			//�ڽ��� �����츦 �׸���.
	public void	OnPaint(GL10 gl);				//��ü �����츦 �׸���.
	public void OnPostPaint(GL10 gl);			//�ڽ��� �׸� �� �������� �׸���.

	
	//IO ��ġ ����
	public int OnLButtonDown(PointF ptPos, boolean focusFlag);
	public EWindow OnRButtonDown(PointF ptPos);

	public EWindow OnLDoubleClicked(PointF ptPos);
	public EWindow OnRDoubleClicked(PointF ptPos);
	
	public EWindow OnLButtonUp(PointF ptPos);
	public EWindow OnRButtonUp(PointF ptPos);
	
	public EWindow OnTrackBallMove(PointF ptPos);
	
	// ��ũ�� ����
	public void SetCurScroll(int o);
	public int GetCurScroll();
}
