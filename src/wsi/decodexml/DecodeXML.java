package wsi.decodexml;

import org.xmlpull.v1.XmlPullParser;

import wsi.psyadjustbook.PsyAdjustActivity;

import android.util.Log;

public class DecodeXML {
	
	 public static  DataAdapter decodexml(int src)
        {
    	XmlPullParser parser=PsyAdjustActivity.activity.getResources().getXml(src);
    	DataAdapter mDataAdapter=new DataAdapter();
    	int id=0;
    	try
    	{
    		while(parser.next()!=XmlPullParser.END_DOCUMENT)
    		{ 	
    			String name=parser.getName();
    			String value=null;
    			int size=parser.getAttributeCount();
    			if((name!=null)&&name.equals("text")){								// text
    				for(int i=0;i<size;i++) value=parser.getAttributeValue(i);
    				if(value!=null){
    					id= mDataAdapter.text.size();
    					mDataAdapter.text.add(value);
    					mDataAdapter.pageItemadd("text",id);
    				}
    			}else if((name!=null)&&name.equals("imglink")){						// img
    				for(int	i=0;i<size;i++) value=parser.getAttributeValue(i);
    				if(value!=null){
    					id=mDataAdapter.imgLink.size();
    					mDataAdapter.imgLink.add(value);;
    					mDataAdapter.pageItemadd("imglink",id);
    				}
    			}else if((name!=null)&&name.equals("videolink")){					// video
    				for(int i=0;i<size;i++) value=parser.getAttributeValue(i);
    				if(value!=null){
    					id=mDataAdapter.videoLink.size();
    					mDataAdapter.videoLink.add( value);
    					mDataAdapter.pageItemadd("videolink",id);
    				}
    			}else if((name!=null)&&name.equals("questionlink")){				// question
    				for(int i=0;i<size;i++) value=parser.getAttributeValue(i);
    				if(value!=null) {
    					id=mDataAdapter.questionLink.size();
    					mDataAdapter.questionLink.add(value);
    					mDataAdapter.pageItemadd("questionlink", id);
    				}
    			}else if((name!=null)&&name.equals("ebooklink")){					// ebook
    				for(int i=0;i<size;i++) value=parser.getAttributeValue(i);
    				if(value!=null) {
    					id=mDataAdapter.ebookLink.size();
    					mDataAdapter.ebookLink.add(value);
    					mDataAdapter.pageItemadd("ebooklink", id);
    				}
    			}
    			else if((name!=null)&&name.equals("trendcurve")){
    				for(int i=0;i<size;i++) value=parser.getAttributeValue(i);
    				if(value!=null) {
    					mDataAdapter.pageItemadd("trendcurve", 0);  //ֻ��һ������ͼ
    				}
    			}
    		}
    	}catch(Exception e)
    	{
    		Log.e("decode XML fils has a error",e.getMessage(),e);
    	}
    	return mDataAdapter;
        }

}
