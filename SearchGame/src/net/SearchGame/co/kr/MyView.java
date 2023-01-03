package net.SearchGame.co.kr;

import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyView extends SurfaceView 
implements SurfaceHolder.Callback, Runnable{

	//더블 버퍼링을 위한 표면 홀더 및 쓰레드
	protected SurfaceHolder holder = null;
	protected Thread thread = null;
	
	protected StageMgr m_StageMgr = new StageMgr();
	protected Stage1 m_Stage1 = null;
	protected Stage2 m_Stage2 = null;
	protected Stage3 m_Stage3 = null;
	protected Stage4 m_Stage4 = null;
	protected Stage5 m_Stage5 = null;
	protected Stage6 m_Stage6 = null;
	protected Stage7 m_Stage7 = null;
	protected Stage8 m_Stage8 = null;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    // TODO Auto-generated method stub
			int iCliekdX = (int)event.getX();
			int iCliekdY = (int)event.getY();
			
	        switch(event.getAction()) {
	            case MotionEvent.ACTION_DOWN :
	            	m_StageMgr.OnClickedXY(iCliekdX, iCliekdY);
	                break;
	                
	            case MotionEvent.ACTION_MOVE :
	                break;
	                
	            case MotionEvent.ACTION_UP :
	            	
	                break;
	        }
	    return super.onTouchEvent(event);
	}
	
	public MyView(Context context, int stageId) {
	    super(context);
	    
		Global G = new Global();
		G.Setup();
	    
		m_Stage1 = new Stage1(m_StageMgr);
		m_Stage2 = new Stage2(m_StageMgr);
		m_Stage3 = new Stage3(m_StageMgr);
		m_Stage4 = new Stage4(m_StageMgr);
		m_Stage5 = new Stage5(m_StageMgr);
		m_Stage6 = new Stage6(m_StageMgr);
		m_Stage7 = new Stage7(m_StageMgr);
		m_Stage8 = new Stage8(m_StageMgr);
		
	    m_StageMgr.Push(m_Stage8);
	    m_StageMgr.Push(m_Stage7);
	    m_StageMgr.Push(m_Stage6);
	    m_StageMgr.Push(m_Stage5);
	    m_StageMgr.Push(m_Stage4);
	    m_StageMgr.Push(m_Stage3);
	    m_StageMgr.Push(m_Stage2);
	    m_StageMgr.Push(m_Stage1);
	   
	    // 이어할 스테이지가 있을 시, 그 스테이지 까지 SKIP 한다.
	    for (int i = 0; i < (stageId - 1); i++){
	    	m_StageMgr.Pop();
	    }
	    
	    //표면 홀더  생성
	    holder = getHolder();
	    holder.addCallback(this);
	    holder.setFixedSize(getWidth(), getHeight());
	
	}

	public void run() {
		// TODO Auto-generated method stub
		Canvas canvas;
		while(thread != null)
		{
			canvas = holder.lockCanvas();
	
			if (m_StageMgr.GetCurStage() == null) ((GameActivity)getContext()).onBackPressed();
			
			m_StageMgr.Update(30, getContext());
			m_StageMgr.Render(canvas);
	
			holder.unlockCanvasAndPost(canvas);
			
			try
			{
				Thread.sleep(30);
			}
			catch(Exception e)
			{
			}
		}
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread = new Thread(this);
		thread.start();
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread = null;
	}


}
