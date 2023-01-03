package spaciall.RPGMaker;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import spaciall.UI.Globals;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class Renderer implements android.opengl.GLSurfaceView.Renderer
{
	public static Context m_Context;
	
	protected int m_width, m_height; // 해상도

	// 메세지 박스의 글자 이미지를 위해 메모리 공간을 할당해야 하는가?
	public static boolean m_bNeedUpdateMsgBox = false;
	
	// 화면을 어둡게 처리해야 하는가?
	public static boolean m_bScreenDarkEffect = false;
	
	// 객체들
	public static Map m_Map = new Map();
	public static Chr m_Chr = new Chr(m_Map, 0, 5, 5);
	
	public static boolean m_bProcessEvent = false;
	public static boolean m_bShowTextBox = false;
	public static boolean m_bMoving = false;
	public static boolean m_bShakeScreen = true;
	
	public static boolean[] m_bGameSwitch = new boolean[256]; 
	
	protected int m_nScreenShakeValue = 10;
	protected int m_nScreenShakeCount = 0;
	
	// 맵 이동 화살표
	protected Texture m_texUpArrow = new Texture();
	protected Texture m_texDownArrow = new Texture();
	protected Texture m_texLeftArrow = new Texture();
	protected Texture m_texRightArrow = new Texture();
	
	// 검은색 흰색 텍스쳐
	protected Texture m_texBlack = new Texture();
	protected Texture m_texWhite = new Texture();

    private Projector mProjector = new Projector();
    private Random m_Random = new Random(System.currentTimeMillis());
    
    // 몬스터 정보
    public static BattleEnemyMgr m_BattleEnemyMgr = new BattleEnemyMgr();

    
    // 여기서 부터는 전투에 관한 변수들
	protected static Paint mLabelPaint;
	protected static Paint mSelectedLabelPaint;
	protected static Paint mInvalidLabelPaint;
	protected static Paint mEnemyInfoPaint;

	protected static LabelMaker mLabels;

	protected Texture m_texLeftMenu = new Texture();
	protected Texture m_texRightMenu = new Texture();
	protected Texture m_texChr = new Texture();
	
	protected int mSelectedLeftIdx = -1;
	protected int mSelectedRightIdx = -1;
	
	protected int mLabel00, mLabel10;
	protected int mLabel01, mLabel11;
	protected int mLabel02, mLabel12;
	protected int mLabel03, mLabel13;
	
	protected int mSelectedLabel00, mSelectedLabel10;
	protected int mSelectedLabel01, mSelectedLabel11;
	protected int mSelectedLabel02, mSelectedLabel12;
	protected int mSelectedLabel03, mSelectedLabel13;
	
	public ArrayList<BattleChr> m_ChrList = new ArrayList<BattleChr>();
	public ArrayList<BattleEnemy> m_EnemyList = new ArrayList<BattleEnemy>();
	
	// 최대  개수
	protected final int m_nMaxEnemyCount = 4;
	protected final int m_nMaxOurCount = 4;
	protected final int m_nMaxMenuCount = 4;
	
	// 개수
	protected int m_nEnemyCount = 4;
	protected int m_nOurCount = 2;
	
	public String[][] m_SelectStrArray = new String[m_nMaxMenuCount][m_nMaxMenuCount];
	public int[][] m_SelectIntArray = new int[m_nMaxMenuCount][m_nMaxMenuCount];
	public int[][] m_SelectInvalidIntArray = new int[m_nMaxMenuCount][m_nMaxMenuCount];
	public int[][] m_SelectValidIntArray = new int[m_nMaxMenuCount][m_nMaxMenuCount];
	
	public String[] m_EnemyHPStrArray = new String[m_nMaxEnemyCount];
	public int[] m_EnemyHPIntArray = new int[m_nMaxEnemyCount];
	
	protected long m_OldMilliSecond = 0;
	protected boolean[] m_bDamageAni = new boolean[m_nMaxEnemyCount];
	protected int[] m_nDamageAniCount = new int[m_nMaxEnemyCount];
	
	protected TextView m_textView = null;
	protected Handler m_Handler = null;
	
	private GL10 m_GL = null;
	
	private Rect rect00 = new Rect(0, 160, 240, 200); private Rect rect10 = new Rect(240, 160, 480, 200); 
	private Rect rect01 = new Rect(0, 200, 240, 240); private Rect rect11 = new Rect(240, 200, 480, 240);
	private Rect rect02 = new Rect(0, 240, 240, 280); private Rect rect12 = new Rect(240, 240, 480, 280);
	private Rect rect03 = new Rect(0, 280, 240, 320); private Rect rect13 = new Rect(240, 280, 480, 320);
	
	private Rect rectEnemy0_When4 = new Rect(16, 72, 16 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	private Rect rectEnemy1_When4 = new Rect(16 + 128, 72, 16 + 128 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	private Rect rectEnemy2_When4 = new Rect(16 + 256, 72, 16 + 256 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	private Rect rectEnemy3_When4 = new Rect(16 + 384, 72, 16 + 384 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	
	private Rect rectEnemy0_When3 = new Rect(16 + 64, 72, 16 + 64 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	private Rect rectEnemy1_When3 = new Rect(16 + 128 + 64, 72, 16 + 128 + 64 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	private Rect rectEnemy2_When3 = new Rect(16 + 256 + 64, 72, 16 + 256 + 64 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);

	private Rect rectEnemy0_When2 = new Rect(16 + 64 + 64, 72, 16 + 64 + 64 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	private Rect rectEnemy1_When2 = new Rect(16 + 128 + 64 + 64, 72, 16 + 128 + 64 + 64 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	
	private Rect rectEnemy0_When1 = new Rect(16 + 64 + 64 + 64 , 72, 16 + 64 + 64 + 64 + Chr.WIDTH_OBJ_PIXEL, 72 + Chr.HEIGHT_OBJ_PIXEL);
	
	protected boolean[] m_ActionValid = new boolean[m_nMaxEnemyCount];
	protected boolean[] m_EnemyActionValid = new boolean[m_nMaxEnemyCount];
	
	// 화면 진동 효과를 위해서
	protected boolean m_bShakeScreenFlag = false;
	protected int m_nShakeScreenCount = 0;
	
	// UpdateText 할 필요가 있는가?
	protected boolean m_bUpdateText = true;
	
	protected long m_ShakeOldMilliSecond = 0;
	protected long[] m_EnemyShakeOldMilliSecond = new long[4];
	protected int[] m_nEnemyShakeScreenCount = new int[4];
	
	
	// 배틀 모드 유무
	boolean m_bBattleMode = false;
   
    
    // Thread 안전한 키 이벤트 List
    ArrayList<MotionEvent> m_touchPool = new ArrayList<MotionEvent>();
    synchronized private void PushKeyPool(MotionEvent event) {
    	m_touchPool.add(event);
    }
    
    synchronized private MotionEvent PullKeyPool() {
    	try {
    		if (m_touchPool.size() > 0) {
	    		MotionEvent event = m_touchPool.get(m_touchPool.size() - 1);
	    		return event;
    		}
    		return null;
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    synchronized void ClearKeyPool() {
   		m_touchPool.clear();
    }
    
	public Renderer( Context context, int width, int height )
	{
		m_Context = context;
		
		// 해상도 설정
		m_width = width;
		m_height = height;
		
		// 여기서 부터는 전투에 관한 설정
		// 텍스트 설명글을 출력하는 텍스트 뷰
		m_textView = ((OpenGL)m_Context).m_textView;
		m_Handler = ((OpenGL)m_Context).mHandler;
		
		m_ChrList.clear();
		m_EnemyList.clear();
		
		for (int j = 0; j < m_nMaxOurCount; ++j) {
			for (int i = 0; i < m_nMaxOurCount; ++i) {
				m_SelectStrArray[j][i] = "";
				m_SelectIntArray[j][i] = -1;
				m_SelectInvalidIntArray[j][i] = -1;
			}
		}
		
		for (int i = 0; i < m_nMaxEnemyCount; ++i) {
			m_ActionValid[i] = false;
			m_EnemyActionValid[i] = false;
			m_bDamageAni[i] = false;
			m_nDamageAniCount[i] = 0;
			m_EnemyShakeOldMilliSecond[i] = 0;
			m_nEnemyShakeScreenCount[i] = 0;
		}

		for (int i = 0; i < m_nEnemyCount; ++i) m_EnemyActionValid[i] = true;
		for (int i = 0; i < m_nOurCount; ++i) m_ActionValid[i] = true;		
	}
	
	public static Context GetContext() {
		return m_Context;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if (m_bBattleMode) processBattleTouchEvent(event);
		else PushKeyPool(event);
		
		return true;
	}
	
	public boolean processBattleTouchEvent(MotionEvent event) {
		
		// 좌표 따기
		int x = (int)event.getX();
		int y = (int)event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (rect00.contains(x, y)) {
				//Toast.makeText(m_Context, "00", Toast.LENGTH_SHORT).show();
				if (mSelectedRightIdx < 0 && m_ActionValid[0] && (m_ChrList.get(0).m_HP > 0)) mSelectedLeftIdx = 0;
				
			} else if (rect01.contains(x, y)) {
				//Toast.makeText(m_Context, "01", Toast.LENGTH_SHORT).show();
				if (mSelectedRightIdx < 0 && m_ActionValid[1] && (m_ChrList.get(1).m_HP > 0)) mSelectedLeftIdx = 1;
				
			} else if (rect02.contains(x, y)) {
				//Toast.makeText(m_Context, "02", Toast.LENGTH_SHORT).show();
				if (mSelectedRightIdx < 0 && m_ActionValid[2] && (m_ChrList.get(2).m_HP > 0)) mSelectedLeftIdx = 2;
				
			} else if (rect03.contains(x, y)) {
				//Toast.makeText(m_Context, "03", Toast.LENGTH_SHORT).show();
				if (mSelectedRightIdx < 0 && m_ActionValid[3] && (m_ChrList.get(3).m_HP > 0)) mSelectedLeftIdx = 3;
				
			} else if (rect10.contains(x, y)) {
				if (mSelectedLeftIdx >= 0) {
					mSelectedRightIdx = 0;
					Toast.makeText(m_Context, "공격할 대상을 클릭해 주십시오.", Toast.LENGTH_SHORT).show();
				}
				
			} else if (rect11.contains(x, y)) {
				//Toast.makeText(m_Context, "11", Toast.LENGTH_SHORT).show();
				//if (mSelectedLeftIdx >= 0) mSelectedRightIdx = 1;
				
			} else if (rect12.contains(x, y)) {
				//Toast.makeText(m_Context, "12", Toast.LENGTH_SHORT).show();
				//if (mSelectedLeftIdx >= 0) mSelectedRightIdx = 2;
				
			} else if (rect13.contains(x, y)) {
				//Toast.makeText(m_Context, "13", Toast.LENGTH_SHORT).show();
				if (mSelectedLeftIdx >= 0) mSelectedRightIdx = 3;
				
			} else { 
				if (m_nEnemyCount == 4) {
					if (rectEnemy0_When4.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(0);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[0] = true;
							m_nDamageAniCount[0] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					} else if (rectEnemy1_When4.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(1);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[1] = true;
							m_nDamageAniCount[1] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					} else if (rectEnemy2_When4.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(2);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[2] = true;
							m_nDamageAniCount[2] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;

							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					} else if (rectEnemy3_When4.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(3);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[3] = true;
							m_nDamageAniCount[3] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
					}
				} else if (m_nEnemyCount == 3) {
					if (rectEnemy0_When3.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(0);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
						
							// 애니메이션 세팅
							m_bDamageAni[0] = true;
							m_nDamageAniCount[0] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					} else if (rectEnemy1_When3.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(1);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[1] = true;
							m_nDamageAniCount[1] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					} else if (rectEnemy2_When3.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(2);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[2] = true;
							m_nDamageAniCount[2] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					} 
				} else if (m_nEnemyCount == 2) {
					if (rectEnemy0_When2.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(0);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[0] = true;
							m_nDamageAniCount[0] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					} else if (rectEnemy1_When2.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(1);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[1] = true;
							m_nDamageAniCount[1] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					}					
				} else if (m_nEnemyCount == 1) {
					if (rectEnemy0_When1.contains(x, y)) {
						if (mSelectedLeftIdx >= 0 && mSelectedRightIdx >= 0) {
							// 전투 계산
							BattleChr chr = m_ChrList.get(mSelectedLeftIdx);
							BattleEnemy enemy = m_EnemyList.get(0);

							AttackResult result = ProcessAttackByOur(chr, enemy);
							
							// 공격 메세지
							Message msg = new Message();
							
							MessageObjectArg msgObj = new MessageObjectArg();
							msgObj.str_ = chr.m_Name + "의 공격!, " + enemy.m_Name + "의 HP -" + result.damage + "!";
							
							msg.what = 0;
							msg.obj = msgObj;
							
							m_Handler.sendMessage(msg);
							
							// 공격했으므로 다음 턴 까지 행동 불가
							m_ActionValid[mSelectedLeftIdx] = false;
		
							// 애니메이션 세팅
							m_bDamageAni[0] = true;
							m_nDamageAniCount[0] = 0;
							
							// 다시 캐릭터 이름 부터 선택 가능 하도록 세팅
							mSelectedLeftIdx = -1;
							mSelectedRightIdx = -1;
							
							// 아군과 적군의 HP등의 숫자정보가 업데이트 될 수 있도록 해 준다.
							m_bUpdateText = true;
						}
						
					}					
				}
			}
		}
		
		return true;
	}	
	
	private boolean ProcessKeyEvent() {
		MotionEvent event = PullKeyPool();
		if (event == null) return false;
		
		ClearKeyPool();
		
		boolean flag = false;

		float x = event.getX();
		float y = event.getY();
		
    	int tile_x = (int)(x / GraphicObject.WIDTH_MOVE_PIXEL);
    	int tile_y = (int)(y / GraphicObject.HEIGHT_MOVE_PIXEL);
    	
		int screen_tile_x = m_Map.screen_x + tile_x;
		int screen_tile_y = m_Map.screen_y + tile_y;
		
		// 메세지박스를 보여주고 있었다면 메세지 박스를 사라지게 하는 처리만 한다.
		if (m_bShowTextBox == true) {
			m_bShowTextBox = false;
			return true;
		}
		
		// 다른 벤트를 진행 중이라면 버튼 처리를 무시한다. 
		if (m_bProcessEvent == true) {
			return true;
		}
		
		// UP 이벤트에 처리
		if (event.getAction() == MotionEvent.ACTION_UP) {
			
	    	// NPC를 클릭했는지 조사한다.
	    	for (int i = 0; i < m_Map.creaturePool.size(); ++i) { 
	    		Creature crt = m_Map.creaturePool.get(i);
	    		
	    		if (screen_tile_x - 1 >= crt.m_nX && screen_tile_x <= (crt.m_nX + Creature.WIDTH_OBJ_PIXEL / Creature.WIDTH_MOVE_PIXEL) &&
	    			screen_tile_y - 1 >= crt.m_nY && screen_tile_y <= (crt.m_nY + Creature.WIDTH_OBJ_PIXEL / Creature.HEIGHT_MOVE_PIXEL)) {
	    		
	    			if (crt.m_nType == Creature.NPC) { // 클릭 한 것이 NPC 라면
	    				NonPC npc = (NonPC)crt;
	    				Event npcEvt = null;
    				
	    				// 숫자가 큰 것부터 게임스위치에 대한 이벤트를 찾는다.
	    				for (int j = 256 - 1; j >= 0; --j) {
	    					if (npc.eventPool.containsKey(j) == true) {
	    						if (Renderer.m_bGameSwitch[j] == true) {
	    							npcEvt = npc.eventPool.get(j);
	    							break;
	    						}
	    					}
	    				}
	    				
	    				// 아무런 이벤트도 찾지 못했을 시, 0번 이벤트를 발동시켜준다.
	    				if (npcEvt == null) {
		    				// 만약에 NPC가 1 이상의 다른 스위치에 대한 이벤트를 가지고 있고 0번 이벤트를 가지고 있지 않다면 
		    				// 처리하지 않는다.
		    				// 숫자가 큰 것부터 게임스위치에 대한 이벤트를 찾는다.
		    				for (int j = 256 - 1; j >= 1; --j) {
		    					if (npc.eventPool.containsKey(j) == true) {
		    						return true;
		    					}
		    				}
		    				
	    					npcEvt = npc.eventPool.get(0);
	    				}
	    				
	    				// 현재 이벤트 조건이 클릭할 떄 발동하는 것 이라면
	    				if (!(npcEvt.actionWhere == Event.TRIGGER_ON_CLICK_BY_CHR)) {
	    				} else {
	    					// 주인공과 NPC 의 DIFF
	    					int x_diff = Math.abs(m_Chr.m_nX - npc.m_nX);
	    					int y_diff = Math.abs(m_Chr.m_nY - npc.m_nY);
	    					int x_diff_not_abs = m_Chr.m_nX - npc.m_nX;
	    					int y_diff_not_abs = m_Chr.m_nY - npc.m_nY;
	
	    					// 대화할 수 있는 거리에 있다면 발동
	    					if (x_diff <= (Creature.WIDTH_OBJ_PIXEL / Creature.WIDTH_MOVE_PIXEL)) {
	    						if (y_diff <= (Creature.HEIGHT_OBJ_PIXEL / Creature.HEIGHT_MOVE_PIXEL)) {
	    							if (npcEvt.dataPool.size() > 0) {
		    							flag = true;
			    						
			    						// 주인공이 npc를 쳐다 보도록 한다.
			    						if (x_diff < y_diff) {
			    							if (y_diff_not_abs >= 0) m_Chr.m_nMotion = Creature.TOP1;
			    							else m_Chr.m_nMotion = Creature.BOTTOM1;
			    						} else {
			    							if (x_diff_not_abs >= 0) m_Chr.m_nMotion = Creature.LEFT1;
			    							else m_Chr.m_nMotion = Creature.RIGHT1;
			    						}
			    						
			    						// npc가 캐릭터를 쳐다보도록 한다.
			    						if (m_Chr.GetReverseMotion() != -1) npc.m_nMotion = m_Chr.GetReverseMotion();
	
			    						// NPC의 이벤트 세팅
			    						npc.curEvent = npcEvt;
			        					// 현재 이벤트 중임을 글로벌 수준에서 알려준다.
			        					Renderer.m_bProcessEvent = true;
			        					// 해당 NPC에게 본인이 이벤트 중임을 알려준다.
			        					npc.m_bProcessEvent = true;
			        					
			        					break;
	    							}
	    						}
	    					}
	    				}
	    				
	    			} else if (crt.m_nType == Creature.CHR) { // 클릭 한 것이 주인공 이라면
	    				Chr chr = (Chr)crt;
	    				
	    			}
	    		}
	    	}
	    	
    		// 이벤트가 발동하지 않았을 시, 다른 맵으로 이동하려고 하는 것인가?
    		if (flag == false) {
    			if (screen_tile_x >= Map.WIDTH_MAP - 1) {
    				if (m_Chr.m_nX >= Map.WIDTH_MAP - 2) {
    					
    	    			// 다른 맵으로 이동 하기 전 기존의 데이터를 저장해 준다.
    	    			((OpenGL)m_Context).SavePreference();
	
	    				Map newMap = new Map();
	    				newMap.SetRenderer(this);
	    				
	    				if (newMap.Setup(m_Context, m_Chr.m_nMapFileX + 1, m_Chr.m_nMapFileY) == true) {
	    					newMap.SetupIvent(m_Context, m_Chr.m_nMapFileX + 1, m_Chr.m_nMapFileY);
	    					
	    					++m_Chr.m_nMapFileX;
	    					
	    					newMap.screen_x = 0;
	    					newMap.screen_y = m_Map.screen_y;
	    					newMap.creaturePool.add(m_Chr);
	    					
	    					m_Chr.m_nX = 0;
	    					m_Chr.m_nTargetX = 0;
	 
	    					m_Chr.m_Map = newMap;
	    					m_Map = newMap;
	    					
	    					System.gc();
	    				}
    				}
    			} else if (screen_tile_x <= 0) {
    				if (m_Chr.m_nX <= 0) {
    					
    	    			// 다른 맵으로 이동 하기 전 기존의 데이터를 저장해 준다.
    	    			((OpenGL)m_Context).SavePreference();
    					
	    				Map newMap = new Map();
	    				newMap.SetRenderer(this);
	    				
	    				if (newMap.Setup(m_Context, m_Chr.m_nMapFileX - 1, m_Chr.m_nMapFileY) == true) {
	    					newMap.SetupIvent(m_Context, m_Chr.m_nMapFileX - 1, m_Chr.m_nMapFileY);
	    					
	    					--m_Chr.m_nMapFileX;
	    					
	    					newMap.screen_x = Map.WIDTH_MAP - Map.WIDTH_SCREEN;
	    					newMap.screen_y = m_Map.screen_y;
	    					newMap.creaturePool.add(m_Chr);
	    					
	    					m_Chr.m_nX = Map.WIDTH_MAP - 2;
	    					m_Chr.m_nTargetX = Map.WIDTH_MAP - 2;
	    					
	    					m_Chr.m_Map = newMap;
	    					m_Map = newMap;
	    					
	    					System.gc();
	    				}
    				}
    					
    			} else if (screen_tile_y >= Map.HEIGHT_MAP - 1) {
    				if (m_Chr.m_nY >= Map.HEIGHT_MAP - 2) {
    					
    	    			// 다른 맵으로 이동 하기 전 기존의 데이터를 저장해 준다.
    	    			((OpenGL)m_Context).SavePreference();
    					
	    				Map newMap = new Map();
	    				newMap.SetRenderer(this);
	    				
	    				if (newMap.Setup(m_Context, m_Chr.m_nMapFileX, m_Chr.m_nMapFileY + 1) == true) {
	    					newMap.SetupIvent(m_Context, m_Chr.m_nMapFileX, m_Chr.m_nMapFileY + 1);
	    					
	    					++m_Chr.m_nMapFileY;
	    					
	    					newMap.screen_x = m_Map.screen_x;
	    					newMap.screen_y = 0;
	    					newMap.creaturePool.add(m_Chr);
	    					
	    					m_Chr.m_nY = 0;
	    					m_Chr.m_nTargetY = 0;
	 
	    					m_Chr.m_Map = newMap;
	    					m_Map = newMap;
	    					
	    					System.gc();
	    				}
    				}
    				
    			} else if (screen_tile_y <= 0) {
    				if (m_Chr.m_nY <= 0) {
    					
    	    			// 다른 맵으로 이동 하기 전 기존의 데이터를 저장해 준다.
    	    			((OpenGL)m_Context).SavePreference();
    					
	    				Map newMap = new Map();
	    				newMap.SetRenderer(this);
	    				
	    				if (newMap.Setup(m_Context, m_Chr.m_nMapFileX, m_Chr.m_nMapFileY - 1) == true) {
	    					newMap.SetupIvent(m_Context, m_Chr.m_nMapFileX, m_Chr.m_nMapFileY - 1);
	    					
	    					--m_Chr.m_nMapFileY;
	    					
	    					newMap.screen_x = m_Map.screen_x;
	    					newMap.screen_y = m_Map.HEIGHT_MAP - m_Map.HEIGHT_SCREEN;
	    					newMap.creaturePool.add(m_Chr);
	    					
	    					m_Chr.m_nY = Map.HEIGHT_MAP - 2;
	    					m_Chr.m_nTargetY = Map.HEIGHT_MAP - 2;
	 
	    					m_Chr.m_Map = newMap;
	    					m_Map = newMap;
	    					
	    					System.gc();
	    				}
    				}
    				
    			}
    		}
    		
	    	if (flag == false) {
		    	// 주인공 이동 목표 좌표 설정
	    		flag = true;
		    	m_Chr.m_nTargetX = m_Chr.m_Map.screen_x + tile_x - (GraphicObject.WIDTH_OBJ_PIXEL / 2) / GraphicObject.WIDTH_MOVE_PIXEL; // 나중에 수식으로 고침
		    	m_Chr.m_nTargetY = m_Chr.m_Map.screen_y + tile_y - (GraphicObject.HEIGHT_OBJ_PIXEL / 2) / GraphicObject.HEIGHT_MOVE_PIXEL; // 나중에 수식으로 고침
	    	}
		}
    	
		return true;
	}
	
	public void onSurfaceCreated( GL10 gl, EGLConfig config )
	{
		// 혹시 홈/전원 버튼으로 인해 다시  이 곳으로 왔다면 글자를 뿌려주기 전 메모리를 다시 할당하도록 해 준다.
		m_bNeedUpdateMsgBox = true;
		
		// 일반 리소스 셋업
		Creature.Setup(gl, m_Context, R.drawable.chr0, R.drawable.msgbox);
		Tile.Setup(gl, m_Context, R.drawable.map);
		
		m_Map.SetRenderer(this);
		m_Map.TestSetup(m_Context);
		
		m_BattleEnemyMgr.Setup(m_Context);
		LevelExpTable.Setup();
		
		// 게임 스위치 세팅
		for (int i = 0; i < 256; ++i) m_bGameSwitch[i] = false;
		m_bGameSwitch[0] = true;
		
		// 맵 이동 화살표 리소스
		m_texUpArrow.LoadTexture(gl, m_Context, R.drawable.up);
		m_texDownArrow.LoadTexture(gl, m_Context, R.drawable.down);
		m_texLeftArrow.LoadTexture(gl, m_Context, R.drawable.left);
		m_texRightArrow.LoadTexture(gl, m_Context, R.drawable.right);
		
		// 검은색 흰색 리소스
		m_texBlack.LoadTexture(gl, m_Context, R.drawable.blackbgrnd);
		m_texWhite.LoadTexture(gl, m_Context, R.drawable.whitebgrnd);
		
		// 여기서 부터는 전투를 위한 세팅
		// 텍스쳐 불러오기
		m_texLeftMenu.LoadTexture(gl, m_Context, R.drawable.msgbox_battle);
		m_texChr.LoadTexture(gl, m_Context, R.drawable.chr0);		

		gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );
		gl.glClearDepthf( 1.0f );
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST );

		gl.glOrthof( 0.0f, GraphicObject.BASE_WIDTH, GraphicObject.BASE_HEIGHT, 0.0f, 1.0f, 1.0f );
		gl.glViewport( 0, 0, (int)GraphicObject.BASE_WIDTH, (int)GraphicObject.BASE_HEIGHT );
		
		// 제목 30분
	    if (Creature.mLabels != null) {
	    	Creature.mLabels.shutdown(gl);
        } else {
        	Creature.mLabels = new LabelMaker(true, (int)GraphicObject.BASE_WIDTH, (int)GraphicObject.BASE_HEIGHT);
        }
	    
	    // 여기서 부터는 전투를 위한 세팅
		// 글자를 나타내기 위해서 필요한 세팅
		// 테스트 데이터 세팅
		BattleSetup();
	    
		SetupTextPaint(gl);
		updateText(gl);
	}
	
	public void onSurfaceChanged( GL10 gl, int width, int height )
	{
		gl.glOrthof( 0.0f, width, height, 0.0f, 1.0f, -1.0f );
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glViewport( 0, 0, width, height );
		mProjector.setCurrentView(0, 0, width, height);
		
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
		gl.glEnable( GL10.GL_BLEND );
		gl.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
		gl.glDisable(GL10.GL_DEPTH_TEST);
	}
	
	private double oldSystemTime = 0;
	public void onDrawFrame( GL10 gl )
	{
		try {
			gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
			
			if (m_bBattleMode) {
				DrawBattle(gl);
			} else {
				if (m_bShakeScreen) {
					if (System.currentTimeMillis() - oldSystemTime > 25) {
						m_nScreenShakeValue = -m_nScreenShakeValue;
						gl.glTranslatef(m_nScreenShakeValue, 0, 0);
						
						if (++m_nScreenShakeCount >= 4) {
							m_nScreenShakeCount = 0;
							m_bShakeScreen = false;
						}
					}
				}
				
				if (System.currentTimeMillis() - oldSystemTime > 50) {
					oldSystemTime = System.currentTimeMillis();
					
					// 키 이벤트 처리
					ProcessKeyEvent();
		
					// 업데이트
					m_Map.Update(gl);
				}
				
				// 렌더링
				m_Map.Render(gl);
		
				gl.glClearDepthf(1.0f);
				gl.glEnable(GL10.GL_BLEND);
				gl.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
				gl.glDisable(GL10.GL_DEPTH_TEST);
				
				// 화살표
				DrawMapMoveArrow(gl);
				
				// 메세지 박스
				if (m_bShowTextBox == true) Creature.drawTextBox(gl);
				
				// 아무것도 진행 중이지 않을 때 일정 확률로 적을 만난 것 처럼 전투 화면으로 전환한다.
				if (m_bProcessEvent == false && m_bShakeScreen == false && m_bShowTextBox == false) {
					if (Math.abs(m_Random.nextInt()) % 1000 <= 1) {
						BattleSetup();
						m_bBattleMode = true;
						
				        // 아군과 적군의 수를 갱신해 준다.
				        m_nOurCount = m_ChrList.size();
				        m_nEnemyCount = m_EnemyList.size();
				        
				        // 아군과 적군의 공격상태를 가능으로 해 준다.
				        for (int i = 0; i < m_nOurCount; ++i) m_ActionValid[i] = true;
				        for (int i = m_nOurCount; i < m_nMaxOurCount - m_nOurCount; ++i) m_ActionValid[i] = false;
	
				        for (int i = 0; i < m_nEnemyCount; ++i) m_EnemyActionValid[i] = true;
				        for (int i = m_nEnemyCount; i < m_nMaxEnemyCount - m_nEnemyCount; ++i) m_EnemyActionValid[i] = false;
				        
						if (m_EnemyList.size() <= 0) { 
							m_bBattleMode = false;
						}
						
						// HP와 MP를 갱신해 준다. 일단 주인공은 1명이라고 정해둔다.
						m_Chr.m_BattleCreature.m_HP = m_ChrList.get(0).m_HP;
						m_Chr.m_BattleCreature.m_MP = m_ChrList.get(0).m_MP;
					}
				}
				
				// 어둑어둑한 배경
				if (m_bScreenDarkEffect == true)
					m_texBlack.DrawTexture(gl, 0, 0, 0, 0, Globals.DEVICE_WIDTH, Globals.DEVICE_HEIGHT, 0, 0, 0, 1, 1);
			}
		} catch (Exception ex) {
			Toast.makeText(m_Context, ex.getMessage(), Toast.LENGTH_LONG).show();
			
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
			}
		}
	}
	
	private int m_nArrayCount = 0;
	public void DrawMapMoveArrow( GL10 gl) {
		if (!(++m_nArrayCount % 4 == 0)) return;
		
		// 왼쪽, 오른쪽
		if (m_Chr.m_nX <= 0) 
			m_texLeftArrow.DrawTexture(gl, 0, (m_Chr.m_nY - m_Map.screen_y) * Creature.WIDTH_MOVE_PIXEL, 0, 0, 32, 64, 0, 0, 0, 1.f, 1.f);
		if (m_Chr.m_nX >= Map.WIDTH_MAP - 2) 
			m_texRightArrow.DrawTexture(gl, (Map.WIDTH_SCREEN - 1) * Creature.WIDTH_MOVE_PIXEL, (m_Chr.m_nY - m_Map.screen_y) * Creature.WIDTH_MOVE_PIXEL, 0, 0, 32, 64, 0, 0, 0, 1.f, 1.f);
		
		// 위, 아래
		if (m_Chr.m_nY <= 0)
			m_texUpArrow.DrawTexture(gl, (m_Chr.m_nX - m_Map.screen_x) * Creature.WIDTH_MOVE_PIXEL, 0, 0, 0, 64, 32, 0, 0, 0, 1.f, 1.f);
		if (m_Chr.m_nY >= Map.HEIGHT_MAP - 2)
			m_texDownArrow.DrawTexture(gl, (m_Chr.m_nX - m_Map.screen_x) * Creature.WIDTH_MOVE_PIXEL, (Map.HEIGHT_SCREEN - 1) * Creature.HEIGHT_MOVE_PIXEL, 0, 0, 64, 32, 0, 0, 0, 1.f, 1.f);
	}
	
	public void setText(int x, int y, String str) {
		if (x >= 0 && x < 4 && y >= 0 && y < 4) {
			m_SelectStrArray[y][x] = str;
		}
	}
	
	public void updateText(GL10 gl) {
		if (!m_bUpdateText) return;
		m_bUpdateText = false;
		
        mLabels.initialize(gl);
        mLabels.beginAdding(gl);
        
        // 아군의 각종 정보와 메뉴에 나타나는 String을 만든다.
        for (int i = 0; i < m_nOurCount; ++i) {
        	if (m_ChrList.size() > i) {
        		m_SelectIntArray[0][i] = mLabels.add(gl, m_ChrList.get(i).m_Name + " " + m_ChrList.get(i).m_MaxHP + "/" + m_ChrList.get(i).m_HP, mLabelPaint);
        	} else {
        		m_SelectIntArray[0][i] = mLabels.add(gl, "", mLabelPaint);
        	}
        }
        for (int i = m_nOurCount; i < m_nMaxOurCount; ++i) { 
        	m_SelectIntArray[0][i] = mLabels.add(gl, "", mLabelPaint);
        }
        
        m_SelectIntArray[1][0] = mLabels.add(gl, "공격", mLabelPaint);
        m_SelectIntArray[1][1] = mLabels.add(gl, "마법", mInvalidLabelPaint);
        m_SelectIntArray[1][2] = mLabels.add(gl, "도구", mInvalidLabelPaint);
        m_SelectIntArray[1][3] = mLabels.add(gl, "도망가기", mLabelPaint);
        
        for (int i = 0; i < m_nOurCount; ++i)
        	if (m_ChrList.size() > i) {
        		m_SelectInvalidIntArray[0][i] = mLabels.add(gl, m_ChrList.get(i).m_Name + " " + m_ChrList.get(i).m_MaxHP + "/" + m_ChrList.get(i).m_HP, mInvalidLabelPaint);
        	} else {
        		m_SelectInvalidIntArray[0][i] = mLabels.add(gl, "", mInvalidLabelPaint);
        	}
        
        for (int i = m_nOurCount; i < m_nMaxOurCount; ++i) 
        	m_SelectInvalidIntArray[0][i] = mLabels.add(gl, "", mInvalidLabelPaint);
        
        
        m_SelectInvalidIntArray[1][0] = mLabels.add(gl, "공격", mInvalidLabelPaint);
        m_SelectInvalidIntArray[1][1] = mLabels.add(gl, "마법", mInvalidLabelPaint);
        m_SelectInvalidIntArray[1][2] = mLabels.add(gl, "도구", mInvalidLabelPaint);
        m_SelectInvalidIntArray[1][3] = mLabels.add(gl, "도망가기", mInvalidLabelPaint);
        
        for (int i = 0; i < m_nOurCount; ++i) 
        {
        	if (m_ChrList.size() > i)
        		m_SelectValidIntArray[0][i] = mLabels.add(gl, m_ChrList.get(i).m_Name + " " + m_ChrList.get(i).m_MaxHP + "/" + m_ChrList.get(i).m_HP, mInvalidLabelPaint);
        	else 
        		m_SelectValidIntArray[0][i] = mLabels.add(gl, "", mInvalidLabelPaint);
        }
        
        for (int i = m_nOurCount; i < m_nMaxOurCount; ++i) 
        	m_SelectValidIntArray[0][i] = mLabels.add(gl, "", mInvalidLabelPaint);
        
        m_SelectValidIntArray[1][0] = mLabels.add(gl, "공격", mSelectedLabelPaint);
        m_SelectValidIntArray[1][1] = mLabels.add(gl, "마법", mInvalidLabelPaint);
        m_SelectValidIntArray[1][2] = mLabels.add(gl, "도구", mInvalidLabelPaint);
        m_SelectValidIntArray[1][3] = mLabels.add(gl, "도망가기", mSelectedLabelPaint);
        
        // 적군의 HP를 나타내는 String을 만든다.
        for (int i = 0; i < m_nEnemyCount; ++i) {
        	if (m_EnemyList.size() > i) m_EnemyHPIntArray[i] =  mLabels.add(gl, m_EnemyList.get(i).m_MaxHP + "/" + m_EnemyList.get(i).m_HP, mEnemyInfoPaint);
        	else m_EnemyHPIntArray[i] =  mLabels.add(gl, "", mEnemyInfoPaint);
        }
        
        mLabels.endAdding(gl);		
        
        // 메모리 부족하면 안되니까 해 준다.
        System.gc();
	}
	
	public void onDrawEnemy(GL10 gl) {
		long curMilliSecond = System.currentTimeMillis();
		
		if ((curMilliSecond - m_OldMilliSecond) > 150) {
			m_OldMilliSecond = curMilliSecond;
			
			for (int i = 0; i < 4; ++i) {
				if (m_bDamageAni[i] == true) 
					if (++m_nDamageAniCount[i] > 10) m_bDamageAni[i] = false;
			}
		}
		
		if (m_nEnemyCount == 4) {
			if (m_EnemyList.size() > 0) {
				if (m_bDamageAni[0] == true) {
					if ((m_nDamageAniCount[0] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(0).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(0).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(0).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}
			
			if (m_EnemyList.size() > 1) {
				if (m_bDamageAni[1] == true) {
					if ((m_nDamageAniCount[1] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 128, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(1).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(1).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 128, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(1).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}
		
			if (m_EnemyList.size() > 2) {
				if (m_bDamageAni[2] == true) {
					if ((m_nDamageAniCount[2] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 256, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(2).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(2).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 256, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(2).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}
			
			if (m_EnemyList.size() > 3) {
				if (m_bDamageAni[3] == true) {
					if ((m_nDamageAniCount[3] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 384, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(3).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(3).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 384, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(3).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}		
			}
		} else if (m_nEnemyCount == 3) {
			if (m_EnemyList.size() > 0) {
				if (m_bDamageAni[0] == true) {
					if ((m_nDamageAniCount[0] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(0).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(0).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(0).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}
			
			if (m_EnemyList.size() > 1) {
				if (m_bDamageAni[1] == true) {
					if ((m_nDamageAniCount[1] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 128 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(1).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(1).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 128 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(1).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}
		
			if (m_EnemyList.size() > 2) {
				if (m_bDamageAni[2] == true) {
					if ((m_nDamageAniCount[2] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 256 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(2).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(2).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 256 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(2).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}			
		} else if (m_nEnemyCount == 2) {
			if (m_EnemyList.size() > 0) {
				if (m_bDamageAni[0] == true) {
					if ((m_nDamageAniCount[0] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 64 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(0).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(0).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 64 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(0).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}
			
			if (m_EnemyList.size() > 1) {
				if (m_bDamageAni[1] == true) {
					if ((m_nDamageAniCount[1] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 128 + 64 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(1).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(1).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 128 + 64 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(1).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}
		} else if (m_nEnemyCount == 1) {
			if (m_EnemyList.size() > 0) {
				if (m_bDamageAni[0] == true) {
					if ((m_nDamageAniCount[0] % 2) != 0) {
						m_texChr.DrawTexture(gl, 16 + 128 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(0).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				} else {
					if (m_EnemyList.get(0).m_HP > 0) {
						m_texChr.DrawTexture(gl, 16 + 128 + 64, 72,  
								Chr.WIDTH_OBJ_PIXEL * Chr.BOTTOM1, m_EnemyList.get(0).m_CreatureID * Chr.HEIGHT_OBJ_PIXEL, Chr.WIDTH_OBJ_PIXEL, Chr.HEIGHT_OBJ_PIXEL, 
								0, 0, 0, 
								1.f, 1.f);
					}
				}
			}
		}
	}
	
	public void onDrawTextBox(GL10 gl) {
		m_texLeftMenu.DrawTexture(gl, 0, 160,  
				0, 0, 480, 160, 
				0, 0, 0, 
				1.f, 1.f);	
		
	    mLabels.beginDrawing(gl, 800.f, 480.f);
	    // 아군의 정보와 메뉴의 문자열을 출력
	    if (m_ActionValid[0] == true && m_ChrList.size() > 0 && m_ChrList.get(0).m_HP > 0) mLabels.draw(gl, 16, 126, m_SelectIntArray[0][0]);
	    else mLabels.draw(gl, 16, 126, m_SelectInvalidIntArray[0][0]);
	    
	    if (m_ActionValid[1] == true && m_ChrList.size() > 1 && m_ChrList.get(1).m_HP > 0) mLabels.draw(gl, 16, 86, m_SelectIntArray[0][1]);
	    else mLabels.draw(gl, 16, 86, m_SelectInvalidIntArray[0][1]);
	    
	    if (m_ActionValid[2] == true && m_ChrList.size() > 2 && m_ChrList.get(2).m_HP > 0) mLabels.draw(gl, 16, 46, m_SelectIntArray[0][2]);
	    else mLabels.draw(gl, 16, 46, m_SelectInvalidIntArray[0][2]);
	    
	    if (m_ActionValid[3] == true && m_ChrList.size() > 3 && m_ChrList.get(3).m_HP > 0) mLabels.draw(gl, 16, 6, m_SelectIntArray[0][3]);
	    else mLabels.draw(gl, 16, 6, m_SelectInvalidIntArray[0][3]);
	    
	    mLabels.draw(gl, 256, 126, m_SelectIntArray[1][0]);
	    mLabels.draw(gl, 256, 86, m_SelectIntArray[1][1]);
	    mLabels.draw(gl, 256, 46, m_SelectIntArray[1][2]);
	    mLabels.draw(gl, 256, 6, m_SelectIntArray[1][3]);
	    
        if (mSelectedLeftIdx == 0) mLabels.draw(gl, 16, 126, m_SelectValidIntArray[0][0]);
        else if (mSelectedLeftIdx == 1) mLabels.draw(gl, 16, 86, m_SelectValidIntArray[0][1]);
        else if (mSelectedLeftIdx == 2) mLabels.draw(gl, 16, 46, m_SelectValidIntArray[0][2]);
        else if (mSelectedLeftIdx == 3) mLabels.draw(gl, 16, 6, m_SelectValidIntArray[0][3]);
        
        if (mSelectedRightIdx == 0) mLabels.draw(gl, 256, 126, m_SelectValidIntArray[1][0]);
        else if (mSelectedRightIdx == 1) mLabels.draw(gl, 256, 86, m_SelectValidIntArray[1][1]);
        else if (mSelectedRightIdx == 2) mLabels.draw(gl, 256, 46, m_SelectValidIntArray[1][2]);
        else if (mSelectedRightIdx == 3) mLabels.draw(gl, 256, 6, m_SelectValidIntArray[1][3]);
        
        // 적군의 정보를 출력
        if (m_nEnemyCount == 4) {
        	mLabels.draw(gl, 16, 158, m_EnemyHPIntArray[0]);
        	mLabels.draw(gl, 126, 158, m_EnemyHPIntArray[1]);
        	mLabels.draw(gl, 256, 158, m_EnemyHPIntArray[2]);
        	mLabels.draw(gl, 384, 158, m_EnemyHPIntArray[3]);
        }
        mLabels.endDrawing(gl);
        
        
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
		gl.glDisable(GL10.GL_DEPTH_TEST);
	}	
	
	public void SetupTextPaint(GL10 gl) {
		// 글자를 나타내기 위해서 필요한 세팅
        mLabelPaint = new Paint();
        mLabelPaint.setTextSize(24);
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setARGB(0xff, 0xff, 0xff, 0xff);	

        mSelectedLabelPaint = new Paint();
        mSelectedLabelPaint.setTextSize(24);
        mSelectedLabelPaint.setAntiAlias(true);
        mSelectedLabelPaint.setARGB(0xff, 0x00, 0x00, 0x00);
        
        mInvalidLabelPaint = new Paint();
        mInvalidLabelPaint.setTextSize(24);
        mInvalidLabelPaint.setAntiAlias(true);
        mInvalidLabelPaint.setARGB(0xff, 0x80, 0x80, 0x80);
        
        mEnemyInfoPaint = new Paint();
        mEnemyInfoPaint.setTextSize(22);
        mEnemyInfoPaint.setAntiAlias(true);
        mEnemyInfoPaint.setARGB(0xff, 0xff, 0xff, 0xff);
        
	    if (mLabels != null) {
	    	mLabels.shutdown(gl);
        } else {
        	mLabels = new LabelMaker(true, (int)GraphicObject.BASE_WIDTH, (int)GraphicObject.BASE_HEIGHT);
        }
	}
	
	public void DrawBattle(GL10 gl) {
		// TODO Auto-generated method stub
		long currMilliSecond = System.currentTimeMillis();
		
		// 적들의 턴이다.
		if (!m_bDamageAni[0] && !m_bDamageAni[1] && !m_bDamageAni[2] && !m_bDamageAni[3]) {
			if (!m_ActionValid[0] && !m_ActionValid[1] && !m_ActionValid[2] && !m_ActionValid[3]) {
				// 아군이 이겼는지는 검사하는 처리부터 먼저 한다.
				boolean isOurWin = true;
				for (int i = 0; i < m_nEnemyCount; ++i) {
					if (m_EnemyList.get(i).m_HP > 0) {
						isOurWin = false;
						break;
					}
				}
				
				// 아군이 이겼다.
				if (isOurWin == true) {
					// 전멸 메세지
					Message msg = new Message();
					
					MessageObjectArg msgObj = new MessageObjectArg();
					msgObj.str_ = "당신은 전투에서 승리하였습니다!";
					
					msg.what = 0;
					msg.obj = msgObj;
					
					m_Handler.sendMessage(msg);			
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ie) {
					}
					
					// 각각의 아군들이 얻은 경험치를 알려준다.
					int nDropExp = 0;
					for (int i = 0; i < m_EnemyList.size(); ++i) {
						nDropExp += m_EnemyList.get(i).m_DropExp;
					}
			
					// 경험치를 누적해 준다.
					m_Chr.m_nExp += nDropExp;
					
					msg = new Message();
					
					msgObj = new MessageObjectArg();
					msgObj.str_ = nDropExp + "의 경험치를 획득했습니다!";
					
					msg.what = 0;
					msg.obj = msgObj;
					
					m_Handler.sendMessage(msg);			
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ie) {
					}
					
					// 각각의 아군들의 레벨업 상황을 알려준다.
					int beforeLevel = m_Chr.m_nLevel;
					int afterLevel = LevelExpTable.GetLevelByExp(m_Chr.m_nExp);
					
					m_Chr.m_nLevel = afterLevel;

					if (beforeLevel != afterLevel) {
						msg = new Message();
						
						msgObj = new MessageObjectArg();
						msgObj.str_ = m_ChrList.get(0).m_Name + "의 레벨이 " + afterLevel + "이 되었습니다!";
						
						msg.what = 0;
						msg.obj = msgObj;
						
						m_Handler.sendMessage(msg);			
						
						try {
							Thread.sleep(2000);
						} catch (InterruptedException ie) {
						}
					}
					
					m_bBattleMode = false;	
				}
				
				if (m_EnemyActionValid[0]  && m_EnemyList.size() > 0 && m_EnemyList.get(0).m_HP > 0) {
					if ((currMilliSecond - m_EnemyShakeOldMilliSecond[0]) > 150) {
						m_EnemyShakeOldMilliSecond[0] = currMilliSecond;
						
						if (m_nEnemyShakeScreenCount[0] < 8) {
							if ((m_nEnemyShakeScreenCount[0] % 2) == 0) {
								gl.glTranslatef(-16, 0, 0);
							} else {
								gl.glTranslatef(16, 0, 0);
							}
						} 
						
						if (m_nEnemyShakeScreenCount[0] == 0) {
							// 전투 계산
							BattleChr chr = GetAIChr();
							BattleEnemy enemy = m_EnemyList.get(0);
								
							if (chr != null) { 
								AttackResult result = ProcessAttackByEnemy(enemy, chr);
								
								// 공격 메세지
								Message msg = new Message();
								
								MessageObjectArg msgObj = new MessageObjectArg();
								msgObj.str_ = enemy.m_Name + "의 공격!, " + chr.m_Name + "의 HP -" + result.damage + "!";
								
								msg.what = 0;
								msg.obj = msgObj;
								
								m_Handler.sendMessage(msg);
							}
						}
						
						if (++m_nEnemyShakeScreenCount[0] >= 18) {
							m_EnemyActionValid[0] = false;
							m_nEnemyShakeScreenCount[0] = 0;
							m_bUpdateText = true;
						}
					}
				} else if (m_EnemyActionValid[1] && m_EnemyList.size() > 1 && m_EnemyList.get(1).m_HP > 0) { 
					if ((currMilliSecond - m_EnemyShakeOldMilliSecond[1]) > 150) {
						m_EnemyShakeOldMilliSecond[1] = currMilliSecond;
						
						if (m_nEnemyShakeScreenCount[1] < 8) {
							if ((m_nEnemyShakeScreenCount[1] % 2) == 0) {
								gl.glTranslatef(-16, 0, 0);
							} else {
								gl.glTranslatef(16, 0, 0);
							}
						} 

						if (m_nEnemyShakeScreenCount[1] == 0) {
							// 전투 계산
							BattleChr chr = GetAIChr();
							BattleEnemy enemy = m_EnemyList.get(1);
								
							if (chr != null) { 
								AttackResult result = ProcessAttackByEnemy(enemy, chr);
								
								// 공격 메세지
								Message msg = new Message();
								
								MessageObjectArg msgObj = new MessageObjectArg();
								msgObj.str_ = enemy.m_Name + "의 공격!, " + chr.m_Name + "의 HP -" + result.damage + "!";
								
								msg.what = 0;
								msg.obj = msgObj;
								
								m_Handler.sendMessage(msg);
							}
						}
						
						if (++m_nEnemyShakeScreenCount[1] >= 18) {
							m_EnemyActionValid[1] = false;
							m_nEnemyShakeScreenCount[1] = 0;
							m_bUpdateText = true;
						}
					}
				} else if (m_EnemyActionValid[2] && m_EnemyList.size() > 2 && m_EnemyList.get(2).m_HP > 0) {
					if ((currMilliSecond - m_EnemyShakeOldMilliSecond[2]) > 150) {
						m_EnemyShakeOldMilliSecond[2] = currMilliSecond;
						
						if (m_nEnemyShakeScreenCount[2] < 8) {
							if ((m_nEnemyShakeScreenCount[2] % 2) == 0) {
								gl.glTranslatef(-16, 0, 0);
							} else {
								gl.glTranslatef(16, 0, 0);
							}
						} 
						
						if (m_nEnemyShakeScreenCount[2] == 0) {
							// 전투 계산
							BattleChr chr = GetAIChr();
							BattleEnemy enemy = m_EnemyList.get(2);
								
							if (chr != null) { 
								AttackResult result = ProcessAttackByEnemy(enemy, chr);
								
								// 공격 메세지
								Message msg = new Message();
								
								MessageObjectArg msgObj = new MessageObjectArg();
								msgObj.str_ = enemy.m_Name + "의 공격!, " + chr.m_Name + "의 HP -" + result.damage + "!";
								
								msg.what = 0;
								msg.obj = msgObj;
								
								m_Handler.sendMessage(msg);
							}
						}
						
						if (++m_nEnemyShakeScreenCount[2] >= 18) {
							m_EnemyActionValid[2] = false;
							m_nEnemyShakeScreenCount[2] = 0;
							m_bUpdateText = true;
						}
					}
				} else if (m_EnemyActionValid[3] && m_EnemyList.size() > 3 &&m_EnemyList.get(3).m_HP > 0) { 
					if ((currMilliSecond - m_EnemyShakeOldMilliSecond[3]) > 150) {
						m_EnemyShakeOldMilliSecond[3] = currMilliSecond;
						
						if (m_nEnemyShakeScreenCount[3] < 8) {
							if ((m_nEnemyShakeScreenCount[3] % 2) == 0) {
								gl.glTranslatef(-16, 0, 0);
							} else {
								gl.glTranslatef(16, 0, 0);
							}
						}
						
						if (m_nEnemyShakeScreenCount[3] == 0) {
							// 전투 계산
							BattleChr chr = GetAIChr();
							BattleEnemy enemy = m_EnemyList.get(3);
								
							if (chr != null) { 
								AttackResult result = ProcessAttackByEnemy(enemy, chr);
								
								// 공격 메세지
								Message msg = new Message();
								
								MessageObjectArg msgObj = new MessageObjectArg();
								msgObj.str_ = enemy.m_Name + "의 공격!, " + chr.m_Name + "의 HP -" + result.damage + "!";
								
								msg.what = 0;
								msg.obj = msgObj;
								
								m_Handler.sendMessage(msg);
							}
						}
						
						if (++m_nEnemyShakeScreenCount[3] >= 18) {
							m_EnemyActionValid[3] = false;
							m_nEnemyShakeScreenCount[3] = 0;
							m_bUpdateText = true;
						}
					}
				} else {
					// 모든 적군의 턴이 다 끝났다. 이제 아군의 턴으로 다시 넘어간다.
					for (int i = 0; i < m_nOurCount; ++i) {
						if (m_ChrList.get(i).m_HP > 0) m_ActionValid[i] = true;
					}
					for (int i = 0; i < m_nEnemyCount; ++i) {
						if (m_EnemyList.get(i).m_HP > 0) m_EnemyActionValid[i] = true;
					}
					
					// 메세지 비워주기
					Message msg = new Message();
					
					MessageObjectArg msgObj = new MessageObjectArg();
					msgObj.str_ = "";
					
					msg.what = 0;
					msg.obj = msgObj;
					
					m_Handler.sendMessage(msg);
					
					// 혹시 아군이 모두 전멸했는가? 전멸했다면 전멸 처리를 해 준다.
					boolean isOurAllDead = true;
					for (int i = 0; i < m_nOurCount; ++i) {
						if (m_ChrList.get(i).m_HP > 0) {
							isOurAllDead = false;
							break;
						}
					}
					
					// 전멸했다.
					if (isOurAllDead == true) {
						// 전멸 메세지
						msg = new Message();
						
						msgObj = new MessageObjectArg();
						msgObj.str_ = "당신은 전투에서 패배하였습니다!";
						
						msg.what = 0;
						msg.obj = msgObj;
						
						m_Handler.sendMessage(msg);			
						
						try {
							Thread.sleep(2000);
						} catch (InterruptedException ie) {
						}
						
						m_bBattleMode = false;
						((OpenGL)m_Context).finish();
					}
				}
			}
		}
		
		// 화면 진동 효과
		if (m_bShakeScreenFlag == true) {
			if ((currMilliSecond - m_ShakeOldMilliSecond) > 150) {
				m_ShakeOldMilliSecond = currMilliSecond;
				
				if ((m_nShakeScreenCount % 2) == 0) {
					gl.glTranslatef(-16, 0, 0);
				} else {
					gl.glTranslatef(16, 0, 0);
				}
				
				if (++m_nShakeScreenCount >= 6) {
					m_bShakeScreenFlag = false;
					m_nShakeScreenCount = 0;
				}
			}
		}
		
		updateText(gl);
		onDrawTextBox(gl);
		onDrawEnemy(gl);
	}	

	// 일반 공격에 대한 결과
	class AttackResult {
		public final static int NORMAL_ATTACK = 0;
		public final static int CRITICAL_ATTACK = 1;
		
		public int type;
		public int damage;
		
		public AttackResult() {
			type = 0;
			damage = 0;
		}
	}
	
	// 아군이 적군을 일반 공격했하는 것 시뮬레이션
	AttackResult ProcessAttackByOur(BattleChr chr, BattleEnemy enemy) 
	{
		AttackResult result = new AttackResult();
		
		int criticalDice = (Math.abs(m_Random.nextInt()) % 100) + 1;
		int criticalMul = (Math.abs(m_Random.nextInt()) % 3) + 2;
		
		int damage = 0;
		
		if (criticalDice <= chr.m_Critical) {
			// 크리티컬 공격 데미지를 준다.
			result.type = AttackResult.CRITICAL_ATTACK;
			
			damage = Math.max(chr.m_Attack * criticalMul - enemy.m_Defense, 0);
			enemy.m_HP -= damage;
			enemy.m_HP = Math.max(enemy.m_HP, 0);
			
		} else {
			// 일반 공격 데미지를 준다.
			result.type = AttackResult.NORMAL_ATTACK;
			
			damage = Math.max(chr.m_Attack - enemy.m_Defense, 0);
			enemy.m_HP -= damage; 
			enemy.m_HP = Math.max(enemy.m_HP, 0);
			
		}
		
		result.damage = damage;
		
		return result;
	}
	
	AttackResult ProcessAttackByEnemy(BattleEnemy enemy, BattleChr chr) 
	{
		AttackResult result = new AttackResult();
		
		int criticalDice = (Math.abs(m_Random.nextInt()) % 100) + 1;
		int criticalMul = (Math.abs(m_Random.nextInt()) % 3) + 2;
		
		int damage = 0;
		
		if (criticalDice <= enemy.m_Critical) {
			// 크리티컬 공격 데미지를 준다.
			result.type = AttackResult.CRITICAL_ATTACK;
			
			damage = Math.max(enemy.m_Attack * criticalMul - chr.m_Defense, 0);
			chr.m_HP -= damage;
			chr.m_HP = Math.max(chr.m_HP, 0);
			
		} else {
			// 일반 공격 데미지를 준다.
			result.type = AttackResult.NORMAL_ATTACK;
			
			damage = Math.max(enemy.m_Attack - chr.m_Defense, 0);
			chr.m_HP -= damage; 
			chr.m_HP = Math.max(chr.m_HP, 0);
			
		}
		
		result.damage = damage;
		
		return result;
	}
	
	// 살아있는 아군중 한명 선택하는 함수, 지금은 랜덤으로 선택하나 나중에는 조금 더 똑똑해 지게 만들자.
	BattleChr GetAIChr() {
		ArrayList<BattleChr> arrayList = new ArrayList<BattleChr>();
		
		for (int i = 0; i < m_nOurCount; ++i) {
			if (m_ChrList.get(i).m_HP > 0) arrayList.add(m_ChrList.get(i));
		}
		
		if (arrayList.size() > 0) {
			int diceNum = Math.abs(m_Random.nextInt()) % arrayList.size();
			
			BattleChr retChr = arrayList.get(diceNum);
			return retChr;
		}
		
		return null;
	}
	
	public void BattleSetup() {
		// 초기화
		m_ChrList.clear();
		m_EnemyList.clear();
		
		// 주인공
		BattleChr battleChr = new BattleChr();
		battleChr.m_Name = m_Chr.m_BattleCreature.m_Name;
		battleChr.m_Attack = m_Chr.m_BattleCreature.m_Attack;
		battleChr.m_Critical = m_Chr.m_BattleCreature.m_Critical;
		battleChr.m_Defense = m_Chr.m_BattleCreature.m_Defense;
		battleChr.m_Dodge = m_Chr.m_BattleCreature.m_Dodge;
		battleChr.m_MaxHP = m_Chr.m_BattleCreature.m_MaxHP;
		battleChr.m_HP = m_Chr.m_BattleCreature.m_HP;
		battleChr.m_MaxMP = m_Chr.m_BattleCreature.m_MaxMP;
		battleChr.m_MP = m_Chr.m_BattleCreature.m_MP;
		battleChr.m_Magic = m_Chr.m_BattleCreature.m_Magic;
		m_ChrList.add(battleChr);
		
		// 1~4 번째 몬스터
		for (int i = 0; i < m_Map.monsterNamePool.size(); ++i) {
			if (m_Map.monsterNamePool.size() > i) {
				BattleEnemy tmpEnemy = m_BattleEnemyMgr.Find(m_Map.monsterNamePool.get(i));
				
				if (tmpEnemy != null) {
					BattleEnemy battleEnemy = new BattleEnemy();
					battleEnemy.m_Name = tmpEnemy.m_Name;
					battleEnemy.m_CreatureID = tmpEnemy.m_CreatureID;
					battleEnemy.m_Attack = tmpEnemy.m_Attack;
					battleEnemy.m_Critical = tmpEnemy.m_CreatureID;
					battleEnemy.m_Defense = tmpEnemy.m_Defense;
					battleEnemy.m_Dodge = tmpEnemy.m_Dodge;
					battleEnemy.m_MaxHP = Math.max(tmpEnemy.m_MaxHP, 1);
					battleEnemy.m_HP = Math.max(tmpEnemy.m_MaxHP, 1);
					battleEnemy.m_MaxMP = Math.max(tmpEnemy.m_MaxMP, 1);
					battleEnemy.m_MP = Math.max(tmpEnemy.m_MaxMP, 1);
					battleEnemy.m_Magic = tmpEnemy.m_Magic;
					battleEnemy.m_DropExp = tmpEnemy.m_Attack;
					m_EnemyList.add(battleEnemy);
				}
			}
		}
		
		// 선택 List 문자열 설정
		for (int i = 0; i < m_ChrList.size(); ++i) {
			BattleChr chr =  m_ChrList.get(i);
			
			if (chr != null) setText(0, i, chr.m_Name);
		}		
	}
	
	public void BattleSetup(ArrayList<String> monsterNamePool) 
	{
		// 초기화
		m_ChrList.clear();
		m_EnemyList.clear();
		
		// 주인공
		BattleChr battleChr = new BattleChr();
		battleChr.m_Name = m_Chr.m_BattleCreature.m_Name;
		battleChr.m_Attack = m_Chr.m_BattleCreature.m_Attack;
		battleChr.m_Critical = m_Chr.m_BattleCreature.m_Critical;
		battleChr.m_Defense = m_Chr.m_BattleCreature.m_Defense;
		battleChr.m_Dodge = m_Chr.m_BattleCreature.m_Dodge;
		battleChr.m_MaxHP = m_Chr.m_BattleCreature.m_MaxHP;
		battleChr.m_HP = m_Chr.m_BattleCreature.m_HP;
		battleChr.m_MaxMP = m_Chr.m_BattleCreature.m_MaxMP;
		battleChr.m_MP = m_Chr.m_BattleCreature.m_MP;
		battleChr.m_Magic = m_Chr.m_BattleCreature.m_Magic;
		m_ChrList.add(battleChr);
		
		// 1~4 번째 몬스터
		for (int i = 0; i < monsterNamePool.size(); ++i) {
			BattleEnemy tmpEnemy = Renderer.m_BattleEnemyMgr.Find(monsterNamePool.get(i));
				
			if (tmpEnemy != null) {
				BattleEnemy battleEnemy = new BattleEnemy();
				battleEnemy.m_Name = tmpEnemy.m_Name;
				battleEnemy.m_CreatureID = tmpEnemy.m_CreatureID;
				battleEnemy.m_Attack = tmpEnemy.m_Attack;
				battleEnemy.m_Critical = tmpEnemy.m_CreatureID;
				battleEnemy.m_Defense = tmpEnemy.m_Defense;
				battleEnemy.m_Dodge = tmpEnemy.m_Dodge;
				battleEnemy.m_MaxHP = Math.max(tmpEnemy.m_MaxHP, 1);
				battleEnemy.m_HP = Math.max(tmpEnemy.m_MaxHP, 1);
				battleEnemy.m_MaxMP = Math.max(tmpEnemy.m_MaxMP, 1);
				battleEnemy.m_MP = Math.max(tmpEnemy.m_MaxMP, 1);
				battleEnemy.m_Magic = tmpEnemy.m_Magic;
				battleEnemy.m_DropExp = tmpEnemy.m_Attack;
				m_EnemyList.add(battleEnemy);
			}
		}
		
		// 선택 List 문자열 설정
		for (int i = 0; i < m_ChrList.size(); ++i) {
			BattleChr chr =  m_ChrList.get(i);
			
			if (chr != null) setText(0, i, chr.m_Name);
		}		
	}
}

