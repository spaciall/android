package net.SearchGame.co.kr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MenuActivity extends Activity implements OnClickListener{

	private ImageView StartView = null;
	private ImageView ContinueView = null;
	private ImageView ExitView = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        StartView = (ImageView)findViewById(R.id.IVStart);
        StartView.setOnClickListener(this);	

        ContinueView = (ImageView)findViewById(R.id.IVContinue);
        ContinueView.setOnClickListener(this);	
        
        StartView = (ImageView)findViewById(R.id.IVExit);
        StartView.setOnClickListener(this);	
        
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent i = null;
		switch(v.getId())
		{
			case R.id.IVStart:
				i = new Intent(this, GameActivity.class);
				startActivity(i);
				break;
				
			case R.id.IVContinue:
				i = new Intent(this, GameActivity.class);
				startActivity(i);
				break;			
			
			case R.id.IVExit:
				System.exit(0);
				break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
