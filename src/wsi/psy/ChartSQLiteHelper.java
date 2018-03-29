package wsi.psy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChartSQLiteHelper extends SQLiteOpenHelper {

	public static final String CHARTDB_NAME = "chart_db";
	public static final String CHARTDB_TABLE_NAME = "chart_table";
	public static final String CHARTDB_ID = "_id";
	public static final String CHARTDB_DATATIME = "dtime";
	public static final String CHARTDB_ATTR_NAME = "attr";
	public static final String CHARTDB_ATTR_SCORE = "score";
	public static final String CHARTDB_BOOLEAN = "pre_hand";
	
	public ChartSQLiteHelper(Context context) {
		super(context, CHARTDB_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String chartSql = "create table if not exists "
				+ CHARTDB_TABLE_NAME + "("
				+ CHARTDB_ID + " integer primary key autoincrement, "
				+ CHARTDB_DATATIME + " integer, "
				+ CHARTDB_ATTR_NAME + " varchar(50), "
				+ CHARTDB_ATTR_SCORE + " integer, "
				+ CHARTDB_BOOLEAN + " integer);";
		db.execSQL(chartSql);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
