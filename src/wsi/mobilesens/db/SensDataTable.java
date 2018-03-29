package wsi.mobilesens.db;

/**
 * 
 * @ClassName: SensdataTable
 * @author qinning
 * 
 */
public class SensDataTable {
	public static final String TABLE_NAME = "systemsens";
	public static class Columns {
		public static final String ID = "_id";
		public static final String TIME = "recordtime";		
		public static final String TYPE = "recordtype";
		public static final String RECORD = "datarecord";
		
	}

	public static String getCreateSQL() {
		String createString = TABLE_NAME + "( " + Columns.ID
				+ " INTEGER PRIMARY KEY, "
				+ Columns.TIME +" TEXT,"
				+ Columns.TYPE +" TEXT,"
				+ Columns.RECORD + " TEXT);";
		return "CREATE TABLE " + createString;
	}

	public static String getDropSQL() {
		return "DROP TABLE " + TABLE_NAME;
	}
}