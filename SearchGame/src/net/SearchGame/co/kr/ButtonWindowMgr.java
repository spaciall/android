package net.SearchGame.co.kr;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;

public class ButtonWindowMgr {
	protected ArrayList<ButtonWindow> m_List = new ArrayList<ButtonWindow>();
	protected int m_iSelectedId = -1;
	
	boolean Setup(Context context)
	{
		return true;
	}
	
	int OnClicked(int x, int y)
	{
		for (int i = 0; i < m_List.size(); i++) {
			ButtonWindow buttonWnd = m_List.get(i);
			
			if (buttonWnd.OnClickedXY(x, y) == true) {
				m_iSelectedId = buttonWnd.GetID();
				buttonWnd.SetSelected(true);
			}
		}
		
		for (int i = 0; i < m_List.size(); i++) {
			ButtonWindow buttonWnd = m_List.get(i);
			
			if (buttonWnd.GetID() != m_iSelectedId) buttonWnd.SetSelected(false);
		}
		
		return m_iSelectedId;
	}
	
	int GetSelectedId()
	{
		return m_iSelectedId;
	}
	
	ButtonWindow GetButtonById(int id)
	{
		for (int i = 0; i < m_List.size(); i++) {
			ButtonWindow btn = m_List.get(i);
			
			if (btn.GetID() == id)
				return btn;
		}
	
		return null;
	}	
	
	void SetSelectedId(int idx, boolean flag)
	{
		if (idx < m_List.size()) {
			ButtonWindow btn = m_List.get(idx);
			btn.SetSelected(flag);
		}
	}
	
	boolean Add(ButtonWindow o)
	{
		m_List.add(o);
		return true;
	}

	boolean Remove(ButtonWindow o)
	{
		m_List.remove(o);
		return true;
	}
	
	boolean Find(ButtonWindow o)
	{
		return m_List.contains(o);
	}
	
	boolean RemoveById(int id)
	{
		ButtonWindow btn = GetButtonById(id);
		
		if (btn != null)
			m_List.remove(btn);
		
		return true;
	}	
	
	boolean Update()
	{
		for (int i = 0; i < m_List.size(); i++) {
			ButtonWindow buttonWnd = m_List.get(i);
			buttonWnd.Update();
		}	
	
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		for (int i = 0; i < m_List.size(); i++) {
			ButtonWindow buttonWnd = m_List.get(i);
			buttonWnd.Render(canvas);
		}	
	
		return true;
	}
	
}
