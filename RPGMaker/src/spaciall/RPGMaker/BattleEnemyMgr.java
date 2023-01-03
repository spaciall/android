package spaciall.RPGMaker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;

public class BattleEnemyMgr {
	public ArrayList<BattleEnemy> mList = new ArrayList<BattleEnemy>();
	
	public BattleEnemyMgr()
	{
		mList.clear();
	}
	
	public BattleEnemy Find(String strName) 
	{
		for (int i = 0; i < mList.size(); ++i) {
			if (mList.get(i).m_Name.equals(strName)) return mList.get(i);
		}
		
		return null;
	}
	
	public boolean Setup(Context context)
	{
		// Monster 정보 불러오기
		AssetManager am = context.getResources().getAssets();
        InputStream is = null;		
		
        try {
            is = am.open("monster.dat");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "euc-kr"));
            DataInputStream dis = new DataInputStream(is);
            String str = "";
            
            String name = "";
            String id = "";
            String attack = "";
            String defense = "";
            String critical = "";
            String dodge = "";
            String magic = "";
            String hp = "";
            String mp = "";
            String magicCount = "";
             
            while (true)
            {
            	BattleEnemy enemy = new BattleEnemy(); 
            	
            	name = br.readLine();
            	
            	if (name == null) break;
            	
            	id = br.readLine();
            	attack = br.readLine();
            	defense = br.readLine();
            	critical = br.readLine();
            	dodge = br.readLine();
            	magic = br.readLine();
            	hp = br.readLine();
            	mp = br.readLine();
            	magicCount = br.readLine();
            	
            	enemy.m_Name = name;
            	enemy.m_CreatureID = Integer.parseInt(id);
            	enemy.m_Attack = Integer.parseInt(attack);
            	enemy.m_Defense = Integer.parseInt(defense);
            	enemy.m_Critical = Integer.parseInt(critical);
            	enemy.m_Dodge = Integer.parseInt(dodge);
            	enemy.m_Magic = Integer.parseInt(magic);
            	enemy.m_HP = Integer.parseInt(hp);
            	enemy.m_MP = Integer.parseInt(mp);
            	
            	for (int i = 0; i < (Integer.parseInt(magicCount) * 3); ++i) {
            		String magicName = "";
            		String effect = "";
            		String successRate = "";
            		
            		magicName = br.readLine();
            		effect = br.readLine();
            		successRate = br.readLine();
            	}
            	
            	mList.add(enemy);
            }
            
            is.close();
            
        } catch (FileNotFoundException ex) {
        	return false;
        } catch (IOException ex) {
        	String msg = ex.getMessage();
        	return false;
        }
        
        System.gc();
        
		return true;		
	}
}
