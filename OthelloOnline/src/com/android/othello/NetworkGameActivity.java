package com.android.othello;

import java.net.InetAddress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class NetworkGameActivity extends Activity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        boolean bIsHost = i.getBooleanExtra("HOST", true);
        	
        if( true == bIsHost){
        	//방을 만들고 방장이 된다.
        	Packet pk = MySocket.ReadPacket();
        	if(pk.mType == MySocket.ROOM_CREATE && pk.mExtra == MySocket.OK){
        	}
        	else{
        		finish();
        	}
        }
        else{
        	//만들어진 방에 들어간다.
        	StringBuffer sb = new StringBuffer();
        	sb.append(i.getStringExtra("IP"));
    		MySocket.SendPacket(sb, MySocket.ROOM_ENTER_ROOM, (short)0, 0);

        	Packet pk = MySocket.ReadPacket();
        	if(pk.mType == MySocket.ROOM_ENTER_ROOM && pk.mExtra == MySocket.OK){
        	}
        	else{
        		finish();
        	}
        }
        
		MyNetworkView view = new MyNetworkView(this);
		view.m_Parent = this;
		view.m_bIsHost = view.m_bIsMyTurn = bIsHost;
       
		setContentView(view);
        
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
		//IP주소가 제목인 방을 없애준다.
		InetAddress netAddress = MySocket.m_sckLobby.getInetAddress();
		
    	StringBuffer sb = new StringBuffer();
    	sb.append(netAddress.getHostAddress());
		MySocket.SendPacket(sb, MySocket.ROOM_DELETE, (short)0, 0);
 	
    	super.onBackPressed();
    	
    }

    /*
    class BackThread extends Thread{
    	
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
    		while(m_bRunning == true){
    			if(MyNetworkView.m_bIsMyTurn == false){
    				Packet pk = MySocket.ReadPacket();
    				
    				if(pk.mTarget == MySocket.GAME_TAKE_XY){
    					
    	    			ByteBuffer bf = ByteBuffer.wrap(pk.mData);
    	    			char x = 0, y = 0, player = 0;
    	    			x = bf.getChar();
    	    			y = bf.getChar();
    	    			player = bf.getChar();
    					
    	    			Log.i(Globals.TAG, "class BackThread - Receive Packet - MySocket.GAME_TAKE_XY - X: " + x + ", Y: " + y + ", Player: " + player);
    			        Toast t = Toast.makeText(getApplicationContext(), "X: " + x + ", Y: " + y + ", Player:"  + player, Toast.LENGTH_SHORT);
    			        t.show();
    	
    	    			
    			        MyNetworkView.m_bIsMyTurn = true;
    				}
    			}
    			
    		}
    	}
	}
	*/
}

