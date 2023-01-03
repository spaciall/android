package spaciall.RPGMaker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;


public class Texture
{
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private int[] textures = new int[1];
    public int imgWidth, imgHeight;

    public Texture()
    {
    	ByteBuffer byteBuf;

        byteBuf = ByteBuffer.allocateDirect(48);
        byteBuf.order( ByteOrder.nativeOrder() );
        vertexBuffer = byteBuf.asFloatBuffer();

        byteBuf = ByteBuffer.allocateDirect(32);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
    }
    
    // x, y 좌표에 텍스쳐를 그립니다.
    // scalex, scaley의 기본값은 1.0f로 주시면 되고, 값이 바뀌면 이미지 크기가 줄어듭니다.
    // 0.5f이면 절반 사이즈로 출력되겠지요. 2.0f면 2배로 늘어날테고요. 
    
	public void DrawTexture( GL10 gl, float x, float y, float scalex, float scaley )
	{
		float[] vertices = {
				x			,	y+imgHeight	,	0.0f,	// LEFT  | BOTTOM
				x+imgWidth	,	y+imgHeight	,	0.0f,	// RIGHT | BOTTOM
				x			,	y			,	0.0f,	// LEFT  | TOP
				x+imgWidth	,	y			,	0.0f	// RIGHT | TOP
		};

		float[] texture = {
				0.0f	, 1.0f,
				1.0f	, 1.0f,
				0.0f	, 0.0f,
				1.0f	, 0.0f
		};

		vertexBuffer.put( vertices );
		vertexBuffer.position( 0 );

		textureBuffer.put( texture );
		textureBuffer.position( 0 );

		gl.glPushMatrix();
		gl.glTranslatef( x + (imgWidth / 2), y + (imgHeight / 2), 0 );
		gl.glScalef( scalex, scaley, 1.0f );
		gl.glTranslatef( -(x + (imgWidth / 2)), -(y + (imgHeight / 2)), 0 );

		gl.glBindTexture( GL10.GL_TEXTURE_2D, textures[0] );
		gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, vertexBuffer );
		gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, textureBuffer );
		gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );
		gl.glPopMatrix();
	}

    // 위의 함수에 옵션을 좀 추가해봤습니다.
	// sx, sy는 텍스쳐 소스에서 출력할 영역의 시작점입니다. w, h는 사이즈이고요.
	// 그러니까 128x128짜리 텍스쳐에서 1/4에 해당하는 0, 0, 64, 64 영역만 그리고 싶다면 sx=0, sy=0, w=64, h=64 해면 됩니다.
	// rx, ry는 회전시 기준점이 되는 좌표입니다. 기본값이 0, 0이기 때문에 왼쪽 상단의 꼭지점을 기준으로 돌게 됩니다.
	// 하지만 텍스쳐 사이즈가 128x128인 경우 -64, -64를 해주면 중심점을 기준으로 돌게 됩니다.
	// angle은 회전 각도이고요.
	// scalex, scaley는 위의 함수와 같습니다. 
    
	public void DrawTexture( GL10 gl, float x, float y, float sx, float sy, float w, float h, float rx, float ry, float angle, float scalex, float scaley )
	{
		float x1, y1, x2, y2, width = w, height = h;
		
		float[] vertices = {
				x		,	y+height	,	0.0f,	// LEFT  | BOTTOM
				x+width	,	y+height	,	0.0f,	// RIGHT | BOTTOM
				x		,	y			,	0.0f,	// LEFT  | TOP
				x+width	,	y			,	0.0f	// RIGHT | TOP
		};
		x1 = sx / imgWidth;
		y1 = sy / imgHeight;
		x2 = (sx + width ) / imgWidth;
		y2 = (sy + height) / imgHeight;

		float[] texture = {
				x1, y2,
				x2, y2,
				x1, y1,
				x2, y1
		};

		vertexBuffer.put( vertices );
		vertexBuffer.position( 0 );

		textureBuffer.put( texture );
		textureBuffer.position( 0 );

		gl.glPushMatrix();

		if ( angle != 0.0f )
		{
			gl.glTranslatef( x - rx, y - ry, 0 );
			gl.glRotatef( angle, 0, 0, 1 );
			gl.glTranslatef( -(x - rx), -(y - ry), 0 );
		}
		gl.glTranslatef( x + (imgWidth / 2), y + (imgHeight / 2), 0 );
		gl.glScalef( scalex, scaley, 1.0f );
		gl.glTranslatef( -(x + (imgWidth / 2)), -(y + (imgHeight / 2)), 0 );

		gl.glBindTexture( GL10.GL_TEXTURE_2D, textures[0] );
		gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, vertexBuffer );
		gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, textureBuffer );
		gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );
		gl.glPopMatrix();
	}

    public void LoadTexture( GL10 gl, Context context, int resource )
    {
        Bitmap bitmap = null;
        InputStream is = context.getResources().openRawResource( resource );

        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
            }
        }
        imgWidth = bitmap.getWidth();
        imgHeight = bitmap.getHeight();

        gl.glGenTextures( 1, textures, 0 );
        gl.glBindTexture( GL10.GL_TEXTURE_2D, textures[0] );

        gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST );
        gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );

        gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );
        gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );

        GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bitmap, 0 );
        
//		recycle해주면 장치를 소실 후, 다시 생성될 때 Texture를 화면에 출력하지 못 하는 현상이 발생한다.
//      bitmap.recycle();
    }
}
