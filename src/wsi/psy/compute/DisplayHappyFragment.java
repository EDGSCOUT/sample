package wsi.psy.compute;

import wsi.survey.result.GConstant;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class DisplayHappyFragment extends Fragment {
    public static Context mContext;
    public static int mscore;
    public static DisplayHappyFragment newInstance(Context context, int score) {
    	System.out.println("DisplayAnxiousFragment.....");
    	mContext = context;
    	mscore = score;
    	DisplayHappyFragment fragment = new DisplayHappyFragment();
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
/*    	
    	FetchData fetchData = new FetchData(mContext, num);
    	list = fetchData.fetchAllData(num);
        Chart achart = new Chart();
		View view = achart.execute(getActivity(), num, list);
		fetchData.close();*/
    	TextView text = new TextView(getActivity());
    	text.setText(" ");
    	text.setTextColor(Color.BLACK);
		text.append("幸福感得分：" + mscore + "\n" + GConstant.strings[2]);
		text.append( Html.fromHtml("<b><font color=#FF0000> \"自助\"</font></b>（在首页菜单栏中）进行自助调节。预测结果仅供参考，如果想了解准确分数，可以填写"));
		text.append( Html.fromHtml("<b><font color=#FF0000> \"测评\"</font></b>（在首页菜单栏中）"));
        text.setTextSize(13 * getResources().getDisplayMetrics().density);
//        text.setPadding(20, 20, 20, 20);
    	
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//        layout.setGravity(Gravity.CENTER);
        layout.addView(text);
        layout.setBackgroundColor(Color.WHITE);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
