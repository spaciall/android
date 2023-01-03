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
        // ������ ȭ�� Ǯ
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
		// ��ġ �ػ� ���� ��������
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
	
        // �۷ι� ���� �ʱ�ȭ
		Globals.InitUIGlobals(width, height);
		
        m_textView = new TextView(this);
        m_textView.setTextColor(Color.WHITE);
        m_textView.setTextSize(24);
        m_textView.setText("");
    
        surface = new GLSurfaceView( this );
      
        renderer = new BattleRenderer(this, width, height);
        surface.setRenderer(renderer);
        
        setContentView( surface );
        
        // ���� ���� Root
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        
        // ���� ���� Sub2, �׸��� TextView�� ���δ�.
        LinearLayout sub2 = new LinearLayout(this);
        sub2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lparam3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sub2.addView(m_textView, lparam3);
        
        root.addView(sub2);

        // Root �並 Activity �� ����Ѵ�.
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
