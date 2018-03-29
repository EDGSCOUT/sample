package wsi.mobilesens.data;

import wsi.mobilesens.db.CategoryInfoTable;
import android.database.Cursor;
import android.util.Log;

/**
 * AppInfo 实体类
 * 
 * @author qinning
 * 
 */
public class CategoryInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1608000492860584608L;
	private String id;
	private int categoryID;
	private String typeName;
	private String columnName;

	public CategoryInfo() {

	}

	public CategoryInfo(int categoryID, String typeName, String columnName) {
		super();
		this.categoryID = categoryID;
		this.typeName = typeName;
		this.columnName = columnName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public static CategoryInfo parseCategoryInfo(Cursor cursor) {
		if (null == cursor || 0 == cursor.getCount()) {
			Log.w("Joke.ParseJoke",
					"Cann't parse Cursor, bacause cursor is null or empty.");
			return null;
		}
		CategoryInfo categoryinfo = new CategoryInfo();
		categoryinfo.categoryID = cursor.getInt(cursor
				.getColumnIndex(CategoryInfoTable.Columns.CATEGORYID));
		categoryinfo.columnName = cursor.getString(cursor
				.getColumnIndex(CategoryInfoTable.Columns.COLUMNNAME));
		categoryinfo.typeName = cursor.getString(cursor
				.getColumnIndex(CategoryInfoTable.Columns.TYPENAME));
		return categoryinfo;
	}

}
