package spaciall.RPGMaker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.AssetManager;



public class Map {
	protected static final int WIDTH_MAP = 30;
	protected static final int HEIGHT_MAP = 18;
	
	protected static final int WIDTH_SCREEN = 15;
	protected static final int HEIGHT_SCREEN = 10;
	
	protected static final int RIGHT_MOVE_SCREEN = 7;
	protected static final int LEFT_MOVE_SCREEN = 6;
	protected static final int TOP_MOVE_SCREEN = 3;
	protected static final int BOTTOM_MOVE_SCREEN = 4;

	protected Tile[][] m_Tile = new Tile[WIDTH_MAP][HEIGHT_MAP];
	protected ArrayList<Creature> creaturePool = new ArrayList<Creature>();
	
	protected ArrayList<String> monsterNamePool = new ArrayList<String>();
	
	protected int screen_x = 0;
	protected int screen_y = 0;
	
	public Renderer renderer;
	
	public void SetRenderer(Renderer o) {
		renderer = o;
	}
	
	// 테스트 함수
	public void TestSetup(Context context) {
		// 맵 불러오기
		AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open("map_0_0.dat");
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        
        final int len = 30 * 18 * 4;
        byte[] bytes = new byte[len];
        
        try {
        	is.read(bytes, 0, len);
        } catch (IOException ex) {
        }
        
        ByteBuffer bf = ByteBuffer.allocate(len);
        bf.order(ByteOrder.LITTLE_ENDIAN);
        bf.put(bytes);
        bf.flip();
       

		for (int j = 0; j < HEIGHT_MAP; ++j) {
			m_Tile[j] = new Tile[WIDTH_MAP];
			
			for (int i = 0; i < WIDTH_MAP; ++i) {
				m_Tile[j][i] = new Tile();
				 
		        int k = bf.getInt();
				m_Tile[j][i].m_Id = k;
				
				
			}
		}
		
		try {
			is.close();
		} catch (IOException ioe) {
		}
		
		// NPC 불러오기
        try {
            is = am.open("ivent_0_0.dat");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "euc-kr"));
            DataInputStream dis = new DataInputStream(is);
            String str = "";
            
            int x = 0, y = 0;
            int swch = 0;
            int id = 0;
            int action = 0;
            int move = 0;
            
            // 몬스터 정보부터 읽어오기
            String monster1 = br.readLine();
            String monster2 = br.readLine();
            String monster3 = br.readLine();
            String monster4 = br.readLine();
            
            monsterNamePool.add(monster1);
            monsterNamePool.add(monster2);
            monsterNamePool.add(monster3);
            monsterNamePool.add(monster4);
            
            while ((str = br.readLine()) != null)
            {
            	if (str.equals("[Pos]")) {
            		String str2 = br.readLine();
            		
            		int offset = str2.indexOf(",");
            		
            		String strX = str2.substring(0, offset);
            		String strY = str2.substring(offset + 1, str2.length());
            		
            		x = Integer.parseInt(strX);
            		y = Integer.parseInt(strY);
            		
            	} else if (str.equals("[Switch]")) {
            		String str2 = br.readLine();
            		swch = Integer.parseInt(str2);
            		
            	} else if (str.equals("[ID]")) {
            		String str2 = br.readLine();
            		id = Integer.parseInt(str2);

            	} else if (str.equals("[Action]")) {
            		String str2 = br.readLine();
            		action = Integer.parseInt(str2);
	
            	} else if (str.equals("[Move]")) {
            		String str2 = br.readLine();
            		move = Integer.parseInt(str2);
            		
            	} else if (str.equals("[Data]")) {
            		ArrayList<String> dataPool = new ArrayList<String>();
            		dataPool.clear();
            		
            		while (true) {
            			String strData = br.readLine();
            			
            			if (strData.equals("[DataEnd]")) break;
            			else dataPool.add(strData);
            		}
            		
            		boolean flag = false;
            		for (int i = 0; i < creaturePool.size(); ++i) {
            			Creature tmpCreature = creaturePool.get(i);
            			
            			if (tmpCreature.m_nType == Creature.NPC) {
            				NonPC tmpNpc = (NonPC)tmpCreature;
            			
	            			// 해당 이벤트 객체가 기존에 존재한다.
	            			if (tmpNpc.m_nIdx == id && tmpNpc.m_nX == x && tmpNpc.m_nY == y) {
	            				// 이 객체의 해당 스위치에 대한 동작의 값이 없다면 넣어준다.
	            				if (tmpNpc.eventPool.containsKey(swch) == false) {
	            					Event event = new Event();
	            					event.actionWhere = action;
	            					event.move = move;
	            					
	            					for (int j = 0; j < dataPool.size(); ++j)
	            						event.dataPool.add(dataPool.get(j));
	            					
	            					tmpNpc.eventPool.put(swch, event);
	            				}
	            				
	            				flag = true;
	            				break;
	            			}
            			}
            		}
            		
            		// 해당 이벤트 객체가 기존에 존재하지 않으므로 생성해 준다.
            		if (flag == false) {
	            		NonPC npc = new NonPC(renderer, Renderer.GetContext(), this, id, x, y);
	            		
    					Event event = new Event();
    					event.actionWhere = action;
    					event.move = move;
    					
    					for (int j = 0; j < dataPool.size(); ++j)
    						event.dataPool.add(dataPool.get(j));
    					
    					npc.eventPool.put(swch, event);
	            		
	            		creaturePool.add(npc);
            		}            		
	        	} 
            }
            
            is.close();
            
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        	String msg = ex.getMessage();
        }
	}
	
	public static byte [] bytes = new byte[30 * 18 * 4];
	public boolean Setup(Context context, int x, int y) {
		// 맵 불러오기
		AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        
        String mapName = "map_" + x + "_" + y + ".dat";
        
        try {
            is = am.open(mapName);
        } catch (FileNotFoundException ex) {
        	return false;
        } catch (IOException ex) {
        	return false;
        }
        
        try {
        	is.read(bytes, 0, 30 * 18 * 4);
        } catch (IOException ex) {
        	return false;
        }
        
        ByteBuffer bf = ByteBuffer.allocate(30 * 18 * 4);
        bf.order(ByteOrder.LITTLE_ENDIAN);
        bf.put(bytes);
        bf.flip();

		for (int j = 0; j < HEIGHT_MAP; ++j) {
			m_Tile[j] = new Tile[WIDTH_MAP];
			
			for (int i = 0; i < WIDTH_MAP; ++i) {
				m_Tile[j][i] = new Tile();
				 
		        int k = bf.getInt();
				m_Tile[j][i].m_Id = k;
				
				
			}
		}
		
		try {
			is.close();
		} catch (IOException ioe) {
			return false;
		}
		
		System.gc();
		
		return true;
	}
	
	public boolean SetupIvent(Context context, int ivent_x, int ivent_y) {
		// NPC 불러오기
        String iventName = "ivent_" + ivent_x + "_" + ivent_y + ".dat";
		
		AssetManager am = context.getResources().getAssets();
        InputStream is = null;		
		
        try {
            is = am.open(iventName);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "euc-kr"));
            DataInputStream dis = new DataInputStream(is);
            String str = "";
            
            int x = 0, y = 0;
            int swch = 0;
            int id = 0;
            int action = 0;
            int move = 0;
            
            // 몬스터 정보부터 읽어오기
            String monster1 = br.readLine();
            String monster2 = br.readLine();
            String monster3 = br.readLine();
            String monster4 = br.readLine();
            
            monsterNamePool.add(monster1);
            monsterNamePool.add(monster2);
            monsterNamePool.add(monster3);
            monsterNamePool.add(monster4);
            
            while ((str = br.readLine()) != null)
            {
            	if (str.equals("[Pos]")) {
            		String str2 = br.readLine();
            		
            		int offset = str2.indexOf(",");
            		
            		String strX = str2.substring(0, offset);
            		String strY = str2.substring(offset + 1, str2.length());
            		
            		x = Integer.parseInt(strX);
            		y = Integer.parseInt(strY);
            		
            	} else if (str.equals("[Switch]")) {
            		String str2 = br.readLine();
            		swch = Integer.parseInt(str2);
            		
            	} else if (str.equals("[ID]")) {
            		String str2 = br.readLine();
            		id = Integer.parseInt(str2);

            	} else if (str.equals("[Action]")) {
            		String str2 = br.readLine();
            		action = Integer.parseInt(str2);
	
            	} else if (str.equals("[Move]")) {
            		String str2 = br.readLine();
            		move = Integer.parseInt(str2);
            		
            	} else if (str.equals("[Data]")) {
            		ArrayList<String> dataPool = new ArrayList<String>();
            		dataPool.clear();
            		
            		while (true) {
            			String strData = br.readLine();
            			
            			if (strData.equals("[DataEnd]")) break;
            			else dataPool.add(strData);
            		}
            		
            		boolean flag = false;
            		for (int i = 0; i < creaturePool.size(); ++i) {
            			Creature tmpCreature = creaturePool.get(i);
            			
            			if (tmpCreature.m_nType == Creature.NPC) {
            				NonPC tmpNpc = (NonPC)tmpCreature;
            			
	            			// 해당 이벤트 객체가 기존에 존재한다.
	            			if (tmpNpc.m_nIdx == id && tmpNpc.m_nX == x && tmpNpc.m_nY == y) {
	            				// 이 객체의 해당 스위치에 대한 동작의 값이 없다면 넣어준다.
	            				if (tmpNpc.eventPool.containsKey(swch) == false) {
	            					Event event = new Event();
	            					event.actionWhere = action;
	            					event.move = move;
	            					
	            					for (int j = 0; j < dataPool.size(); ++j)
	            						event.dataPool.add(dataPool.get(j));
	            					
	            					tmpNpc.eventPool.put(swch, event);
	            				}
	            				
	            				flag = true;
	            				break;
	            			}
            			}
            		}
            		
            		// 해당 이벤트 객체가 기존에 존재하지 않으므로 생성해 준다.
            		if (flag == false) {
	            		NonPC npc = new NonPC(renderer, Renderer.GetContext(), this, id, x, y);
	            		
    					Event event = new Event();
    					event.actionWhere = action;
    					event.move = move;
    					
    					for (int j = 0; j < dataPool.size(); ++j)
    						event.dataPool.add(dataPool.get(j));
    					
    					npc.eventPool.put(swch, event);
    					
	            		creaturePool.add(npc);
            		}            		
	        	} 
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

	
	public void Update(GL10 gl) 
	{
		for (int i = 0; i < creaturePool.size(); ++i)
			creaturePool.get(i).Update(gl);
	}
	
	
	public void Render(GL10 gl) {
		for (int j = screen_y; j < screen_y + HEIGHT_SCREEN ; ++j) {
			for (int i = screen_x; i < screen_x + WIDTH_SCREEN; ++i) {
				// Render
				m_Tile[j][i].Render(gl, i - screen_x, j - screen_y);
			}
		}
		
		for (int i = 0; i < creaturePool.size(); ++i)
			creaturePool.get(i).Render(gl);
	}
	
	public Tile GetTileXY(int x, int y) {
		return m_Tile[y][x];
	}
}
