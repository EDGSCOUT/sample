package wsi.mobilesens.data;

import java.text.ParseException;

import wsi.mobilesens.db.ActivityDataTable;
import wsi.mobilesens.db.AppInfoTable;

import android.database.Cursor;
import android.util.Log;

/**
 * ActivityData 实体类
 * 
 * @author qinning
 * 
 */
public class ActivityData implements java.io.Serializable {
	private static final long serialVersionUID = 1608000492860584608L;
	private int id;
	private String packageName;
	private long time;

	public ActivityData() {

	}

	

	public ActivityData(String activityName, long time) {
		super();
		this.packageName = activityName;
		this.time = time;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getPackageName() {
		return packageName;
	}



	public void setPackageName(String activityName) {
		this.packageName = activityName;
	}



	public long getTime() {
		return time;
	}



	public void setTime(long time) {
		this.time = time;
	}



	public static ActivityData parseActivityData(Cursor cursor) {
		if (null == cursor || 0 == cursor.getCount()) {
			Log.w("Joke.ParseJoke",
					"Cann't parse Cursor, bacause cursor is null or empty.");
			return null;
		}
		ActivityData activityData = new ActivityData();
		activityData.id =cursor.getInt(cursor.getColumnIndex(ActivityDataTable.Columns.ID));
		activityData.packageName = cursor.getString(cursor.getColumnIndex(ActivityDataTable.Columns.ACTIVITY));
		activityData.time = cursor.getLong(cursor.getColumnIndex(ActivityDataTable.Columns.TIME));		
		return activityData;
	}

}
