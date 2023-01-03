package spaciall.RPGMaker;

import javax.microedition.khronos.opengles.GL10;

import android.content.Intent;

public class Chr extends Creature {
	public int m_nMapFileX = 0;
	public int m_nMapFileY = 0;
	
	public BattleCreature m_BattleCreature = new BattleCreature();
	
	public int m_nLevel = 1;
	public int m_nExp = 0;
	
	public Chr(Map map, int id, int x, int y) {
		m_Map = map;
		m_Map.creaturePool.add(this);
		
		m_nIdx = id;
		
		m_nX = m_nTargetX = x;
		m_nY = m_nTargetY = y;
		
		// �ɷ�ġ �ӽ� ����
		m_BattleCreature.m_Name = "��";
		m_BattleCreature.m_Attack = 10;
		m_BattleCreature.m_Critical = 10;
		m_BattleCreature.m_Defense = 5;
		m_BattleCreature.m_Dodge = 10;
		m_BattleCreature.m_MaxHP = 100;
		m_BattleCreature.m_HP = 100;
		m_BattleCreature.m_MaxMP = 100;
		m_BattleCreature.m_MP = 100;
	}
	
	@Override
	public boolean Update(GL10 gl) {
		
		// �̺�Ʈ�� ���� ���̶�� ���ڸ� ������ �ɾ��ش�. 
		if (Renderer.m_bShowTextBox == true || Renderer.m_bProcessEvent == true) {
			int div = m_nMotion / 2;
			int mod = m_nMotion % 2;
			
			m_nMotion = (div * 2) + (++mod % 2);
			return true;
		}
		
		// ó�� �÷���
		boolean move_flag = false;

		// ���� ��ǥ ��
		int old_x = m_nX;
		int old_y = m_nY;
		int old_screen_x = m_Map.screen_x;
		int old_screen_y = m_Map.screen_y;	
		
		// �̵��ؾ� �Ѵ�
		if (m_nTargetX != m_nX) {
			int diff = m_nTargetX - m_nX;
			
			if (diff > 0) {
				if (m_nX >= m_Map.screen_x + Map.RIGHT_MOVE_SCREEN) {
					if (++m_Map.screen_x >= (Map.WIDTH_MAP - Map.WIDTH_SCREEN)) m_Map.screen_x = (Map.WIDTH_MAP - Map.WIDTH_SCREEN);
				}
				++m_nX;
				
				// ��ũ���� ���� �浹 ó���� �Ѵ�
				if (m_nX > (Map.WIDTH_MAP - m_nWidthSize)) {
					m_nX = Map.WIDTH_MAP - m_nWidthSize;
					
					++collisionCount;
				}
				
				// ������ ���� �浹 ó���� �Ѵ�
				if (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable()) {
					m_nX = old_x;
					m_nY = old_y;
					m_Map.screen_x = old_screen_x;
					m_Map.screen_y = old_screen_y;
					
					++collisionCount;
				} else {
					if (m_nMotion == RIGHT1) m_nMotion = RIGHT2;
					else m_nMotion = RIGHT1;
					move_flag = true;
				}
				
				// �ٸ�  Creature ��� �浹 ó���� �Ѵ�.
				for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
					Creature tmpChr = m_Map.creaturePool.get(i);

					// �浹�ϴ� ����� NPC�̰� ����� ó�� �Ǿ� �ִٸ� �浹ó�� ���� �ʴ´�.
					if (tmpChr.m_nType == Creature.NPC) {
						NonPC tmpNPC = (NonPC)tmpChr;
						
						if (tmpNPC.curEvent != null && tmpNPC.curEvent.move == 4) continue;
						if (tmpNPC.curEvent == null) continue; // �� ��쿡�� NPC�� �׷����� �ʴ´�. ������ �Ͱ� ���� ȿ��
					}
					
					if (tmpChr != this) {
						if (tmpChr.m_nY == m_nY && tmpChr.m_nX == m_nX + 1) {
							m_nX = old_x;
							m_nY = old_y;
							m_Map.screen_x = old_screen_x;
							m_Map.screen_y = old_screen_y;
							
							++collisionCount;
							break;
						}
					}
				}		
				
			} else {
				if (m_nX <= m_Map.screen_x + Map.LEFT_MOVE_SCREEN) {
					if (--m_Map.screen_x < 0) m_Map.screen_x = 0;
				}
				--m_nX;

				// ��ũ���� ���� �浹 ó���� �Ѵ�
				if (m_nX < 0) {
					m_nX = 0;
					
					++collisionCount;
				}
				
				// ������ ���� �浹 ó���� �Ѵ�
				if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable()) {
					m_nX = old_x;
					m_nY = old_y;
					m_Map.screen_x = old_screen_x;
					m_Map.screen_y = old_screen_y;
					
					++collisionCount;
				} else {
					if (m_nMotion == LEFT1) m_nMotion = LEFT2;
					else m_nMotion = LEFT1;
					move_flag = true;
				}
				
				// �ٸ�  Creature ��� �浹 ó���� �Ѵ�.
				for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
					Creature tmpChr = m_Map.creaturePool.get(i);
					
					// �浹�ϴ� ����� NPC�̰� ����� ó�� �Ǿ� �ִٸ� �浹ó�� ���� �ʴ´�.
					if (tmpChr.m_nType == Creature.NPC) {
						NonPC tmpNPC = (NonPC)tmpChr;
						
						if (tmpNPC.curEvent != null && tmpNPC.curEvent.move == 4) continue; 
						if (tmpNPC.curEvent == null) continue; // �� ��쿡�� NPC�� �׷����� �ʴ´�. ������ �Ͱ� ���� ȿ��
					}
	
					if (tmpChr != this) {
						if (tmpChr.m_nY == m_nY && tmpChr.m_nX + 1 == m_nX) {
							m_nX = old_x;
							m_nY = old_y;
							m_Map.screen_x = old_screen_x;
							m_Map.screen_y = old_screen_y;
							
							++collisionCount;
							break;
						}
					}
				}
			}
		}  
		if (move_flag == false) {
			if (m_nTargetY != m_nY) {
	
				int diff = m_nTargetY - m_nY;
				
				if (diff > 0) {
					if (m_nY >= m_Map.screen_y + Map.BOTTOM_MOVE_SCREEN) {
						if (++m_Map.screen_y >= (Map.HEIGHT_MAP - Map.HEIGHT_SCREEN)) m_Map.screen_y = (Map.HEIGHT_MAP - Map.HEIGHT_SCREEN); 
					}
					++m_nY;

					// ��ũ���� ���� �浹 ó���� �Ѵ�
					if (m_nY > (Map.HEIGHT_MAP - m_nHeightSize)) {
						m_nY = Map.HEIGHT_MAP - m_nHeightSize;
						
						++collisionCount;
					}
					
					// ������ ���� �浹 ó���� �Ѵ�
					if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable() || (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable())) {
						m_nX = old_x;
						m_nY = old_y;
						m_Map.screen_x = old_screen_x;
						m_Map.screen_y = old_screen_y;
						
						++collisionCount;
					} else {
						if (m_nMotion == BOTTOM1) m_nMotion = BOTTOM2;
						else m_nMotion = BOTTOM1;
						move_flag = true;
					}
					
					// �ٸ�  Creature ��� �浹 ó���� �Ѵ�.
					for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
						Creature tmpChr = m_Map.creaturePool.get(i);
						
						// �浹�ϴ� ����� NPC�̰� ����� ó�� �Ǿ� �ִٸ� �浹ó�� ���� �ʴ´�.
						if (tmpChr.m_nType == Creature.NPC) {
							NonPC tmpNPC = (NonPC)tmpChr;
							
							if (tmpNPC.curEvent != null && tmpNPC.curEvent.move == 4) continue; 
							if (tmpNPC.curEvent == null) continue; // �� ��쿡�� NPC�� �׷����� �ʴ´�. ������ �Ͱ� ���� ȿ��
						}
		
						if (tmpChr != this) {
							if (tmpChr.m_nY == m_nY && !((tmpChr.m_nX < m_nX) || (tmpChr.m_nX > m_nX + 1))) {
								m_nX = old_x;
								m_nY = old_y;
								m_Map.screen_x = old_screen_x;
								m_Map.screen_y = old_screen_y;
								
								++collisionCount;
								break;
							}
						}
					}
					
				} else {
					if (m_nY <= m_Map.screen_y + Map.TOP_MOVE_SCREEN) {
						if (--m_Map.screen_y < 0) m_Map.screen_y = 0;
					}
					--m_nY;
					
					// ��ũ���� ���� �浹 ó���� �Ѵ�
					if (m_nY < 0) {
						m_nY = 0;
					}
					
					// ������ ���� �浹 ó���� �Ѵ�
					if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable() || (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable())) {
						m_nX = old_x;
						m_nY = old_y;
						m_Map.screen_x = old_screen_x;
						m_Map.screen_y = old_screen_y;
						
						++collisionCount;
					} else {
						if (m_nMotion == TOP1) m_nMotion = TOP2;
						else m_nMotion = TOP1;
						move_flag = true;
					}
					
					// �ٸ�  Creature ��� �浹 ó���� �Ѵ�.
					for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
						Creature tmpChr = m_Map.creaturePool.get(i);
						
						// �浹�ϴ� ����� NPC�̰� ����� ó�� �Ǿ� �ִٸ� �浹ó�� ���� �ʴ´�.
						if (tmpChr.m_nType == Creature.NPC) {
							NonPC tmpNPC = (NonPC)tmpChr;
							
							if (tmpNPC.curEvent != null && tmpNPC.curEvent.move == 4) continue; 
							if (tmpNPC.curEvent == null) continue; // �� ��쿡�� NPC�� �׷����� �ʴ´�. ������ �Ͱ� ���� ȿ��
						}
		
						if (tmpChr != this) {
							if ((tmpChr.m_nY + 1) == (m_nY + 1) && !((tmpChr.m_nX + 1 < m_nX) || (tmpChr.m_nX > m_nX + 1))) {
								m_nX = old_x;
								m_nY = old_y;
								m_Map.screen_x = old_screen_x;
								m_Map.screen_y = old_screen_y;
								
								++collisionCount;
								break;
							}
						}
					}
				}
			}
		}
		
		if (move_flag == false) {
			int div = m_nMotion / 2;
			int mod = m_nMotion % 2;
			
			m_nMotion = (div * 2) + (++mod % 2);
			
			move_flag = true;
		}
		
		return true;
	}
	
	@Override
	public boolean Render(GL10 gl) {

		m_Tex.DrawTexture(gl, (m_nX - m_Map.screen_x) * WIDTH_MOVE_PIXEL, (m_nY - m_Map.screen_y) * HEIGHT_MOVE_PIXEL,  
				m_nMotion * WIDTH_OBJ_PIXEL, m_nIdx * HEIGHT_OBJ_PIXEL, WIDTH_OBJ_PIXEL, HEIGHT_OBJ_PIXEL, 
				0, 0, 0, 
				1.f, 1.f);
		
		return true;
	}		
}
