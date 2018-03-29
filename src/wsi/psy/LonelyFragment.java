package wsi.psy;

import java.util.List;

import org.achartengine.chartdemo.demo.Chart;

import android.R.menu;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LonelyFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    public static  int num;
    public static Context mContext;
    public List<Double> list;
    public List<String[]> timeList;
    public static LonelyFragment newInstance(Context context, int position) {
    	num = position;
    	mContext = context;
    	LonelyFragment fragment = new LonelyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	FetchData fetchData = new FetchData(mContext, num);
    	list = fetchData.fetchAllData(num);
    	timeList = fetchData.fetchTime();
        Chart achart = new Chart();
		View view = achart.execute(getActivity(), num, list, timeList);
		fetchData.close();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(KEY_CONTENT, mContent);
    }

}
