package spaciall.RPGMaker;

import spaciall.UI.Globals;
import android.app.Activity;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BattleActivity extends Activity {
	private GLSurfaceView surface;
	private BattleRenderer renderer;
	
	public TextView m_textView = null;
	
	public Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			MessageObjectArg msgObj = (MessageObjectArg)msg.obj;
			switch(msg.what) {
			case 0:
			{
				m_textView.setText(msgObj.str_);
			}
			break;
			case 1:
			{
			}
			break;
			}
		};
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 윈도우 화면 풀
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
		// 장치 해상도 정보 가져오기
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
	
        // 글로벌 변수 초기화
		Globals.InitUIGlobals(width, height);
		
        m_textView = new TextView(this);
        m_textView.setTextColor(Color.WHITE);
        m_textView.setTextSize(24);
        m_textView.setText("");
    
        surface = new GLSurfaceView( this );
      
        renderer = new BattleRenderer(this, width, height);
        surface.setRenderer(renderer);
        
        setContentView( surface );
        
        // 동적 뷰의 Root
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        
        // 동적 뷰의 Sub2, 그리고 TextView를 붙인다.
        LinearLayout sub2 = new LinearLayout(this);
        sub2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lparam3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sub2.addView(m_textView, lparam3);
        
        root.addView(sub2);

        // Root 뷰를 Activity 에 등록한다.
        addContentView(root,
        		new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
        		LinearLayout.LayoutParams.WRAP_CONTENT));
    }
	
	@Override
	protected void onResume()
	{
		super.onResume();
		surface.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		surface.onPause();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return renderer.onTouchEvent(event);
	}
}
