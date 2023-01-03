package net.SearchGame.co.kr;

import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.Canvas;

public class CharMgr {
	protected ArrayList<Char> m_CharList = new ArrayList<Char>();
	
	int GetSize()
	{
		return m_CharList.size();
	}
	
	Char GetChar(int o)
	{
		return m_CharList.get(o);
	}
	
	Char GetCharById(int id)
	{
		for (int i = 0; i < m_CharList.size(); i++) {
			Char chr = m_CharList.get(i);
			
			if (chr.GetID() == id)
				return chr;
		}
	
		return null;
	}
	
	boolean MixChrs(int num)
	{
		int size = m_CharList.size();
		
		for (int i = 0; i < num; i++) {
			int n1 = Rand.NextInt();
			int n2 = Rand.NextInt();
			n1 = (Math.abs(n1)) % size;
			n2 = (Math.abs(n2)) % size;
			
			if (n1 != n2 ) {
				Char chr1 = m_CharList.get(n1);
				Char chr2 = m_CharList.get(n2);
				
				m_CharList.set(n1, chr2);
				m_CharList.set(n2, chr1);
			}
		}
		
		return true;
	}
	
	void RemoveAll()
	{
		m_CharList.clear();
	}	
	
	boolean Add(Char o)
	{
		m_CharList.add(o);
		return true;
	}

	boolean Remove(Char o)
	{
		m_CharList.remove(o);
		return true;
	}
	
	boolean Find(Char o)
	{
		return m_CharList.contains(o);
	}
	
	boolean RemoveById(int id)
	{
		Char chr = GetCharById(id);
		
		if (chr != null)
			m_CharList.remove(chr);
		
		return true;
	}
	
	boolean HaveXY(Char o)
	{
		for (int i = 0; i < m_CharList.size(); i++) {
			Char chr = m_CharList.get(i);
			
			if ( o != chr)
				if (chr.GetX() == o.GetX() && chr.GetY() == o.GetX())
					return true;
		}
		
		return false;
	}
	
	boolean HaveToXY(Char o)
	{
		for (int i = 0; i < m_CharList.size(); i++) {
			Char chr = m_CharList.get(i);
			
			if ( o != chr)
				if (chr.GetToX() == o.GetToX() && chr.GetToX() == o.GetToY())
					return true;
		}
		
		return false;
	}
	
	boolean IsInOther(Char o)
	{
		int left = o.GetDispX();
		int top = o.GetDispY();
		int right = left + 32;
		int bottom = top + 32;
	
		int chr_left = 0, chr_top = 0, chr_right = 0, chr_bottom = 0;
		
		
		for (int i = 0; i < m_CharList.size(); i++) {
			Char chr = m_CharList.get(i);
			
			if ( o != chr) {
				
				chr_left = chr.GetDispX();
				chr_top = chr.GetDispY();
				chr_right = chr_left + 32;
				chr_bottom = chr_top + 32;
				
				if ( left >= chr_right || right <= chr_left || bottom <= chr_top || top >= chr_bottom)
					continue;
	
				return true;	
			}
		}
		
		return false;
	}
	
	int GetCrashedChr(int x, int y)
	{
		int iX = 0, iY = 0;
		
		for (int i = 0; i < m_CharList.size(); i++) {
			Char chr = m_CharList.get(i);
			
				iX = chr.GetDispX();
				iY = chr.GetDispY();
				
				if ((iX < x) && (iX + 32 > x) && (iY < y) && (iY + 32 > y))
					return chr.GetID();
		}
		
		return -1;
	}	
	
	boolean Update()
	{
		for (int i = 0; i < m_CharList.size(); i++) {
			Char chr = m_CharList.get(i);
			chr.Update();
		}
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		for (int i = 0; i < m_CharList.size(); i++) {
			Char chr = m_CharList.get(i);
			chr.Render(canvas);
		}
	
		return true;
	}
}
