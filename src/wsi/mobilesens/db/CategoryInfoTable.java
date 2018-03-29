package wsi.mobilesens.db;

/**
 * 
 * @ClassName: CategoryInfoTable
 * @author qinning
 * 
 */
public class CategoryInfoTable {
	public static final String TABLE_NAME = "categoryinfo";
	public static class Columns {
		public static final String CATEGORYID = "category_id";
		public static final String TYPENAME = "type_name";
		public static final String COLUMNNAME = "column_name";
		
	}

	public static String getCreateSQL() {
		String createString = TABLE_NAME + "( " + Columns.CATEGORYID
				+ " INTEGER PRIMARY KEY, "
				+ Columns.TYPENAME +" Text,"
				+ Columns.COLUMNNAME + " Text);";
		return "CREATE TABLE " + createString;
	}

	public static String getDropSQL() {
		return "DROP TABLE " + TABLE_NAME;
	}
}