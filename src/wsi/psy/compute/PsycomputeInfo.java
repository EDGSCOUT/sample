package wsi.psy.compute;

import wsi.psy.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PsycomputeInfo extends Activity {
	public TextView textView;
	public TextView textViewTitle;
	public Button button1;
	public Button button2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.psycomputeinfo);
		
		textViewTitle = (TextView)findViewById(R.id.psycompute_title);
		textView = (TextView)findViewById(R.id.psycompute_tv);
		textView.setText("");
		
		Intent intent = getIntent();
		textView.append("计算抑郁时使用到的信息："+ "\n" +"calllogOutAvg、contactlogAvg、media、system" + "\n" +
		"计算焦虑时使用到的信息："+ "\n" +"weibo、office" + "\n" + 
		"计算幸福感时使用到的信息："+ "\n" +"gpslogAvg、contactlogAvg、communicate、game_compety、game_shot、photo" + "\n" +
		"计算孤独感时使用到的信息："+ "\n" +"smslogOutAvg、contactlogAvg、renren、office"
		);
		textView.append("\n\n");
		final int score1 = intent.getIntExtra("score1", 0);
		final int score2 = intent.getIntExtra("score2", 0);
		final int score3 = intent.getIntExtra("score3", 0);
		final int score4 = intent.getIntExtra("score4", 0);
		button1 = (Button)findViewById(R.id.psycompute_btnDown);
		button2 = (Button)findViewById(R.id.psycompute_btnUp);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PsycomputeInfo.this, PsycomputeDisplay.class);
        		intent.putExtra("score1", score1);
            	intent.putExtra("score2", score2);
            	intent.putExtra("score3", score3);
            	intent.putExtra("score4", score4);
//				startActivity(intent);
            	startActivityForResult(intent, 0);
			}
		});
		button2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		finish();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 0 && resultCode == RESULT_OK){
			setResult(RESULT_OK);
			finish();
		}
	}
	
}
