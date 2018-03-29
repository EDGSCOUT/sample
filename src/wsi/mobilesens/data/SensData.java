package wsi.mobilesens.data;

import java.text.ParseException;
import java.util.Date;

import wsi.mobilesens.db.AppInfoTable;
import wsi.mobilesens.db.SensDataTable;

import android.database.Cursor;
import android.util.Log;

/**
 * SensData 实体类
 * 
 * @author qinning
 * 
 */
public class SensData implements java.io.Serializable {
	private static final long serialVersionUID = 1608000492860584608L;
	private int id;
	private String datetime;
	private String type;
	private String record;

	public SensData() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public static SensData parseSensData(Cursor cursor) {
		if (null == cursor || 0 == cursor.getCount()) {
			Log.w("Joke.ParseJoke",
					"Cann't parse Cursor, bacause cursor is null or empty.");
			return null;
		}
		SensData sensdata = new SensData();
		sensdata.datetime = cursor.getString(cursor
				.getColumnIndex(SensDataTable.Columns.TIME));
		sensdata.id = cursor.getInt(cursor
				.getColumnIndex(SensDataTable.Columns.ID));
		sensdata.record = cursor.getString(cursor
				.getColumnIndex(SensDataTable.Columns.RECORD));
		sensdata.type = cursor.getString(cursor
				.getColumnIndex(SensDataTable.Columns.TYPE));
		return sensdata;
	}

}
