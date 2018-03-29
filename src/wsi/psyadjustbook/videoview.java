package wsi.psyadjustbook;

import wsi.psy.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.VideoView;

public class videoview extends Activity {


	public int state;
	
	public int groupPosition;
	public int childPosition;
	public String[] group =Listitem.parent;	
	public String[][] children =Listitem.chiled;
	int groupsize=group.length;
	int childsize;
	
	private int childrennum=0;
	private int currentprogress=0;

	int startposition=0;
	int endposition;
	public ImageButton home;
	public ImageButton arrowleft;
	public ImageButton arrowright;
	public ProgressBar progressbar;
	public VideoView mVideoView;
	
	
	public Intent Qintent;
	public  Context mcontext;
	
	public static videoview mvideoview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.videoadapter);
		mvideoview=this;
		
		Qintent=getIntent();  
		initview();
		startvideopl();
		
	}
	public void startvideopl(){
		String src=extparameter();
		
		System.out.println("trans  src is"+src);
		childsize=children[groupPosition].length;
		getchildrennum();          
		this.progressbar.setMax(childrennum);     
		
		getcurrentprogress();       
		this.progressbar.setProgress(currentprogress);
		createanddispplayVideoView( src);
		
	}
	public void initview(){
		this.home=(ImageButton)findViewById(R.id.homevideo);
		this.arrowleft=(ImageButton)findViewById(R.id.arrowleftvideo);
		this.arrowright=(ImageButton)findViewById(R.id.arrowrightvideo);
		this.progressbar=(ProgressBar)findViewById(R.id.progressBarvideo1);
		this.mVideoView=new VideoView(this);
		this.home.setOnTouchListener(hemotouch);
		this.arrowleft.setOnTouchListener(arrowlefttouch);
		this.arrowright.setOnTouchListener(arrowrighttouch);
		
		DisplayMetrics  dm = new DisplayMetrics();   
		getWindowManager().getDefaultDisplay().getMetrics(dm);   
		int screenWidth = dm.widthPixels;
		TableRow.LayoutParams params=new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.width=screenWidth-150;
		params.gravity=Gravity.CENTER_VERTICAL;
		this.progressbar.setLayoutParams(params);
		
	}
	public void getchildrennum(){
		for(int i=0;i<Listitem.chiled.length;i++)
			childrennum=childrennum+Listitem.chiled[i].length;
	}

	public  OnTouchListener hemotouch=new OnTouchListener(){

		public boolean onTouch(View arg0, MotionEvent arg1) {
			returnbaseactivity("home");
			return false;
		}
	};
	
	public  OnTouchListener arrowlefttouch=new OnTouchListener(){
		public boolean onTouch(View arg0, MotionEvent arg1) {
			videoview.this.finish();
			return false;
		}
	};
	
	public  OnTouchListener arrowrighttouch=new OnTouchListener(){

		public boolean onTouch(View arg0, MotionEvent arg1) {
			videoview.this.finish();
			return false;
		}
	};
	
	public void returnbaseactivity( String tag){
	    Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		intent.putExtras(bundle);
		videoview.this.setResult(RESULT_OK, intent);	
		videoview.this.finish(); 

	}
	public String extparameter(){

		Bundle bundle = Qintent.getBundleExtra("tag");  
		String src=bundle.getString("src");
		groupPosition=bundle.getInt("groupPosition");
		Log.i("input groupposition",Integer.toString(groupPosition));
		childPosition=bundle.getInt("childPosition");
		Log.i("input childposition",Integer.toString(childPosition));
		return src;
	}
	public class parameter{
		public int groupPosition;
		public int childPosition;
		public parameter(){
			
		}

		
	}
	
	 public void getcurrentprogress(){
		 currentprogress=0;
		 for(int i=0;i<groupPosition;i++)
			 currentprogress=currentprogress+Listitem.chiled[i].length;
		 currentprogress=currentprogress+childPosition+1; 
	 }
public void createanddispplayVideoView(String src1){
		VideoView img = new VideoView(videoview.mvideoview); 

		Uri uri=Uri.parse(src1);
		
		img.setVideoURI(uri);

	    DisplayMetrics  dm = new DisplayMetrics();   
	    getWindowManager().getDefaultDisplay().getMetrics(dm);   
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.LinearLayoutvideo2);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(    
				LinearLayout.LayoutParams.WRAP_CONTENT,   
				LinearLayout.LayoutParams.WRAP_CONTENT); 
		lp.gravity=17; 
		linearLayout.addView(img, lp);
		MediaController mediaController = new MediaController(videoview.mvideoview);
		img.setMediaController(mediaController);   
	}

}
