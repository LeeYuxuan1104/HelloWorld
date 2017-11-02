package com.view;



import com.model.tool.MTDrawChart;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

public class VDrawLineActivity extends Activity{
	private Handler mHandler;
	private MTDrawChart view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_drawline);
		init();
	}
	
	 private void init() {  
	        LinearLayout layout=(LinearLayout) findViewById(R.id.root);  
	        view = new MTDrawChart(this);  
	      
	        view.invalidate();  
	        layout.addView(view);
	        
	    	mHandler = new Handler();
			mHandler.post(new TimerProcess());
	          
	    }  
	    
		private class TimerProcess implements Runnable {
			public void run() {
				view.invalidate();
				mHandler.postDelayed(this, 1000);
			}
		}
}
