package spaciall.RPGMaker;

import java.util.ArrayList;


public class BattleCreature {
	public BattleCreature() {
		m_MagicList.clear();
	}
	
	public String m_Name;
	public int m_Job;
	
	public int m_HP;
	public int m_MaxHP;
	public int m_MP;
	public int m_MaxMP;
	public int m_Attack;
	public int m_Magic;
	public int m_Critical;
	public int m_Defense;
	public int m_Dodge;
	
	public static final int MAGIC_EFFECT_SINGLE_ATTACK = 0;
	public static final int MAGIE_EFFECT_MULTI_ATTACK = 1;
	public static final int MAGIC_EFFECT_POISON = 2;
	public static final int MAGIC_EFFECT_FREEZE = 3;
	public static final int MAGIC_EFFECT_SINGLE_HEAL = 4;
	public static final int MAGIC_EFFECT_MULTI_HEAL = 5;
	
	public static final int JOB_WARRIOR = 0;
	public static final int JOB_MAGICIAN = 1;
	public static final int JOB_HEALER = 2;
	
	public class MagicInfo {
		public MagicInfo() {
		}
		
		public String name;
		public int successRate;
		public int effect; 
	}
	
	public ArrayList<MagicInfo> m_MagicList = new ArrayList<BattleCreature.MagicInfo>();
}
