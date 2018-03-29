package wsi.psy.compute;

import java.util.Calendar;
import java.util.GregorianCalendar;

import wsi.psy.FetchData;
import android.content.Context;

public class PerCalculate {
//	public int day;
//	public int month;
	public static int DAY = 3600*24;
	public int now;
	public int differ;
	public int pre;

	Context mContext;

	public PerCalculate(Context context, int previous) {
		// TODO Auto-generated constructor stub
		now = (int)System.currentTimeMillis()/1000;
		pre = previous;
		differ = now - previous;
		mContext = context;
	}
	public void compare(String str) {
		// TODO Auto-generated method stub
		if ("day".equals(str)) {
			if (differ > DAY  ) {
				calculate();
				pre = now;
			}
		} else if ("week".equals(str)) {
			if (differ > (DAY*7)) {
				calculate();
				pre = now;
			}
		} else if ("month".equals(str)) {
			if (differ > (DAY*30)) {
				calculate();
				pre = now;
			}
		}
	}
/*	public PerCalculate(Context context, int previous) {
		// TODO Auto-generated constructor stub
		Calendar calendar = new GregorianCalendar();
		day = calendar.get(Calendar.DATE);
		month = calendar.get(Calendar.MONTH);
		System.out.println("day:" + day + "month:" + month);
		pre = previous;
		mContext = context;
	}

	public void compare(String str) {
		// TODO Auto-generated method stub
		if ("day".equals(str)) {
			if (day > pre || (day==1 && pre!=1)) {
				calculate();
				 pre = day;
			}
		} else if ("week".equals(str)) {
			if(day < pre && (day + 30) > (pre + 7) ){
				calculate();
				pre = day;
			}
			if (day > (pre + 7)) {
				calculate();
				pre = day;
			}
		} else if ("month".equals(str)) {
			if(month < pre && (month==1 && pre == 12)){
				calculate();
				pre = month;
			}
			if (month > pre) {
				calculate();
				pre = month;
			}
		}
	}*/
	public void calculate(){
		final FetchData fetchData = new FetchData(mContext);
    	fetchData.insert();
	}
	public int getPre() {
		return pre;
	}

	public void setPre(int pre) {
		this.pre = pre;
	}
}
