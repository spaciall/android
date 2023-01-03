package spaciall.RPGMaker;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Paint;


public class Creature extends GraphicObject {
	protected static Texture m_Tex = new Texture();
    protected static Texture m_MsgTex = new Texture();
    
    protected static Paint mLabelPaint;
    protected static LabelMaker mLabels;

	protected static final int TOP1 = 0;
	protected static final int TOP2 = 1;
	protected static final int RIGHT1 = 2;
	protected static final int RIGHT2 = 3;
	protected static final int BOTTOM1 = 4;
	protected static final int BOTTOM2 = 5;
	protected static final int LEFT1 = 6;
	protected static final int LEFT2 = 7;
	
	protected static final int CHR = 0;
	protected static final int NPC = 1;
	
	public int m_nType = Creature.CHR;
	
    protected int m_nMotion = BOTTOM1;
	
    public Map m_Map = null;
	
	public int m_nIdx = 0;
	
	public int m_nTargetX = 0;
	public int m_nTargetY = 0;
	
	public int m_nX = 0;
	public int m_nY = 0;
	
	public int m_nCenterX = 0;
	public int m_nCenterY = 0;
	
	public int m_nWidthSize = 2;
	public int m_nHeightSize = 2;
	
	public int collisionCount = 0;
	
	public static ArrayList<String> msgArray = new ArrayList<String>();
	
	public int GetReverseMotion() 
	{
		switch (m_nMotion) {
		case TOP1:
			return BOTTOM1;
		case TOP2:
			return BOTTOM2;
		case BOTTOM1:
			return TOP1;
		case BOTTOM2:
			return TOP2;
		case RIGHT1:
			return LEFT1;
		case RIGHT2:
			return LEFT2;
		case LEFT1:
			return RIGHT1;
		case LEFT2:
			return RIGHT2;
		}
		
		return -1;
	}

	public static boolean Setup(GL10 gl, Context context, int chr_id, int text_box_id) {

		// 캐릭터 텍스쳐 Setup
		m_Tex.LoadTexture( gl, context, chr_id );
		
		// 텍스트 Box Setup
		m_MsgTex.LoadTexture(gl, context, text_box_id);
	
        mLabelPaint = new Paint();
        mLabelPaint.setTextSize(24);
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setARGB(0xff, 0x00, 0x00, 0x00);

		return true;
	}
	
	public boolean Update(GL10 gl) { 
		return true;
	}
	
	public boolean Render(GL10 gl) {
		return true;
	}
	
	
	public static int mLabelA = 0;
	public static int mLabelB = 0;
	public static int mLabelC = 0;
	public static int mLabelD = 0;
	public static int mLabelE = 0;
	public static int mLabelF = 0;
	public static void drawTextBox( GL10 gl ) 
	{
		// 메세지 상자를 그린다
		m_MsgTex.DrawTexture(gl, 64, 42, 0, 0, 362, 182, 0, 0, 0, 1.0f, 1.0f);
		
		// 그 위에 글자를 출력한다
		if (Renderer.m_bNeedUpdateMsgBox == true) {
	        mLabels.initialize(gl);
	        mLabels.beginAdding(gl);
	        mLabelA = mLabels.add(gl, msgArray.get(0), mLabelPaint);
	        mLabelB = mLabels.add(gl, msgArray.get(1), mLabelPaint);
	        mLabelC = mLabels.add(gl, msgArray.get(2), mLabelPaint);
	        mLabelD = mLabels.add(gl, msgArray.get(3), mLabelPaint);
	        mLabelE = mLabels.add(gl, msgArray.get(4), mLabelPaint);
	        mLabelF = mLabels.add(gl, msgArray.get(5), mLabelPaint);
	        mLabels.endAdding(gl);
	        
	        Renderer.m_bNeedUpdateMsgBox = false;
		}
		
	    mLabels.beginDrawing(gl, 800.f, 480.f);
	    mLabels.draw(gl, 76 * 1, 240, mLabelA);
        mLabels.draw(gl, 76 * 1, 216, mLabelB);
        mLabels.draw(gl, 76 * 1, 192, mLabelC);
        mLabels.draw(gl, 76 * 1, 168, mLabelD);
        mLabels.draw(gl, 76 * 1, 144, mLabelE);
        mLabels.draw(gl, 76 * 1, 120, mLabelF);
        mLabels.endDrawing(gl);
        
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
		gl.glDisable(GL10.GL_DEPTH_TEST);
	}
}
