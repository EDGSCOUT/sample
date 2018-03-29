package wsi.psy;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import wsi.mobilesens.MobileSens;
import wsi.psy.compute.Psycompute;
import wsi.psy.recommend.Recommend;
import wsi.psy.setting.SetPreference;
import wsi.psyadjustbook.PsyAdjustActivity;
import wsi.survey.MobileApplication;
import wsi.survey.QuestionMain;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.TabPageIndicator;

public class Main extends FragmentActivity {
    private static final String[] CONTENT = new String[] { "抑郁", "焦虑", "幸福", "孤独"};
    /*i have change the FragmentStatePagerAdapter instead of FragmentPagerAdapter
     * FragmentPagerAdapter has a problem that  at android.support.v4.app.FragmentManagerImpl.saveFragmentBasicState
     * (FragmentManager.java:1576)
     */
/* 	private ChartSQLiteHelper chartSQLiteHelper;
	private SQLiteDatabase sqLiteDatabase;*/
    FragmentStatePagerAdapter adapter;
    ViewPager pager;
    TabPageIndicator indicator;
    public static final String NEW_LIFEFORM_DETECTED =  
	        "com.android.broadcast.NEW_LIFEFORM";
    public List<Double> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.simple_tabs);    
        
        //只是目前使用
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		int value = mPreferences.getInt("pre", 0);
		if(value == 0){
			Editor editor = mPreferences.edit();
			editor.putInt("pre", 1);
			editor.commit();
			FetchData fetchData = new FetchData(this);
			fetchData.insertCSVData();
		}
        //结束
        adapter = new PAdapter(getSupportFragmentManager());
        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
    	if(!isServiceRunning(MobileApplication.mContext, MobileSens.class.getName())){
			Log.e(Main.class.getSimpleName(), "service not running");
			Intent it = new Intent(NEW_LIFEFORM_DETECTED);           
	        sendBroadcast(it); 
		}	
    }
    
	/**
     * 用来判断服务是否后台运行
     * @param context
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext,String className) {
        boolean IsRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList   = activityManager.getRunningServices(30);
       if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                IsRunning = true;
                break;
            }
        }
        return IsRunning ;
    }


	class PAdapter extends FragmentStatePagerAdapter {
        public PAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	System.out.println("position = " + position);
        	switch (position) {
			case 0:
				return GloomyFragment.newInstance(Main.this, position);
			case 1:
				return AnxiousFragment.newInstance(Main.this, position);
			case 2:
				return HappyFragment.newInstance(Main.this, position);
			case 3:
				return LonelyFragment.newInstance(Main.this, position);
			default:
				break;
			}
            return GloomyFragment.newInstance(Main.this, position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
          return CONTENT.length;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	final FetchData fetchData = new FetchData(this);
        switch (item.getItemId()) {
            case R.id.psy_compute:
            	fetchData.setOnCalculateResult(new FetchData.OnCalculateResult() {
					@Override
					public void calculateResult() {
						// TODO Auto-generated method stub
						int array[] = fetchData.getInsertData();
						fetchData.close();
						HashMap<String, Float> feature = new HashMap<String, Float>();
						feature = fetchData.getFeatureMap();
						Iterator iter = feature.entrySet().iterator();
		            	Intent psycompute_intent = new Intent(Main.this, Psycompute.class);
		            	Bundle bundle = new Bundle();
		        		while (iter.hasNext()) {
		        			Entry entry = (Entry) iter.next();
		        			bundle.putString(entry.getKey().toString(), entry.getValue().toString());
		        		}
		        		psycompute_intent.putExtra("bundleData", bundle);
		        		psycompute_intent.putExtra("score1", array[0]);
		            	psycompute_intent.putExtra("score2", array[1]);
		            	psycompute_intent.putExtra("score3", array[2]);
		            	psycompute_intent.putExtra("score4", array[3]);
		            	startActivity(psycompute_intent);
					}
				});
            	fetchData.insert();
            	return true;
            case R.id.psy_table:
            	Intent questionIntent = new Intent(this, QuestionMain.class);
            	startActivity(questionIntent);
                return true;

            case R.id.psy_psy:
            	Intent intent = new Intent(this, PsyAdjustActivity.class);
            	startActivity(intent);
                return true;
                
            case R.id.psy_set:
            	Intent setIntent = new Intent(this, SetPreference.class);
            	startActivity(setIntent);
            	return true;
            case R.id.psy_recommend:
            	int[] a = fetchData.fetchLastData();
            	Intent reIntent = new Intent(this, Recommend.class);
            	reIntent.putExtra("score1", a[0]);
            	reIntent.putExtra("score2", a[1]);
            	reIntent.putExtra("score3", a[2]);
            	reIntent.putExtra("score4", a[3]);
            	startActivity(reIntent);
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
