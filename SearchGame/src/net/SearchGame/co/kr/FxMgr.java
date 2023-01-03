package net.SearchGame.co.kr;

import java.util.LinkedList;

import android.graphics.Canvas;

public class FxMgr {
	protected LinkedList<Fx> m_FxList = new LinkedList<Fx>();
	protected CharMgr m_CharMgr = null;
	
	int GetSize()
	{
		return m_FxList.size();
	}
	
	Fx GetFx(int o)
	{
		return m_FxList.get(o);
	}
	
	void RemoveAll()
	{
		m_FxList.clear();
	}
	
	Fx GetFxById(int id)
	{
		for (int i = 0; i < m_FxList.size(); i++) {
			Fx fx = m_FxList.get(i);
			
			if (fx.GetID() == id)
				return fx;
		}
	
		return null;
	}
	
	boolean Add(Fx o)
	{
		m_FxList.add(o);
		return true;
	}

	boolean Remove(Fx o)
	{
		m_FxList.remove(o);
		return true;
	}
	
	boolean Find(Fx o)
	{
		return m_FxList.contains(o);
	}
	
	boolean Setup(CharMgr o)
	{
		m_CharMgr = o;
		
		return true;
	}
	
	boolean RemoveById(int id)
	{
		Fx chr = GetFxById(id);
		
		if (chr != null)
			m_FxList.remove(chr);
		
		return true;
	}
	
	boolean Update()
	{
		for (int i = 0; i < m_FxList.size(); i++) {
			Fx fx = m_FxList.get(i);
			fx.Update();
		}
		
		//수명이 다한 이펙트는 지운다.
		for (int i = 0; i < m_FxList.size(); i++) {
			Fx fx = m_FxList.get(i);
			
			if (fx.IsLifeEnd() == true)
				m_FxList.remove(fx);
		}
		
		//캐릭터와 충돌하면 캐릭터 HP를 깍는다.
		if (m_CharMgr != null) {
			for (int i = 0; i < m_FxList.size(); i++) {
				Fx fx = m_FxList.get(i);
				
				if (fx.IsLifeEnd() == false) {
				
					for (int j = 0; j < m_CharMgr.GetSize(); j++) {
						Char chr = m_CharMgr.GetChar(j);
					
						if (fx.IsCrashed(chr) == true) {
							chr.SetHP(chr.GetHP() - fx.GetDamage());
							chr.RotateDir();
						}
					}
				}

			}
		}
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		for (int i = 0; i < m_FxList.size(); i++) {
			Fx fx = m_FxList.get(i);
			fx.Render(canvas);
		}
	
		return true;
	}
	
}
