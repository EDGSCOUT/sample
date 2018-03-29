package wsi.psyadjustbook;

import wsi.psy.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class PsyAdjustActivity extends Activity { // <DemoTestActivity>

	LinearLayout buses;
	Button gooutgloomyButton;
	public static PsyAdjustActivity activity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		activity = this;
		
		this.gooutgloomyButton = (Button) findViewById(R.id.start);
		final Intent intentGoOutGloomy = new Intent(PsyAdjustActivity.this, GoOutGloomy.class);
		Bundle bundle = new Bundle();
		bundle.putInt("groupPosition", 0);
		bundle.putInt("childPosition", 0);
		intentGoOutGloomy.putExtra("tag", bundle);
		
		this.gooutgloomyButton.setOnClickListener(new OnClickListener() {
			public void onClick(View V) {
				startActivity(intentGoOutGloomy);
			}
		});
	}
}