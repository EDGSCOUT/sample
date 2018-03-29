package wsi.mobilesens.dao;

import java.util.List;

import wsi.mobilesens.data.AppInfo;
import wsi.mobilesens.db.AppInfoTable;
import wsi.mobilesens.db.SQLiteTemplate;
import wsi.mobilesens.db.SQLiteTemplate.RowMapper;
import wsi.mobilesens.util.DataBaseAdaptor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AppInfoDAO {
	private static final String TAG = "AppInfoDAO";

	private SQLiteTemplate mSqlTemplate;

	public AppInfoDAO(Context context) {
		mSqlTemplate = new SQLiteTemplate(DataBaseAdaptor.getInstance(context)
				.getDatabaseHelper());
		mSqlTemplate.setPrimaryKey(AppInfoTable.Columns.APPID);
	}

	/**
	 * 
	 * @Title: insertAppInfo
	 * @Description: 插入一条状态
	 * @param appinfo 状态信息
	 * @return
	 */
	public long insertAppInfo(AppInfo appinfo) {
		if (!isExists(appinfo)) {
			return mSqlTemplate.getDb(true).insert(AppInfoTable.TABLE_NAME,
					null, appinfoToContentValues(appinfo));
		} else {
//			updateAppInfo(appinfo);
			Log.v(TAG, appinfo.getId() + " is exists.");
			return -1;
		}
	}

	
	public int insertAppInfos(List<AppInfo> appinfos,String jokeId) {
		int result = 0;
		SQLiteDatabase db = mSqlTemplate.getDb(true);
		try {
			db.beginTransaction();
			for (int i = appinfos.size() - 1; i >= 0; i--) {
				AppInfo appinfo = appinfos.get(i);
				long id=insertAppInfo(appinfo);

				if (-1 == id) {
//					updateAppInfo(appinfo);
					Log.v(TAG, "cann't insert the appinfo : " + appinfo.toString());
				} else {
					++result;
					Log.v(TAG, String.format(
							"Insert a appinfo into database : %s",
							appinfo.toString()));
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return result;
	}

	/**
	 * Find a appinfo by appinfo ID
	 * 
	 * @param appinfoId
	 * @return
	 */
	public AppInfo fetchAppInfo(String appinfoId) {
		return mSqlTemplate.queryForObject(mRowMapper,
				AppInfoTable.TABLE_NAME, null, AppInfoTable.Columns.APPID
						+ " = ? "
						, new String[] { appinfoId }, null, null,
						AppInfoTable.Columns.APPID + " DESC", "1");
	}
	
	public List<AppInfo> fetchAppInfos() {
		return mSqlTemplate.queryForList(mRowMapper,
				AppInfoTable.TABLE_NAME, null,
				null, null, null,
				null, AppInfoTable.Columns.APPID + " DESC", null);
	}
	
	
	

	/**
	 * 
	 * @Title: isExists
	 * @Description: 检查状态是否存在
	 * @param appinfo
	 * @return
	 */
	public boolean isExists(AppInfo appinfo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
				.append(AppInfoTable.TABLE_NAME).append(" WHERE ")
				.append(AppInfoTable.Columns.APPID).append(" = "+appinfo.getId());

		return mSqlTemplate.isExistsBySQL(sql.toString());
	}
	
	
	
	/**
	 * AppInfo -> ContentValues
	 * 
	 * @param appinfo
	 * @param isUnread
	 * @return
	 */
	private ContentValues appinfoToContentValues(AppInfo appinfo) {
		final ContentValues v = new ContentValues();
		v.put(AppInfoTable.Columns.APPID, appinfo.getAppinfoId());
		v.put(AppInfoTable.Columns.CATEGORYID, appinfo.getCategoryId());
		v.put(AppInfoTable.Columns.PACKAGENAME, appinfo.getPackageName());
		return v;
	}

	/**
	 * 接口实现
	 */
	private static final RowMapper<AppInfo> mRowMapper = new RowMapper<AppInfo>() {

		@Override
		public AppInfo mapRow(Cursor cursor) {
			try {
				if(cursor!=null){
					return AppInfo.parseAppInfo(cursor);
				}else{
					return null;
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.getMessage());
				return null;
			}
		}
	};
}
