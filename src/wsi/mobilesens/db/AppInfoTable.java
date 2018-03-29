package wsi.mobilesens.db;

/**
 * 
 * @ClassName: AppInfoTable
 * @author qinning
 * 
 */
public class AppInfoTable {
	public static final String TABLE_NAME = "appinfo";
	public static class Columns {
		public static final String APPID = "app_id";
		public static final String PACKAGENAME = "package_name";
		public static final String CATEGORYID = "category_id";
		
	}

	public static String getCreateSQL() {
		String createString = TABLE_NAME + "( " + Columns.APPID
				+ " INTEGER PRIMARY KEY, "
				+ Columns.PACKAGENAME +" Text,"
				+ Columns.CATEGORYID + " INTEGER);";
		return "CREATE TABLE " + createString;
	}

	public static String getDropSQL() {
		return "DROP TABLE " + TABLE_NAME;
	}
}