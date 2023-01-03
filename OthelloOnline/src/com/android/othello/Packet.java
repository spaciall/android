package com.android.othello;

//ÆÐÅ¶
public class Packet
{
	public Packet()
	{
		mLen = 0;
		mType = 0;
		mExtra = 0;
		mTarget = 0;
		mData = null;
	}
	public short  mLen;
	public short  mType;
	public short  mExtra;
	public int	  mTarget;
	public byte[] mData;
}