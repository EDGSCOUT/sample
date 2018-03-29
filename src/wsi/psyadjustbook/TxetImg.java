package wsi.psyadjustbook;

import wsi.psy.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TxetImg extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//setContentView(PsyAdjustActivity.applaysource.findViewById(R.layout.main));
		setContentView(R.layout.home);
		Log.i("start activity","hello");
	}
	

}
