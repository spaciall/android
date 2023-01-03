package spaciall.UI;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import spaciall.RPGMaker.Texture;
import spaciall.UI.Interface.IWindow;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class CWindow implements IWindow {
	protected int m_nID = 0;
	
	protected PointF m_ptPos = new PointF(0, 0);
	protected float m_nWidth = 0;
	protected float m_nHeight = 0;

	protected Paint m_pntBackGround = new Paint(Globals.DEFAULT_PAINT);;
	protected Texture m_bmpBackGround = null;

	protected CWindow m_cParent = null;
	protected ArrayList<CWindow> m_cModales = new ArrayList<CWindow>();
	protected ArrayList<CWindow> m_cChilds = new ArrayList<CWindow>();

	protected boolean m_bMovable = false;
	protected boolean m_bFocus = false;
	protected boolean m_bCheckSiblingCollide = false;

	protected EZPos m_eZPos = EZPos.NORMAL;
	
	// 스크롤 관련
	protected CWindow m_wndCallBackWindow = null;

	public CWindow() {
	}

	public RectF GetRect() {
		RectF parentRect = null;
		if (m_cParent != null)
			parentRect = m_cParent.GetRect();

		RectF selfRect = new RectF(m_ptPos.x, m_ptPos.y,
				(m_ptPos.x + m_nWidth), (m_ptPos.y + m_nHeight));

		if (parentRect != null) {
			selfRect.bottom += parentRect.top;
			selfRect.left += parentRect.left;
			selfRect.right += parentRect.left;
			selfRect.top += parentRect.top;
		}

		return selfRect;
	}

	public RectF GetRealRect() {
		RectF rect = GetRect();
		return new RectF(rect.left * Globals.DEVICE_WIDTH, rect.top
				* Globals.DEVICE_HEIGHT, (rect.left + m_nWidth)
				* Globals.DEVICE_WIDTH, (rect.top + m_nHeight)
				* Globals.DEVICE_HEIGHT);
	}

	public void OnPaint(GL10 gl) {
		OnPrePaint(gl); // 그리기 전 밑 작업을 한다.
		OnSelfPaint(gl); // 자신의 배경을 그려준다.
		OnPostPaint(gl); // 자식 윈도우를 그려준다.
	}

	// Grapihc 관련
	public void OnPrePaint(GL10 gl) {
	}

	public void OnSelfPaint(GL10 gl) {
		// TODO Auto-generated method stub
		if (gl != null) {
			if (m_pntBackGround != null) {
				if (m_bmpBackGround != null) {
					Rect rtSrc = new Rect(0, 0, m_bmpBackGround.imgWidth,
							m_bmpBackGround.imgHeight);
					
					RectF realRect = GetRealRect();
					m_bmpBackGround.DrawTexture(gl, realRect.left, realRect.top, 0, 0, m_bmpBackGround.imgWidth, m_bmpBackGround.imgHeight, 0, 0, 0, 1.0f, 1.0f);
				} else {
					// 기본 색상으로 윈도우를 칠해준다.
				}
			}
		}
	}

	public void OnPostPaint(GL10 gl) {
		// 자식 윈도우를 그려준다.
		for (int i = 0; i < m_cChilds.size(); ++i) {
			CWindow wndChild = m_cChilds.get(i);
			if (wndChild != null)
				wndChild.OnPaint(gl);
		}
	}

	// IO 장치관련
	public int OnLButtonDown(PointF ptPos, boolean focusFlag) {
		m_bFocus = false;

		// 자식 윈도우에게 메세지를 보내준다.
		int res = 0;	
		for (int i = m_cChilds.size() - 1; i >= 0; --i) {
			CWindow wndChild = m_cChilds.get(i);
			if (wndChild != null) {
				// 하나의 윈도우 에서 메세지를 처리 했으면 다른 윈도우 에선 메세지를 처리 할 수 없다.
				int tmp = wndChild.OnLButtonDown(ptPos, focusFlag);
				if (tmp > 0) {
					focusFlag = true;
					
					if (tmp > 0 && res <= 0) res = tmp;
				}
			}
		}

		if (focusFlag == true)
			return res;

		// 자식 윈도우 에선 메세지를 처리 하지 못했지만
		// 현재의 윈도우는 메세지를 처리했다.
		if (true == GetRect().contains(ptPos.x, ptPos.y)) {
			m_bFocus = true;

			// 윈도우의 Z좌표를 상신시켜 준다.
			if (m_cParent != null)
				m_cParent.SetZPositionTopMost(this);

			return m_nID;
		}

		return res;
	}

	// 자식 윈도우를 찾아서 지워준다.
	public boolean RemoveChild(CWindow o) {
		for (int i = 0; i < m_cChilds.size(); ++i) {
			if (m_cChilds.get(i) == o) {
				m_cChilds.remove(i);
				return true;
			}
		}
		return false;
	}

	// 자신의 자식 윈도우 중 인자로 온 윈도우를 최상위 윈도우로 교체한다.
	public boolean SetZPositionTopMost(CWindow o) {
		int n = 0;

		if (m_cChilds != null) {
			int size = m_cChilds.size();

			for (int i = 0; i < size; ++i) {
				if (m_cChilds.get(i) == o) {
					n = i;
					break;
				}
			}

			// 재귀 - 계속해서 타고 올라간다.
			if (m_cParent != null)
				m_cParent.SetZPositionTopMost(this);

			if (n >= 0 && n < (size - 1)) {
				for (int i = (size - 1); i > n; --i) {
					if (m_cChilds.get(i).GetEZpos() != EZPos.TOP_SIBLING) {
						m_cChilds.set(n, m_cChilds.get(i));
						m_cChilds.set(i, o);

						return true;
					}
				}
			}
		}

		return false;
	}

	public EWindow OnRButtonDown(PointF ptPos) {
		if (false == GetRect().contains(ptPos.x, ptPos.y))
			return EWindow.WINDOW_IO_FAIL;

		return EWindow.WINDOW_IO_SUCCESS;
	}

	public EWindow OnLDoubleClicked(PointF ptPos) {
		if (false == GetRect().contains(ptPos.x, ptPos.y))
			return EWindow.WINDOW_IO_FAIL;

		return EWindow.WINDOW_IO_SUCCESS;
	}

	public EWindow OnRDoubleClicked(PointF ptPos) {
		if (false == GetRect().contains(ptPos.x, ptPos.y))
			return EWindow.WINDOW_IO_FAIL;

		return EWindow.WINDOW_IO_SUCCESS;
	}

	public EWindow OnTrackBallMove(PointF ptPos) {
		// 자식 윈도우에게 메세지를 보내준다.
		for (int i = 0; i < m_cChilds.size(); i++) {
			CWindow wndChild = m_cChilds.get(i);
			if (wndChild != null) {
				// 하나의 윈도우 에서 메세지를 처리 했으면 다른 윈도우 에선 메세지를 처리 할 수 없다.
				if (wndChild.OnTrackBallMove(ptPos) == EWindow.WINDOW_IO_SUCCESS)
					return EWindow.WINDOW_IO_SUCCESS;
			}
		}

		if (m_bMovable == true) {
			if (m_bFocus == true) {
				float old_x = m_ptPos.x;
				float old_y = m_ptPos.y;
				
				m_ptPos.x += ptPos.x;
				m_ptPos.y += ptPos.y;

				// 이동은 부모 윈도우를 벗어날 수 없다.
				if (m_cParent != null) {
					if (m_ptPos.x < 0)
						m_ptPos.x = 0;
					if (m_ptPos.x > (m_cParent.GetWidth() - m_nWidth))
						m_ptPos.x = m_cParent.GetWidth() - m_nWidth;
					if (m_ptPos.y < 0)
						m_ptPos.y = 0;
					if (m_ptPos.y > (m_cParent.GetHeight() - m_nHeight))
						m_ptPos.y = m_cParent.GetHeight() - m_nHeight;
					
					if (m_bCheckSiblingCollide == true) {
						for (int i = 0; i < m_cParent.GetChildSize(); i++) {
							CWindow child = m_cParent.GetChild(i);
							
							if (child != this) {
								RectF myRect = GetRealRect();
								RectF childRect = child.GetRealRect();
								
								if (myRect.intersect(childRect) == true)
									m_ptPos.x = old_x;
									m_ptPos.y = old_y;
									break;
							}
						}
					}
				}
				return EWindow.WINDOW_IO_SUCCESS;
			}
		}

		return EWindow.WINDOW_IO_FAIL;
	}

	public EWindow OnLButtonUp(PointF ptPos) {
		// 자식 윈도우에게 메세지를 보내준다.
		for (int i = 0; i < m_cChilds.size(); i++) {
			CWindow wndChild = m_cChilds.get(i);
			if (wndChild != null) {
				// 하나의 윈도우 에서 메세지를 처리 했으면 다른 윈도우 에선 메세지를 처리 할 수 없다.
				wndChild.OnLButtonUp(ptPos);
			}
		}

		// 자식 윈도우 에선 메세지를 처리 하지 못했지만
		// 현재의 윈도우는 메세지를 처리했다.
		return EWindow.WINDOW_IO_SUCCESS;
	}

	public EWindow OnRButtonUp(PointF ptPos) {
		if (false == GetRect().contains(ptPos.x, ptPos.y))
			return EWindow.WINDOW_IO_FAIL;

		return EWindow.WINDOW_IO_SUCCESS;
	}

	// Set
	public void SetID(int o) {
		m_nID = o;
	}
	
	public void SetPos(PointF o) {
		m_ptPos = o;
	}

	public void SetPos(float x, float y) {
		m_ptPos.x = x;
		m_ptPos.y = y;
	}

	public void SetX(float o) {
		m_ptPos.x = o;
	}

	public void SetY(float o) {
		m_ptPos.y = o;
	}

	public void SetWidth(float o) {
		m_nWidth = o;
	}

	public void SetHeight(float o) {
		m_nHeight = o;
	}

	public void SetBackGrndPaint(Paint o) {
		m_pntBackGround = o;
	}

	public void SetBackGrndBmp(Texture o) {
		m_bmpBackGround = o;
	}

	public void SetParent(CWindow o) {
		m_cParent = o;
	}

	public void SetChildsArray(ArrayList<CWindow> o) {
		m_cChilds = o;
	}

	public void SetMovable(boolean o) {
		m_bMovable = o;
	}

	public void SetFocus(boolean o) {
		m_bFocus = o;
	}

	public void SetEZPos(EZPos o) {
		m_eZPos = o;
	}
	
	public void SetCallBackWindow(CWindow o) {
		m_wndCallBackWindow = o;
	}
	
	public void SetCheckSiblingCollide(boolean o) {
		m_bCheckSiblingCollide = o;
	}

	
	public void SetCurScroll(int o) {
		if (m_wndCallBackWindow != null) m_wndCallBackWindow.SetCurScroll(o);
	}
	public int GetCurScroll() {
		if (m_wndCallBackWindow != null) return m_wndCallBackWindow.GetCurScroll();
		else return -1;
	}

	// Get
	public int GetID() {
		return m_nID;
	}
	
	public float GetX() {
		return m_ptPos.x;
	}

	public float GetY() {
		return m_ptPos.y;
	}

	public float GetWidth() {
		return m_nWidth;
	}

	public float GetHeight() {
		return m_nHeight;
	}

	public boolean GetMovable() {
		return m_bMovable;
	}

	public boolean GetFocus() {
		return m_bFocus;
	}

	public EZPos GetEZpos() {
		return m_eZPos;
	}
	
	public CWindow GetCallBackWindow() {
		return m_wndCallBackWindow;
	}
	
	public boolean GetCheckSiblingCollide() {
		return m_bCheckSiblingCollide;
	}
	
	public CWindow GetChild(int o) {
		if (o >= 0 && o < m_cChilds.size()) return m_cChilds.get(o);
		else return null;
	}
	
	public int GetChildSize() {
		return m_cChilds.size();
	}

	// Add
	public void AddModaless(CWindow o) {
		m_cModales.add(o);
	}

	public void AddChild(CWindow o) {
		if (o != null) {
			o.SetParent(this);
			m_cChilds.add(o);
		}
	}

	// Remove
	public void RemoveAllChild() {
		m_cChilds.clear();
	}

	// Delete

}
