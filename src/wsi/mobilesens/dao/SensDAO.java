package wsi.mobilesens.dao;

import java.util.List;

import wsi.mobilesens.data.SensData;
import wsi.mobilesens.db.SQLiteTemplate;
import wsi.mobilesens.db.SQLiteTemplate.RowMapper;
import wsi.mobilesens.db.SensDataTable;
import wsi.mobilesens.util.DataBaseAdaptor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SensDAO {
	private static final String TAG = "SensDAO";

	private SQLiteTemplate mSqlTemplate;

	public SensDAO(Context context) {
		mSqlTemplate = new SQLiteTemplate(DataBaseAdaptor.getInstance(context)
				.getDatabaseHelper());
		mSqlTemplate.setPrimaryKey(SensDataTable.Columns.ID);
	}

	/**
	 * 
	 * @Title: insertSensData
	 * @Description: 插入一条状态
	 * @param sensdata
	 *            状态信息
	 * @return
	 */
	public long insertSensData(SensData sensdata) {
		if (!isExists(sensdata)) {
			return mSqlTemplate.getDb(true).insert(SensDataTable.TABLE_NAME,
					null, sensdataToContentValues(sensdata));
		} else {
			// updateSensData(sensdata);
			Log.v(TAG, sensdata.getId() + " is exists.");
			return -1;
		}
	}

	public int insertSensDatas(List<SensData> sensdatas, String jokeId) {
		int result = 0;
		SQLiteDatabase db = mSqlTemplate.getDb(true);
		try {
			db.beginTransaction();
			for (int i = sensdatas.size() - 1; i >= 0; i--) {
				SensData sensdata = sensdatas.get(i);
				long id = insertSensData(sensdata);

				if (-1 == id) {
					Log.v(TAG,
							"cann't insert the sensdata : "
									+ sensdata.toString());
				} else {
					++result;
					Log.v(TAG, String.format(
							"Insert a sensdata into database : %s",
							sensdata.toString()));
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return result;
	}

	public List<SensData> fetchSensDatas(String type) {
		return mSqlTemplate.queryForList(mRowMapper,
				SensDataTable.TABLE_NAME, null,
				SensDataTable.Columns.TYPE + " = ? " , new String[]{type}, null,
				null, SensDataTable.Columns.TIME + " DESC", null);
	}
	
	public List<SensData> fetchSensDatas() {
		return mSqlTemplate.queryForList(mRowMapper,
				SensDataTable.TABLE_NAME, null,
				null,null, null,
				null, SensDataTable.Columns.TIME + " DESC", null);
	}
	
	public int getSensDatasCount(){
		return mSqlTemplate.getCount(SensDataTable.TABLE_NAME);
	}
	
	public int getSensDatasCount(String type){
		return mSqlTemplate.getCount(SensDataTable.TABLE_NAME,SensDataTable.Columns.TYPE,type);
	}

	/**
	 * 
	 * @Title: isExists
	 * @Description: 检查状态是否存在
	 * @param sensdata
	 * @return
	 */
	public boolean isExists(SensData sensdata) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ").append(SensDataTable.TABLE_NAME)
				.append(" WHERE ").append(SensDataTable.Columns.ID)
				.append(" = " + sensdata.getId());

		return mSqlTemplate.isExistsBySQL(sql.toString());
	}

	/**
	 * SensData -> ContentValues
	 * 
	 * @param sensdata
	 * @param isUnread
	 * @return
	 */
	private ContentValues sensdataToContentValues(SensData sensdata) {
		final ContentValues v = new ContentValues();
		// v.put(SensDataTable.Columns.ID, sensdata.getAppinfoId());
		v.put(SensDataTable.Columns.RECORD, sensdata.getRecord());
		v.put(SensDataTable.Columns.TIME, sensdata.getDatetime());
		v.put(SensDataTable.Columns.TYPE, sensdata.getType());
		return v;
	}

	/**
	 * 接口实现
	 */
	private static final RowMapper<SensData> mRowMapper = new RowMapper<SensData>() {

		@Override
		public SensData mapRow(Cursor cursor) {
			try {
				if (cursor != null) {
					return SensData.parseSensData(cursor);
				} else {
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
