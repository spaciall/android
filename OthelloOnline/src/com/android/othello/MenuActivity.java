package com.android.othello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MenuActivity extends Activity implements OnClickListener{
	
	private ImageView StartView = null;
	private ImageView NetworkView = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
      
        StartView = (ImageView)findViewById(R.id.IVStart);
        StartView.setOnClickListener(this);
        
        NetworkView = (ImageView)findViewById(R.id.IVNetwork);
        NetworkView.setOnClickListener(this);
    }
 
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId())
		{
			case R.id.IVStart:
			{
				Intent i = new Intent(this, GameActivity.class);
				startActivity(i);
				break;
			}
			case R.id.IVNetwork:
			{
				Intent i = new Intent(this, NetworkConnectorActivity.class);
				startActivity(i);
				break;
			}
		}
	}
}
