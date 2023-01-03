package com.android.othello;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

//원래 비동기로 쓰려고 만들었는데  처음 해 보는 거라 그런지 API들이 말을 잘 듣지 않아
//일단 보류한다.
public class MyNSocket {
	//로비 서버와의 상태
	public static final short GAME_NULL						= 0;
	public static final short ROOM_CREATE					= 1;					//< 방을 만든다
	public static final short ROOM_DELETE					= 2;					//< 방을 지운다
	public static final short ROOM_REQUEST_LIST				= 3;					//< 방 목록을 요청한다
	public static final short ROOM_ONE_INFO					= 4;					//< 방 하나의 정보를 보내준다
	public static final short ROOM_RESPOND_LIST_COMPLETE	= 5;					//< 방 목록을 모두 다 보냈다고 알려준다.
	
	public static final byte	NONE						=0;
	public static final byte	OK							=1;
	public static final byte	FAIL						=2;
	
	
	public static Selector 		 selector = null;
	public static Charset  		 charset = null;
	public static CharsetDecoder decoder = null;
	
	//로비용
	public static SocketChannel m_sckLobby;
	
	public static boolean ConnectLobby(String ip_, int port_)
	{
		try
		{
			selector = Selector.open();
			m_sckLobby = SocketChannel.open(new InetSocketAddress(ip_, port_));
			m_sckLobby.configureBlocking(false);
			m_sckLobby.register(selector, SelectionKey.OP_READ);
			
			return true;
		}
		catch(IOException e)
		{
			return false;
		}
	}
	
	public static ByteBuffer MakePacket(short type, String msg)
	{
		try
		{
			//len = 4[2(패킷길이) + 2(패킷타입)] + 메세지 길이
			short len = (short)((short)(4) + (short)(msg.length()));
			
			ByteBuffer bf = ByteBuffer.allocate(len);
			bf.order(ByteOrder.LITTLE_ENDIAN);
			bf.putShort(len);
			bf.putShort(type);
			bf.put(msg.getBytes());
			
			return bf;
		}
		catch(Exception e)
		{
    		return null;
		}

	}	
	

	public static boolean CloseLobbySocket()
	{
		try
		{
			m_sckLobby.finishConnect();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
