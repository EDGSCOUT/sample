package wsi.psyadjustbook;

import java.util.Calendar;

public class record{
	 int recodnum;
	 String date;
	 Calendar cal;
	 
	 record(){
		 
	 }
	 
	 record(int recordnum,String datetime,Calendar cal){
		 this.recodnum=recordnum;
		 this.date=datetime;
		 this.cal=cal; 
	 }
	 
}