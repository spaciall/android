package spaciall.RPGMaker;

import java.util.HashMap;


public class LevelExpTable {
	public static HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
	
	public static void Setup() 
	{
		hashMap.put(0, 0);
		hashMap.put(1, 0);
		hashMap.put(2, 500);
		hashMap.put(3, 1000);
		hashMap.put(4, 2000);
		hashMap.put(5, 5000);
		hashMap.put(6, 8000);
		hashMap.put(7, 12000);
		hashMap.put(8, 18000);
		hashMap.put(9, 25000);
		hashMap.put(10, 33000);
	}
	
	public static int GetLevelByExp(int exp) 
	{
		for (int i = hashMap.size() - 1; i >= 0; --i) {
			int value = hashMap.get(i).intValue();
			if (exp >= value) return i;
		}
		
		return 1;
	}
}
