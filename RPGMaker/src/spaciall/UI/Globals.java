package spaciall.UI;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.WindowManager;


public class Globals {
	// ��ġ �ػ�
	public static int DEVICE_WIDTH = 480;
	public static int DEVICE_HEIGHT = 800;

	// �÷�
	public static Paint DEFAULT_PAINT = new Paint();
	public static Paint WHITE_PAINT = new Paint();
	public static Paint GREEN_PAINT = new Paint();
	public static Paint BLACK_PAINT = new Paint();
	
	public static void InitUIGlobals(int width, int height)
	{
		// ��ġ �ػ�
		DEVICE_WIDTH = width;
		DEVICE_HEIGHT = height;
		
		// �÷� �ʱ�ȭ
		DEFAULT_PAINT.setColor(Color.argb(255, 255, 255, 255)); 	
		WHITE_PAINT.setColor(Color.argb(255, 255, 255, 255)); 
		GREEN_PAINT.setColor(Color.argb(255, 0, 255, 0)); 	
		BLACK_PAINT.setColor(Color.argb(255, 0, 0, 0));
	}
}
