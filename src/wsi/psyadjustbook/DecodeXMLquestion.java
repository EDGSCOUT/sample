package wsi.psyadjustbook;


import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import android.util.Log;

class DecodeXMLquestion {
	
	 public static  List<question> decodexmlquestion(int src)
     {
 	XmlPullParser parser=PsyAdjustActivity.activity.getResources().getXml(src);
 	List<question> mQuestionsheet=new ArrayList<question>();
 	try
 	{
 		
 		String title="";
 		while(parser.next()!=XmlPullParser.END_DOCUMENT)
 		{ 	
 			String name=parser.getName();
 			String value=null;
 			List<String> option=new ArrayList<String>();
 			int size=parser.getAttributeCount();
 			question mquestion;
 			
 			if((name!=null)&&name.equals("subject")){
 				for(int i=0;i<size;i++)value=parser.getAttributeValue(i);
 				if(value!=null){
 						title=value;
// 						Log.i("objects",value);
 				}
 			}else if((name!=null)&&name.equals("obtions")){
 				for(int	i=0;i<=size;i++)
 					{
// 						Log.i("size",Integer.toString(size));
	 					if(i<size)
	 					{
	 						value=parser.getAttributeValue(i);
			 				if(value!=null){
//			 					mquestion.obtions.add(value);
			 					
			 					option.add(value);
			 				}
	 					}
	 					else{
	 						mquestion=new question(title,option);
////	 						int k=0;
////	 						Log.i("size",Integer.toString(k));
//	 						for(int k=0;k<mquestion.obtions.size();k++){
//	 							Log.i("options",mquestion.obtions.get(k));
//	 							Log.i("objects",Integer.toString(k));
//	 							Log.i("objects",mquestion.subject);
//	 						}
	 						//mquestion.subject=title;
	 					
	 						mQuestionsheet.add(mquestion);
//	 		 				Log.i("objects",mquestion.subject);
//	 		 				mquestion.removeobtion();

	 					}
 					}

 			}
 			
 		}
 	}catch(Exception e)
 	{
 		Log.e("decode XML fils has a error",e.getMessage(),e);
 	}
 	return mQuestionsheet;
     }


}
