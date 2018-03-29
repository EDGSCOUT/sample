package wsi.psy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import wsi.mobilesens.cal.CalFeatures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FetchData {

	public interface OnCalculateResult {
		void calculateResult();
	}

	public static final String[] attr = { "CES-D_Phone", "IAS_Phone",
			"PWB_Phone", "UCLAAl_Phone" };
	private ChartSQLiteHelper chartSQLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	public boolean isStop = false;
	List<Double> list;
	private int num;
	public int[] insertData = { 0, 0, 0, 0 };
	public int[] lastData = { 0, 0, 0, 0 };
	public HashMap<String, Float> featureMap = new HashMap<String, Float>();

	public int[] getInsertData() {
		return this.insertData;
	}

	Context context;

	public FetchData(Context con) {
		// TODO Auto-generated constructor stub
		this.context = con;
		chartSQLiteHelper = new ChartSQLiteHelper(context);
		sqLiteDatabase = chartSQLiteHelper.getWritableDatabase();
	}

	OnCalculateResult onCalculateResult;

	public void setOnCalculateResult(OnCalculateResult onCalculateResult) {
		this.onCalculateResult = onCalculateResult;
	}

	public FetchData(Context context, int position) {
		num = position;
		this.context = context;
		chartSQLiteHelper = new ChartSQLiteHelper(context);
		sqLiteDatabase = chartSQLiteHelper.getWritableDatabase();
		// fetchAllData(num);
	}

	public void insert() {

		// 预测抑郁、焦虑、幸福、孤独四个维度的分数
		final CalFeatures calFeatures = new CalFeatures(context);
		calFeatures.setOnCalculateResult(new OnCalculateResult() {

			@Override
			public void calculateResult() {
				// TODO Auto-generated method stub
				for (int i = 0; i < 4; i++) {
					ContentValues cv = new ContentValues();
					cv.put(ChartSQLiteHelper.CHARTDB_ATTR_NAME, attr[i]);
					cv.put(ChartSQLiteHelper.CHARTDB_ATTR_SCORE,
							calFeatures.predictScores[i]);
					cv.put(ChartSQLiteHelper.CHARTDB_BOOLEAN, 0);
					sqLiteDatabase.insert(ChartSQLiteHelper.CHARTDB_TABLE_NAME,
							null, cv);
					insertData[i] = (int) calFeatures.predictScores[i];
					Log.e("PREDICT SCORES",
							"预测值为："
									+ String.valueOf(calFeatures.predictScores[i]));
					System.out.println("produce data : " + insertData[i]);
				}
				featureMap = calFeatures.features;

				onCalculateResult.calculateResult();
			}
		});
		calFeatures.calculate();
	}

	public HashMap<String, Float> getFeatureMap() {
		return featureMap;
	}

	public void insert(String strAttr, int score) {
		ContentValues cv = new ContentValues();
		// cv.put(ChartSQLiteHelper.CHARTDB_ATTR_NAME, attr[rand.nextInt(4)]);
		cv.put(ChartSQLiteHelper.CHARTDB_ATTR_NAME, strAttr);
		cv.put(ChartSQLiteHelper.CHARTDB_ATTR_SCORE, score);
		cv.put(ChartSQLiteHelper.CHARTDB_BOOLEAN, 0);
		sqLiteDatabase.insert(ChartSQLiteHelper.CHARTDB_TABLE_NAME, null, cv);
		System.err.println("=====================================================Successfull");
	}

	public List<Double> fetchAllData(int position) {
		list = new ArrayList();
		Cursor cursor = sqLiteDatabase.query(
				ChartSQLiteHelper.CHARTDB_TABLE_NAME,
				new String[] { ChartSQLiteHelper.CHARTDB_ATTR_SCORE },
				ChartSQLiteHelper.CHARTDB_ATTR_NAME + "=\"" + attr[position]
						+ "\"", null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (cursor.getCount() > 0) {
				int value = cursor.getInt(cursor
						.getColumnIndex(ChartSQLiteHelper.CHARTDB_ATTR_SCORE));
				list.add((double) value);
			}
		}
		while (cursor.moveToNext()) {
			int value = cursor.getInt(cursor
					.getColumnIndex(ChartSQLiteHelper.CHARTDB_ATTR_SCORE));
			list.add((double) value);
		}
		// System.out.println("fetchdata:" + list.toString());
		cursor.close();
		return list;
	}

	public int[] fetchLastData() {
		for (int i = 0; i < lastData.length; i++) {
			Cursor cursor = sqLiteDatabase.query(
					ChartSQLiteHelper.CHARTDB_TABLE_NAME,
					new String[] { ChartSQLiteHelper.CHARTDB_ATTR_SCORE },
					ChartSQLiteHelper.CHARTDB_ATTR_NAME + "=\"" + attr[i]
							+ "\"", null, null, null, null);
			if (cursor != null) {
				if(cursor.moveToLast()){
						int value = cursor.getInt(cursor
						.getColumnIndex(ChartSQLiteHelper.CHARTDB_ATTR_SCORE));
						lastData[i] = value;
				}
			}
			cursor.close();
		}
//		System.out.println("lastdata222: " + lastData[0]+ lastData[1]+ lastData[2]+ lastData[3]);
		return lastData;
	}

	class ThreadTest implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!isStop) {
				insert();
			}
		}
	}

	public void close() {
		sqLiteDatabase.close();
		chartSQLiteHelper.close();
	}
	
	
	public List<String[]> fetchTime() {
		List<String[]> list = new ArrayList();
		Cursor cursor = sqLiteDatabase.query(
				ChartSQLiteHelper.CHARTDB_TABLE_NAME,
				new String[] { ChartSQLiteHelper.CHARTDB_DATATIME },
				null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (cursor.getCount() > 0) {
				String value = cursor.getString(cursor
						.getColumnIndex(ChartSQLiteHelper.CHARTDB_DATATIME));
				list.add(value.split("-"));
				System.out.println("fetchTime111 " + value);
			}
		}
		while (cursor.moveToNext()) {
			if(cursor.moveToNext()){
				if(cursor.moveToNext()){
					if(cursor.moveToNext()){
						String value = cursor.getString(cursor
								.getColumnIndex(ChartSQLiteHelper.CHARTDB_DATATIME));
						list.add(value.split("-"));
					}
				}
			}
			
		}
		System.out.println("listsize:"+ list.size());
		// System.out.println("fetchdata:" + list.toString());
		cursor.close();
		return list;
	}
	
	
// 演示使用
	public void insertCSVData(){
		ContentValues cv = new ContentValues();
		// cv.put(ChartSQLiteHelper.CHARTDB_ATTR_NAME, attr[rand.nextInt(4)]);

		try{
			File csv = new File("/sdcard/file.csv");
			BufferedReader br = new BufferedReader(new FileReader(csv));
			String line ="";
			while ((line = br.readLine())!=null){
				StringTokenizer st = new StringTokenizer(line, ",");
				while(st.hasMoreTokens()){
					String str = st.nextToken();
					
					for(int i = 0; i<4; i++){
						cv.put(ChartSQLiteHelper.CHARTDB_DATATIME, str);
						if(i == 0){
							cv.put(ChartSQLiteHelper.CHARTDB_ATTR_NAME, "CES-D_Phone");
						}
						else if(i==1){
							cv.put(ChartSQLiteHelper.CHARTDB_ATTR_NAME, "IAS_Phone");
						}
						else if(i == 2){
							cv.put(ChartSQLiteHelper.CHARTDB_ATTR_NAME, "PWB_Phone");
						}
						else if(i == 3){
							cv.put(ChartSQLiteHelper.CHARTDB_ATTR_NAME, "UCLAAl_Phone");
						}
						cv.put(ChartSQLiteHelper.CHARTDB_ATTR_SCORE, st.nextToken());
						cv.put(ChartSQLiteHelper.CHARTDB_BOOLEAN, 0);
						sqLiteDatabase.insert(ChartSQLiteHelper.CHARTDB_TABLE_NAME, null, cv);
					}
					
				}
			}
			br.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
