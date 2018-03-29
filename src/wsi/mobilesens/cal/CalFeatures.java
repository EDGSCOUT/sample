package wsi.mobilesens.cal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import wsi.mobilesens.dao.ActivityDAO;
import wsi.mobilesens.dao.AppInfoDAO;
import wsi.mobilesens.dao.CategoryInfoDAO;
import wsi.mobilesens.dao.SensDAO;
import wsi.mobilesens.data.ActivityData;
import wsi.mobilesens.data.AppInfo;
import wsi.mobilesens.data.CategoryInfo;
import wsi.mobilesens.data.SensData;
import wsi.mobilesens.db.AppInfoTable;
import wsi.mobilesens.db.CategoryInfoTable;
import wsi.mobilesens.util.DataBaseAdaptor;
import wsi.psy.FetchData.OnCalculateResult;
import wsi.survey.Upload;
import wsi.survey.util.UpdateManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalFeatures{

	public float[] predictScores =new float[4];
	int days = 0;
	public HashMap<String, Float> features = new HashMap<String, Float>();
	public HashMap<String, Float> getFeatures() {
		return features;
	}

	Button calButton;
	TextView resultTextView;
	SensDAO sensDAO;
	Context context;
	OnCalculateResult onCalculateResult;
	public void setOnCalculateResult(OnCalculateResult onCalculateResult){
		this.onCalculateResult = onCalculateResult;
	}
	public CalFeatures(Context context){
		// TODO Auto-generated method stub
		this.context = context;
	
		
	}
	
	public void calculate(){
		PageTask task = new PageTask(this.context);
		task.execute();
	}
	
	class PageTask extends AsyncTask<String, Integer, String> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog progressDialog;

		public PageTask(Context context) {
			progressDialog = new ProgressDialog(context);
		}

		@Override
		protected String doInBackground(String... params) {
//			Upload.doPostAll();
			sensDAO = new SensDAO(context);
			days = getDay();
			Log.e("计算天数", String.valueOf(days));
			if (!isSaveTextToDB()) {
				saveTextToDB();
			}
			beginToCalculate();
			return "";
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// 此处的result参数是来自doInBackground的返回值
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if(days==0){
				Toast.makeText(context, "暂无数据", Toast.LENGTH_LONG)
				.show();
			}
			onCalculateResult.calculateResult();
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(context, "正在处理中……",
					"请等待", true, false);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更新进度
		}

	}

	public void beginToCalculate() {
		if (days == 0) {
			
			return;
		}
		// 计算activity
		{
//			calculateActivity();
			getPackageColumnHashMap();
			getActivityInfoFromDB();
		}

		// 计算短信
		try {
			calculateSMS();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 电话
		try {
			calculateCall();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 计算社交软件
		calculateSocialApp();

		// 计算other log
		calculateOther();

		// 计算完毕，计算公式
		calculateFinalResult();
	}

	/**
	 * 计算最终公式
	 */
	public void calculateFinalResult() {
		Iterator iter = features.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			Log.e("hello: " + entry.getKey().toString(), entry.getValue().toString());
		}
		
		CalResult calResult = new CalResult();
		calResult.setHashMap(features);
		
		predictScores[0] = (float)calResult.getCES();
		predictScores[1] = (float)calResult.getIAS();
		predictScores[2] = (float)calResult.getPWB();
		predictScores[3] = (float)calResult.getUCLAAL();
		
	}

	/**
	 * 判断是否已经将txt中数据写入本地数据库
	 * 
	 * @return
	 */
	public boolean isSaveTextToDB() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("text",
				0);
		return sharedPreferences.getBoolean("save_text", false);
	}

	private void saveTextPreference() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("text",
				0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("save_text", true);
		editor.commit();
	}

	/**
	 * 预处理，将txt中数据存储数据库中
	 */
	public void saveTextToDB() {

		String[] fileNames = new String[] { "appinfo", "categoryinfo" };
		for (int i = 0; i < fileNames.length; i++) {
			String[] lineText = new String[3];
			InputStream inputStream;
			try {
				inputStream = context.getAssets().open(
						"appinfo/" + fileNames[i] + ".txt");
				try {
					InputStreamReader read = new InputStreamReader(inputStream,
							"UTF-8");// 考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxtStr = null;
					while ((lineTxtStr = bufferedReader.readLine()) != null) {
						lineText = lineTxtStr.split("\\t");
						if (i == 0) {
							AppInfoDAO appInfoDAO = new AppInfoDAO(context);
							appInfoDAO.insertAppInfo(new AppInfo(Integer
									.valueOf(lineText[0]), lineText[1], Integer
									.valueOf(lineText[2])));
						} else {
							CategoryInfoDAO categoryInfoDAO = new CategoryInfoDAO(
									context);
							categoryInfoDAO
									.insertCategoryInfo(new CategoryInfo(
											Integer.valueOf(lineText[0]),
											lineText[1], lineText[2]));
						}
					}
					read.close();
				} catch (Exception e) {
					System.out.println("读取文件内容出错");
					e.printStackTrace();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		saveTextPreference();
	}
//
//	/**
//	 * 将activity数据拆分存储到数据库中
//	 */
//	private void calculateActivity() {
//		SensDAO sensDAO = new SensDAO(context);
//		ActivityDAO activityDAO = new ActivityDAO(context);
//		List<SensData> sensDatas = sensDAO.fetchSensDatas("activitylog");
//		for (int i = 0; i < sensDatas.size(); i++) {
//			// Log.e("actiivty", sensDatas.get(i).getRecord());
//			try {
//				ArrayList<ActivityData> activityDatas = getActivityList(sensDatas
//						.get(i).getRecord());
//				activityDAO.insertActivityDatas(activityDatas);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			;
//		}
//	}

//	/*
//	 * 将activity log拆分存入数据库中
//	 */
//	private ArrayList<ActivityData> getActivityList(String message)
//			throws JSONException {
//
//		if (message.equals("[]")) { // 过滤掉内容为空的记录
//			return new ArrayList<ActivityData>();
//		}
//		JSONObject jsonObject = new JSONObject(message);
//		ArrayList<ActivityData> activityDatas = new ArrayList<ActivityData>();
//		JSONObject jsonobject = jsonObject.getJSONObject("data");
//		for (Iterator iter = jsonobject.keys(); iter.hasNext();) {
//			String time = (String) iter.next();
//			JSONObject innerJsonobject = jsonobject.getJSONObject(time);
//			ActivityData activityData = new ActivityData(
//					innerJsonobject.getString("Activity"), Long.parseLong(time));
//			activityDatas.add(activityData);
//			String split[] = activityData.getPackageName().split("/");
//			activityData.setPackageName(split[0]);
//		}
//		return activityDatas;
//	}

	/**
	 * 计算 包名跟列名对应关系
	 */
	HashMap<String, String> packHashMap = new HashMap<String, String>();

	public void getPackageColumnHashMap() {
		String sql = "select column_name,package_name from categoryinfo,appinfo "
				+ " where categoryinfo.category_id=appinfo.category_id ";
		Cursor c = DataBaseAdaptor.getInstance(context).getDb(true)
				.rawQuery(sql, null);
		try {
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				packHashMap
						.put(c.getString(c
								.getColumnIndex(AppInfoTable.Columns.PACKAGENAME)),
								c.getString(c
										.getColumnIndex(CategoryInfoTable.Columns.COLUMNNAME)));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 从拆分之后的数据库中读取activity数据
	 */
	HashMap<String, Integer> columnNumHashMap = new HashMap<String, Integer>();

	private void getActivityInfoFromDB() {
		ActivityDAO activityDAO = new ActivityDAO(context);
		List<ActivityData> activityDatas = activityDAO.fetchActivityDatas();
		for (int i = 0; i < activityDatas.size(); i++) {
			String packageName = activityDatas.get(i).getPackageName();
			String columnName = packHashMap.get(packageName);
			if (columnNumHashMap.containsKey(columnName)) {
				columnNumHashMap.put(columnName,
						columnNumHashMap.get(columnName) + 1);
			} else {
				columnNumHashMap.put(columnName, 0);
			}
		}
		Iterator iter = columnNumHashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			if(entry==null){
				continue;
			}
			if(entry.getKey()==null){
				continue;
			}
			features.put(
					entry.getKey().toString(),
					(float) (columnNumHashMap.get(entry.getKey().toString()) * 1.0 / days));
		}

		// 数量在columnNumHashmap中，以hashmap的形式存放，可以通过迭代器方式遍历
	}

	// 对短信的处理
	private void calculateSMS() throws JSONException {
		List<SensData> sensDatas = sensDAO.fetchSensDatas("smslog");
		String result = "smslog的数量是：" + sensDatas.size();
		int receiveCount = 0;
		int sendCount = 0;
		for (int i = 0; i < sensDatas.size(); i++) {
			SensData sensData = sensDatas.get(i);
			String record = sensData.getRecord();
			JSONObject jsonObject = new JSONObject(record);
			JSONObject dataJsonObject = jsonObject.getJSONObject("data");
			if (dataJsonObject.getString("type").equals("receive")) {
				receiveCount++;
			} else {
				sendCount++;
			}
		}
		String result2 = "发出的短信数量：" + sendCount + ",收到的短信数量：" + receiveCount;
		features.put("smslogOutAvg", (float) (sendCount) / days);
		//features.put("smslogReceiveAvg", (float) (receiveCount) / days);
	}

	// 对电话的处理
	private void calculateCall() throws JSONException {
		List<SensData> sensDatas = sensDAO.fetchSensDatas("calllog");
		String result = "calllog的数量是：" + sensDatas.size();
		int receiveCount = 0;
		int outCount = 0;
		for (int i = 0; i < sensDatas.size(); i++) {
			SensData sensData = sensDatas.get(i);
			String record = sensData.getRecord();
			JSONObject jsonObject = new JSONObject(record);
			JSONObject dataJsonObject = jsonObject.getJSONObject("data");
			if (dataJsonObject.getString("calldirection").equals("outgoing")) {
				receiveCount++;
			} else {
				outCount++;
			}
		}
		String result2 = "打出电话的数量：" + outCount + ",接到电话的数量：" + receiveCount;
		features.put("calllogOutAvg", (float) (outCount) / days);
		//features.put("calllogReceiveAvg", (float) (receiveCount) / days);
	}

	private void calculateSocialApp() {
		ActivityDAO activityDAO = new ActivityDAO(context);
		String[] packageNames = new String[] { "com.renren.mobile.android",
				"com.tencent.mobileqq", "com.tencent.mm", "com.sina.weibo",
				"cn.com.fetion" };
		String[] featureNames = new String[] { "renren", "qq", "weixin",
				"weibo", "fetion" };
		for (int i = 0; i < packageNames.length; i++) {
//			List<ActivityData> lists = activityDAO
//					.fetchActivityDatas(packageNames[i]);
			int count = activityDAO.getSensDatasCount(packageNames[i]);
			features.put(featureNames[i], (float) count / days);
		}
	}

	/*
	 * 其他的方式可以通过此方式遍历出来此处需要填写完所有信息
	 */
	private void calculateOther() {
		String[] categorys = new String[] { "apklog", "contactlog",
				"gpslog", "headssetlog", "screenlog", "powerlog",
				"wallpaperchangedlog" };
		for (int j = 0; j < categorys.length; j++) {
//			List<SensData> sensDatas = sensDAO.fetchSensDatas(categorys[j]);
			int count = sensDAO.getSensDatasCount(categorys[j]);
			features.put(categorys[j].replace("log", "logAVG"),
					(float) count / getDay());
		}
	}

	/**
	 * 获取天数
	 * 
	 * @return
	 */
	private int getDay() {
//		String time2 = "1989-1-1 12:20:23";
//		time2 = time2.substring(0,time2.indexOf(" "));
		List<SensData> sensDatas = sensDAO.fetchSensDatas();
		Set<String> sets = new HashSet<String>();
		for (int i = 0; i < sensDatas.size(); i++) {
			SensData sensData = sensDatas.get(i);
			String time = sensData.getDatetime();
			time = time.substring(0, time.indexOf(" "));
			System.out.println("GET DAT :" + time);
			sets.add(time);
		}
		return sets.size();
	}

}
