package spaciall.RPGMaker;

import spaciall.UI.Globals;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

public class OpenGL extends Activity
{
	private GLSurfaceView surface;
	private Renderer renderer;
	
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
        
        m_textView = new TextView(this);
        m_textView.setTextColor(Color.WHITE);
        m_textView.setTextSize(24);
        m_textView.setText("");
       
		// ��ġ �ػ� ���� ��������
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
	
        // �۷ι� ���� �ʱ�ȭ
		Globals.InitUIGlobals(width, height);
    
        surface = new GLSurfaceView( this );
        
        renderer = new Renderer(this, width, height);
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
    	
    	finish();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	return renderer.onTouchEvent(event);
    	
    	
    }
    
    void LoadPreference() 
    {
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		String name = pref.getString("name" ,"None");
		int id = pref.getInt("id", 0);
		int attack = pref.getInt("attack", 0);
		int defense = pref.getInt("defense", 0);
		int critical = pref.getInt("critical", 0);
		int dodge = pref.getInt("dodge", 0);
		int magic = pref.getInt("magic", 0);
		int maxhp = pref.getInt("maxhp", 0);
		int hp = pref.getInt("hp", 0);
		int maxmp = pref.getInt("maxmp", 0);
		int mp = pref.getInt("mp", 0);
		int exp = pref.getInt("exp", 0);
		int level = pref.getInt("level", 0);
		int mx = pref.getInt("mx", 0);
		int my = pref.getInt("my", 0);
		int x = pref.getInt("x", 0);
		int y = pref.getInt("y", 0);
		
		boolean[] gameSwitch = new boolean[256];
		
		for (int i = 0; i < 256; ++i) {
			gameSwitch[i] = pref.getBoolean("switch" + i, false);
		}
    }
    
    void SavePreference() 
    {
		// �߰� �����͸� ������ �ش�.
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		Editor editor = pref.edit();
		
		// ���� �Ӽ� �� �ɷ�ġ ����
		editor.putString("name", renderer.m_Chr.m_BattleCreature.m_Name);
		editor.putInt("id", renderer.m_Chr.m_nIdx);
		editor.putInt("attack", renderer.m_Chr.m_BattleCreature.m_Attack);
		editor.putInt("defense", renderer.m_Chr.m_BattleCreature.m_Defense);
		editor.putInt("critical", renderer.m_Chr.m_BattleCreature.m_Critical);
		editor.putInt("dodge", renderer.m_Chr.m_BattleCreature.m_Dodge);
		editor.putInt("magic", renderer.m_Chr.m_BattleCreature.m_Magic);
		editor.putInt("maxhp", renderer.m_Chr.m_BattleCreature.m_MaxHP);
		editor.putInt("hp", renderer.m_Chr.m_BattleCreature.m_HP);
		editor.putInt("maxmp", renderer.m_Chr.m_BattleCreature.m_MaxMP);
		editor.putInt("mp", renderer.m_Chr.m_BattleCreature.m_MP);
		editor.putInt("exp", renderer.m_Chr.m_nExp);
		editor.putInt("level", renderer.m_Chr.m_nLevel);
		editor.putInt("mx", renderer.m_Chr.m_nMapFileX);
		editor.putInt("my", renderer.m_Chr.m_nMapFileY);
		editor.putInt("x", renderer.m_Chr.m_nX);
		editor.putInt("y", renderer.m_Chr.m_nY);
		
		// ���� ����ġ ����
		for (int i = 0; i < 256; ++i) {
	    	editor.putBoolean("switch" + i, renderer.m_bGameSwitch[i]);
		}
		
		editor.commit();
    }
}
