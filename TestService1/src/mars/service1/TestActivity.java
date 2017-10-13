package mars.service1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestActivity extends Activity {
	/** Called when the activity is first created. */
	private Button startServiceButton = null;
	private Button stopServiceButton = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startServiceButton = (Button) findViewById(R.id.startService);
		startServiceButton.setOnClickListener(new StartServiceListener());
		stopServiceButton = (Button) findViewById(R.id.stopService);
		stopServiceButton.setOnClickListener(new StopServiceListener());
		System.out.println("Activity onCreate");
	}

	class StartServiceListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, FirstService.class);
			startService(intent);           //启动一个service对象，会传递到FirstService中的onStartCommand，这个函数当中去
		}
	}

	class StopServiceListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TestActivity.this, FirstService.class);
			stopService(intent);       
		}
	}
}