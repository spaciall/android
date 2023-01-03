package spaciall.RPGMaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;


public class NonPC extends Creature {
	static final int HORIZONTAL_MOVE = 0;
	static final int VERTICAL_MOVE = 1;
	static final int RANDOM_MOVE = 2;
	static final int NONE_MOVE = 3;
	static final int REMOVE_MOVE = 4;
	
	protected Context m_Context;
	
	public NonPC(Renderer renderer, Context context, Map map, int id, int x, int y) {
		m_Renderer = renderer;
		m_Context = context;
		
		m_nType = Creature.NPC;
		
		m_Map = map;
		
		m_nIdx = id;
		m_nX = m_nTargetX = x;
		m_nY = m_nTargetY = y;	
		
		m_Rand.setSeed(System.currentTimeMillis());

		collisionCount = m_Rand.nextInt(10);
		
		curEvent = null;
	}
	public Renderer m_Renderer;
	
	public int curSwitch = 0;
	public boolean m_bProcessEvent = false;
	
	public HashMap<Integer, Event> eventPool = new HashMap<Integer, Event>();
	public Event curEvent;
	
	public Random m_Rand = new Random();

	@Override
	public boolean Update(GL10 gl) {
		
		try {
			if (curEvent != null) {
				
				// 이벤트가 지워져야 하는가?
				if (curEvent.move == 4){
					if (m_Map.creaturePool.contains(this) == true) {
						m_Map.creaturePool.remove(this);
						return true;
					}
				}
				
				if (m_bProcessEvent == false) {
					// 같은 맵에 있을 때 무조건 발동해야 하는 이벤트라면, 발동시켜 준다.	
					if (curEvent.actionWhere == Event.TRIGGER_ON_SAME_MAP_WITH_CHR) {
						m_bProcessEvent = true;
						Renderer.m_bProcessEvent = true;
						
					} else if (curEvent.actionWhere == Event.TRIGGER_ON_SCREEN_WITH_CHR) {
						// 주인공의 시야에 띄어야 발동하는 이벤트라면, 조건을 확인 후, 발동시켜 준다.
						int screen_x = m_Map.screen_x;
						int screen_y = m_Map.screen_y;
						
						if (screen_x <= m_nX && (screen_x + Map.WIDTH_SCREEN - 2) >= m_nX) {
							if (screen_y <= m_nY && (screen_x + Map.HEIGHT_SCREEN - 2) >= m_nY) {
								m_bProcessEvent = true;
								Renderer.m_bProcessEvent = true;
							}
						}
					}
				}
			
				// 현재 이벤트 발동 조건이 총족되었다면 처리한다.
				if (m_bProcessEvent == true) {
	
					// 이동 이벤트 중인가?
					if (Renderer.m_bMoving == true) {
						if (m_nTargetX == m_nX && m_nTargetY == m_nY) {
							Renderer.m_bMoving = false;
						} else {
							{
								// 처리 플래그
								boolean move_flag = false;
								boolean collision_flag = false;
								
								// 이전 좌표 값
								int old_x = m_nX;
								int old_y = m_nY;
								
								// 이동해야 한다
								if (m_nTargetX != m_nX) {
									int diff = m_nTargetX - m_nX;
									
									if (diff > 0) {
										++m_nX;
										// 지형에 대한 충돌 처리를 한다
										if (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable()) {
											m_nX = old_x;
											m_nY = old_y;
											
											if (collision_flag == false) {
												++collisionCount;
												collision_flag = true;
											}
										} else {
											if (m_nMotion == RIGHT1) m_nMotion = RIGHT2;
											else m_nMotion = RIGHT1;
										}
										
										// 스크린에 대한 충돌 처리를 한다
										if (m_nX > (Map.WIDTH_MAP - m_nWidthSize - 1)) {
											m_nX = Map.WIDTH_MAP - m_nWidthSize - 1;
											
											if (collision_flag == false) {
												m_nX = old_x;
												m_nY = old_y;
												
												++collisionCount;
												collision_flag = true;
											}
										}
					
										// 다른  Creature 들과 충돌 처리를 한다.
										for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
											Creature tmpChr = m_Map.creaturePool.get(i);
							
											if (tmpChr != this) {
												if (tmpChr.m_nY == m_nY && tmpChr.m_nX == m_nX + 1) {
													m_nX = old_x;
													m_nY = old_y;
													
													if (collision_flag == false) {
														++collisionCount;
														collision_flag = true;
													}
													
													break;
												}
											}
										}
										move_flag = true;
										
									} else {
										--m_nX;
										// 지형에 대한 충돌 처리를 한다
										if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable()) {
											m_nX = old_x;
											m_nY = old_y;
											
											if (collision_flag == false) {
												++collisionCount;
												collision_flag = true;
											}
					
										} else {
											if (m_nMotion == LEFT1) m_nMotion = LEFT2;
											else m_nMotion = LEFT1;
										}
										
										// 스크린에 대한 충돌 처리를 한다
										if (m_nX < 1) {
											m_nX = 1;
											
											if (collision_flag == false) {
												m_nX = old_x;
												m_nY = old_y;
												
												++collisionCount;
												collision_flag = true;
											}
										}
										
										// 다른  Creature 들과 충돌 처리를 한다.
										for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
											Creature tmpChr = m_Map.creaturePool.get(i);
							
											if (tmpChr != this) {
												if (tmpChr.m_nY == m_nY && tmpChr.m_nX + 1 == m_nX) {
													m_nX = old_x;
													m_nY = old_y;
													
													if (collision_flag == false) {
														++collisionCount;
														collision_flag = true;
													}
													
													break;
												}
											}
										}
									}
									
									// 가로 이동에 성공했는가?
									if (m_nX == old_x && m_nY == old_y) move_flag = false;
									else move_flag = true;
								}  
								if (move_flag == false) {
									if (m_nTargetY != m_nY) {
							
										int diff = m_nTargetY - m_nY;
										
										if (diff > 0) {
											++m_nY;
											// 지형에 대한 충돌 처리를 한다
											if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable() || (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable())) {
												m_nX = old_x;
												m_nY = old_y;
												
												if (collision_flag == false) {
													++collisionCount;
													collision_flag = true;
												}
											} else {
												if (m_nMotion == BOTTOM1) m_nMotion = BOTTOM2;
												else m_nMotion = BOTTOM1;
											}
											
											// 스크린에 대한 충돌 처리를 한다
											if (m_nY > (Map.HEIGHT_MAP - m_nHeightSize - 1)) {
												m_nY = Map.HEIGHT_MAP - m_nHeightSize - 1;
												
												if (collision_flag == false) {
													m_nX = old_x;
													m_nY = old_y;
													
													++collisionCount;
													collision_flag = true;
												}
											}
											
											// 다른  Creature 들과 충돌 처리를 한다.
											for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
												Creature tmpChr = m_Map.creaturePool.get(i);
								
												if (tmpChr != this) {
													if (tmpChr.m_nY == m_nY && !((tmpChr.m_nX + 1 < m_nX) || (tmpChr.m_nX > m_nX + 1))) {
														m_nX = old_x;
														m_nY = old_y;
														
														if (collision_flag == false) {
															++collisionCount;
															collision_flag = true;
														}
														
														break;
													}
												}
											}
											move_flag = true;
											
										} else {
											--m_nY;
											// 지형에 대한 충돌 처리를 한다
											if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable() || (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable())) {
												m_nX = old_x;
												m_nY = old_y;
												
												if (collision_flag == false) {
													++collisionCount;
													collision_flag = true;
												}
											} else {
												if (m_nMotion == TOP1) m_nMotion = TOP2;
												else m_nMotion = TOP1;
											}
											
											// 스크린에 대한 충돌 처리를 한다
											if (m_nY < 1) {
												m_nY = 1;
												
												if (collision_flag == false) {
													m_nX = old_x;
													m_nY = old_y;
													
													++collisionCount;
													collision_flag = true;
												}
											}
											
											// 다른  Creature 들과 충돌 처리를 한다.
											for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
												Creature tmpChr = m_Map.creaturePool.get(i);
								
												if (tmpChr != this) {
													if (tmpChr.m_nY == m_nY && !((tmpChr.m_nX + 1 < m_nX) || (tmpChr.m_nX > m_nX + 1))) {
														m_nX = old_x;
														m_nY = old_y;
														
														if (collision_flag == false) {
															++collisionCount;
															collision_flag = true;
														}
														
														break;
													}
												}
											}
											
											move_flag = true;
										}
									}
								}
								
								if (move_flag == false) {
									int tmpDiv = m_nMotion / 2;
									int tmpMod = m_nMotion % 2;
									
									m_nMotion = (tmpDiv * 2) + (++tmpMod % 2);
									
									move_flag = true;
								}
							}
							return true;
						}
					}
					
					// 이벤트 진행 중에도 걸음은 걸어준다.
					int div = m_nMotion / 2;
					int mod = m_nMotion % 2;
					
					m_nMotion = (div * 2) + (++mod % 2);
					
					// 메세지를 보여주고 있는 상태라면 현재 스위치 이벤트의 다음 명령 처리를 미뤄준다.
					if (Renderer.m_bShowTextBox == true) return true;
					if (Renderer.m_bMoving == true) return true;
					if (Renderer.m_bShakeScreen == true) return true;
	
					// 현재 스위치 이벤트의 처리할 명령이 더 있는가?
					if (curEvent.curProcess < curEvent.dataPool.size()) {
						String strEvent = curEvent.dataPool.get(curEvent.curProcess);
						
						// 현재의 명령을 "," 단위로 파싱한다.
						StringTokenizer tokenizer = new StringTokenizer(strEvent, ",");
						ArrayList<String> arrayList = new ArrayList<String>();
						
						while(tokenizer.hasMoreTokens()) {
							arrayList.add(tokenizer.nextToken());
						}
						
						// 어떤 명령인가?
						String strOp = arrayList.get(0);
						arrayList.remove(0);
						
						if (Renderer.m_bShowTextBox == true) return true;
						
						// 메세지를 출력
						if (strOp.equals("Msg")) {
							msgArray.clear();
							
							for (int i = 0; i < arrayList.size(); ++i) msgArray.add(arrayList.get(i));
							for (int i = 0; i < 6 - arrayList.size(); ++i) msgArray.add("");
							
							Renderer.m_bNeedUpdateMsgBox= true;
							Renderer.m_bShowTextBox = true;
							
						} else if (strOp.equals("TargetPos")) {
							m_nTargetX = Integer.parseInt(arrayList.get(0).trim());
							m_nTargetY =  Integer.parseInt(arrayList.get(1).trim());
							
							Renderer.m_bMoving = true;
							Renderer.m_bProcessEvent = true;
							
						} else if (strOp.equals("ShakeScreen")) {
							Renderer.m_bShakeScreen = true;
							Renderer.m_bProcessEvent = true;
						
						} else if (strOp.equals("SwitchOn")) {
							int switchNum = Integer.parseInt(arrayList.get(0).trim());
							Renderer.m_bGameSwitch[switchNum] = true;
							Renderer.m_bProcessEvent = true;
							
						} else if (strOp.equals("SwitchOff")) {
							int switchNum = Integer.parseInt(arrayList.get(0).trim());
							Renderer.m_bGameSwitch[switchNum] = false;
							Renderer.m_bProcessEvent = true;
							
						} else if (strOp.equals("MapMove")) {
							int mapX = Integer.parseInt(arrayList.get(0).trim());
							int mapY = Integer.parseInt(arrayList.get(1).trim());
							int x = Integer.parseInt(arrayList.get(2).trim());
							int y = Integer.parseInt(arrayList.get(3).trim());
							
							((OpenGL)m_Context).SavePreference();
							((OpenGL)m_Context).LoadPreference();
							
		    				Map newMap = new Map();
		    				newMap.SetRenderer(m_Renderer);
		    				
		    				if (newMap.Setup(Renderer.GetContext(), mapX, mapY) == true) {
		    					newMap.SetupIvent(Renderer.GetContext(), mapX, mapY);
		    					
		    					m_Renderer.m_Chr.m_nMapFileX = mapX;
		    					m_Renderer.m_Chr.m_nMapFileY = mapY;
		    					
		    					newMap.screen_x = Math.max(x - 7, 0);
		    					newMap.screen_y = Math.max(y - 4, 0);
		    					
		    					newMap.screen_x = Math.min(newMap.screen_x, Map.WIDTH_MAP - Map.WIDTH_SCREEN);
		    					newMap.screen_y = Math.min(newMap.screen_y, Map.HEIGHT_MAP - Map.HEIGHT_SCREEN);
		    					
		    					newMap.creaturePool.add(m_Renderer.m_Chr);
		    					
		    					m_Renderer.m_Chr.m_nX = x;
		    					m_Renderer.m_Chr.m_nTargetX = x;
		    					m_Renderer.m_Chr.m_nY = y;
		    					m_Renderer.m_Chr.m_nTargetY = y;
	
		    					m_Renderer.m_Chr.m_Map = newMap;
		    					m_Renderer.m_Map = newMap;

		    					// 맵을 이동하는 것이므로 이벤트의 마지막 정리 처리를 해준다.
								curEvent.curProcess = 0;
								curEvent = null;
								m_bProcessEvent = false;
								Renderer.m_bProcessEvent = false;
		    				}
						} else if (strOp.equals("Monster")) {
							ArrayList<String> monsterNamePool = new ArrayList<String>();
							monsterNamePool.clear();
							
							for (int i = 0; i < arrayList.size(); ++i) {
								if (!arrayList.get(i).trim().equals("None"))
									monsterNamePool.add(arrayList.get(i).trim());
							}
							
							if (m_Renderer != null) {
								m_Renderer.BattleSetup(monsterNamePool);
								m_Renderer.m_bBattleMode = true;
								
						        // 아군과 적군의 수를 갱신해 준다.
						        m_Renderer.m_nOurCount = m_Renderer.m_ChrList.size();
						        m_Renderer.m_nEnemyCount = m_Renderer.m_EnemyList.size();
						        
						        // 아군과 적군의 공격상태를 가능으로 해 준다.
						        for (int i = 0; i < m_Renderer.m_nOurCount; ++i) m_Renderer.m_ActionValid[i] = true;
						        for (int i = m_Renderer.m_nOurCount; i < m_Renderer.m_nMaxOurCount - m_Renderer.m_nOurCount; ++i) m_Renderer.m_ActionValid[i] = false;

						        for (int i = 0; i < m_Renderer.m_nEnemyCount; ++i) m_Renderer.m_EnemyActionValid[i] = true;
						        for (int i = m_Renderer.m_nEnemyCount; i < m_Renderer.m_nMaxEnemyCount - m_Renderer.m_nEnemyCount; ++i) m_Renderer.m_EnemyActionValid[i] = false;
						        
								if (m_Renderer.m_EnemyList.size() <= 0) { 
									m_Renderer.m_bBattleMode = false;
								}

								// HP와 MP를 갱신해 준다. 일단 주인공은 1명이라고 정해둔다.
								m_Renderer.m_Chr.m_BattleCreature.m_HP = m_Renderer.m_ChrList.get(0).m_HP;
								m_Renderer.m_Chr.m_BattleCreature.m_MP = m_Renderer.m_ChrList.get(0).m_MP;
								
							} else {
							}
							
							Renderer.m_bProcessEvent = true;
							
						} else if (strOp.equals("ScreenDarkEffect")) {
							if (arrayList.get(0).trim().equals("true")) Renderer.m_bScreenDarkEffect = true;
							else Renderer.m_bScreenDarkEffect = false;
							
							Renderer.m_bProcessEvent = true;
							
						} else if (strOp.equals("WaitOneSecond")) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException ie) {
							}
							
							Renderer.m_bProcessEvent = true;
							
						} else if (strOp.equals("ChrMove")) {
							if (arrayList.get(0).trim().equals("Left")) --Renderer.m_Chr.m_nX;
							else if (arrayList.get(0).trim().equals("Right")) ++Renderer.m_Chr.m_nX;
							else if (arrayList.get(0).trim().equals("Up")) --Renderer.m_Chr.m_nY;
							else if (arrayList.get(0).trim().equals("Down")) ++Renderer.m_Chr.m_nY;
								
							Renderer.m_bProcessEvent = true;
						}
						
						// 하나의 명령을 처리했다.
						++curEvent.curProcess;
					} else {
						
						// 더 이상 처리할 이벤트 명령이 없다.
						
						if (Renderer.m_bShowTextBox == false &&
							Renderer.m_bMoving == false &&
							Renderer.m_bShakeScreen == false) {
							// 메세지 박스가 열려있는 상태가 아니라면 해당 스위치에 대한 이벤트 처리를 모두 종료한다.
							curEvent.curProcess = 0;
							curEvent = null;
							m_bProcessEvent = false;
							Renderer.m_bProcessEvent = false;
						}
					}
					
					return true;
				}
			} else {
				// 숫자가 큰 것부터 게임스위치에 대한 이벤트를 찾는다.
				for (int i = 256 - 1; i >= 1; --i) {
					if (eventPool.containsKey(i) == true) {
						if (Renderer.m_bGameSwitch[i] == true) {
							curEvent = eventPool.get(i);
							break;
						}
					}
				}
				
				// 아무런 이벤트도 찾지 못했을 시, 0번 이벤트를 발동시켜준다.
				if (curEvent == null) curEvent = eventPool.get(0);
			}
			
			// 처리 플래그
			boolean move_flag = false;
			boolean collision_flag = false;
			
			// 이전 좌표 값
			int old_x = m_nX;
			int old_y = m_nY;
			
			// 이동해야 한다
			if (m_nTargetX != m_nX) {
				int diff = m_nTargetX - m_nX;
				
				if (diff > 0) {
					++m_nX;
					// 지형에 대한 충돌 처리를 한다
					if (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable()) {
						m_nX = old_x;
						m_nY = old_y;
						
						if (collision_flag == false) {
							++collisionCount;
							collision_flag = true;
						}
					} else {
						if (m_nMotion == RIGHT1) m_nMotion = RIGHT2;
						else m_nMotion = RIGHT1;
						move_flag = true;
					}
					
					// 스크린에 대한 충돌 처리를 한다
					if (m_nX > (Map.WIDTH_MAP - m_nWidthSize - 1)) {
						m_nX = Map.WIDTH_MAP - m_nWidthSize - 1;
						
						if (collision_flag == false) {
							++collisionCount;
							collision_flag = true;
						}
					}

					// 다른  Creature 들과 충돌 처리를 한다.
					for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
						Creature tmpChr = m_Map.creaturePool.get(i);
		
						if (tmpChr != this) {
							if (tmpChr.m_nY == m_nY && tmpChr.m_nX == m_nX + 1) {
								m_nX = old_x;
								m_nY = old_y;
								
								if (collision_flag == false) {
									++collisionCount;
									collision_flag = true;
								}
								
								break;
							}
						}
					}
					
				} else {
					--m_nX;
					// 지형에 대한 충돌 처리를 한다
					if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable()) {
						m_nX = old_x;
						m_nY = old_y;
						
						if (collision_flag == false) {
							++collisionCount;
							collision_flag = true;
						}

					} else {
						if (m_nMotion == LEFT1) m_nMotion = LEFT2;
						else m_nMotion = LEFT1;
						move_flag = true;
					}
					
					// 스크린에 대한 충돌 처리를 한다
					if (m_nX < 1) {
						m_nX = 1;
						
						if (collision_flag == false) {
							++collisionCount;
							collision_flag = true;
						}
					}
					
					// 다른  Creature 들과 충돌 처리를 한다.
					for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
						Creature tmpChr = m_Map.creaturePool.get(i);
		
						if (tmpChr != this) {
							if (tmpChr.m_nY == m_nY && tmpChr.m_nX + 1 == m_nX) {
								m_nX = old_x;
								m_nY = old_y;
								
								if (collision_flag == false) {
									++collisionCount;
									collision_flag = true;
								}
								
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
						++m_nY;
						// 지형에 대한 충돌 처리를 한다
						if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable() || (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable())) {
							m_nX = old_x;
							m_nY = old_y;
							
							if (collision_flag == false) {
								++collisionCount;
								collision_flag = true;
							}
						} else {
							if (m_nMotion == BOTTOM1) m_nMotion = BOTTOM2;
							else m_nMotion = BOTTOM1;
							move_flag = true;
						}
						
						// 스크린에 대한 충돌 처리를 한다
						if (m_nY > (Map.HEIGHT_MAP - m_nHeightSize - 1)) {
							m_nY = Map.HEIGHT_MAP - m_nHeightSize - 1;
							
							if (collision_flag == false) {
								++collisionCount;
								collision_flag = true;
							}
						}
						
						// 다른  Creature 들과 충돌 처리를 한다.
						for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
							Creature tmpChr = m_Map.creaturePool.get(i);
			
							if (tmpChr != this) {
								if (tmpChr.m_nY == m_nY && !((tmpChr.m_nX + 1 < m_nX) || (tmpChr.m_nX > m_nX + 1))) {
									m_nX = old_x;
									m_nY = old_y;
									
									if (collision_flag == false) {
										++collisionCount;
										collision_flag = true;
									}
									
									break;
								}
							}
						}						
						
					} else {
						--m_nY;
						// 지형에 대한 충돌 처리를 한다
						if (!m_Map.GetTileXY(m_nX, m_nY + 1).IsWalkable() || (!m_Map.GetTileXY(m_nX + 1, m_nY + 1).IsWalkable())) {
							m_nX = old_x;
							m_nY = old_y;
							
							if (collision_flag == false) {
								++collisionCount;
								collision_flag = true;
							}
						} else {
							if (m_nMotion == TOP1) m_nMotion = TOP2;
							else m_nMotion = TOP1;
							move_flag = true;
						}
						
						// 스크린에 대한 충돌 처리를 한다
						if (m_nY < 1) {
							m_nY = 1;
							
							if (collision_flag == false) {
								++collisionCount;
								collision_flag = true;
							}
						}
						
						// 다른  Creature 들과 충돌 처리를 한다.
						for (int i = 0; i < m_Map.creaturePool.size(); ++i) {
							Creature tmpChr = m_Map.creaturePool.get(i);
			
							if (tmpChr != this) {
								if (tmpChr.m_nY == m_nY && !((tmpChr.m_nX + 1 < m_nX) || (tmpChr.m_nX > m_nX + 1))) {
									m_nX = old_x;
									m_nY = old_y;
									
									if (collision_flag == false) {
										++collisionCount;
										collision_flag = true;
									}
									
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
			
			
			// 이동 목표 좌표 설정
			if (curEvent.move == 0) { // 좌우
				if ((collisionCount % 2) == 0) m_nTargetX = m_nX + 1;
				else m_nTargetX = m_nX - 1;
				
			} else if (curEvent.move == 1) { // 상하
				if ((collisionCount % 2) == 0) m_nTargetY = m_nY + 1;
				else m_nTargetY = m_nY - 1;
				
			} else if (curEvent.move == 3) {
				m_nTargetX = m_nX;
				m_nTargetY = m_nY;
			} else {	// == 2 랜덤, == 4 없어짐
			}
		} catch (Exception ex) {
			String str = ex.getMessage();
			return true;
		}
		
		return true;
	}
	
	@Override
	public boolean Render(GL10 gl) {
		if (curEvent == null) return true;
		if (curEvent.move == 4) return true;

		m_Tex.DrawTexture(gl, (m_nX - m_Map.screen_x) * WIDTH_MOVE_PIXEL, (m_nY - m_Map.screen_y) * HEIGHT_MOVE_PIXEL,  
				m_nMotion * WIDTH_OBJ_PIXEL, m_nIdx * HEIGHT_OBJ_PIXEL, WIDTH_OBJ_PIXEL, HEIGHT_OBJ_PIXEL, 
				0, 0, 0, 
				1.f, 1.f);
		
		return true;
	}		

}
