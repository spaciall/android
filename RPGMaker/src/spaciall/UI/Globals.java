package spaciall.UI;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.WindowManager;


public class Globals {
	// 장치 해상도
	public static int DEVICE_WIDTH = 480;
	public static int DEVICE_HEIGHT = 800;

	// 컬러
	public static Paint DEFAULT_PAINT = new Paint();
	public static Paint WHITE_PAINT = new Paint();
	public static Paint GREEN_PAINT = new Paint();
	public static Paint BLACK_PAINT = new Paint();
	
	public static void InitUIGlobals(int width, int height)
	{
		// 장치 해상도
		DEVICE_WIDTH = width;
		DEVICE_HEIGHT = height;
		
		// 컬러 초기화
		DEFAULT_PAINT.setColor(Color.argb(255, 255, 255, 255)); 	
		WHITE_PAINT.setColor(Color.argb(255, 255, 255, 255)); 
		GREEN_PAINT.setColor(Color.argb(255, 0, 255, 0)); 	
		BLACK_PAINT.setColor(Color.argb(255, 0, 0, 0));
	}
}
