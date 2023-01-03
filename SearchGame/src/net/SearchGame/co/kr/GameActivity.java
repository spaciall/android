package net.SearchGame.co.kr;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class GameActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		int stageId = pref.getInt(Global.g_strContinueStagePrefernce, 1);

        MyView view = new MyView(this, stageId);
        setTheme(android.R.style.Theme_NoTitleBar_Fullscreen); 
        setContentView(view); 
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(Global.g_strContinueStagePrefernce, StageMgr.GetLastStageId());
		editor.commit();
    }
}