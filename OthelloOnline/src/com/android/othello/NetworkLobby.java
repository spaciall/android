package com.android.othello;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkLobby extends Activity  implements OnClickListener{
	
	private final int 				TIMER_PERIOD = 1000;
	private Handler					m_hHandler = null;
	private boolean 				m_bRunning = false;
	private byte[] 					m_ReceiveByte = new byte[1024 * 10];
	private int						m_iTotalRead = 0;
	
	private Button					m_btnSendMeg = null;
	private Button					m_btnCreateHost = null;
	private Button					m_btnRefreshGameRoom = null;
	private EditText				m_etSendMsg = null;
	private ListView				m_lvRoomList = null;
	
	private ArrayList<RoomInfo>		m_RoomInfo = new ArrayList<RoomInfo>();


	//방 목록을 보여줄 어댑터
    private class RoomListAdapter extends ArrayAdapter {
    	 
        private ArrayList items;
 
        public RoomListAdapter(Context context, int textViewResourceId, ArrayList items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.room_list_row, null);
                }
                RoomInfo p = (RoomInfo) items.get(position); 
               
                if (p != null) {
                        TextView tt = (TextView) v.findViewById(R.id.room_list_toptext);
                        if (tt != null){
                         tt.setText(p.mHostIP);                            
                        }
    
                }
                return v;
        }
    }
 	
    
    
    @Override
    public void onBackPressed() {
       	//소켓이 연결되어 있으면 끊어 준다.
        MySocket.ReleaseAll();
    	
    	super.onBackPressed();
    }

    
    //액티비티 시작
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.network_lobby);
       
        try
        {
        	Intent i = getIntent();
      
        	//접속
        	if(true == MySocket.ConnectSocket(i.getStringExtra("IP"), 41642))
        	{
        	}
        	else
        	{
        		finish();
        	}
 	    }
        catch(Exception e)
        {
            Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            t.show();
 	
        	finish();
        }
        
        

        //컨트롤들을 연결한다.
        //리스큐 뷰 연동
        //선택한 방으로 들어간다.
        final RoomListAdapter m_adapter = new RoomListAdapter(this, R.layout.room_list_row, m_RoomInfo);
        m_lvRoomList = (ListView)findViewById(R.id.LVGameRoom);
        m_lvRoomList.setAdapter(m_adapter);
        m_lvRoomList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				RoomInfo  Item = (RoomInfo)m_adapter.getItem(arg2);
				if( null != Item ){
					Intent i = new Intent(getApplicationContext(), NetworkGameActivity.class);
					i.putExtra("HOST", false);
					i.putExtra("IP", Item.mHostIP);
					startActivity(i);
				}
			}
		});	
        	

        //버튼 연동
        m_btnSendMeg = (Button)findViewById(R.id.BTSendChattingMessage);
        m_btnSendMeg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//채팅 메세지 전송하기
	    	}
		});
        
        m_etSendMsg	 = (EditText)findViewById(R.id.ETChattingMessage);        
        
        m_btnCreateHost = (Button)findViewById(R.id.BTCreateGameHost);
        m_btnCreateHost.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v){ 
				// TODO Auto-generated method stub
	        	do{
	        		try
	        		{
	        			InetAddress netAddress = MySocket.m_sckLobby.getInetAddress();
	        			
	        			//IP주소가 제목인 방을 만든다.
			        	StringBuffer sb = new StringBuffer();
			        	sb.append(netAddress.getHostAddress());
			    		MySocket.SendPacket(sb, MySocket.ROOM_CREATE, (short)0, 0);
			    		
			    		//네트워크 ACTIVITY를 시작한다.
						Intent i = new Intent(v.getContext(), NetworkGameActivity.class);
						i.putExtra("HOST", true);
						startActivity(i);
						
	        		}
	        		catch(Exception e)
	        		{
	                    Toast t = Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT);
	                    t.show();
	         	
	                	finish();		
	        		}
		       	}while(false);	
			}
		});
        
        m_btnRefreshGameRoom = (Button)findViewById(R.id.BTGetGameRoomList);
        m_btnRefreshGameRoom.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
				{
					//기존의 방 리스트 목록을 지워준다
					m_RoomInfo.clear();
					
		        	//방 리스트 요청
					StringBuffer sb = new StringBuffer();
	        		MySocket.SendPacket(sb, MySocket.ROOM_REQUEST_LIST, (short)0, 0);
		 
	        		//방 목록을 하나 씩 받는다
	        		Packet pk = null;
	        		while(true)
	        		{
	        			pk = MySocket.ReadPacket();
	        			
	        			if(pk.mType == MySocket.ROOM_RESPOND_LIST_COMPLETE)
	        				break;
	        			
	        			ByteBuffer bf = ByteBuffer.wrap(pk.mData);
	        			
	        			byte[] AddrName = new byte[32];
	        			
	        			bf.get(AddrName);
	        			
	            		//받은 정보를 통해 방을 하나 추가해 준다.
	            		RoomInfo roomInfo = new RoomInfo();
	            		roomInfo.mHostIP = new String(AddrName, "EUC-KR");
	            		
	            		m_RoomInfo.add(roomInfo);
	            		m_lvRoomList.invalidateViews();
	        		}
				}
				catch(Exception e)
				{
		           Toast t = Toast.makeText(getApplicationContext(), "Error! Re Login, Please!", Toast.LENGTH_SHORT);
		           t.show();
		 	}
	 	}
		});
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	m_bRunning = false;
    	super.onStop();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	m_bRunning = true;
    	m_RoomInfo.clear();
    	m_lvRoomList.invalidateViews();
    	super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	
    	try
    	{
    	   	m_bRunning = false;
    	}
    	catch(Exception e)
    	{
            Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            t.show();
    	}
    	
    	super.onDestroy();
    }
 
    public void onClick(View v) {
		// TODO Auto-generated method stub
		
	};	


    
	//액티비티 쓰레드를 같이 사용하니까 UI와 충돌이 일어나는 것 같다. 일단 주석처리.
	//원래 의도 대로라면 이렇게 해서 계속 백단에서 RECV가 되어야 한다.
	/*
	public void run() {
		// TODO Auto-generated method stub
		try
		{
			
			while(true == m_bRunning )
			{
				ByteBuffer inputByte = ByteBuffer.allocate(1024);
				
				InputStream in = MySocket.m_sckLobby.getInputStream();
				ReadableByteChannel rbc = Channels.newChannel(in);
				
				
				if( m_iTotalRead <= (1024 * (10 - 1)) )
				{
					int nLen = rbc.read(inputByte);
					
					if(nLen <= 0 )
					{
						//로비 서버와 접속이 끊어졌다. 추후에 처리 해 준다.
					}
					
					for(int i = 0; i < nLen; i++)
						m_ReceiveByte[m_iTotalRead + i] = inputByte.get(i);
					
					m_iTotalRead += nLen;
					
					int iDoneLength = 0;
					do
					{
						iDoneLength = ProcessPacket(m_ReceiveByte, m_iTotalRead);
						m_iTotalRead -=iDoneLength;
					}while(iDoneLength > 0);
					
				}
			}
		}
		catch(Exception e)
		{
            Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            t.show();			
		}
	}
	
	public int ProcessPacket(byte[] ByteBuffer_, int lenght_)
	{
		int length = lenght_;
		
		if(4 <= length)
		{
			int len = 0, type = 0;
			short len1 = ByteBuffer_[0];
			short len2 = ByteBuffer_[1];
			short type1 = ByteBuffer_[2];
			short type2 = ByteBuffer_[3];
			
			len = len1 + (256 * len2);
			type = type1 + (256 * type2);
			
			if( len <= length )
			{
				byte[] onePacket = new byte[len - 4]; 
				
				//패킷을 하나 가져온다.
				int iDataLen = len - 4;
				for(int i = 0; i < iDataLen; i++)
					onePacket[i] = ByteBuffer_[i + 4];
				
				//바이트 버퍼를 앞으로 당겨준다.
				for(int i = 0; i < length - len; i++)
					ByteBuffer_[i] = ByteBuffer_[len + i];
				
				//앞으로 당겨 준 만큼 뒤의 바이트 들을 0으로 처리해 준다.
				for(int i = 0; i < len; i++)
					ByteBuffer_[length - len + i] = 0;
				
				ProcessOnePacket(onePacket, (short)type, iDataLen);
		
				Log.i("INFO - NetworkLobby.java - Received Packet ->", len + "/" + type + "/" + onePacket.toString());	
				return len;
			}
		}
		
		//아무 것도 처리 되지 않았다.
		return 0;
	}	
	
	public int ProcessOnePacket(byte[] ByteBuffer_, short type, int len)
	{
		switch (type)
		{
			//로그인에 성공했다.
			case MySocket.LMSG_LOGIN_ID_RESULT:
			{
				MySocket.m_isLogin = true;
				m_bRunning = false;

				Toast t = Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT);
	            t.show();		
	            
	            break;
			}
		}
		
		return 0;
	}
	*/
	
}



