package wsi.psyadjustbook;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class DecodeXMlebook {
	
	public static ebook decodexmlebook(int src) {
		
		XmlPullParser parser = PsyAdjustActivity.activity.getResources().getXml(src);
		ebook returnebook = new ebook();
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				String value = null;
				int size = parser.getAttributeCount();
				if ((name != null) && name.equals("title")) {
					for (int i = 0; i < size; i++)
						value = parser.getAttributeValue(i);
					if (value != null) {
						returnebook.title = value;						// title
					}
				} else if ((name != null) && name.equals("text")) {		// text
					for (int i = 0; i < size; i++)
						value = parser.getAttributeValue(i);
					if (value != null) {
						returnebook.content = returnebook.content + value;
					}
				}

			}
		} catch (Exception e) {
			Log.e("decode XML fils has a error", e.getMessage(), e);
		}
		return returnebook;
	}
}
