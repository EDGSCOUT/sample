package wsi.psy.compute;

import wsi.psy.Main;
import wsi.psy.R;
import wsi.psy.recommend.Recommend;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DisplayRecommendFragment extends Fragment {
    public static Context mContext;
    static int gloomyScore;
    static int anxiousScore;
    static int happyScore;
    static int lonelyScore;
    public static DisplayRecommendFragment newInstance(Context context, int score1, int score2, int score3, int score4) {
    	mContext = context;
    	gloomyScore = score1;
    	anxiousScore = score2;
    	happyScore = score3;
    	lonelyScore = score4;
    	DisplayRecommendFragment fragment = new DisplayRecommendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	View root = inflater.inflate(R.layout.recommendbtn, container, false);
    	Button button = (Button)root.findViewById(R.id.button_recommend);
    	button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), Recommend.class);
        		intent.putExtra("score1", gloomyScore);
            	intent.putExtra("score2", anxiousScore);
            	intent.putExtra("score3", happyScore);
            	intent.putExtra("score4", lonelyScore);
				getActivity().startActivity(intent);
				getActivity().setResult(Activity.RESULT_OK);
			}
		});
    	Button button1 = (Button)root.findViewById(R.id.button_main);
    	button1.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			getActivity().startActivity(new Intent(getActivity(),wsi.psy.Main.class));
    			getActivity().setResult(Activity.RESULT_OK);
    			getActivity().finish();
    		}
    	});
    	return root;
    }

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
