package wsi.mobilesens.db;

/**
 * 
 * @ClassName: AppInfoTable
 * @author qinning
 * 
 */
public class ActivityDataTable {
	public static final String TABLE_NAME = "activity";
	public static class Columns {
		public static final String ID = "_id";
		public static final String ACTIVITY = "activity_name";
		public static final String TIME = "time";
		
	}

	public static String getCreateSQL() {
		String createString = TABLE_NAME + "( " + Columns.ID
				+ " INTEGER PRIMARY KEY, "
				+ Columns.ACTIVITY +" Text,"
				+ Columns.TIME + " INTEGER);";
		return "CREATE TABLE " + createString;
	}

	public static String getDropSQL() {
		return "DROP TABLE " + TABLE_NAME;
	}
}