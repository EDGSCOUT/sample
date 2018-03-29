package wsi.psyadjustbook;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import wsi.decodexml.DataAdapter;
import wsi.decodexml.DecodeXML;
import wsi.decodexml.DataAdapter.Item;
import wsi.psy.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

public class Text extends Activity {
	TextView mTextView;
	DataAdapter mDataAdapter;

	ImageButton home;
	ImageButton arrowleft;
	ImageButton arrowright;
	ProgressBar progressbar;
	parameter mparameter = new parameter(); 
	public String[] group = Listitem.parent;
	public String[][] children = Listitem.chiled;
	int xmlfile;
	private int childrennum = 0;
	public int groupPosition;
	public int childPosition;
	private int currentprogress = 0;
	public static Text mText;
	public List<record> recordlist = new ArrayList<record>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.text);
		initview();

		mText = this;
		mparameter = extparameter();
		groupPosition = mparameter.groupPosition;
		childPosition = mparameter.childPosition;
		String src = "page" + Integer.toString(groupPosition) + Integer.toString(childPosition);
		int xmlfile = getRid(src);
		screendisplay(xmlfile);

	}

	public void initview() {

		this.home = (ImageButton) findViewById(R.id.homebutton);
		this.arrowleft = (ImageButton) findViewById(R.id.backebutton);
		this.arrowright = (ImageButton) findViewById(R.id.nextbutton);
		this.progressbar = (ProgressBar) findViewById(R.id.progressbar1);
		this.home.setOnTouchListener(hemotouch);
		this.arrowleft.setOnTouchListener(arrowlefttouch);
		this.arrowright.setOnTouchListener(arrowrighttouch);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		TableRow.LayoutParams params = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = screenWidth - 150;
		params.gravity = Gravity.CENTER_VERTICAL;
		this.progressbar.setLayoutParams(params);

		getchildrennum();
		this.progressbar.setMax(childrennum);
	}

	public void getchildrennum() {
		for (int i = 0; i < Listitem.chiled.length; i++)
			childrennum = childrennum + Listitem.chiled[i].length;
	}

	public OnTouchListener hemotouch = new OnTouchListener() {
		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				returnhome();
			}
			return false;
		}
	};

	public void returnbaseactivity(String tag) {

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		intent.putExtras(bundle);
		Text.this.setResult(RESULT_OK, intent);
		Text.this.finish(); 

	}

	public OnTouchListener arrowlefttouch = new OnTouchListener() {

		public boolean onTouch(View arg0, MotionEvent arg1) {

			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				ScrollView mscrollview = (ScrollView) findViewById(R.id.ScrollView);
				mscrollview.fullScroll(ScrollView.FOCUS_UP);
				getNextparameter("arrowleft");
				String src = "page" + Integer.toString(groupPosition)
						+ Integer.toString(childPosition);
				System.out.println(src);
				int xmlfile = getRid(src);
				screendisplay(xmlfile);
			}

			return false;
		}

	};
	public OnTouchListener arrowrighttouch = new OnTouchListener() {

		public boolean onTouch(View arg0, MotionEvent arg1) {

			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				ScrollView mscrollview = (ScrollView) findViewById(R.id.ScrollView);
				mscrollview.fullScroll(ScrollView.FOCUS_UP);

				getNextparameter("arrowright");
				String src = "page" + Integer.toString(groupPosition)
						+ Integer.toString(childPosition);
				System.out.println(src);
				int xmlfile = getRid(src);
				screendisplay(xmlfile);
			}

			return false;
		}

	};

	public parameter extparameter() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("tag"); // ��ȡ��Դ
		parameter mparameter = new parameter();
		mparameter.groupPosition = bundle.getInt("groupPosition");
		mparameter.childPosition = bundle.getInt("childPosition");
		Log.i("get groupPosition", Integer.toString(mparameter.groupPosition));
		Log.i("get childposition", Integer.toString(mparameter.childPosition));
		return mparameter;
	}

	public class parameter {
		public int groupPosition;
		public int childPosition;

		public parameter() {

		}
		// public parameter(int src,int groupPosition,int childPosition){
		// this.xmlfile=src;
		// this.groupPosition=groupPosition;
		// this.childPosition=childPosition;
		// }

	}

	public void getNextparameter(String buttonname) {
		int groupsize = group.length;
		int childsize = children[groupPosition].length;

		if (buttonname == "arrowleft") {
			if (childPosition == 0 && groupPosition > 0) {
				groupPosition = groupPosition - 1;
				childPosition = children[groupPosition].length - 1;
			} else if (childPosition == 0 && groupPosition == 0) {
			} else {
				childPosition = childPosition - 1;
			}
		} else if (buttonname == "arrowright") {
			if (childPosition == (childsize - 1)
					&& groupPosition == (groupsize - 1)) {

			} else if (childPosition == (childsize - 1)
					&& groupPosition < groupsize) {
				childPosition = 0;
				groupPosition = groupPosition + 1;
			} else {
				childPosition = childPosition + 1;
			}

		}

	}

	public void screendisplay(int xmlFile) {

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutimgtext2);
		linearLayout.removeViews(0, linearLayout.getChildCount()); 

		mDataAdapter = DecodeXML.decodexml(xmlFile);
		Item item;
		getcurrentprogress();
		this.progressbar.setProgress(currentprogress);
		for (int itemNum = 0; itemNum < mDataAdapter.pageItem.size(); itemNum++) {
			item = mDataAdapter.pageItem.get(itemNum);
			if ((item) != null) {
				if (item.name == "text") {
					String content = mDataAdapter.text.get(item.id);
					createanddisplayTextView(content); 
				} else if (item.name == "imglink") {
					String src = mDataAdapter.imgLink.get(item.id);
					createanddispplayImageView(src, null); 
				} else if (item.name == "videolink") {

					String videosrc = mDataAdapter.videoLink.get(item.id);

					Item videoface = mDataAdapter.pageItem.get(itemNum + 1);
					itemNum += 1;
					String facesrc = mDataAdapter.imgLink.get(videoface.id);
					// createanddisplayVideoView(videosrc); //����һ��VideoView
					createanddispplayImageView(facesrc, videosrc);

				} else if (item.name == "questionlink") {
					String src = mDataAdapter.questionLink.get(item.id);
					int srcid = getRid(src);
					Intent intentQ = new Intent();
					intentQ.setClass(Text.this, Questionnaire.class);
					Bundle bundle = new Bundle();
					bundle.putInt("srcid", srcid);
					bundle.putInt("groupPosition", groupPosition);
					bundle.putInt("childPosition", childPosition);
					intentQ.putExtra("tag", bundle);
					startActivityForResult(intentQ, 1);
				} else if ((item.name == "ebooklink")) {
					String src = mDataAdapter.ebookLink.get(item.id);
					int srcid = getRid(src);
					Intent intentebook = new Intent();
					intentebook.setClass(Text.this, EbookOnPhoneActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("srcid", srcid);
					bundle.putInt("groupPosition", groupPosition);
					bundle.putInt("childPosition", childPosition);
					intentebook.putExtra("tag", bundle);
					startActivity(intentebook);

				} else if ((item.name == "trendcurve")) {
					displaytrendcurve();
				}

			}
		}

	}

	public void createanddisplayTextView(String content) {
		TextView testText = new TextView(Text.mText);
		testText.setTextSize((float) 20.0);
		testText.setTextColor(Color.GRAY);
		testText.setText(content);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutimgtext2);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearLayout.addView(testText, params);

	}

	public void createanddispplayImageView(String src, String src1) {
		final String videosrc = src1;
		ImageView img = new ImageView(Text.mText);

		Uri uri = Uri.parse(src);

		img.setImageURI(uri);

		img.setAdjustViewBounds(true);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		img.setMaxWidth(screenWidth - (int) (screenWidth * 0.25));
		img.setScaleType(ScaleType.FIT_CENTER);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutimgtext2);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = 17;
		linearLayout.addView(img, lp);
		if (src1 != null) {
			img.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					Intent intentvideo = new Intent();
					intentvideo.setClass(Text.this, videoview.class);
					Bundle bundle = new Bundle();
					bundle.putString("src", videosrc);
					bundle.putInt("groupPosition", groupPosition);
					bundle.putInt("childPosition", childPosition);
					intentvideo.putExtra("tag", bundle);
					startActivityForResult(intentvideo, 1);
				}
			});

		}
	}

	public static int getRid(String src) {
		int id = 0;
		try {
			Field field = R.xml.class.getField(src);
			id = field.getInt(new R.xml());
			// Log.i("question",Integer.toString(id));
		} catch (Exception e) {
			Log.e("icon", e.toString());
		}

		return id;
	}

	public static int getVid(String src) {
		int id = 0;
		try {
			Field field = R.raw.class.getField(src);
			id = field.getInt(new R.raw());
		} catch (Exception e) {
			Log.e("icon", e.toString());
		}

		return id;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) { // requestCode

			if (resultCode == RESULT_OK) { // resultCode

				Bundle bundle = data.getExtras();// Intent

				String tag = bundle.getString("tag");
				System.out.println(tag);
				if (tag.equals("right")) {
					getNextparameter("arrowright");
					String src = "page" + Integer.toString(groupPosition)
							+ Integer.toString(childPosition);
					System.out.println(src);
					int xmlfile = getRid(src);
					screendisplay(xmlfile);

				} else if (tag.equals("left")) {

					getNextparameter("arrowright");
					String src = "page" + Integer.toString(groupPosition)
							+ Integer.toString(childPosition);
					System.out.println(src);
					int xmlfile = getRid(src);
					screendisplay(xmlfile);

				} else {
					returnhome();
				}
			}
		}
	}

	public void returnhome() {
		Intent intent = new Intent();
		Bundle returntooodbundle = new Bundle();
		returntooodbundle.putInt("groupPosition", groupPosition);
		returntooodbundle.putInt("childPosition", childPosition);
		intent.putExtra("ood", returntooodbundle);
		Text.this.setResult(RESULT_OK, intent);
		Text.this.finish();
	}

	public void displaytrendcurve() {

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayoutimgtext2);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		final TestResultView mTestResultView = new TestResultView(this);
		linearLayout.addView(mTestResultView, params);
	}

	public void getcurrentprogress() {
		currentprogress = 0;
		for (int i = 0; i < groupPosition; i++)
			currentprogress = currentprogress + Listitem.chiled[i].length;
		currentprogress = currentprogress + childPosition + 1;
	}

}
