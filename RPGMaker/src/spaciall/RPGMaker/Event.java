package spaciall.RPGMaker;

import java.util.ArrayList;

import java.util.ArrayList;

public class Event {
	public int actionWhere;
	public int move;
	
	public int curProcess;
	
	public Event() {
		curProcess = 0;
	}
	
	public ArrayList<String> dataPool = new ArrayList<String>();
	
	static final int TRIGGER_ON_SAME_MAP_WITH_CHR = 0;
	static final int TRIGGER_ON_SCREEN_WITH_CHR = 1;
	static final int TRIGGER_ON_COLLIDE_WITH_CHR = 2;
	static final int TRIGGER_ON_CLICK_BY_CHR = 3;
}
