package spaciall.RPGMaker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLUtils;

public class Text3d {
	 
	 private IntBuffer   mVertexBuffer;
	    private IntBuffer   mColorBuffer;
	    private ByteBuffer  mIndexBuffer;
	    private FloatBuffer mfTexBuffer;
	    private int mTextureID;
	    
	 public Text3d(){
	     int one = 50;
	         int vertices[] = {
	                 0, one, 0,
	                 one, one, 0,
	                 0,  0, 0,
	                 one,  0, 0,
	               
	         };
	         
	         float tex[] = {
	           0.0f ,0.0f,
	           1.0f,0.0f,
	           0.0f ,1.0f,
	           1.0f,1.0f,
	           
	         };
	         one = 100000;
	         int colors[] = {
	           one,    one,    one,  one,
	                 one,    one,    one,  one,
	                 one,  one,    one,  one,
	                 one,  one,    one,  one,
	                
	         };
	         byte indices[] = {
	                 0, 1, 2, 
	                 1, 3, 2,
	         };
	     
	         ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	         vbb.order(ByteOrder.nativeOrder());
	         mVertexBuffer = vbb.asIntBuffer();
	         mVertexBuffer.put(vertices);
	         mVertexBuffer.position(0);
	         
	         ByteBuffer tbb = ByteBuffer.allocateDirect(tex.length*4);
	         tbb.order(ByteOrder.nativeOrder());
	         mfTexBuffer = tbb.asFloatBuffer();
	         mfTexBuffer.put(tex);
	         mfTexBuffer.position(0);
	         ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
	         cbb.order(ByteOrder.nativeOrder());
	         mColorBuffer = cbb.asIntBuffer();
	         mColorBuffer.put(colors);
	         mColorBuffer.position(0);
	         mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
	         mIndexBuffer.put(indices);
	         mIndexBuffer.position(0);
	         
	      
	     }
	 
	 public void TexCreate(GL10 gl,Context mContext)
	  {
	   int[] textures = new int[1];
	         gl.glGenTextures(1, textures, 0);
	         mTextureID = textures[0];
	         gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
	         
	         gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
	                 GL10.GL_NEAREST);
	         gl.glTexParameterf(GL10.GL_TEXTURE_2D,
	                 GL10.GL_TEXTURE_MAG_FILTER,
	                 GL10.GL_LINEAR);
	         gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
	                 GL10.GL_CLAMP_TO_EDGE);
	         gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
	                 GL10.GL_CLAMP_TO_EDGE);
	         gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
	                 GL10.GL_REPLACE);
	         
	        
	         
	      Bitmap mBitmap;
	      
	      Canvas mCanvas;
	      
	      Bitmap.Config config =   Bitmap.Config.ARGB_8888;
	      
	      mBitmap = Bitmap.createBitmap(128, 128, config);
	         mCanvas = new Canvas(mBitmap);
	         mBitmap.eraseColor(0);
	         
	         
	         mCanvas.drawColor(0x00ffffff);
	         
	         
	         
	         Paint Pnt = new Paint();
	         Pnt.setColor(0xff00ff00);
	         Pnt.setTextSize(128);
	         Pnt.setAntiAlias(false);
	         Pnt.setTextScaleX(1);
	         
	         mCanvas.drawText("B", 0, 128, Pnt);
	         
	         
	         GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0); 
	         
	         
	         
	         mBitmap.recycle();    
	         

	   
	  }
	     public void draw(GL10 gl)
	     {
	      
	         gl.glFrontFace(GL10.GL_CW);
	         gl.glEnable(GL10.GL_TEXTURE_2D);
	         gl.glColor4f(1f, 1f, 1f, 1f); 
	         gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
	                 GL10.GL_MODULATE);
	         
	         gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
	         
	         gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	         gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	         gl.glActiveTexture(GL10.GL_TEXTURE0);
	        
	                 
	        
	         gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mfTexBuffer);
	         gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
	         gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
	         gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
	         
	         
	     }
 
}

