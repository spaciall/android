package com.android.othello;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class MySocket {
	
	//로비 서버와의 상태
	public static final short GAME_NULL						= 0;
	public static final short ROOM_CREATE					= 1;					//< 방을 만든다
	public static final short ROOM_DELETE					= 2;					//< 방을 지운다
	public static final short ROOM_REQUEST_LIST				= 3;					//< 방 목록을 요청한다
	public static final short ROOM_ONE_INFO					= 4;					//< 방 하나의 정보를 보내준다
	public static final short ROOM_RESPOND_LIST_COMPLETE	= 5;					//< 방 목록을 모두 다 보냈다고 알려준다.
	public static final short ROOM_ENTER_ROOM				= 6;
	public static final short GAME_ALL_READY				= 8;
	public static final short GAME_TAKE_XY					= 9;
	public static final short GAME_WIN						= 10;
	public static final short GAME_LOSE						= 11;

	
	public static final byte	NONE							=0;
	public static final byte	OK								=1;
	public static final byte	FAIL							=2;
	
	private static final String mSendPrefix = "CS";
	private static final String mRecvPrefix = "SC";
	
	
	//게임 이름
	public static String m_GameName = "Othello";
	public static int	 m_GameNum	= 1;
	
	//로비용
	public static Socket m_sckLobby = null;
	
	//로그인 상태
	public static boolean m_isLogin = false;
	
	public static boolean ConnectSocket(String ip_, int port_)
	{
		try
		{
        	MySocket.m_sckLobby = new Socket(ip_, port_);
        	if(MySocket.m_sckLobby != null && MySocket.m_sckLobby.isConnected() == true)
        		return true;
        	else
        		return false;
		}
		catch(Exception e)
		{
			return false;
		}		
	}
	
	public static byte[] MakePacket(short type, short extra, int target, String msg)
	{
		try
		{
			//len = 12[2(프리픽스) + 2(패킷길이) + 2(패킷타입) + 2(부가) + 4(타겟)] + 메세지 길이
			short len = (short)(12); 
			
			len += (short)(msg.length());
			
			ByteBuffer bf = ByteBuffer.allocate(len);
			bf.order(ByteOrder.LITTLE_ENDIAN);
			bf.put(mSendPrefix.getBytes());
			bf.putShort(len);
			bf.putShort(type);
			bf.putShort(extra);
			bf.putInt(target);
			
			bf.put(msg.getBytes());
			
			return bf.array();
		}
		catch(Exception e)
		{
    		return null;
		}
	}
	
	public static boolean SendPacket(StringBuffer sb_, short type_, short extra_, int target_)
	{
		try
		{
			byte[] w;
			
			w = MySocket.MakePacket(type_, extra_, target_, sb_.toString());
			
    		OutputStream out = MySocket.m_sckLobby.getOutputStream();
    		out.write(w);
    		out.flush();			
			return true;
		}
		catch(Exception o)
		{
			return false;
		}
	}
	
	public static Packet ReadPacket()
	{
    	//결과를 읽는다.
		try
		{
    		int ilen, itype, iextra, itarget;

     		byte[] prefix = new byte[2];
    		byte[] len = new byte[2];
    		byte[] type = new byte[2];
    		byte[] extra = new byte[2];
    		byte[] target = new byte[4];
    		byte[] data = null;
    		
    		InputStream in = MySocket.m_sckLobby.getInputStream();
    		in.read(prefix);
    		in.read(len);
    		in.read(type);
    		in.read(extra);
    		in.read(target);

    		ilen = (int)len[0] + (int)len[1] * 256;
    		if(ilen < 0)
    			ilen = 256 + ilen;
    		
    		ilen -= 12;
    		
    		itype = (int)type[0] + (int)type[1] * 256;
    		if(itype < 0)
    			itype = 256 + itype;

    		iextra = (int)extra[0] + (int)extra[1] * 256;
       		if(iextra < 0)
       			iextra = 256 + iextra;

    
       	//int 이긴 하지만  Realy 정보를 빼면 Key 값은 short 와 같으므로 이렇게 해 준다.
    		//Realy 정보가 빠졌으므로 무조건 Player 에 관한 정보로 가정한다.
    		itarget = (int)target[0] + (int)target[1] * 256;
       		if(itarget < 0)
       			itarget = 256 + itarget;
    		
    	
 			ByteBuffer bf = ByteBuffer.allocate(ilen);
    		
    		//안전하게 받기 위해서 while을 처리 한다.
    		if(ilen > 0);
    		{
    			int iTotalRecvLen = 0;
    			
    			while(iTotalRecvLen < ilen)
    			{
    		
	    			int recvLen = in.read(bf.array());
	    			bf.position(iTotalRecvLen);
	    			
	    			iTotalRecvLen += recvLen;
	    		}
    		}	    	
    		
    		bf.position(0);
    		
   			Packet res = new Packet();
	   		res.mLen = (short)ilen;
	   		res.mType= (short)itype;
	   		res.mExtra = (short)iextra;
	    	res.mTarget = (short)itarget;
	    	res.mData= bf.array();
	    		
	    	return res;
	   	
		}
		catch(IOException o)
		{
			return null;
		}
	}
	
	public static void ReleaseAll()
	{
    	try
    	{
    		m_sckLobby.close();
    	}
    	catch(Exception e)
    	{
    	}		
	}

}


