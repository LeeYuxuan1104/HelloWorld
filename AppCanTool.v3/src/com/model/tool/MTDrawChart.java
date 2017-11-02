package com.model.tool;


import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public class MTDrawChart extends View{
	/*图形的基本属性*/
	// 01.高度;
	private int CHARTH = 600;
	// 02.宽度;
	private int CHARTW = 400;
	private int OFFSET_LEFT = 70;
	// 03.距离顶部;
	private int OFFSET_TOP = 20;
	private int TEXT_OFFSET = 20;
	private int X_INTERVAL = 20;
	// 04.承装数据的列表;
	private List<Point> plist;
	// 05.工具成分;
	private Paint paint;
	private ArrayList<Integer> values;
	
	public MTDrawChart(Context context,ArrayList<Integer> values) {
		super(context);
		plist = new ArrayList<Point>();
		this.values=values;
	}
	
	public List<Point> getPlist() {
		return plist;
	}

	public void setPlist(List<Point> plist) {
		this.plist = plist;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//	绘制画布;
		drawTable(canvas);
		prepareLine();
		drawCurve(canvas);
	}
	/*绘制画布*/
	public void drawTable(Canvas canvas){
		/*声明画笔*/
		//	01.声明画笔工具;
		paint 		 = new Paint();
		//	02.声明画笔颜色;
		paint.setColor(Color.WHITE);
		//	03.绘制图形的外檐边框;
		//	03.1画布空心;
		paint.setStyle(Paint.Style.STROKE);
		//paint.setStrokeWidth(Pain);		
		Rect chartRec = new Rect(OFFSET_LEFT, OFFSET_TOP,CHARTW+OFFSET_LEFT, CHARTH+OFFSET_TOP);
		canvas.drawRect(chartRec, paint);
		
		//画左边的文字
		Path textPath = new Path();
		paint.setStyle(Paint.Style.FILL);
		textPath.moveTo(30, 420);
		textPath.lineTo(30, 300);
		paint.setTextSize(15);
		paint.setAntiAlias(true);
		canvas.drawTextOnPath("[物理数值]", textPath, 0, 0, paint);
	
		//画左侧数字
        for(int i=0;i<=10;i++){
        	int num=100-10*i;
        	String sum=String.valueOf(num);        	
        	canvas.drawText(sum, OFFSET_LEFT - TEXT_OFFSET-5, OFFSET_TOP + (CHARTH/10)*i, paint);
        }
        
        //画表格中的虚线
        Path path = new Path();     
        PathEffect effects = new DashPathEffect(new float[]{2,2,2,2},1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(false);
        paint.setPathEffect(effects); 		
		for(int i = 1 ; i<10 ; i++){
			path.moveTo(OFFSET_LEFT, OFFSET_TOP + CHARTH/10*i);  
	        path.lineTo(OFFSET_LEFT+CHARTW,OFFSET_TOP + CHARTH/10*i); 
	        canvas.drawPath(path, paint);
		}
	}
	
	public void drawCurve(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
	
		if(plist.size() >= 2){
			for(int i = 0; i<plist.size()-1; i++){
				
				canvas.drawLine(plist.get(i).x, plist.get(i).y, plist.get(i+1).x, plist.get(i+1).y, paint);
			}
		}
	}
	//TODO 很重要;
//	public void prepareLine(){
//		for(int n:values){
//			double rate=(double)n/100;
//			int py=(int) (OFFSET_TOP+CHARTH-(rate*CHARTH));
//			Point p = new Point(OFFSET_LEFT + CHARTW , py );
//			if(plist.size() > 21){
//				plist.remove(0);
//				for(int i = 0; i<20; i++){
//					if(i == 0) plist.get(i).x -= (X_INTERVAL - 2);
//					else plist.get(i).x -= X_INTERVAL;
//				}
//				plist.add(p);
//			}
//			else{
//				for(int i = 0; i<plist.size()-1; i++){
//					plist.get(i).x -= X_INTERVAL;
//				}
//				plist.add(p);
//			}
//		}
////		int py = OFFSET_TOP + (int)(Math.random()*(CHARTH - OFFSET_TOP));
////		Point p = new Point(OFFSET_LEFT + CHARTW , py );
//
//	}
	private void prepareLine(){
		int py = OFFSET_TOP + (int)(Math.random()*(CHARTH - OFFSET_TOP));
		Point p = new Point(OFFSET_LEFT + CHARTW , py );
		if(plist.size() > 21){
			plist.remove(0);
			for(int i = 0; i<20; i++){
				if(i == 0) plist.get(i).x -= (X_INTERVAL - 2);
				else plist.get(i).x -= X_INTERVAL;
			}
			plist.add(p);
		}
		else{
			for(int i = 0; i<plist.size()-1; i++){
				plist.get(i).x -= X_INTERVAL;
			}
			plist.add(p);
		}

	}
//	private void initPlist(){
//		int py = OFFSET_TOP + (int)(Math.random()*(CHARTH - OFFSET_TOP));
//		Point p = new Point(OFFSET_LEFT + CHARTW , py );
//		plist.add(p);
//	}
//	
}
