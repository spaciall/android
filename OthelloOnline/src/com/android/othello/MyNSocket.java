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

//���� �񵿱�� ������ ������µ�  ó�� �� ���� �Ŷ� �׷��� API���� ���� �� ���� �ʾ�
//�ϴ� �����Ѵ�.
public class MyNSocket {
	//�κ� �������� ����
	public static final short GAME_NULL						= 0;
	public static final short ROOM_CREATE					= 1;					//< ���� �����
	public static final short ROOM_DELETE					= 2;					//< ���� �����
	public static final short ROOM_REQUEST_LIST				= 3;					//< �� ����� ��û�Ѵ�
	public static final short ROOM_ONE_INFO					= 4;					//< �� �ϳ��� ������ �����ش�
	public static final short ROOM_RESPOND_LIST_COMPLETE	= 5;					//< �� ����� ��� �� ���´ٰ� �˷��ش�.
	
	public static final byte	NONE						=0;
	public static final byte	OK							=1;
	public static final byte	FAIL						=2;
	
	
	public static Selector 		 selector = null;
	public static Charset  		 charset = null;
	public static CharsetDecoder decoder = null;
	
	//�κ��
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
			//len = 4[2(��Ŷ����) + 2(��ŶŸ��)] + �޼��� ����
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
