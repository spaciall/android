package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.Canvas;

public class Stage {
	
	protected int m_iId = -1;
	
	protected ButtonWindowMgr m_ButtonWndMgr = new ButtonWindowMgr();
	protected static ScreenQuake m_ScreenQuake = new ScreenQuake();
	protected Timer m_Timer = new Timer();

	protected static CharImgMgr m_CharImgMgr = new CharImgMgr();
	protected CharMgr m_CharMgr = new CharMgr();
	
	protected static FxImgMgr m_FxImgMgr = new FxImgMgr();
	protected FxMgr m_FxMgr = new FxMgr();	
	
	protected TextWindow m_StageResultText = new TextWindow();
	protected GoalWindow m_GoalWnd = new GoalWindow(m_CharMgr, 475, 10, 8, 1);
	
	protected static FxAni m_FxAni = new FxAni();
	protected static FxAni m_FxAni1 = new FxAni();
	protected static FxAni m_FxAni2 = new FxAni();
	protected static FxAni m_CurFxAni = null;
	
	enum eStageStatus {
		STAGE_SETUP,
		STAGE_START,
		STAGE_UPDATE,
		STAGE_END,
		STAGE_FINISH
	}
	
	protected eStageStatus m_eStageStatus = eStageStatus.STAGE_SETUP;
	protected StageMgr m_StageMgr = null;
	
	protected int m_iTargetTimeForStageStart = 600;
	protected int m_iTargetTimeForStageEnd = 600;
	
	protected int m_iCurTimeForCurStage = 0;
	
	Stage(StageMgr stageMgr) {
		m_StageMgr = stageMgr;
	}
	
	void SetStatus(eStageStatus o)
	{
		m_iCurTimeForCurStage = 0;
		m_eStageStatus = o;
	}
	
	eStageStatus GetStatus()
	{
		return m_eStageStatus;
	}
	
	int GetId()
	{
		return m_iId;
	}
	
	boolean OnClickedXY(int x, int y)
	{
		return true;
	}
	
	boolean Setup(Context context)
	{
		return true;
	}
	
	boolean ClearStage()
	{
		return true;
	}
	
	boolean Update(int elapsedTime, Context context)
	{
		switch (m_eStageStatus)
		{
		case STAGE_SETUP:
			Setup(context);
			m_eStageStatus = eStageStatus.STAGE_START;
			break;
		case STAGE_START:
			if (m_iCurTimeForCurStage > m_iTargetTimeForStageStart) {
				m_iCurTimeForCurStage = 0;
				m_eStageStatus = eStageStatus.STAGE_UPDATE;
			}
			else {
				m_ScreenQuake.Stop();
				m_FxMgr.RemoveAll();
				m_Timer.Start();
				ClearStage();
				m_CharMgr.MixChrs(m_CharMgr.GetSize());

				StageMgr.SetLastStageId(GetId());
			}
			break;
			
		case STAGE_UPDATE:
			if (m_Timer.GetCurSecond() <= 0) {
				m_iCurTimeForCurStage = 0;
				m_StageResultText.Start(150, 200, "Time Out!!", 300, Global.g_StageResultPaint);
				SetStatus(eStageStatus.STAGE_START);		
			}
			break;

		case STAGE_END:
			if (m_iCurTimeForCurStage > m_iTargetTimeForStageEnd) {
				m_iCurTimeForCurStage = 0;
				m_eStageStatus = eStageStatus.STAGE_FINISH;
			}
			break;
		}

		m_iCurTimeForCurStage += elapsedTime;

		return true;
	}
	
	boolean Render(Canvas canvas)
	{
		return true;
	}
}
