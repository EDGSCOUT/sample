package wsi.psy.setting;

import java.util.ArrayList;

import wsi.psy.R;
import wsi.psy.R.id;
import wsi.psy.R.layout;
import wsi.survey.util.PreferenceUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
public class Setting extends Activity {
    
    private ListView lv;
    private SetAdapter mAdapter;
    private ArrayList<String> list;
    private static final String[] SET = {
    	"应用程序",
    	"安装包信息",
    	"呼叫信息",
    	"相机按键",
    	"配置信息",
    	"联系人信息",
    	"日期时间改变信息",
    	"GPS信息",
    	"耳机信息",
    	"充电信息",
    	"开关机信息",
    	"屏幕信息",
    	"服务程序信息",
    	"短信息",
    	"壁纸信息",
    	"每天计算",
    	"每周计算",
    	"每月计算"
    };
//    private int checkNum; // 记录选中的条目数量
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        /* 实例化各个控件 */
        lv = (ListView) findViewById(R.id.setting_listView);
        list = new ArrayList<String>();
        // 为Adapter准备数据
        initDate();
        // 实例化自定义的MyAdapter
        mAdapter = new SetAdapter(list, this);
        // 绑定Adapter
        lv.setAdapter(mAdapter);
        // 绑定listView的监听器
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤　　　　　　　　　　
            	ViewHolder holder = (ViewHolder) arg1.getTag();                // 改变CheckBox的状态
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来
                SetAdapter.getIsSelected().put(arg2, holder.cb.isChecked()); 
                PreferenceUtil.saveData(String.valueOf(arg2), holder.cb.isChecked());
                System.out.println("Setting .." + String.valueOf(arg2) + holder.cb.isChecked());
                // 调整选定条目
/*                if (holder.cb.isChecked() == true) {
                    checkNum++;
                } else {
                    checkNum--;
                }*/
                
            }
        });
    }
    // 初始化数据
    private void initDate() {
        for (int i = 0; i < SET.length; i++) {
            list.add(SET[i]);
        }
//        for (int i = 0; i < 15; i++) {
//        	if (PreferenceUtil.getBooleanData(String.valueOf(i))) {
//				ViewHolder vHolder = (ViewHolder)SetAdapter.getView().getTag();
//			}
//        }
    }
    // 刷新listview和TextView的显示
    private void dataChanged() {
        // 通知listView刷新
        mAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
    }
    
}
