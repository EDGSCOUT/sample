package wsi.survey;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

import com.nevin.dmeo.jnimac.jni.MacAddr;

import wsi.mobilesens.MobileSens;
import wsi.psy.FetchData;
import wsi.psy.Main;
import wsi.survey.question.OptionItem;
import wsi.survey.question.QuestionNaire;
import wsi.survey.result.GConstant;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.telephony.CellLocation;     
import android.telephony.PhoneStateListener;      
import java.net.NetworkInterface;

public class SaveResult {
	private static final String TAG = "SaveResult";
	private static String MAC_ADDRESS=null;
	private QuestionNaire qnNaire;
    private SQLiteDatabase mDatabase = null;
	private String imei;
	private String fileName;
	private String sqlAttr;
	private String dtime;
	private int questionsNum;
	private String answerScore; // 设置一个get函数，用来向数据库中存储
	private int answerScoreSum; // 设置一个get函数，用来向数据库中存储

	private String answerScoreTotal;
	private String answerScoreStd;
	
	private double latitude = 0;
	private double longitude = 0;
	private long  timeSpan;
	
	
	private Map<String, String> tagsMap;
	private Map<String, String> fileNameMap;

	private Context mContext;
	private String result;
	public SaveResult(Context context) {
		this.mContext = context;

		fileName = AnswerQuesion.fileName;
		String[] str = fileName.split("\\.");
		sqlAttr = str[0];
		System.err.println(sqlAttr);
		qnNaire = AnswerQuesion.qnNaire;
		runResult();
	}
	
	public SaveResult(Context context,double latitude,double longitude,long timeSpan){
		this.mContext = context;

		fileName = AnswerQuesion.fileName;
		String[] str = fileName.split("\\.");
		sqlAttr = str[0];
		System.err.println(sqlAttr);
		qnNaire = AnswerQuesion.qnNaire;
		this.latitude=latitude;
		this.longitude=longitude;
		this.timeSpan=timeSpan;

		runResult();
	}

	public String getAnswer() {
		return result;
	}

	/** 处理结果 */
	public void runResult() {
		savedQuestionsResult();
		//saveSurveyResult2SQL();
	}

	/** 保存问卷每道题的得分记录 ,记录在answerScore里 */
	private void savedQuestionsResult() {
		dtime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(
				System.currentTimeMillis()));// 记录问卷填写的时间
		questionsNum = qnNaire.getQuestionsNum();

		tagsMap = new HashMap<String, String>();
		answerScore = "";
		answerScoreSum = 0;
		for (int idx = 0; idx < questionsNum; idx++) {
			String qid = qnNaire.getQuestionItem(idx).getQid(); // qid
			String tag = qnNaire.getQuestionItem(idx).getTag(); // tag

			if (!tagsMap.containsKey(tag)) {
				tagsMap.put(tag, qnNaire.getQuestionItem(idx).getTag()); // 记录tags
			}

			String answer = ""; // answer
			String type = qnNaire.getQuestionItemOptionType(idx);
			if ("text".equals(type)) {
				String content = qnNaire.getQuestionItemAnswerContent(idx);
				answerScore += "#" + qid + "@[" + content + "]";
			} else if ("multi".equals(type)) {
				int num=qnNaire.getQuestionItemOptionNum(idx);
				answerScore+= "#"+qid;
				for(int i=0;i<num;i++){
					if(qnNaire.getQuestionItemAnswerOfN(idx, i)){
						OptionItem optionItem = qnNaire.getQuestionItem(idx)
								.getOptionItem(i); 
						answer=optionItem.getAid();
						answerScore+="@"+answer;
					}
				}
			} else {
				int oidx_selected = qnNaire
						.getQuestionItemAnswerOptionIndex(idx);
				if (oidx_selected >= 0) {
					OptionItem optionItem = qnNaire.getQuestionItem(idx)
							.getOptionItem(oidx_selected); // 获取选中项
					answer = optionItem.getAid();
					answerScoreSum = answerScoreSum + Integer.parseInt(optionItem.getScore());
				} else {
					answer = "0";
				}
				answerScore = answerScore +  "#" + qid + "@" + answer ; // 格式
																		// q01@a0@so#q02@a1@an#q03@a2@oc#
			}

		}
//		Log.e(SaveResult.class.getSimpleName(), answerScore);
		Log.e("answerScore:", answerScore);
		Log.e("answerScoreSum:", String.valueOf(answerScoreSum));
		result=getFinalResult();
		Log.e(SaveResult.class.getSimpleName(), result);
		FetchData fetchData = new FetchData(this.mContext);
		fetchData.insert(sqlAttr, answerScoreSum);
	}
	/** 保存问卷每道题的得分记录 ,记录在answerScore里 */
	/** 获得问卷得分*/
	public int getScore() {
		dtime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(
				System.currentTimeMillis()));// 记录问卷填写的时间
		questionsNum = qnNaire.getQuestionsNum();
		
		tagsMap = new HashMap<String, String>();
		answerScore = "";
		answerScoreSum = 0;
		for (int idx = 0; idx < questionsNum; idx++) {
			String qid = qnNaire.getQuestionItem(idx).getQid(); // qid
			String tag = qnNaire.getQuestionItem(idx).getTag(); // tag
			
			if (!tagsMap.containsKey(tag)) {
				tagsMap.put(tag, qnNaire.getQuestionItem(idx).getTag()); // 记录tags
			}
			
			String answer = ""; // answer
			String type = qnNaire.getQuestionItemOptionType(idx);
			if ("text".equals(type)) {
				String content = qnNaire.getQuestionItemAnswerContent(idx);
				answerScore += "#" + qid + "@[" + content + "]";
			} else if ("multi".equals(type)) {
				int num=qnNaire.getQuestionItemOptionNum(idx);
				answerScore+= "#"+qid;
				for(int i=0;i<num;i++){
					if(qnNaire.getQuestionItemAnswerOfN(idx, i)){
						OptionItem optionItem = qnNaire.getQuestionItem(idx)
								.getOptionItem(i); 
						answer=optionItem.getAid();
						answerScore+="@"+answer;
					}
				}
			} else {
				int oidx_selected = qnNaire
						.getQuestionItemAnswerOptionIndex(idx);
				if (oidx_selected >= 0) {
					OptionItem optionItem = qnNaire.getQuestionItem(idx)
							.getOptionItem(oidx_selected); // 获取选中项
					answer = optionItem.getAid();
					
					answerScoreSum = answerScoreSum + Integer.parseInt(optionItem.getScore());
				} else {
					answer = "0";
				}
				answerScore = answerScore +  "#" + qid + "@" + answer ; // 格式
				// q01@a0@so#q02@a1@an#q03@a2@oc#
			}
			
		}
//		Map<String, String> map = qnNaire.getRemarksMap();
//		System.out.println("map........" + map.toString());
		return answerScoreSum;
	}
	 
	/** 显示问卷属性，及计算的分数 */
	 private String getFinalResult(){ 
		 if(MAC_ADDRESS==null){
			 MAC_ADDRESS=MacAddr.getMacAddr(MobileApplication.mContext);
		 }
		 JSONObject jsonSurvey= new JSONObject();
		 // 问卷试题属性
		 try { 
			 jsonSurvey.put("DeviceId", MAC_ADDRESS); 
			 jsonSurvey.put("filename",fileName);
			 jsonSurvey.put("dtime", dtime);  
			 jsonSurvey.put("latitude", String.valueOf(latitude));
			 jsonSurvey.put("longitude", String.valueOf(longitude));
			 jsonSurvey.put("questionsAnswerScore", answerScore);
			 jsonSurvey.put("costSeconds", String.valueOf(timeSpan));
		 }catch(Exception e) {
			 e.printStackTrace();
			 return null;
		}
		return jsonSurvey.toString();
	 }
	 
	
	 
	/* private String getIMEI(){
		 TelephonyManager mTelManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		
		
     
	 }*/
	 
	 /** 保存结果到SQLite数据库（每道题得分、总分、标准分） *//*
		private void saveSurveyResult2SQL() {
			GSQLiteHelper sqlHelper = GSQLiteHelper.getInstance(mContext,
					GSQLiteHelper.DB_NAME, null, 1);
			SQLiteDatabase sqlDB = null;

			try {
				sqlDB = sqlHelper.getWritableDatabase();

				ContentValues values = new ContentValues();
				values.put(GSQLiteHelper.survey_imei, imei);
				values.put(GSQLiteHelper.survey_filename, fileName);
				values.put(GSQLiteHelper.survey_dtime, dtime);
				// values.put(GSQLiteHelper.survey_questionsNum, questionsNum);
				values.put(GSQLiteHelper.survey_questionsAnswerScore, answerScore);
				// values.put(GSQLiteHelper.survey_questionsAnswerScoreTotal,
				// answerScoreTotal);
				// values.put(GSQLiteHelper.survey_questionsAnswerScoreStd,
				// answerScoreStd);
				sqlDB.insert(GSQLiteHelper.TABLE_NAME, null, values);

			} catch (Exception e) {
				if (sqlDB != null) {
					sqlDB.close();
					sqlDB = null;
				}
				e.printStackTrace();
			} finally {
				if (sqlDB != null) {
					sqlDB.close();
					sqlDB = null;
				}
			}
		}*/
	 
	 /** 计算问卷每道题的总分和标准分（最终结果） */
		
		/*  private void calSurveyResult() { try {
		 * 
		 * String className = "wsi.survey.result." + qnNaire.getInterfaceType();
		 * Class<?> clazz = Class.forName(className); Object instance =
		 * clazz.newInstance();
		 * 
		 * Method method_getSurveyResultTotal =
		 * clazz.getMethod("getSurveyResultTotal", String.class); answerScoreTotal =
		 * (String)method_getSurveyResultTotal.invoke(instance, answerScore);
		 * Log.i(TAG, "answerScoreTotal = " + answerScoreTotal);
		 * 
		 * 
		 * Method method_getSurveyResultStd = clazz.getMethod("getSurveyResultStd");
		 * answerScoreStd = (String)method_getSurveyResultStd.invoke(instance);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } }*/

}
