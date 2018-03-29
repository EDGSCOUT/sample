package wsi.mobilesens.data;

import java.text.ParseException;

import wsi.mobilesens.db.AppInfoTable;

import android.database.Cursor;
import android.util.Log;

/**
 * AppInfo 实体类
 * 
 * @author qinning
 * 
 */
public class AppInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1608000492860584608L;
	private String id;
	private int appinfoId;
	private int categoryId;
	private String packageName;

	public AppInfo() {

	}

	public AppInfo(int appinfoId, String packageName, int categoryId) {
		super();
		this.appinfoId = appinfoId;
		this.categoryId = categoryId;
		this.packageName = packageName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAppinfoId() {
		return appinfoId;
	}

	public void setAppinfoId(int appinfoId) {
		this.appinfoId = appinfoId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public static AppInfo parseAppInfo(Cursor cursor) {
		if (null == cursor || 0 == cursor.getCount()) {
			Log.w("Joke.ParseJoke",
					"Cann't parse Cursor, bacause cursor is null or empty.");
			return null;
		}
		AppInfo appInfo = new AppInfo();
		appInfo.appinfoId =cursor.getInt(cursor.getColumnIndex(AppInfoTable.Columns.APPID));
		appInfo.categoryId = cursor.getInt(cursor.getColumnIndex(AppInfoTable.Columns.CATEGORYID));
		appInfo.packageName = cursor.getString(cursor.getColumnIndex(AppInfoTable.Columns.PACKAGENAME));		
		return appInfo;
	}

}
