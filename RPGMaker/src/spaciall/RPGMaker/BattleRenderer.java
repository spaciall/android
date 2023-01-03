package spaciall.RPGMaker;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class BattleRenderer implements android.opengl.GLSurfaceView.Renderer  
{
	private Projector m_Projector = new Projector();
	
	protected Context m_Context;
	protected int m_width, m_height;

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
	
	// 랜덤 변수
	protected Random m_Random = new Random(System.currentTimeMillis());
	
	public BattleRenderer( Context context, int width, int height )
	{
		m_Context = context;
		
		// 해상도 설정
		m_width = width;
		m_height = height;
		
		// 텍스트 설명글을 출력하는 텍스트 뷰
		m_textView = ((BattleActivity)m_Context).m_textView;
		m_Handler = ((BattleActivity)m_Context).mHandler;
		
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
	
	public void TestSetup() {
		BattleChr battleChr = new BattleChr();
		battleChr.m_Name = "용준";
		battleChr.m_Attack = 110;
		battleChr.m_Critical = 10;
		battleChr.m_Defense = 10;
		battleChr.m_Dodge = 10;
		battleChr.m_MaxHP = 100;
		battleChr.m_HP = 100;
		battleChr.m_MaxMP = 100;
		battleChr.m_MP = 10;
		battleChr.m_Magic = 10;
		m_ChrList.add(battleChr);
		
		battleChr = new BattleChr();
		battleChr.m_Name = "준호";
		battleChr.m_Attack = 110;
		battleChr.m_Critical = 10;
		battleChr.m_Defense = 10;
		battleChr.m_Dodge = 10;
		battleChr.m_MaxHP = 100;
		battleChr.m_HP = 100;
		battleChr.m_MaxMP = 100;
		battleChr.m_MP = 10;
		battleChr.m_Magic = 10;
		m_ChrList.add(battleChr);

		battleChr = new BattleChr();
		battleChr.m_Name = "피에르";
		battleChr.m_Attack = 10;
		battleChr.m_Critical = 10;
		battleChr.m_Defense = 10;
		battleChr.m_Dodge = 10;
		battleChr.m_MaxHP = 100;
		battleChr.m_HP = 100;
		battleChr.m_MaxMP = 100;
		battleChr.m_MP = 10;
		battleChr.m_Magic = 10;
		m_ChrList.add(battleChr);

		battleChr = new BattleChr();
		battleChr.m_Name = "유나";
		battleChr.m_Attack = 10;
		battleChr.m_Critical = 10;
		battleChr.m_Defense = 10;
		battleChr.m_Dodge = 10;
		battleChr.m_MaxHP = 100;
		battleChr.m_HP = 100;
		battleChr.m_MaxMP = 100;
		battleChr.m_MP = 10;
		battleChr.m_Magic = 10;
		m_ChrList.add(battleChr);
		
		BattleEnemy battleEnemy = new BattleEnemy();
		battleEnemy.m_Name = "슬라임";
		battleEnemy.m_CreatureID = 0;
		battleEnemy.m_Attack = 11;
		battleEnemy.m_Critical = 10;
		battleEnemy.m_Defense = 10;
		battleEnemy.m_Dodge = 10;
		battleEnemy.m_MaxHP = 100;
		battleEnemy.m_HP = 100;
		battleEnemy.m_MaxMP = 100;
		battleEnemy.m_MP = 10;
		battleEnemy.m_Magic = 10;
		battleEnemy.m_DropExp = 10;
		m_EnemyList.add(battleEnemy);
		
		battleEnemy = new BattleEnemy();
		battleEnemy.m_Name = "도적";
		battleEnemy.m_CreatureID = 1;
		battleEnemy.m_Attack = 11;
		battleEnemy.m_Critical = 10;
		battleEnemy.m_Defense = 10;
		battleEnemy.m_Dodge = 10;
		battleEnemy.m_MaxHP = 100;
		battleEnemy.m_HP = 100;
		battleEnemy.m_MaxMP = 100;
		battleEnemy.m_MP = 10;
		battleEnemy.m_Magic = 10;
		battleEnemy.m_DropExp = 10;
		m_EnemyList.add(battleEnemy);

		battleEnemy = new BattleEnemy();
		battleEnemy.m_Name = "악당";
		battleEnemy.m_CreatureID = 2;
		battleEnemy.m_Attack = 110;
		battleEnemy.m_Critical = 10;
		battleEnemy.m_Defense = 10;
		battleEnemy.m_Dodge = 10;
		battleEnemy.m_MaxHP = 100;
		battleEnemy.m_HP = 100;
		battleEnemy.m_MaxMP = 100;
		battleEnemy.m_MP = 10;
		battleEnemy.m_Magic = 10;
		battleEnemy.m_DropExp = 10;
		m_EnemyList.add(battleEnemy);
		
		battleEnemy = new BattleEnemy();
		battleEnemy.m_Name = "악마";
		battleEnemy.m_CreatureID = 3;
		battleEnemy.m_Attack = 110;
		battleEnemy.m_Critical = 10;
		battleEnemy.m_Defense = 10;
		battleEnemy.m_Dodge = 10;
		battleEnemy.m_MaxHP = 100;
		battleEnemy.m_HP = 100;
		battleEnemy.m_MaxMP = 100;
		battleEnemy.m_MP = 10;
		battleEnemy.m_Magic = 10;
		battleEnemy.m_DropExp = 10;
		m_EnemyList.add(battleEnemy);
		
		// 선택 List 문자열 설정
		for (int i = 0; i < 4; ++i) {
			BattleChr chr =  m_ChrList.get(i);
			
			if (chr != null) setText(0, i, chr.m_Name);
		}
	}

	protected long m_ShakeOldMilliSecond = 0;
	protected long[] m_EnemyShakeOldMilliSecond = new long[4];
	protected int[] m_nEnemyShakeScreenCount = new int[4];
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
		
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
					msg = new Message();
					
					msgObj = new MessageObjectArg();
					msgObj.str_ = "45의 경험치를 획득했습니다!";
					
					msg.what = 0;
					msg.obj = msgObj;
					
					m_Handler.sendMessage(msg);			
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ie) {
					}
					
					// 각각의 아군들의 레벨업 상황을 알려준다.
					msg = new Message();
					
					msgObj = new MessageObjectArg();
					msgObj.str_ = "용준의 레벨이 47이 되었습니다!";
					
					msg.what = 0;
					msg.obj = msgObj;
					
					m_Handler.sendMessage(msg);			
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ie) {
					}
					
					((BattleActivity)m_Context).finish();	
				}
				
				if (m_EnemyActionValid[0] && m_EnemyList.get(0).m_HP > 0) {
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
				} else if (m_EnemyActionValid[1] && m_EnemyList.get(1).m_HP > 0) { 
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
				} else if (m_EnemyActionValid[2] && m_EnemyList.get(2).m_HP > 0) {
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
				} else if (m_EnemyActionValid[3] && m_EnemyList.get(3).m_HP > 0) { 
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
						
						((BattleActivity)m_Context).finish();
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
        
        for (int i = 0; i < m_nOurCount; ++i) 
        	m_SelectIntArray[0][i] = mLabels.add(gl, m_ChrList.get(i).m_Name + " " + m_ChrList.get(i).m_MaxHP + "/" + m_ChrList.get(i).m_HP, mLabelPaint);
        for (int i = m_nOurCount; i < m_nMaxOurCount; ++i) 
        	m_SelectIntArray[0][i] = mLabels.add(gl, "", mLabelPaint);
        
        m_SelectIntArray[1][0] = mLabels.add(gl, "공격", mLabelPaint);
        m_SelectIntArray[1][1] = mLabels.add(gl, "마법", mInvalidLabelPaint);
        m_SelectIntArray[1][2] = mLabels.add(gl, "도구", mInvalidLabelPaint);
        m_SelectIntArray[1][3] = mLabels.add(gl, "도망가기", mLabelPaint);
        
        for (int i = 0; i < m_nOurCount; ++i) 
        	m_SelectInvalidIntArray[0][i] = mLabels.add(gl, m_ChrList.get(i).m_Name + " " + m_ChrList.get(i).m_MaxHP + "/" + m_ChrList.get(i).m_HP, mInvalidLabelPaint);
        for (int i = m_nOurCount; i < m_nMaxOurCount; ++i) 
        	m_SelectInvalidIntArray[0][i] = mLabels.add(gl, "", mInvalidLabelPaint);
        
        
        m_SelectInvalidIntArray[1][0] = mLabels.add(gl, "공격", mInvalidLabelPaint);
        m_SelectInvalidIntArray[1][1] = mLabels.add(gl, "마법", mInvalidLabelPaint);
        m_SelectInvalidIntArray[1][2] = mLabels.add(gl, "도구", mInvalidLabelPaint);
        m_SelectInvalidIntArray[1][3] = mLabels.add(gl, "도망가기", mInvalidLabelPaint);
        
        for (int i = 0; i < m_nOurCount; ++i) 
        	m_SelectValidIntArray[0][i] = mLabels.add(gl, m_ChrList.get(i).m_Name + " " + m_ChrList.get(i).m_MaxHP + "/" + m_ChrList.get(i).m_HP, mInvalidLabelPaint);
        for (int i = m_nOurCount; i < m_nMaxOurCount; ++i) 
        	m_SelectValidIntArray[0][i] = mLabels.add(gl, "", mInvalidLabelPaint);
        
        m_SelectValidIntArray[1][0] = mLabels.add(gl, "공격", mSelectedLabelPaint);
        m_SelectValidIntArray[1][1] = mLabels.add(gl, "마법", mInvalidLabelPaint);
        m_SelectValidIntArray[1][2] = mLabels.add(gl, "도구", mInvalidLabelPaint);
        m_SelectValidIntArray[1][3] = mLabels.add(gl, "도망가기", mSelectedLabelPaint);
        
        // 적군의 HP를 나타내는 String을 만든다.
        for (int i = 0; i < m_nEnemyCount; ++i) m_EnemyHPIntArray[i] =  mLabels.add(gl, m_EnemyList.get(i).m_MaxHP + "/" + m_EnemyList.get(i).m_HP, mEnemyInfoPaint); 
        
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
	    if (m_ActionValid[0] == true && m_ChrList.get(0).m_HP > 0) mLabels.draw(gl, 16, 126, m_SelectIntArray[0][0]);
	    else mLabels.draw(gl, 16, 126, m_SelectInvalidIntArray[0][0]);
	    
	    if (m_ActionValid[1] == true && m_ChrList.get(1).m_HP > 0) mLabels.draw(gl, 16, 86, m_SelectIntArray[0][1]);
	    else mLabels.draw(gl, 16, 86, m_SelectInvalidIntArray[0][1]);
	    
	    if (m_ActionValid[2] == true && m_ChrList.get(2).m_HP > 0) mLabels.draw(gl, 16, 46, m_SelectIntArray[0][2]);
	    else mLabels.draw(gl, 16, 46, m_SelectInvalidIntArray[0][2]);
	    
	    if (m_ActionValid[3] == true && m_ChrList.get(3).m_HP > 0) mLabels.draw(gl, 16, 6, m_SelectIntArray[0][3]);
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

	public void onSurfaceChanged( GL10 gl, int width, int height) {
		m_GL = gl;
		
		gl.glOrthof( 0.0f, width, height, 0.0f, 1.0f, -1.0f );
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glViewport( 0, 0, width, height );
		m_Projector.setCurrentView(0, 0, width, height);
		
		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
		gl.glEnable( GL10.GL_BLEND );
		gl.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
		gl.glDisable(GL10.GL_DEPTH_TEST);		
	}

	public void onSurfaceCreated( GL10 gl, EGLConfig config )
	{
		m_GL = gl;
		
		// 텍스쳐 불러오기
		m_texLeftMenu.LoadTexture(gl, m_Context, R.drawable.msgbox_battle);
		m_texChr.LoadTexture(gl, m_Context, R.drawable.chr0);
		
		// 테스트 데이터 세팅
		TestSetup();
		
		gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );
		gl.glClearDepthf( 1.0f );
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST );

		gl.glOrthof( 0.0f, GraphicObject.BASE_WIDTH, GraphicObject.BASE_HEIGHT, 0.0f, 1.0f, 1.0f );
		gl.glViewport( 0, 0, (int)GraphicObject.BASE_WIDTH, (int)GraphicObject.BASE_HEIGHT );
		
		// 글자를 나타내기 위해서 필요한 세팅
		SetupTextPaint(gl);
		updateText(gl);
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

	public boolean onTouchEvent(MotionEvent event) {
		
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
}
