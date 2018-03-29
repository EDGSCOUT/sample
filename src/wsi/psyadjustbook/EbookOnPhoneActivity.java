package wsi.psyadjustbook;

import java.io.IOException;
import java.lang.reflect.Field;

import wsi.psy.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

public class EbookOnPhoneActivity extends Activity{
	private EbookView mEbookView;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	BookPageFactory pagefactory;
	public static EbookOnPhoneActivity appfile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		appfile=this;
		 DisplayMetrics  dm = new DisplayMetrics();   
		    getWindowManager().getDefaultDisplay().getMetrics(dm);   
		    int screenWidth = dm.widthPixels;
		    int screenHeight=dm.heightPixels;
//		    int screenHeight=(int)(screenWidth*1);
//		    int screenWidth=LayoutParams.FILL_PARENT;
//		    int screenHeight=LayoutParams.FILL_PARENT;
		 mEbookView = new EbookView(this,screenWidth,screenHeight);
		//setContentView(mPageWidget);
		 setContentView(R.layout.ebookonphonactivity);
		LinearLayout ebooklinearlayout2=(LinearLayout)findViewById(R.id.LinearLayoutebookonphone2);
		LayoutParams params=new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		ebooklinearlayout2.addView(mEbookView, params);
		//mPageWidget =(EbookView)findViewById(R.id);


		mCurPageBitmap = Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap
				.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new BookPageFactory(screenWidth,screenHeight);

//		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
//				this.getResources(), R.drawable.bg));   //���ñ���
		
		
		int srcid=getRid("test");
		try {
			pagefactory.openbook(srcid);     //��ȡ������
			pagefactory.onDraw(mCurPageCanvas);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Toast.makeText(this, "hello",
					Toast.LENGTH_SHORT).show();
		}
		mEbookView.setBitmaps(mCurPageBitmap, mCurPageBitmap);

		mEbookView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub
				
				boolean ret=false;
				if (v == mEbookView) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mEbookView.abortAnimation();
						mEbookView.calcCornerXY(e.getX(), e.getY());

						pagefactory.onDraw(mCurPageCanvas);
						if (mEbookView.DragToRight()) {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}						
							if(pagefactory.isfirstPage())return false;
							pagefactory.onDraw(mNextPageCanvas);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if(pagefactory.islastPage())return false;
							pagefactory.onDraw(mNextPageCanvas);
						}
						mEbookView.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}
                 
					 ret = mEbookView.doTouchEvent(e);
					return ret;
				}
				return false;
			}

		});
	}
	
	 public static int getRid(String src){
		 int id=0;		 
			try{
		 			  Field field=R.raw.class.getField(src);
		 			  id= field.getInt(new R.raw());
		 			  Log.i("question",Integer.toString(id));
		 			}catch(Exception e){
		 			  Log.e("icon",e.toString());
		 			}
		 
	 	return id;
	 }
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	       switch (keyCode)
	        {
	            case KeyEvent.KEYCODE_DPAD_CENTER:
	            	 TableLayout menubar=(TableLayout)findViewById(R.id.TableLayoutebookonphone1);
	            	 if(menubar.getVisibility()==8){
	            		 Log.i("num1 is",Integer.toString(menubar.getVisibility()));
	            	 menubar.setVisibility(0);
	            	 }
	            	 else if(menubar.getVisibility()==0){
	            		 Log.i("num2 is",Integer.toString(menubar.getVisibility()));
	            		 menubar.setVisibility(8);
	            	 }
	                break;
	            case KeyEvent.KEYCODE_DPAD_UP:
	                break;
	            case KeyEvent.KEYCODE_DPAD_DOWN:
	                break;
	            case KeyEvent.KEYCODE_DPAD_LEFT:
	                break;
	            case KeyEvent.KEYCODE_DPAD_RIGHT:
	                break;
	            default:
	                break;
	        }
	        return super.onKeyDown(keyCode, event);
	 }

}
