package wsi.mobilesens.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database Helper
 * 
 * @see SQLiteDatabase
 */
public class SQLiteTemplate {

	private static String TAG = SQLiteTemplate.class.getSimpleName();
	/**
	 * Default Primary key
	 */
	protected String mPrimaryKey = "_id";

	/**
	 * SQLiteDatabase Open Helper
	 */
	protected SQLiteOpenHelper mDatabaseOpenHelper;

	/**
	 * Construct
	 * 
	 * @param databaseOpenHelper
	 */
	public SQLiteTemplate(SQLiteOpenHelper databaseOpenHelper) {
		mDatabaseOpenHelper = databaseOpenHelper;
	}

	/**
	 * Construct
	 * 
	 * @param databaseOpenHelper
	 * @param primaryKey
	 */
	public SQLiteTemplate(SQLiteOpenHelper databaseOpenHelper, String primaryKey) {
		this(databaseOpenHelper);
		setPrimaryKey(primaryKey);
	}

	/**
	 * 根据某一个字段和值删除一行数据, 如 name="jack"
	 * 
	 * @param table
	 * @param field
	 * @param value
	 * @return
	 */
	public int deleteByField(String table, String field, String value) {
		return getDb(true).delete(table, field + "=?", new String[] { value });
	}

	/**
	 * 根据主键删除一行数据
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public int deleteById(String table, String id) {
		return deleteByField(table, mPrimaryKey, id);
	}

	/**
	 * 根据主键更新一行数据
	 * 
	 * @param table
	 * @param id
	 * @param values
	 * @return
	 */
	public int updateById(String table, String id, ContentValues values) {
		return getDb(true).update(table, values, mPrimaryKey + "=?",
				new String[] { id });
	}

	/**
	 * 根据主键查看某条数据是否存在
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public boolean isExistsById(String table, String id) {
		return isExistsByField(table, mPrimaryKey, id);
	}

	/**
	 * 根据某字段/值查看某条数据是否存在
	 * 
	 * @param status
	 * @return
	 */
	public boolean isExistsByField(String table, String field, String value) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM ").append(table).append(" WHERE ")
				.append(field).append(" = "+value);

		return isExistsBySQL(sql.toString());
	}
	
	public int getCount(String table,String field,String value){
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ").append(table).append(" where ")
				.append(field).append(" = '"+value+"'");
		Cursor c = getDb(false).rawQuery(sql.toString(),null);
		if(c.moveToFirst()){
			return c.getInt(0);
		}
		c.close();
		return 0;
	}
	
	public int getCount(String table){
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ").append(table);
		Cursor c = getDb(false).rawQuery(sql.toString(),null);
		if(c.moveToFirst()){
			return c.getInt(0);
		}
		c.close();
		return 0;
	}

	/**
	 * 使用SQL语句查看某条数据是否存在
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public boolean isExistsBySQL(String sql) {
		boolean result = false;

		Cursor c = getDb(false).rawQuery(sql,null);
		try {
			if(c.moveToFirst()==false){
				result=false;
			}else{
				result=true;
			}
		} finally {
			c.close();
		}
 		return result;
	}

	/**
	 * Query for cursor
	 * 
	 * @param <T>
	 * @param rowMapper
	 * @return a cursor
	 * 
	 * @see SQLiteDatabase#query(String, String[], String, String[], String,
	 *      String, String, String)
	 */
	public <T> T queryForObject(RowMapper<T> rowMapper, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		T object = null;

		final Cursor c = getDb(false).query(table, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		try{
			if(c.moveToFirst()){
				object = rowMapper.mapRow(c);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return object;
	}

	/**
	 * Query for list
	 * 
	 * @param <T>
	 * @param rowMapper
	 * @return list of object
	 * 
	 * @see SQLiteDatabase#query(String, String[], String, String[], String,
	 *      String, String, String)
	 */
	public <T> List<T> queryForList(RowMapper<T> rowMapper, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		List<T> list = new ArrayList<T>();

		final Cursor c = getDb(false).query(table, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		try {
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				list.add(rowMapper.mapRow(c));
			}
		} finally {
			c.close();
		}
		return list;
	}
	/**
	 * Get Primary Key
	 * 
	 * @return
	 */
	public String getPrimaryKey() {
		return mPrimaryKey;
	}

	/**
	 * Set Primary Key
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(String primaryKey) {
		this.mPrimaryKey = primaryKey;
	}

	/**
	 * Get Database Connection
	 * 
	 * @param writeable
	 * @return
	 * @see SQLiteOpenHelper#getWritableDatabase();
	 * @see SQLiteOpenHelper#getReadableDatabase();
	 */
	public SQLiteDatabase getDb(boolean writeable) {
		if (writeable) {
			return mDatabaseOpenHelper.getWritableDatabase();
		} else {
			return mDatabaseOpenHelper.getReadableDatabase();
		}
	}

	/**
	 * Some as Spring JDBC RowMapper
	 * 
	 * @see org.springframework.jdbc.core.RowMapper
	 * @see com.ch_linghu.fanfoudroid.db.dao.SqliteTemplate
	 * @param <T>
	 */
	public interface RowMapper<T> {
		public T mapRow(Cursor cursor);
	}

}
