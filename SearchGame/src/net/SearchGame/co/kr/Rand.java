package net.SearchGame.co.kr;
import java.util.Random;

public class Rand {
	protected static Random rand = new Random(System.currentTimeMillis());
	
	static int NextInt()
	{
		return rand.nextInt();
	}
	
}
