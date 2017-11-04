package com.model.tool;

import com.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;



public class MTDialView extends SurfaceView implements Callback,Runnable{
	private SurfaceHolder holder;
	private Thread thread;
	private Paint paint;
	private Canvas canvas;
	private int screenW,screenH;
	private Bitmap bigDialBmp,bigPointerBmp,bgBmp;
	private boolean flag;
	private int bigDialX,bigDialY,bigPointerX,bigPointerY

	;
	private Rect bgRect;

	public int bigDialDegrees,smallDialDegrees;
	public MTDialView(Context context) {
		super(context);
		holder=getHolder();
		holder.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setColor(Color.argb(255, 207, 60, 11));
		paint.setTextSize(22);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	/* 01.构造函数*/
	public MTDialView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder=getHolder();
		holder.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setColor(Color.argb(255, 207, 60, 11));
		paint.setTextSize(22);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	/* 02.画布的构造函数*/
	public void surfaceCreated(SurfaceHolder holder) {

		bigDialBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_dashboard_01);
		bigPointerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_pointer_01);
		
		/**/
		bgBmp = BitmapFactory.decodeResource(getResources(), R.drawable.signsec_dj_ll_blue);

		screenH=bgBmp.getHeight();
		screenW=getWidth();
		bgRect=new Rect(0, 0,screenW , screenH);
		bigDialX =20;
		bigDialY =50;
		bigPointerX = 20/2+bigDialBmp.getWidth()/2-bigPointerBmp.getWidth()/2+10;
		bigPointerY = 50;

		/**/
		flag=true;
		thread= new Thread(this);
		thread.start();
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		flag=false;
	}
	
	///////////////////////////////////////////////////
	/*画画的线程*/
	public void run() {
		while(flag){
			long start = System.currentTimeMillis();
	        myDraw();
	        
	        long end = System.currentTimeMillis();
	        try {
	            if (end - start < 50)
	           Thread.sleep(50 - (end - start));
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
		}
	}

	
	/*画笔定义类*/
	public void myDraw(){
		try {
			canvas=holder.lockCanvas(bgRect);
			canvas.drawColor(Color.WHITE);
			drawBigDial();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
   //01.大画笔
   public void drawBigDial(){
		canvas.drawBitmap(bigDialBmp, bigDialX, bigDialY, paint);
		canvas.save();
		canvas.rotate(bigDialDegrees,bigPointerX+bigPointerBmp.getWidth()/2, bigPointerY+bigPointerBmp.getHeight()/2);
		canvas.drawBitmap(bigPointerBmp,bigPointerX,bigPointerY,paint);
		canvas.restore();
	}
	
	public int getBigDialDegrees() {
		return bigDialDegrees;
	}
	public void setBigDialDegrees(int bigDialDegrees) {
		Log.i("MyLog", "de="+bigDialDegrees);
		this.bigDialDegrees = bigDialDegrees;
	}
	
}
