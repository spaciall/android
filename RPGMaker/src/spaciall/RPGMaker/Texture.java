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
    
    // x, y ��ǥ�� �ؽ��ĸ� �׸��ϴ�.
    // scalex, scaley�� �⺻���� 1.0f�� �ֽø� �ǰ�, ���� �ٲ�� �̹��� ũ�Ⱑ �پ��ϴ�.
    // 0.5f�̸� ���� ������� ��µǰ�����. 2.0f�� 2��� �þ�װ��. 
    
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

    // ���� �Լ��� �ɼ��� �� �߰��غý��ϴ�.
	// sx, sy�� �ؽ��� �ҽ����� ����� ������ �������Դϴ�. w, h�� �������̰��.
	// �׷��ϱ� 128x128¥�� �ؽ��Ŀ��� 1/4�� �ش��ϴ� 0, 0, 64, 64 ������ �׸��� �ʹٸ� sx=0, sy=0, w=64, h=64 �ظ� �˴ϴ�.
	// rx, ry�� ȸ���� �������� �Ǵ� ��ǥ�Դϴ�. �⺻���� 0, 0�̱� ������ ���� ����� �������� �������� ���� �˴ϴ�.
	// ������ �ؽ��� ����� 128x128�� ��� -64, -64�� ���ָ� �߽����� �������� ���� �˴ϴ�.
	// angle�� ȸ�� �����̰��.
	// scalex, scaley�� ���� �Լ��� �����ϴ�. 
    
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
        
//		recycle���ָ� ��ġ�� �ҽ� ��, �ٽ� ������ �� Texture�� ȭ�鿡 ������� �� �ϴ� ������ �߻��Ѵ�.
//      bitmap.recycle();
    }
}
