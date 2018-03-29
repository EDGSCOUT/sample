package wsi.psy.compute;

import wsi.psy.Main;
import wsi.psy.R;
import wsi.psy.recommend.Recommend;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PsycomputeDisplay extends FragmentActivity {
	FragmentStatePagerAdapter adapter;
	ViewPager pager;
	PageIndicator mIndicator;
	int score1;
	int score2;
	int score3;
	int score4;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.psycompute_display);
		Intent intent = getIntent();
		score1 = intent.getIntExtra("score1", 0);
		score2 = intent.getIntExtra("score2", 0);
		score3 = intent.getIntExtra("score3", 0);
		score4 = intent.getIntExtra("score4", 0);
//		System.out.println("Display: " + score1);
		adapter = new DisplayAdapter(getSupportFragmentManager());
		pager = (ViewPager)findViewById(R.id.pager0);
		pager.setAdapter(adapter);
		
		mIndicator = (CirclePageIndicator)findViewById(R.id.indicator0);
	    mIndicator.setViewPager(pager);
	}
	
	class DisplayAdapter extends FragmentStatePagerAdapter{

		public DisplayAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch(arg0){
			case 0:
				return DisplayGloomyFragment.newInstance(PsycomputeDisplay.this, score1);
			case 1:
				return DisplayAnxiousFragment.newInstance(PsycomputeDisplay.this, score2);
			case 2:
				return DisplayHappyFragment.newInstance(PsycomputeDisplay.this, score3);
			case 3:
				return DisplayLonelyFragment.newInstance(PsycomputeDisplay.this, score4);
			case 4:
				return DisplayRecommendFragment.newInstance(PsycomputeDisplay.this, score1, score2, score3, score4);
			default:
				break;
			}
			return DisplayGloomyFragment.newInstance(PsycomputeDisplay.this, score1);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
    	getMenuInflater().inflate(R.menu.recommend, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
    	switch(item.getItemId()){
    	case R.id.psy_recommend:
    		Intent recommend = new Intent(this, Recommend.class);
        	startActivity(recommend);
        	setResult(RESULT_OK);
    		return true;
    	case R.id.psy_main:
    		Intent main = new Intent(this, Main.class);
        	startActivity(main);
        	setResult(RESULT_OK);
    		return true;
    		
    	}
		return super.onOptionsItemSelected(item);
	}

	
}
