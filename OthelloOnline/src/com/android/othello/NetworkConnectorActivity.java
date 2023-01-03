package com.android.othello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NetworkConnectorActivity extends Activity implements OnClickListener{
	
	private Button m_btnConnector;
	private EditText m_etIP;
	private EditText m_etID;
	private EditText m_etPassword;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.network_connector);
        
        m_btnConnector = (Button)findViewById(R.id.BTServerConnect);
        m_btnConnector.setOnClickListener(this);
        
        m_etIP = (EditText)findViewById(R.id.ETIP);
        m_etIP.setOnClickListener(this);
        
        m_etID = (EditText)findViewById(R.id.ETID);
        m_etID.setOnClickListener(this);

        m_etPassword = (EditText)findViewById(R.id.ETPassword);
        m_etPassword.setOnClickListener(this);
    }
 
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId())
		{
			case R.id.BTServerConnect:
			{
				Intent i = new Intent(this, NetworkLobby.class);
				i.putExtra("IP", m_etIP.getText().toString());
				i.putExtra("ID", m_etID.getText().toString());
				i.putExtra("PASSWORD", m_etPassword.getText().toString());
	
				startActivity(i);
				break;
			}
		}
	}
}
