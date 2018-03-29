package wsi.mobilesens.cal;

import java.util.HashMap;

import android.util.Log;


public class CalResult {
	
	private HashMap<String, Float> hashMap;
	
	public void setHashMap(HashMap<String, Float> hashMap){
		this.hashMap = hashMap;
	}
	
	/**
	 * 计算时不存在该key则返回0
	 * @param key
	 * @return
	 */
	private float getKey(String key){
		if(hashMap.containsKey(key)){
			return hashMap.get(key);
		}
		return 0;
	}
	
	/**
	 * CES =

     31.426  +
      0.071  * calllogOutNumAvg +
      0.2683 * contactlogNumAvg +
      0.0101 * media +
      0.0016 * system 
	 * @return
	 */
	public double getCES(){
		return 31.426 +0.071*getKey("calllogOutAvg")+
				0.2683 * getKey("contactlogAvg") +
				0.0101 *getKey("media") +
				0.0016 * getKey("system");
	}

	/**
	 * IAS =

     41.7949 +
     -0.0107 * weibo +
      0.0557 * office
	 * @return
	 */
	public double getIAS(){
		return 41.7949 
				-0.0107 * getKey("weibo")+
				0.0557 * getKey("office");
				
	}
	
	
	/**
	 * PWB =

     15.1471 +
     -0.0446 * gpslogNumAvg +
     -0.1386 * contactlogNumAvg +
      0.0011 * communicate +
      0.0777 * game_compety +
     -0.9292 * game_shot +
     -0.0583 * photo
	 * @return
	 */
	public double getPWB(){
		return 15.1471 
				- 0.0446 * getKey("gpslogAvg")
				- 0.1386 * getKey("contactlogAvg")
				+ 0.0011 * getKey("communicate")
				+ 0.0777 * getKey("game_compety")
				- 0.9292 * getKey("game_shot")
				- 0.0583 * getKey("photo");
	}
	
	/**
	 * UCLAAL =

     40.8646 +
     -0.089  * smslogOutAvg +
      0.2672 * contactlogNumAvg +
      0.0105 * renren +
      0.0576 * office
	 * @return
	 */	
	public double getUCLAAL(){
		return 40.8646 
				- 0.089 * getKey("smslogOutAvg")
				+ 0.2672 * getKey("contactlogAvg")
				+ 0.0105 * getKey("renren")
				+ 0.0576 * getKey("office");
	}
}
