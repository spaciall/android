package net.SearchGame.co.kr;

import java.util.Stack;

import net.SearchGame.co.kr.Stage.eStageStatus;
import android.content.Context;
import android.graphics.Canvas;

public class StageMgr {
	protected Stack<Stage> m_Stack = new Stack<Stage>();
	protected Stage m_CurStage = null;
	
	protected static int m_iLastStageId = -1;
	
	static void SetLastStageId(int o)
	{
		m_iLastStageId = o;
	}
	
	static int GetLastStageId()
	{
		return m_iLastStageId;
	}
	
	Stage GetCurStage()
	{
		return m_CurStage;
	}
	
	boolean Push(Stage o)
	{
		m_Stack.add(o);
		m_CurStage = m_Stack.peek();
	
		return true;
	}
	
	Stage Pop()
	{
		if (m_Stack.size() > 0) {
			Stage res = m_Stack.pop();
			
			if (m_Stack.size() > 0) m_CurStage = m_Stack.peek();
			else m_CurStage = null;
			
			return res;
		}
		
		return null;
	}
	
	boolean Setup(Context context)
	{
		m_CurStage.Setup(context);
		
		return true;
	}
	
	boolean OnClickedXY(int x, int y)
	{
		m_CurStage.OnClickedXY(x, y);
		
		return true;
	}
	
	boolean Update(int elapsedTime, Context context)
	{
		if (m_CurStage == null) return false;
		if (m_CurStage.GetStatus() == eStageStatus.STAGE_FINISH) Pop();

		m_CurStage.Update(elapsedTime, context);
		
		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		if (m_CurStage == null) return false;
	
		m_CurStage.Render(canvas);
		
		return true;
	}
}
