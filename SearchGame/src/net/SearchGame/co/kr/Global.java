package net.SearchGame.co.kr;

import android.graphics.Paint;
import android.graphics.Paint.Style;

public final class Global {
	static Paint g_BackPaint = null;
	static Paint g_WhitePaint = null;
	static Paint g_RedPaint = null;
	static Paint g_BluePaint = null;
	static Paint g_GreenPaint = null;

	static Paint g_StageResultPaint = null;
	
	static Paint g_WhiteStrokPaint = null;
	
	static Paint g_HalfAlphaBackPaint = null;
	static Paint g_HalfAlphaWhitePaint = null;
	static Paint g_HalfAlphaRedPaint = null;
	static Paint g_HalfAlphaBluePaint = null;
	static Paint g_HalfAlphaGreenPaint = null;
	
	static String g_strContinueStagePrefernce = "ContinueStage";
	
	public Global() {
		// TODO Auto-generated constructor stub
	}
	
	boolean Setup()
	{
		g_BackPaint = new Paint();
		g_BackPaint.setARGB(255, 0, 0, 0);
		
		g_WhitePaint = new Paint();
		g_WhitePaint.setARGB(255, 255, 255, 255);

		g_RedPaint = new Paint();
		g_RedPaint.setARGB(255, 255, 0, 0);
		
		g_BluePaint = new Paint();
		g_BluePaint.setARGB(255, 0, 0, 255);
		
		g_GreenPaint = new Paint();
		g_GreenPaint.setARGB(255, 0, 255, 0);

		g_StageResultPaint = new Paint();
		g_StageResultPaint.setARGB(255, 255, 255, 255);
		g_StageResultPaint.setFakeBoldText(true);
		g_StageResultPaint.setDither(true);
		g_StageResultPaint.setTextSize(30);
		
		g_WhiteStrokPaint = new Paint();
		g_WhiteStrokPaint.setARGB(255, 255, 255, 255);
		g_WhiteStrokPaint.setStyle(Style.STROKE);
		
		g_HalfAlphaBackPaint = new Paint();
		g_HalfAlphaBackPaint.setARGB(128, 0, 0, 0);

		g_HalfAlphaWhitePaint = new Paint();
		g_HalfAlphaWhitePaint.setARGB(128, 255, 255, 255);
		
		g_HalfAlphaRedPaint = new Paint();
		g_HalfAlphaRedPaint.setARGB(128, 255, 0, 0);
		
		g_HalfAlphaBluePaint = new Paint();
		g_HalfAlphaBluePaint.setARGB(128, 0, 255, 0);
		
		g_HalfAlphaGreenPaint = new Paint();
		g_HalfAlphaGreenPaint.setARGB(128, 0, 0, 255);

		return true;
	}
}
