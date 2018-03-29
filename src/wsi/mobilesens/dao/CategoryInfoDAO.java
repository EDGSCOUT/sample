package wsi.mobilesens.dao;

import java.util.List;

import wsi.mobilesens.data.CategoryInfo;
import wsi.mobilesens.db.CategoryInfoTable;
import wsi.mobilesens.db.SQLiteTemplate;
import wsi.mobilesens.db.SensDataTable;
import wsi.mobilesens.db.SQLiteTemplate.RowMapper;
import wsi.mobilesens.util.DataBaseAdaptor;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CategoryInfoDAO {
	private static final String TAG = "CategoryInfoDAO";

	private SQLiteTemplate mSqlTemplate;

	public CategoryInfoDAO(Context context) {
		mSqlTemplate = new SQLiteTemplate(DataBaseAdaptor.getInstance(context)
				.getDatabaseHelper());
		mSqlTemplate.setPrimaryKey(CategoryInfoTable.Columns.CATEGORYID);
	}

	/**
	 * 
	 * @Title: insertCategoryInfo
	 * @Description: 插入一条状态
	 * @param categoryinfo 状态信息
	 * @return
	 */
	public long insertCategoryInfo(CategoryInfo categoryinfo) {
		if (!isExists(categoryinfo)) {
			return mSqlTemplate.getDb(true).insert(CategoryInfoTable.TABLE_NAME,
					null, categoryinfoToContentValues(categoryinfo));
		} else {
//			updateCategoryInfo(categoryinfo);
			Log.v(TAG, categoryinfo.getId() + " is exists.");
			return -1;
		}
	}

	
	public int insertCategoryInfos(List<CategoryInfo> categoryinfos,String jokeId) {
		int result = 0;
		SQLiteDatabase db = mSqlTemplate.getDb(true);
		try {
			db.beginTransaction();
			for (int i = categoryinfos.size() - 1; i >= 0; i--) {
				CategoryInfo categoryinfo = categoryinfos.get(i);
				long id=insertCategoryInfo(categoryinfo);

				if (-1 == id) {
//					updateCategoryInfo(categoryinfo);
					Log.v(TAG, "cann't insert the categoryinfo : " + categoryinfo.toString());
				} else {
					++result;
					Log.v(TAG, String.format(
							"Insert a categoryinfo into database : %s",
							categoryinfo.toString()));
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return result;
	}

	/**
	 * Find a categoryinfo by categoryinfo ID
	 * 
	 * @param categoryinfoId
	 * @return
	 */
	public CategoryInfo fetchCategoryInfo(String id) {
		return mSqlTemplate.queryForObject(mRowMapper,
				CategoryInfoTable.TABLE_NAME, null, CategoryInfoTable.Columns.CATEGORYID
						+ " = ? "
						, new String[] { id }, null, null,
						CategoryInfoTable.Columns.CATEGORYID + " DESC", "1");
	}
	
	public List<CategoryInfo> fetchCategoryInfos() {
		return mSqlTemplate.queryForList(mRowMapper,
				CategoryInfoTable.TABLE_NAME, null,
				null, null, null,
				null, CategoryInfoTable.Columns.CATEGORYID + " DESC", null);
	}
	
	
	public int getSensDatasCount(){
		return mSqlTemplate.getCount(CategoryInfoTable.TABLE_NAME);
	}
	
	

	/**
	 * 
	 * @Title: isExists
	 * @Description: 检查状态是否存在
	 * @param categoryinfo
	 * @return
	 */
	public boolean isExists(CategoryInfo categoryinfo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ")
				.append(CategoryInfoTable.TABLE_NAME).append(" WHERE ")
				.append(CategoryInfoTable.Columns.CATEGORYID).append(" = "+categoryinfo.getId());

		return mSqlTemplate.isExistsBySQL(sql.toString());
	}
	
	
	
	/**
	 * CategoryInfo -> ContentValues
	 * 
	 * @param categoryinfo
	 * @param isUnread
	 * @return
	 */
	private ContentValues categoryinfoToContentValues(CategoryInfo categoryinfo) {
		final ContentValues v = new ContentValues();
		v.put(CategoryInfoTable.Columns.CATEGORYID, categoryinfo.getCategoryID());
		v.put(CategoryInfoTable.Columns.COLUMNNAME, categoryinfo.getColumnName());
		v.put(CategoryInfoTable.Columns.TYPENAME, categoryinfo.getTypeName());
		return v;
	}

	/**
	 * 接口实现
	 */
	private static final RowMapper<CategoryInfo> mRowMapper = new RowMapper<CategoryInfo>() {

		@Override
		public CategoryInfo mapRow(Cursor cursor) {
			try {
				if(cursor!=null){
					return CategoryInfo.parseCategoryInfo(cursor);
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
