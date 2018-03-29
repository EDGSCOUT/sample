package wsi.psyadjustbook;

import wsi.decodexml.DataAdapter;
import wsi.decodexml.DecodeXML;
import wsi.psy.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImgText extends Activity {

	TextView mTextView;
	DataAdapter mDataAdapter;
	Button home;
	Button back;
	Button next;
	ProgressBar progressbar;
	public static ImgText mImgText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text);
	}

	public void screendisplay() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("tag");
		int sr = bundle.getInt("source");
		mDataAdapter = DecodeXML.decodexml(sr);
		String result = "";
		for (int i = 0; i < mDataAdapter.text.size(); i++)
			result = result + mDataAdapter.text.get(i);
		mTextView.setText(result);

	}

}
