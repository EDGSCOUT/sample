package wsi.mobilesens.dao;

import java.util.List;

import wsi.mobilesens.data.ActivityData;
import wsi.mobilesens.db.ActivityDataTable;
import wsi.mobilesens.db.SQLiteTemplate;
import wsi.mobilesens.db.SensDataTable;
import wsi.mobilesens.db.SQLiteTemplate.RowMapper;
import wsi.mobilesens.util.DataBaseAdaptor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ActivityDAO {
	private static final String TAG = "ActivityDAO";

	private SQLiteTemplate mSqlTemplate;

	public ActivityDAO(Context context) {
		mSqlTemplate = new SQLiteTemplate(DataBaseAdaptor.getInstance(context)
				.getDatabaseHelper());
		mSqlTemplate.setPrimaryKey(ActivityDataTable.Columns.ID);
	}

	/**
	 * 
	 * @Title: insertActivityData
	 * @Description: 插入一条状态
	 * @param activityData 状态信息
	 * @return
	 */
	public long insertActivityData(ActivityData activityData) {
		if (!isExists(activityData)) {
			return mSqlTemplate.getDb(true).insert(ActivityDataTable.TABLE_NAME,
					null, activityDataToContentValues(activityData));
		} else {
//			updateActivityData(activityData);
			Log.v(TAG, activityData.getId() + " is exists.");
			return -1;
		}
	}

	
	public int insertActivityDatas(List<ActivityData> activityDatas) {
		int result = 0;
		SQLiteDatabase db = mSqlTemplate.getDb(true);
		try {
			db.beginTransaction();
			for (int i = activityDatas.size() - 1; i >= 0; i--) {
				ActivityData activityData = activityDatas.get(i);
				long id=insertActivityData(activityData);

				if (-1 == id) {
//					updateActivityData(activityData);
					Log.v(TAG, "cann't insert the activityData : " + activityData.toString());
				} else {
					++result;
					Log.v(TAG, String.format(
							"Insert a activityData into database : %s",
							activityData.toString()));
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return result;
	}

	/**
	 * Find a activityData by activityData ID
	 * 
	 * @param activityDataId
	 * @return
	 */
	public ActivityData fetchActivityData(String id) {
		return mSqlTemplate.queryForObject(mRowMapper,
				ActivityDataTable.TABLE_NAME, null, ActivityDataTable.Columns.ID
						+ " = ? "
						, new String[] { id }, null, null,
						ActivityDataTable.Columns.ID + " DESC", "1");
	}
	
	public List<ActivityData> fetchActivityDatas() {
		return mSqlTemplate.queryForList(mRowMapper,
				ActivityDataTable.TABLE_NAME, null,
				null, null, null,
				null, ActivityDataTable.Columns.ID + " DESC", null);
	}
	
	public List<ActivityData> fetchActivityDatas(String packageName) {
		return mSqlTemplate.queryForList(mRowMapper,
				ActivityDataTable.TABLE_NAME, null,
				ActivityDataTable.Columns.ACTIVITY + " = ? ",  new String[] { packageName }, null,
				null, ActivityDataTable.Columns.ID + " DESC", null);
	}
	
	
	public int getActivityDatasCount(){
		return mSqlTemplate.getCount(ActivityDataTable.TABLE_NAME);
	}
	
	public int getSensDatasCount(String packageName){
		return mSqlTemplate.getCount(ActivityDataTable.TABLE_NAME,ActivityDataTable.Columns.ACTIVITY,packageName);
	}
	
	
	
	

	/**
	 * 
	 * @Title: isExists
	 * @Description: 检查状态是否存在
	 * @param activityData
	 * @return
	 */
	public boolean isExists(ActivityData activityData) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
				.append(ActivityDataTable.TABLE_NAME).append(" WHERE ")
				.append(ActivityDataTable.Columns.ID).append(" = "+activityData.getId());

		return mSqlTemplate.isExistsBySQL(sql.toString());
	}
	
	
	
	/**
	 * ActivityData -> ContentValues
	 * 
	 * @param activityData
	 * @param isUnread
	 * @return
	 */
	private ContentValues activityDataToContentValues(ActivityData activityData) {
		final ContentValues v = new ContentValues();
		v.put(ActivityDataTable.Columns.TIME, activityData.getTime());
		v.put(ActivityDataTable.Columns.ACTIVITY, activityData.getPackageName());
		return v;
	}

	/**
	 * 接口实现
	 */
	private static final RowMapper<ActivityData> mRowMapper = new RowMapper<ActivityData>() {

		@Override
		public ActivityData mapRow(Cursor cursor) {
			try {
				if(cursor!=null){
					return ActivityData.parseActivityData(cursor);
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
