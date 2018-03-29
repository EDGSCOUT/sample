package wsi.survey;

import wsi.psy.R;
import wsi.psyadjustbook.PsyAdjustActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Feedback extends Activity {
	public TextView textView;
	public TextView textViewTitle;
//	public Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.feedback);
		Intent intent = getIntent();
		String feeback = intent.getStringExtra("feedback");
		String fileName = intent.getStringExtra("fileName");
		int score = intent.getIntExtra("score", 0);
		textViewTitle = (TextView)findViewById(R.id.feedback_title);
		textView = (TextView)findViewById(R.id.feedback_tv);
		textView.setText("您的得分是： " + String.valueOf(score) + " \n " + feeback);
		
		
		String s1 = "CES-D_Phone";
		if (fileName.equals(s1)) {
//			button.setVisibility(View.VISIBLE);
			textView.append( Html.fromHtml("<b><font color=#FF0000>\"自助\"</font></b>（在首页菜单栏中）进行自助调节。"));
		}
		/*button = (Button)findViewById(R.id.feedback_btn);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            	Intent intent = new Intent(Feedback.this, PsyAdjustActivity.class);
            	startActivity(intent);
            	finish();
			}
		});
		*/
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		finish();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("result", "result");
		setResult(RESULT_OK, intent);
		finish();
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	
}
